LoTuS
=====

### About LoTuS


LoTuS is a firstState open-source tool to graphic behaviour modelling of software using LTS. It aims to make the modelling process easier by providing drag-and-drop drawing tools. Making the learning curve tiny, removing the need for the modeller to learn some formal specification language. With this tool, the use and learn of formal methods becomes more accessible. Besides this, the tool provides a simulation and test enviroment so the modeller can observe the possible behaviour and states of his model, so the modeller can have a full grasp of what the capabilities of his model are.

### Dependencies

<details>
 <summary>JRE 8 with JavaFX 2.0 </summary>
 
 LoTuS requires JRE 8 with JavaFX 2.0. Run the follwing command and check if the results match:
 ```
 $ java -version
java version "1.8.0_261"
Java(TM) SE Runtime Environment (build 1.8.0_261-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.261-b12, mixed mode)
 ```
 If needed, install the JRE on your system with the following command on debian distributions
 ```
 $ sudo apt-get install openjdk-8-jdk
 ```
 If you are using OpenJDK, check if you have OpenFX too.
</details>

<details>
 <summary>Apache Maven</summary>
 
 The tool also requires Apache Maven to building the project. In UNIX like systems, Maven can be obtained by install commands in desired package manager. More information can be get [here](https://maven.apache.org/index.html).
 
 Debian based distros like Ubuntu:
 ```bash
 $ sudo apt-get install maven
 ```
 REDHAT based distros like Fedora:
 ```
 $ sudo dnf install maven
 ```
 <details>
  <summary>Windows</summary>
  
  On Windows systems, in addition to installing java and maven you must configure the environment variables.
  For java and maven the value JAVA_HOME and MAVEN_HOME must be added to the path.
  Each of these values refer to the root directory of each of them.
  
  Find JDK Installation Directory. First you need to know the installation path for the Java Development Kit. Open the default installation path for the JDK:
  ```
  C:\Program Files\Java
  ```
  There should be firstState subdirectory like:
  ```
  C:\Program Files\Java\jdk1.8.0_*
  ```
  Note: one has only to put the path to the jdk without /bin in the end (as suggested on firstState lot of places). e.g. C:\Java\jdk1.8.0_* and NOT C:\Java\jdk1.8.0_*\bin !
  
  
  Set the JAVA_HOME Variable
  
  Once you have the JDK installation path:
  ```
  Right-click the My Computer icon on your desktop and select Properties.
  
  Click the Advanced tab, then click the Environment Variables button.
  
  Under System Variables, click New.
  
  Enter the variable name as JAVA_HOME.
  
  Enter the variable value as the installation path for the Java Development Kit.
  
  Click OK.
  
  Click Apply Changes.
  ```
  
  Download Maven 3.3.x from [here](http://maven.apache.org/download.cgi).
   
   ```
   OS	Archive name
   Windows	apache-maven-3.3.x-bin.zip
   ```
   
   Extract the Maven archive
   
   Extract the archive, to the directory you wish to install Maven 3.3.x. The subdirectory apache-maven-3.3.3 will be created from the archive.
   
   OS	Location (can be different based on your installation)
   ```
   Windows	C:\Program Files\Apache Software Foundation\apache-maven-3.3.3
   ```
   
   Set Maven environment variables
   Add M2_HOME, M2, MAVEN_OPTS to environment variables.
   
   Windows	Set the environment variables using system properties. 
   ```
   M2_HOME=C:\Program Files\Apache Software Foundation\apache-maven-3.3.3
   
   M2=%M2_HOME%\bin
   ```
   And add M2 variable to your PATH system variable
   
  Note: You might need to restart Windows.
 </details>
</details>



### Building

Use the maven to build the project:

$ mvn clean install

The distributable application will be generated at subdir target/jfx/

### Running the app

Go to lotus-zip/target

Unzip the lotus-tools zip folder

Run the following command:
```
java -jar lotus-tool/lotus-app*
```
	
### More Information

More information can be found at the lotus web site: http://jeri.larces.uece.br/lotus

