###Ford-Fulkerson-Algorithm-Simulator
This is Java application which shows step by step how Ford-Fulkerson algorithm works.
Ford-Fulkerson algorithm is used to compute maximal flow network.

![Demo](https://github.com/adrianzgaljic/Ford-Fulkerson-Algorithm-Simulator/blob/master/images/simulation.png?raw=true)

###Instructions

To start algorithm select file->open and navigate to configuration 
Configuration file describes flow network as folows:
* each line represents one link
* line is desciribed with 4 parameters separated with semicolon ";"
* first parameter is name of starting node
* second parameter is name of ending node
* third parameter is link capacity
* fourth parameter describes if link is directional-'u' or undirectional-'n'

For example, simple network can be described as:
```
    1, 2, 3, u
    1, 3, 3, u
    2, 4, 2, u
    2, 5, 3, u
    3, 6, 2, u
    6, 7, 3, u
    3, 6, 4, u
    3, 7, 2, u
```

Some other examples to try: [Examples](https://github.com/adrianzgaljic/Ford-Fulkerson-Algorithm-Simulator/tree/master/examples)

When flow network is loaded, next step is to select source and sink nodes by clicking on them.
To start simulation click "start algorithm" button. Button is than replaced with "next step" button which allows you to track changes in network after each step. When algorithm is finished, next step button will be disabled and result in form of maximal flow will be shown.
