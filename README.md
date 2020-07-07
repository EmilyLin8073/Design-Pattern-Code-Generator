# Design-Pattern-Code-Generator

## Overview

Design Pattern Code Generator(DePaCoGe) IntelliJ Plugin is a code generator plugin which will generate user's desired design pattern. This DePaCoGe plugin is designed as a dockable window pane. The tab to open the window is located on the right side of the IDE. When users open the window, they will see a list of available design patterns. After the user chose the design pattern they want, the pop-up window will prompt the user to enter the interface, class, methods and field names. The name clash analyzer will then check if there is any name clash error happening in the same scope. If there's no name clash, the generated code will be inside the user’s project’s src folder so that the user can easily locate the generated design pattern. Otherwise, a error dialog will pop up and tell user whcih name they entered has a name clash. The code of design pattern won't be generated if there is a name clash. All generated files are in .java format

## Installation

This DePaCoGe IntelliJ Plugin should be imported using IntelliJ with SDK 1.8 and with Gradle tool and add all the dependencies in the build.gradle

## Deployment

When running the plugin, please use runIde from gradle. You can find the runIde option under Gradle -> Tasks -> intellij -> runIde. After the plugin is running, it will generate a project simulator. You will be able to see the plugin on the right hand side of the simulator

## Document
Document shows the project overview, the project design, implementation of each design pattern, results, and conclusion 
