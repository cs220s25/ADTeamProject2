#!/bin/bash

# maven clean and package
# builds application
mvn clean
mvn package

# Run application
java -jar target/dbot-1.0-SNAPSHOT.jar
