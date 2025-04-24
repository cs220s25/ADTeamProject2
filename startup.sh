#!/bin/bash

# Create network if it doesn't exist
if ! docker network inspect trivia &>/dev/null; then
    docker network create trivia
    echo "Network trivia created."
else
    echo "Network trivia already exists."
fi

docker rm -f redisdb 2</dev/null
docker rm -f trivia-bot 2>/dev/null

# Start Redis
docker run -d --network trivia --name redisdb -v $(pwd)/data:/data redis redis-server --save 10 1

# Build and start bot
docker build -t trivia-bot .
docker run -d --network trivia --name trivia-bot --env-file aws.env trivia-bot
