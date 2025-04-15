# Start local redis server 
brew services start redis

mvn clean
mvn package

java -jar target/dbot-1.0-SNAPSHOT.jar
