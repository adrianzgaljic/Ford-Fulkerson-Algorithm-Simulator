import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;



/**
 * Created by adrianzgaljic on 04/12/15.
 */
public class Graph extends JFrame {
    public GPanel jp;
    public final int WIDTH = 800;
    public final int HEIGHT = 600;
    public final int NODEDIAMETAR = 80;
    public JTextArea instructionsText = new JTextArea();
    public JButton btnStart = new JButton("start simulation");
    ArrayList<Line> graphLines = new ArrayList<Line>();
    ArrayList<Node> graphNodes = new ArrayList<Node>();
    public Node source;
    public Node sink;
    public int chooser = 0;
    public boolean nextStep;
    public boolean started = false;


    public Graph() {

        super("Simple Drawing");
        createMenu();
        setTitle("Graph");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        instructionsText.setText("choose source");
        instructionsText.setEditable(false);
        instructionsText.setVisible(false);
        btnStart.setVisible(false);


        jp = new GPanel();
        jp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (chooser<2){
                    int nodeNumber = getSelectedNode(e.getX(), e.getY());
                    if (nodeNumber!=-1){
                        Node node = null;
                        for (int i=0; i<graphNodes.size(); i++){
                            node = graphNodes.get(i);
                            if (node.getNumber()==nodeNumber){
                                if (chooser == 0){
                                    graphNodes.add(new Node(node.getNumber(), node.getSerial(), Color.blue));
                                } else {
                                    graphNodes.add(new Node(node.getNumber(), node.getSerial(), Color.green));
                                }

                                graphNodes.remove(node);
                                break;

                            }
                        }
                        repaint();
                        if (chooser==0){
                            instructionsText.setText("choose sink");
                            source = node;
                            chooser=1;
                        } else {
                            sink = node;
                            chooser = 2;
                            btnStart.setVisible(true);
                            instructionsText.setText("");
                        }

                       // System.out.println(nodeNumber);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        btnStart.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextStep = true;
                if (!started){
                    btnStart.setText("next step");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            executeAlgorithm(source,sink);
                        }
                    }).start();

                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        BorderLayout borderLayout = new BorderLayout();
        FlowLayout flowLayout = new FlowLayout();
        //layout.addLayoutComponent(jp,BorderLayout.CENTER);
        JPanel panel = new JPanel();
        JPanel menuPanel = new JPanel();
        panel.setLayout(borderLayout);
        menuPanel.setLayout(flowLayout);
        menuPanel.add(instructionsText);
        menuPanel.add(btnStart);
        panel.add(jp, BorderLayout.CENTER);
        panel.add(menuPanel,BorderLayout.PAGE_START);
        borderLayout.addLayoutComponent(new TextField(""), BorderLayout.PAGE_END);
        getContentPane().add(panel);
    
    }



