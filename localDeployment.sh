# Start local redis server 
brew services start redis

# Build the app
mvn clean
mvn package

# Run with Redis config
REDIS_HOST=localhost REDIS_PORT=6379 java -jar target/dbot-1.0-SNAPSHOT.jar
