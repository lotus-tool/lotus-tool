call mvn clean install
"C:\Program Files\Winrar\winrar.exe" x -y "%~dp0%~1\lotus-zip\target\lotus-tool.zip" *.* "%~dp0%~1\lotus-zip\target\"
java -jar lotus-zip\target\lotus-tool\lotus-app-3.0-alpha-SNAPSHOT-jfx.jar