# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1777302154@sha256:f2968665e5946ff6cc8bdaa02817132027454ed8793920b11685ee7810408af5

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar