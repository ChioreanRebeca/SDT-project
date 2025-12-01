# 1. Start with Java 17
FROM openjdk:17-jdk-slim

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the built JAR file from your computer's 'target' folder into the container
#    (Maven builds into 'target', Gradle builds into 'build/libs')
COPY target/*.jar app.jar

# 4. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]