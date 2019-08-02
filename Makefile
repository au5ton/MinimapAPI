ci:
	mvn install
	mvn compile
	mvn package
	mvn javadoc:javadoc
	mvn deploy
