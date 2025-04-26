# Start local redis server 
brew services start redis

mvn clean
mvn package

REDIS_HOST=localhost REDIS_PORT=6379 java -jar target/dbot-1.0-SNAPSHOT.jar
