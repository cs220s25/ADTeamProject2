# Trivia Discord Bot

## CI STATUS
[![Deploy on 
AWS](https://github.com/cs220s25/ADTeamProject2/actions/workflows/DeployOnAws.yml/badge.svg)](https://github.com/cs220s25/ADTeamProject2/actions/workflows/DeployOnAws.yml)

[![Run Maven 
Tests](https://github.com/cs220s25/ADTeamProject2/actions/workflows/run_tests.yml/badge.svg)](https://github.com/cs220s25/ADTeamProject2/actions/workflows/run_tests.yml)



## Contributors
* Amanda McNesby
* Deborah Rabinovich

# Setup
The first step for setup is to clone the repository to your laptop. For 
either method you will need to start an AWS learner lab.

```sh
git clone https://github.com/cs220s25/ADTeamProject.git
```

# Local Deployment:

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

Once you complete your credentials directory, you will need to cd into the 
repository and change the permissions of your deploy script and then run 
the script. You should do this with the following commands: 
```sh
chmod +x localDeployment.sh
./localDeployment.sh
```

# EC2 Deployment


## Technologies Used
Discord API
https://discord.com/developers/docs/intro

Redis
https://redis.io/docs/latest/

Maven
https://maven.apache.org/

AWS Secrets Manager
https://aws.amazon.com/secrets-manager/

Github Actions
https://github.com/features/actions


## Background


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
