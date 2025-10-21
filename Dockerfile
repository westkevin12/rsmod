# Use the official OpenJDK base image for Java 21
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /home/west/github.com/secondlifegaming/rsmod

# Install findutils
RUN apt-get update && apt-get install -y findutils

COPY gradle-9.0.0-bin.zip gradle/wrapper/
COPY . .

# Expose the port the application runs on
EXPOSE 43594

# Set the command to run the server
CMD ["./gradlew", "run", "--console=plain"]