    private void createMenu() {

        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem oMenuItem = new JMenuItem("Open");
        oMenuItem.setMnemonic(KeyEvent.VK_O);
        oMenuItem.setToolTipText("Open file");
        oMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("open file");
                if (fc.showOpenDialog(Graph.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                Path file = fc.getSelectedFile().toPath();
                if (!Files.isReadable(file)) {
                    JOptionPane.showMessageDialog(Graph.this,
                            "Odabrana datoteka (" + file + ") nije čitljiva",
                            "Pogreška",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String pathName = file.toString();
                String[] pathSplitLIst = pathName.split("\\\\");
                String fileName = pathSplitLIst[pathSplitLIst.length - 1];

                try {
                    byte[] okteti = Files.readAllBytes(file);
                    String str = (new String(okteti, StandardCharsets.UTF_8));
                    createGraph(str);
                    instructionsText.setVisible(true);


                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(Graph.this,
                            "Pograška pri čitanju datoteka " + file + ": " + e1.getMessage() + ".",
                            "Pogreška",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        file.add(oMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);

    }

    private void createGraph(String str) {

        String[] lines = str.split("\n");
        String[] parts;
        String node;
        String line;
        int inc = 0;
        boolean contains = false;
        Node startNode;
        Node endNode;

        for (int i=0; i<lines.length; i++){
            line = lines[i];
            parts = line.split(",");

            node = parts[0].trim();
            startNode = new Node(Integer.parseInt(node), inc, Color.yellow);
            for (Node gNode:graphNodes){
                if (gNode.getNumber()==Integer.parseInt(node)){
                    contains = true;
                }
            }
            if (!contains){
                graphNodes.add(startNode);
                inc++;
            }

            contains = false;

            node = parts[1].trim();
            endNode = new Node(Integer.parseInt(node), inc, Color.yellow);
            for (Node gNode:graphNodes){
                if (gNode.getNumber()==Integer.parseInt(node)){
                    contains = true;
                }
            }
            if (!contains){
                graphNodes.add(endNode);
                inc++;
            }

            contains = false;

            boolean direction = false;
            if (parts[3].trim().equals("u")){
                direction = true;
            } else {
              /*  Line graphLine = new Line(endNode,
                        startNode,
                        Integer.parseInt(parts[2].trim()),
                        direction);
                graphLines.add(graphLine);
                graphLine = new Line(startNode,
                        endNode,
                        0,
                        direction);
                graphLines.add(graphLine); */
            }

            Line graphLine = new Line(startNode,
                    endNode,
                    Integer.parseInt(parts[2].trim()),
                    direction);
            graphLines.add(graphLine);
            graphLine = new Line(endNode,
                    startNode,
                    0,
                    direction);
            graphLines.add(graphLine);


        }

        System.out.println("Čvorovi:");
        for (Node n:graphNodes){
            System.out.println(n.getNumber());
        }

        System.out.println("Grane:");
        for (Line l:graphLines){
            System.out.println(l);
        }



        jp.repaint();

    }

    private int getSelectedNode(int x, int y) {

        if (!graphNodes.isEmpty()){
            int noOfNodes = graphNodes.size();

            int rx = getContentPane().getWidth()/2;
            int ry = getContentPane().getHeight()/2;
            int r;
            if (rx<ry){
                r = rx- NODEDIAMETAR -10;
            } else {
                r = ry- NODEDIAMETAR -10;
            }
            int nodeRx;
            int nodeRy;
            int degrees = 360 / noOfNodes;
            for (int i = 0; i < noOfNodes; i++) {

                nodeRx = (int) (rx + r * Math.cos(Math.toRadians(degrees) * i) + NODEDIAMETAR / 2);
                nodeRy = (int) (ry + r * Math.sin(Math.toRadians(degrees) * i) + NODEDIAMETAR / 2);

                if (Math.pow((x-nodeRx),2)+Math.pow((y-nodeRy),2)<Math.pow(NODEDIAMETAR/2,2)){
                    for (Node node:graphNodes){
                        if (node.getSerial()==i){
                            return node.getNumber();
                        }
                    }
                    return -1;
                }

            }
        }

        return -1;


    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Graph graph = new Graph();
                graph.setVisible(true);
            }
        });
    }

    class GPanel extends JPanel {



        public GPanel() {
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.white);

            drawNodes(g);
            drawLines(g);

        }

        public void drawLines(Graphics g){
            int noOfLines = graphLines.size();
            int noOfNodes = graphNodes.size();
            if (noOfLines > 0){
                int rx = getContentPane().getWidth()/2;
                int ry = getContentPane().getHeight()/2-20;
                int r;
                if (rx<ry){
                    r = rx- NODEDIAMETAR;
                } else {
                    r = ry- NODEDIAMETAR;
                }

                int nodeRxStart = 0;
                int nodeRyStart = 0;
                int nodeRxEnd = 0;
                int nodeRyEnd = 0;

                for (int j=0; j<noOfLines; j++){
                    int degrees = 360/noOfNodes;
                    for (int i=0; i<noOfNodes; i++){
                        if (getNode(i).getNumber()==graphLines.get(j).getStart().getNumber()){
                            nodeRxStart = (int) (rx + r*Math.cos(Math.toRadians(degrees)*i)+ NODEDIAMETAR /2);
                            nodeRyStart = (int) (ry + r*Math.sin(Math.toRadians(degrees)* i)+ NODEDIAMETAR /2);
                        }


                    }

                    degrees = 360/noOfNodes;
                    for (int i=0; i<noOfNodes; i++){
                        if (getNode(i).getNumber()==graphLines.get(j).getEnd().getNumber()){
                            nodeRxEnd = (int) (rx + r*Math.cos(Math.toRadians(degrees)*i)+ NODEDIAMETAR /2);
                            nodeRyEnd = (int) (ry + r*Math.sin(Math.toRadians(degrees)* i)+ NODEDIAMETAR /2);
                        }


                    }

                    if (!graphLines.get(j).isDirection()){
                        Color currColor = g.getColor();
                        g.setColor(graphLines.get(j).getColor());
                        g.drawLine(nodeRxStart, nodeRyStart, nodeRxEnd, nodeRyEnd);
                        g.setColor(currColor);
                    } else {
                        drawArrowLine(g, nodeRxStart,nodeRyStart,nodeRxEnd,nodeRyEnd,10,5, graphLines.get(j).getColor());
                    }

                    int midX;
                    int midY;

                    if (nodeRxStart <nodeRxEnd){
                        midX = nodeRxStart +(nodeRxEnd- nodeRxStart)/4;
                    } else {
                        midX = nodeRxStart - (nodeRxStart -nodeRxEnd)/4;
                    }

                    if (nodeRyStart<nodeRyEnd){
                        midY = nodeRyStart+(nodeRyEnd-nodeRyStart)/4;
                    } else {
                        midY = nodeRyStart - (nodeRyStart - nodeRyEnd) /4;
                    }
                    g.setColor(Color.white);
                    g.fillOval(midX - 12, midY - 12, 20, 20);
                    g.setColor(Color.red);
                    String text = Integer.toString(graphLines.get(j).getFlow())+"/"+Integer.toString(graphLines.get(j).getCapacity());
                    g.drawString(text, midX - 6, midY + 2);
                    g.setColor(Color.black);




                }



            }


        }

        private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h, Color color){
            int dx = x2 - x1, dy = y2 - y1;
            double D = Math.sqrt(dx*dx + dy*dy);
            double xm = D - d, xn = xm, ym = h, yn = -h, x;
            double sin = dy/D, cos = dx/D;

            x = xm*cos - ym*sin + x1;
            ym = xm*sin + ym*cos + y1;
            xm = x;

            x = xn*cos - yn*sin + x1;
            yn = xn*sin + yn*cos + y1;
            xn = x;

            int[] xpoints = {x2, (int) xm, (int) xn};
            int[] ypoints = {y2, (int) ym, (int) yn};
            Color currColor = g.getColor();
            g.setColor(color);
            g.drawLine(x1, y1, x2, y2);
            g.fillPolygon(xpoints, ypoints, 3);
            g.setColor(currColor);
        }





        public void drawNodes(Graphics g){
            int noOfNodes = graphNodes.size();
            if (noOfNodes > 0){
                int rx = getContentPane().getWidth()/2;
                int ry = getContentPane().getHeight()/2-20;

                int r;
                if (rx<ry){
                    r = rx- NODEDIAMETAR;
                } else {
                    r = ry- NODEDIAMETAR;
                }

                int nodeRx;
                int nodeRy;

                int degrees = 360/noOfNodes;
                for (int i=0; i<noOfNodes; i++){
                    nodeRx = (int) (rx + r*Math.cos(Math.toRadians(degrees)*i));
                    nodeRy = (int) (ry + r*Math.sin(Math.toRadians(degrees)* i));
                    drawCircle(g, nodeRx, nodeRy, NODEDIAMETAR, getNode(i).getNumber(), getNode(i).getColor());

                }
            }

        }

        public void drawCircle(Graphics g,int x,int y,int r,Integer number, Color color){
            g.setColor(color);
            g.fillOval(x, y, r, r);
            g.setColor(Color.black);
            g.drawString(Integer.toString(number), x, y);

        }

        public Node getNode(int i){
            for (Node node:graphNodes){
                if (node.getSerial()==i){
                    return node;
                }
            }
            return null;
        }
    }

    public void executeAlgorithm(Node source, Node sink){
        started = true;
        GPath path = new GPath();

        GPath p = findPath(source, sink, path);


        while (p!=null) {

            for (Line l:graphLines){
                l.setColor(Color.black);
            }
            //repaint();

            int min;
            if (p != null && p.getLines().size() > 0) {
                min = p.getLines().get(0).getCapacity() - p.getLines().get(0).getFlow();
                int value;
                for (Line l : p.getLines()) {
                    value = l.getCapacity() - l.getFlow();
                    if (value < min) {
                        min = value;
                    }
                }
                System.out.println("Put od izvorišta do odredišta: ");
                System.out.println(p);
                System.out.println("Protok kroz put: "+min);
                System.out.println("Ispis protoka i kapaciteta za svaku granu:");

                for (Line l : p.getLines()) {

                    for (int i = 0; i < graphLines.size(); i++) {
                        if (l.equals(graphLines.get(i))) {
                            graphLines.remove(i);
                            Line newLine = new Line(l.getStart(), l.getEnd(), l.getCapacity(), l.isDirection());
                            newLine.setColor(Color.red);
                            newLine.setFlow(min + l.getFlow());
                            graphLines.add(i, newLine);
                            System.out.println("Linija " + newLine + "   protok:" + newLine.getFlow() + "   kapacitet: " + newLine.getCapacity());

                        }
                        if (l.getStart()==graphLines.get(i).getEnd() && l.getEnd()==graphLines.get(i).getStart()){
                            Line tempLine = graphLines.get(i);
                            graphLines.remove(i);
                            Line newLine = new Line(l.getEnd(), l.getStart(), tempLine.getCapacity(), l.isDirection());
                            newLine.setColor(Color.red);
                            newLine.setFlow(tempLine.getFlow() - min);
                            graphLines.add(i, newLine);

                        }
                    }
                }
            }
            repaint();


            nextStep = false;
            while(!nextStep){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            p = findPath(source, sink, path);
        }
        int flow = calculateFlow();
        instructionsText.setText("Algorithm finished, maximal flow: "+Integer.toString(flow));
        btnStart.setVisible(false);
    }



    private GPath findPath(Node source, Node sink, GPath p) {
        if (source.getNumber()==sink.getNumber()){
           return p;
        }
        for (Line line:getLines(source)){
            int residual = line.getCapacity()-line.getFlow();
            if (residual>0 && !p.exists(line)){
                GPath pp = new GPath();
                for (Line l:p.getLines()){
                    pp.addLine(l);
                }
                pp.addLine(line);
                GPath result = findPath(line.getEnd(),sink,pp);
                if (result!=null){
                    return result;
                }
            }
        }
        return null;
    }

    //dohvaća sve linije koje izviru iz čvora node
    public ArrayList<Line> getLines(Node node){
        ArrayList<Line> lines = new ArrayList<Line>();
        for (Line l:graphLines){
            if (l.getStart().getNumber() == node.getNumber()){
                lines.add(l);
            }
        }
        return lines;
    }
    public  int calculateFlow() {
        int flow = 0;
        for(Line l:graphLines){
           // System.out.println(l);
            if (l.getStart().getNumber()==source.getNumber()){
                flow = flow+l.getFlow();
            }
        }
        return flow;
    }
}