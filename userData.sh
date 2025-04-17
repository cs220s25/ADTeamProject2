#!/bin/bash
yum install -y git
yum install -y maven-amazon-corretto21
yum install -y redis6
systemctl enable redis6
systemctl start redis6
git clone https://github.com/cs220s25/ADTeamProject2.git /ADTeamProject2
cd ADTeamProject2
chmod +x redeploy.sh
mvn clean
mvn package
cp discordBot.service /etc/systemd/system
systemctl enable discordBot.service
systemctl start discordBot.service
