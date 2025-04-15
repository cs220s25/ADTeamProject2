# ADTeamProject
Amanda and Deb's Team Project Repository

## CI STATUS
[![Deploy on 
AWS](https://github.com/cs220s25/ADTeamProject/actions/workflows/DeployOnAWS.yml/badge.svg)](https://github.com/cs220s25/ADTeamProject/actions/workflows/DeployOnAWS.yml)
[![Run Maven Tests](https://github.com/cs220s25/ADTeamProject/actions/workflows/run_tests.yml/badge.svg)](https://github.com/cs220s25/ADTeamProject/actions/workflows/run_tests.yml)


# Setup

git clone https://github.com/cs220s25/ADTeamProject.git

- create .env file that inclues these things:

DISCORD_TOKEN=<API key>
CHANNEL_NAME=<Channel Name>

To use with Amazon Secrets Manager:
Find the values for the items listed below in your learner lab. Next, put 
these values in ~/aws/credentials. Format the credentials directory as 
shown below:

 ```sh
[default]
aws_access_key_id=< VALUE >
aws_secret_access_key=< VALUE >
aws_session_token=< VALUE >
```


For Running locally: Before running localDeployment.sh 
- chmod +x localDeployment.sh
- Then to run script ./localDeployment.sh

# Project Description

This bot will allow users to start a game, stop a game, and select a category that the user will have to answer questions about the categoory they selected.

![System Diagram](discordBotProject/uml.jpg)


# Usage

Users will interact with the bot using commands. The bot will then respond to the user based on the original command. The bot is also able to keep track of the users score. Below is the list of valid commands a user can use:

- !categories - lists all the available categories
- !about - gets information about the game
- !start <category> - starts a new game with selected category
- !join - join the game
- !go - starts the game when all players are ready
- !status - see the current game status
- !quit - quits the game
- !help - lists all of the game commands
- !question - displays the current question
- !scores - gets all user scores
