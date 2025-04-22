FROM amazonlinux

# Working directory is inside container
WORKDIR /app

# Install Java and Maven
RUN yum install -y maven-amazon-corretto21

# Copy project files
COPY pom.xml .
COPY src src

# Building jar inside of container
RUN mvn clean package

# Run application
CMD ["java", "-jar", "target/dbot-1.0-SNAPSHOT.jar"]
