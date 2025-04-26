#!/bin/bash

# Remove the trivia-bot container if it exists
docker rm -f trivia-bot 2>/dev/null

# Runs inside the redisdb container
docker exec redisdb redis-cli save 2>/dev/null

# Forcefully remove the redisdb container if it exists
docker rm -f redisdb 2>/dev/null
