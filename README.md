# Force plate app

Application is designed to process data recorded with AMTI Accugait force platform. It generates .txt files with forces and respective forces momentums in six columns
(Fx- force in frontal plane, Fy- force in horizontal plane, Fz- force in vertical plane and Mx, My, Mz as force momentums).
This app loads this data, filters it with butterworth filter (4th order with 7Hz cutoff frequency), and calculates for each file resulting data consisting of for center of pressure (COP), ranges, velocity lenghts etc.
Data is also viewed on charts for one direction of choice (COPx, COPy) or both for each file (subject). Lastly it allows to save resulting data file in .txt format
in desired location. 

## Getting Started

Sample data to see the work of the app is saved in Resources/RawData.

### Prerequisites

You will need at Java 10 or more recent to run the app. In older versions JavaFX was part of the JDK, here it is imported as a library.
If you wany to run it on older version anyway you would need to delete module-info.java.

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Rafal Zajac** - *Initial work* - (https://github.com/RZajacc)

## Acknowledgments

* My previous work on Academy of Physical education in Katowice inspired me to do this work.
Normally we used Matlab code but it was a little to complicated for young studends who wanted
to focus more on results than the procedure of data analysis itself. This helps to get results quick
and easy without any knowledge of coding. 
