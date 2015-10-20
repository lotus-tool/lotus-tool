#!/bin/bash

DEBUG_PORT=4000
APP_FILE="lotus-zip/target/lotus-tool/lotus-app-3.0-alpha-SNAPSHOT-jfx.jar"

[ "$1" = "debug" ] && DEBUG="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$DEBUG_PORT,suspend=n"
[ -e $APP_FILE ] || ./build.sh

java $DEBUG -jar $APP_FILE
