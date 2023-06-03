# Use the official maven/Java 11 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3-jdk-11-slim AS build-env

# Set the working directory to /app
WORKDIR /app
# Copy the pom.xml file to download dependencies
COPY pom.xml ./
# Copy local code to the container image.
COPY src ./src

# Download dependencies and build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK for base image.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM openjdk:11.0.16-jre-slim
#Download and install google cloud SDK
# Is this needed?
#RUN apt-get update && apt-get install -y curl gnupg
#RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
#RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg  add -
#RUN apt-get update && apt-get install -y google-cloud-sdk

# Copy the jar to the production image from the builder stage.
COPY --from=build-env /app/target/callcontroller-*.jar /callcontroller.jar
COPY --from=build-env /app/target/classes/* /

# Run the web service on container startup.
CMD ["java", "-jar", "/callcontroller.jar"]