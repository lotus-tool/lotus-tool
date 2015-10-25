call mvn clean install
"%java_home%\bin\jar" xf "%~dp0%~1lotus-zip\target\lotus-tool.zip"
move "%~dp0%~1lotus-tool" "%~dp0%~1lotus-zip\target\"
java -jar lotus-zip\target\lotus-tool\lotus-app-3.0-alpha-SNAPSHOT-jfx.jar