ci:
	mvn package
	mvn deploy:deploy-file \
	 -DgroupId=net.austinj.xaerominimap \
	 -DartifactId=MinimapAPI \
	 -Dversion=1.0-SNAPSHOT \
	 -Dfile=./target/MinimapAPI-1.0-SNAPSHOT.jar \
	 -Dpackaging=jar \
	 -DrepositoryId=net.austinj.xaerominimap \
	 -Durl=file://maven
