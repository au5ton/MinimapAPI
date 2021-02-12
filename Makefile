html:
	apt-get install --no-install-recommends -y maven
	mvn install
	mvn compile
	mvn package
	mvn javadoc:javadoc
	mvn deploy
