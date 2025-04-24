#!/bin/bash

git config --global --add safe.directory /ADTeamProject2
cd /ADTeamProject2
git pull origin main

docker build -t trivia-bot . 

docker rm -f trivia-bot 2>/dev/null || true

docker run -d --network --name trivia-bot -e REDIS_HOST=redisdb -e REDIS_PORT=6379 trivia-bot
