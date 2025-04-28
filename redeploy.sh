#!/bin/bash

# Pull any updates
sudo git pull origin main

# Restart service 
sudo systemctl restart discordBot.service
