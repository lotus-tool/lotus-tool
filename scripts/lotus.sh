#!/bin/sh

# Startup script for LOTUS (Linux/Solaris)

# LOTUS home directory
LOTUS_DIR=/home/emerson/bin/lotus

# Default value for notification after the scipt has finished (yes/no)
NOTIFY_DEFAULT=no

# Command to launch Java
if [ "$LOTUS_JAVA" = "" ]; then
	LOTUS_JAVA=java
fi

# Max memory for Java
if [ "$LOTUS_JAVAMAXMEM" != "" ]; then
	LOTUS_JAVAMAXMEM=`echo "$LOTUS_JAVAMAXMEM" | awk /^[0-9]+[kmg]?$/`
	if [ "$LOTUS_JAVAMAXMEM" = "" ]; then
		echo; echo "Error: Environment variable LOTUS_JAVAMAXMEM is invalid."; exit
	fi
	LOTUS_JAVAMAXMEM="-Xmx$LOTUS_JAVAMAXMEM"
else
	# default
	LOTUS_JAVAMAXMEM="-Xmx512m"
fi

# Add LOTUS to LD_LIBRARY_PATH
if [ "$LD_LIBRARY_PATH" = "" ]; then
	LD_LIBRARY_PATH="$LOTUS_DIR"/lib/prism
else
	LD_LIBRARY_PATH="$LOTUS_DIR"/lib/prism:$LD_LIBRARY_PATH
fi

# Set up CLASSPATH:
#  - LOTUS jar file (for binary versions) (gets priority)
#  - classes directory (most LOTUS classes)
#  - top-level directory (for images, dtds)
#  - lib/pepa.zip (PEPA stuff)
#  - lib/*.jar (all other jars)
#LOTUS_CLASSPATH="$LOTUS_DIR"/lib/*:"$LOTUS_DIR"/lib/prism/*:"$LOTUS_DIR"/extensions/*
LOTUS_CLASSPATH="$LOTUS_DIR"/lib/*:"$LOTUS_DIR"/lib/prism/*

# Export environment variables
export LOTUS_DIR LD_LIBRARY_PATH
echo $LD_LIBRARY_PATH
# Main Java class to launch
if [ "$LOTUS_MAINCLASS" = "" ]; then
	LOTUS_MAINCLASS=br.uece.seed.app.Startup
fi

# Do we run headless? (GUI overrides this)
if [ "$LOTUS_HEADLESS" = "" ]; then
	LOTUS_HEADLESS=true
fi

# Run LOTUS through Java
"$LOTUS_JAVA" $LOTUS_JAVA_DEBUG $LOTUS_JAVAMAXMEM -classpath "$LOTUS_CLASSPATH" -Dlotus.extensions.path="$LOTUS_DIR/extensions" -Djava.library.path="$LOTUS_DIR"/lib/prism_linux $LOTUS_MAINCLASS "$@"

LOTUS_EXIT_CODE=$?

exit $LOTUS_EXIT_CODE
