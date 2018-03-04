# KROKI-MDE-Tool
Kroki MDE tool source repo

[Kroki](http://www.kroki-mde.net/) is a rapid prototyping tool for participatory development of business applications. It is developed at the [Chair of Informatics](http://informatika.ftn.uns.ac.rs/) on Faculty of Technical sciences in Novi Sad, Serbia. This tool has mainly research purposes.

This repository contains source code distributed as a set of Eclipse IDE projects, executables can be found in the [binaries repository](https://github.com/KROKIteam/KROKI-binaries).

## Prerequisites
1. [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. [Eclipse IDE](https://www.eclipse.org/ide/)

## Usage
1. Import the following projects into Eclipse workbench:
  * `GraphEdit`
  * `Kroki`
  * `Kroki-Administration`
  * `Kroki-API`
  * `kroki-commons`
  * `kroki-intl`
  * `Kroki-UIProfil`
  * `KrokiMockupTool`
2. Run the main class `KrokiMockupTool\src\kroki\app\KrokiMockupToolApp.java` as Java application

### Notes
* Eclipse project `WebApp` is a placeholder project for generated code, if you import it please ignore the errors in it.
* Currently, Java 8 is a mandatory requirement, while the tool has been tested on various Eclipse IDE releases that support Java 8.
