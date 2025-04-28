#!/bin/bash

# Pull any uodates
sudo git pull origin main

# Restart service 
sudo systemctl restart discordBot.service
