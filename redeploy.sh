#!/bin/bash
cd /ADTeamProject2

sudo systemctl stop discordBot.service

# Pull any updates
sudo git pull origin main
sudo mvn clean package

# Start service 
sudo systemctl start discordBot.service
