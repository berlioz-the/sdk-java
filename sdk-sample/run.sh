docker stop berlioz-java-sample
docker kill berlioz-java-sample
docker rm berlioz-java-sample
docker rmi berlioz-java-sdk-sample

docker build -t berlioz-java-sdk-sample .

docker run berlioz-java-sdk-sample --name berlioz-java-sample
