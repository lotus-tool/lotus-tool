@echo off

rem Startup script for LOTUS (Windows)
echo Loading LOTUS...

rem LOTUS home directory
rem Default is .. so it can be run directly from the bin directory.
rem An example would be: set LOTUS_DIR=c:\Program Files\lotus
rem Note: Do not put quotes ("...") around the path.
set LOTUS_DIR=..

rem Add LOTUS to path
rem path=%LOTUS_DIR%\lib;%path%

rem Set up CLASSPATH:
rem  - LOTUS jar file (for binary versions) (gets priority)
rem  - classes directory (most LOTUS classes)
rem  - top-level directory (for images, dtds)
rem  - lib/pepa.zip (PEPA stuff)
rem  - lib/*.jar (all other jars)

set CP=%LOTUS_DIR%\lib\*;%LOTUS_DIR%\lib\prism\*;%LOTUS_DIR%\extensions\*
set LOTUS_EXT_DIR=%LOTUS_DIR%\extensions

rem Run LOTUS through Java
rem start "LOTUS" javaw -Djava.library.path="%LOTUS_DIR%\lib\prism" -classpath "%CP%" br.uece.seed.app.Startup %*
java -Dlotus.extensions.path=%LOTUS_EXT_DIR% -classpath "%CP%" br.uece.seed.app.Startup %*
rem exit
pause
