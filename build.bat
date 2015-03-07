call mvn clean install
"C:\Program Files\Winrar\winrar.exe" x "%~dp0%~1\lotus-zip\target\lotus-tool.zip" *.* "%~dp0%~1\lotus-zip\target\"
java -jar lotus-zip\target\lotus-tool\lotus-app-2.13_alpha-SNAPSHOT-jfx.jar