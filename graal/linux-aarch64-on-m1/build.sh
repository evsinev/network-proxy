#!/bin/bash -eux

export JAVA_HOME=${JAVA_HOME:-~/.sdkman/candidates/java/17.0.3-oracle}

docker build -t graalvm-linux-aarch64 .

(cd ../.. && ./mvnw clean package)

docker run \
  --rm \
  --name graalvm-1 \
  -v `pwd`/../../network-proxy-server/target/network-proxy-server-1.0-SNAPSHOT.jar:/target/network-proxy-server.jar \
  -v `pwd`/target:/target \
  -w /target \
  graalvm-linux-aarch64 \
  /graalvm-ce-java17-22.3.0/bin/native-image \
  --verbose \
  --allow-incomplete-classpath \
  --no-fallback  \
  -jar /target/network-proxy-server.jar
