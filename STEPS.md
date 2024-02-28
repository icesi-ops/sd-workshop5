# <b> *First WorkShop Distribuidos* </b>



## <b> STEP BY STEP </b> 


Fork and clone the next repo:
*git clone* https://github.com/icesi-ops/training_microservices.git

# *Login*

- sudo docker login


# *Create a network for microservices*

- docker network create distribuidos

# * Enter to the corresponding folders to begin the configurations*

- cd training_microservices/

- cd pay-app-spring-microservices/

- cd app-config/


# *Change Dockerfile to app-config/ port to 8888*

- vi Dockerfile cambiar al puerto 8888


# *Consul Microservice Deployment* 

- docker run -d -p 8500:8500 -p 8600:8600/udp --network distribuidos --name consul consul:1.15 agent -server -bootstrap-expect 1 -ui -data-dir /tmp -client=0.0.0.0


# *Kafka Microservice Deployment*

- docker run -p 2181:2181 -d -p 9092:9092 --name servicekafka --network distribuidos -e ADVERTISED_HOST=servicekafka -e NUM_PARTITIONS=3 johnnypark/kafka-zookeeper:2.6.0


# *Postgres Database Deployment*

- sudo docker run -p 5434:5432  --name postgres --network distribuidos -e POSTGRES_PASSWORD=postgres -e  POSTGRES_DB=db_invoice -d postgres:12-alpine


# *Build app-config image*

- docker build -t paulatrujillo/app-config:0.0.1 .

# *Run app-config image*

- docker run -d -p 8888:8888 --network distribuidos --name app-config paulatrujillo/app-config:0.0.1 .


# *Build app-invoice image*

- sudo docker build -t paulatrujillo/app-invoice:0.0.1 .


# *Run app-invoice image*

- docker run -d -p 8006:8006 --network distribuidos --name app-invoice paulatrujillo/app-invoice:0.0.1 .



## <b> Built with </b> 🛠


+ [Docker](https://www.docker.com/) - Docker is an open platform for developing, shipping, and running applications.




## <b> By: </b>


+ [Paula Andrea Trujillo Mejia](https://github.com/PaulaTrujillo27 "Paula T.")
+ [Icesi-Ops](https://github.com/icesi-ops")




<br>


