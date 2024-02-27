#bash

docker network create distribuidos

docker run -p 5432:5432 --name postgres --network distribuidos -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=db_invoice -d postgres

docker run -p 3306:3306 --name mysql --network distribuidos -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_DATABASE=db_operation -d mysql:latest

docker run -p 27017:27017 --network distribuidos --name mongodb -d mongo

docker run --network distribuidos -d --name express-gateway-data-store -p 6379:6379 redis:alpine

docker run -p 2181:2181 -d -p 9092:9092 --name servicekafka --network distribuidos -e ADVERTISED_HOST=servicekafka -e NUM_PARTITIONS=3 johnnypark/kafka-zookeeper

docker run -d -p 8500:8500 -p 8600:8600/udp --network distribuidos --name consul consul:1.15.4 agent -server -bootstrap-expect 1 -ui -data-dir /tmp -client=0.0.0.0

docker run -d -p 8888:8888 --network distribuidos --name app-config arturodiaz02/app-config:v1

docker run -d -p 1936:1936 -p 5050:80 --network distribuidos --name loadbalancer arturodiaz02/loadbalancer:0.2.0

docker run -d --name express-gateway --network distribuidos -v ./appgw:/var/lib/eg -p 8080:8080 -p 9876:9876 express-gateway

sleep 13
docker-compose up -d
