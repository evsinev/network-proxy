FROM debian:bullseye

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update && apt install -y curl gcc

RUN curl -sL https://get.graalvm.org/jdk | bash

RUN apt install zlib1g-dev

#RUN wget https://get.graalvm.org/jdk -o install.sh \
#    bash install.sh
