#!/usr/bin/env bash
docker login -u $DOCKER_HUB_CREDS_USR -p $DOCKER_HUB_CREDS_PSW
# mvn package
# mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build -t nishantchouhan/currency-utilities-server:latest  .
docker tag nishantchouhan/currency-utilities-server:latest nishantchouhan/currency-utilities-server:$BUILD_NUMBER
docker push nishantchouhan/currency-utilities-server:latest
docker push nishantchouhan/currency-utilities-server:$BUILD_NUMBER
docker logout

