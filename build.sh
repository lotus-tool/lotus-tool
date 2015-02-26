#!/bin/bash

DEBUG_PORT=4000

[ "$1" = "debug" ] && DEBUG="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$DEBUG_PORT,suspend=n"

mvn clean install
unzip lotus-zip/target/lotus-tool.zip -d lotus-zip/target
java $DEBUG -jar lotus-zip/target/lotus-tool/lotus-app-2.13_alpha-SNAPSHOT-jfx.jar
