#!/bin/bash

docker rm -f trivia-bot 2>/dev/null
docker exec redisdb redis-cli save 2>/dev/null
docker rm -f redisdb 2>/dev/null
