# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-5@sha256:80663340693d8863d518116b9382f758364c5394a41cf549eef6240ebd9e67af

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar