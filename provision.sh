#!/bin/bash

function install_oracle_java_8 {
	su -
	echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list
	echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
	apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
	apt-get update
	apt-get install -y oracle-java8-installer
}

function install_maven_3 {
	sudo apt-get install -y maven
}

function main {
	install_oracle_java_8
	install_maven_3
}
	
main $@
