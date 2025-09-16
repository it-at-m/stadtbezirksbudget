# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1757607786@sha256:b223fdb4fdd1b09fd5792f86e9d504a9cbb7a13bad65fb016812287c85718c60

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar