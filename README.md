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



# Local Docker deployment
* In the Learner lab, click "AWS Details" and then copy the text in the window below "AWS CLI"

```
[default]
aws_access_key_id==<VALUE>
aws_secret_access_key==<VALUE>
aws_session_token=<VALUE>
REDIS_HOST=redisdb
REDIS_PORT=6379
```

* Paste this string into a file named `aws.env` in the root of this repo.

* Edit this file to:

  1. Remove the `[default]` line
  2. Change all the variable names to all caps


```
AWS_ACCESS_KEY_ID=<VALUE>
AWS_SECRET_ACCESS_KEY=<VALUE>
AWS_SESSION_TOKEN=<VALUE>
```
Next, give execute permissions and run script by running 
```
chmod +x startup.sh
./startup.sh
```

## Technologies Used
- By using Discord API, this project creates a bot that can start a categories game for users.  [Discord 
API](https://discord.com/developers/docs/intro)

- The main way of data storage for our project is a Redis database.  [Redis](https://redis.io/docs/latest/)

- Maven is used to package the bot, so we have a .jar file to run the bot.  [Maven](https://maven.apache.org/)

- AWS Secrets allows users to store sensitive information. In this specific project it was used to store the 
discord 
token and the channel name.  [AWS Secrets Manager](https://aws.amazon.com/secrets-manager/)

- Github Actions is useful to run a process after a certain commit or action, like push and pulls. Actions used in 
our project are for running tests and deployments.  [Github Actions](https://github.com/features/actions)


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
