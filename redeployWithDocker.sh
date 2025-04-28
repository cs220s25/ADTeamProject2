#!/bin/bash

# Configure Git safe directory
git config --global --add safe.directory /ADTeamProject2

# Navigate to project directory and pull latest changes
git pull origin main

# Build Docker image
docker build -t trivia-bot . 

# Remove existing container if it exists
docker rm -f trivia-bot 2>/dev/null || true

# Run new container with Redis configuration
docker run -d --network trivia --name trivia-bot -e REDIS_HOST=redisdb -e REDIS_PORT=6379 trivia-bot
