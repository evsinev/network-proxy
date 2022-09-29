#!/bin/bash -eux

native-image \
  --verbose \
  --allow-incomplete-classpath \
  --no-fallback  \
  -jar ../network-proxy-server/target/network-proxy-server-1.0-SNAPSHOT.jar
