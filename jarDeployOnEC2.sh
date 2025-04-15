#!/bin/bash

# maven clean and package

mvn clean
mvn package

java -jar target/dbot-1.0-SNAPSHOT.jar
