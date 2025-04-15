## install git
sudo yum install -y git

## install java and maven package
sudo yum install -y maven-amazon-corretto21

## install and start redis
sudo yum install -y redis6
sudo systemctl enable redis6
sudo systemctl start redis6

## clone the repo and cd into the project
git clone https://github.com/cs220s25/ADTeamProject2.git
cd ADTeamProject2/
