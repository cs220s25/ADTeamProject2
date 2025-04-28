#!/bin/bash

# Create network if it doesn't exist
if ! docker network inspect trivia &>/dev/null; then
    docker network create trivia
    echo "Network trivia created."
else
    echo "Network trivia already exists."
fi

# Removes existing containers
docker rm -f redisdb 2>/dev/null
docker rm -f trivia-bot 2>/dev/null

# Start Redis
docker run -d --network trivia --name redisdb -v $(pwd)/data:/data redis redis-server --save 10 1

# Build and start bot
docker build -t trivia-bot .

# Check if running on EC2 already or not 
if curl --connect-timeout 1 -s http://169.254.169.254/latest/meta-data/instance-id > /dev/null; then
	echo "EC2 environment found. Will use IAM role for credentials."
	docker run -d --network trivia --name trivia-bot -e REDIS_HOST=redisdb -e REDIS_PORT=6379 trivia-bot
else
	echo "Using local environment. Will use aws.env for credentials"
	if [ ! -f aws.env ]; then 
		echo "aws.env not found"
		exit 1
	fi
	docker run -d --network trivia --name trivia-bot --env-file aws.env trivia-bot
fi
