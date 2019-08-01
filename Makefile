ci:
	mvn package
	mvn install:install-file \
	 -DgroupId=net.austinj.xaerominimap \
	 -DartifactId=MinimapAPI \
	 -Dversion=1.0 \
	 -Dfile=./target/MinimapAPI-1.0-SNAPSHOT.jar \
	 -Dpackaging=jar \
	 -DgeneratePom=true \
	 -DlocalRepositoryPath=./maven \
	 -DcreateChecksum=true \
