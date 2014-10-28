@set LOTUS_HOME=.
for %%i in ("%LOTUS_HOME%\lotus-app-*.jar") do set LOTUS_JAR="%LOTUS_HOME%\%%i" 
@set LOTUS_EXTENSIONS_PATH=%LOTUS_HOME%\extensions

java -Dlotus.extensions.path="%LOTUS_EXTENSIONS_PATH%" -jar "%LOTUS_JAR%" %*
