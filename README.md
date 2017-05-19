LoTuS
=====

1. About LoTuS
------------

LoTuS is a open-source tool to graphic behaviour modelling of software using LTS. It aim to make the modelling process easier by providing drag-and-drop drawing tools. Making the learning curve tiny, removing the pré-requisite for modeller learn some formal specification language. This path, the use and learn of formal methods becames more accessible.

2. Dependencies
---------------

LoTuS requires JRE 8 with JavaFX 2.0. Check your java version with:
```
$ java -version
```
If needed, install the JRE on your system.
If you are using OpenJDK, check if you have OpenFX too.


The tool also requires Apache Maven to building the project. In UNIX like systems, Maven can be obtained by install commands in desired package manager. More information can be get [here](https://maven.apache.org/index.html).

Debian based distros like Ubuntu:
```bash
$ sudo apt-get install maven
```
REDHAT based distros like Fedora:
```
$ sudo dnf install maven
```
On Windows systems, in addition to installing java and maven you must configure the environment variables.
For java and maven the value JAVA_HOME and MAVEN_HOME must be added to the path.
Each of these values refer to the root directory of each of them.

Find JDK Installation Directory. First you need to know the installation path for the Java Development Kit. Open the default installation path for the JDK:
```
C:\Program Files\Java
```
There should be a subdirectory like:
```
C:\Program Files\Java\jdk[version]
```
Note: one has only to put the path to the jdk without /bin in the end (as suggested on a lot of places). e.g. C:\Java\jdk[version] and NOT C:\Java\jdk[version]\bin !


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
Note: You might need to restart Windows.



Download Maven 3.3.x from [here](http://maven.apache.org/download.cgi).

```
OS	Archive name
Windows	apache-maven-3.3.x-bin.zip
Linux	apache-maven-3.3.x-bin.tar.gz
Mac	apache-maven-3.3.x-bin.tar.gz
```

Extract the Maven archive

Extract the archive, to the directory you wish to install Maven 3.3.x. The subdirectory apache-maven-3.3.3 will be created from the archive.

OS	Location (can be different based on your installation)
```
Windows	C:\Program Files\Apache Software Foundation\apache-maven-3.3.3
Linux	/usr/local/apache-maven
Mac	/usr/local/apache-maven
```

Set Maven environment variables
Add M2_HOME, M2, MAVEN_OPTS to environment variables.

Windows	Set the environment variables using system properties. 
```
M2_HOME=C:\Program Files\Apache Software Foundation\apache-maven-3.3.3

M2=%M2_HOME%\bin
```

Now open console, execute the following mvn command to check if it has been successfully installed.

Windows	Open Command Console	
```
c:\> mvn --version
```
Linux	Open Command Terminal	
```
$ mvn --version
```
Now you have the necessary requirements to build the application.

3. Building
-----------
Use the maven to build the project:

$ mvn clean install

The distributable application will be generated at subdir target/jfx/
	
4. More Information
-------------------

More information can be found at the lotus web site: http://jeri.larces.uece.br/lotus

