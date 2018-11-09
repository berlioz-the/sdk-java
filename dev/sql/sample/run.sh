#!/bin/sh

docker stop berlioz-sample-1
docker kill berlioz-sample-1
docker rm berlioz-sample-1

docker build -t berlioz-sample-1 .

docker run --rm  \
           --name berlioz-sample-1 \
           --memory=1024g \
           -p 8080:8080 \
           berlioz-sample-1