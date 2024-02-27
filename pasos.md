## Steps
### Create the network
docker network create distribuidos
### Build config Server image in app-config folder
docker build --platform=linux/amd64 -t estebanm1812/app-config .
### Push the server config server image to Docker hub in app-config folder
docker push estebanm1812/app-config 
### Run app-config the container
docker run -d -p 8888:8888 --network distribuidos --name app-config estebanm1812/app-config:latest
### Build  invoice image in app-invoice folder
docker build --platform=linux/amd64 -t estebanm1812/app-invoice .
### Build app-pay image in app-pay folder
docker build --platform=linux/amd64 -t estebanm1812/app-pay .
### Build app-transaction image in app-transaction folder
docker build --platform=linux/amd64 -t estebanm1812/app-transaction .
### Push Image into Docker-hub
docker push estebanm1812/app-invoice
### Up/Run Kafka Service
docker run -p 2181:2181 -d -p 9092:9092 --name servicekafka --network distribuidos -e ADVERTISED_HOST=servicekafka -e NUM_PARTITIONS=3 johnnypark/kafka-zookeeper
### Run in-voice Container in app-invoice folder
docker run -d -p 8006:8006 --network distribuidos --name app-invoice estebanm1812/app-invoice:latest
### Build Posgress Database in folder
docker build -t estebanm1812/postgres .
### Build Mysql Database in folder
docker build -t estebanm1812/mysql .
### Up MongoDB
$ docker run -p 27018:27017 --network distribuidos --name mongodb -d mongo
### Up Posgres Data-base in postgres folder
docker run -d -p 5434:5432  --name postgres --network distribuidos estebanm1812/postgres:latest
### Up mysql Data-base in postgres folder
docker run -d -p 3306:3306  --name mysql --network distribuidos estebanm1812/mysql:latest
### Up Consult Service
docker run -d -p 8500:8500 -p 8600:8600/udp --network distribuidos --name consul consul:1.15 agent -server -bootstrap-expect 1 -ui -data-dir /tmp -client=0.0.0.0
### Run App-Invoice container
docker run -d -p 8006:8006 --network distribuidos --name app-invoice estebanm1812/app-invoice
### Run App-pay container
docker run -d -p 8010:8010 --network distribuidos --name app-pay estebanm1812/app-pay
### Run App-transaction container
docker run -d -p 8082:8082 --network distribuidos --name app-transaction estebanm1812/app-transaction
### To show the App-invoice service in the Consult the next lines need to be added to the app-invoice-dev-properties file in the folder config
spring.cloud.consul.host=consul
spring.cloud.consul.port=8500
spring.cloud.consul.discovery.health-check-interval=5s
spring.cloud.consul.discovery.prefer-ip-address=true

### Add the next dependencies in the build.grade in the app-invoice folder to make it work 
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.cloud:spring-cloud-starter-consul-discovery'
### To add Info to the postgres DB do the next commands
sudo docker exec -it containerID bash
psql -h localhost -d db_invoice -U postgres
INSERT INTO invoice(id_invoice, amount, state) VALUES(1, 1000, 0);
INSERT INTO invoice(id_invoice, amount, state) VALUES(2, 5000, 1);
INSERT INTO invoice(id_invoice, amount, state) VALUES(3, 300, 0);
INSERT INTO invoice(id_invoice, amount, state) VALUES(4, 600, 0);
INSERT INTO invoice(id_invoice, amount, state) VALUES(5, 400, 0);

### DNS Masq (Mac OS version)
brew update
brew install dnsmasq
sudo mkdir -pv $(brew --prefix)/etc/
echo -e "\n\nUpdating /usr/local/etc/dnsmasq.conf-------------------------------"
echo "server=/consul/127.0.0.1#8600" >> $(brew --prefix)/etc/dnsmasq.conf

# Restart dnsmasq service
echo -e "\n\nRestarting dnsmasq service------------------------------"
sudo brew services restart dnsmasq
sudo killall -HUP mDNSResponder

# Create a dns resolver for consul
sudo mkdir -v /etc/resolver/
cat <<EOF | sudo tee /etc/resolver/consul
nameserver 127.0.0.1#8600
EOF

# Check dnsmasq service status
echo -e "\n\nChecking dnsmasq service status------------------------------"
sudo brew services list | grep dnsmasq

### LOAD BALANCER 

#in Haproxy folder
docker build --platform=linux/amd64 -t estebanm1812/load-balancer .
docker run -d -p 1936:1936 -p 9000:80 --network distribuidos --name load-balancer estebanm1812/load-balancer
### Up Express-gateway database
docker run --network distribuidos -d --name express-gateway-data-store \
-p 6379:6379 \
redis:alpine
### Up Express-gateway service in folder appwg
docker run -d --name express-gateway \
--network distribuidos \
-v .:/var/lib/eg \
-p 8080:8080 \
-p 9876:9876 \
express-gateway
### Create User for express-gateway
docker exec -it express-gateway sh
eg users create

#
#"isActive": true,
#"username": "esteban",
#"id": "d83631ae-c2ae-4a92-929b-893994935813",
#"firstname": "esteban",
#"lastname": "mendoza",
#"email": "EstebanMendoza02@hotmail.com",
#"createdAt": "Tue Feb 27 2024 00:39:24 GMT+0000 (Coordinated Universal Time)",
#"updatedAt": "Tue Feb 27 2024 00:39:24 GMT+0000 (Coordinated Universal Time)"
#}

eg credentials create -c esteban -t key-auth -q

### Key 2BEBKwq2rmpUlMeSuaaQlE:1OVA4GdIuLNW4qMUfzasKE

##Test
curl -H "Authorization: apiKey 2BEBKwq2rmpUlMeSuaaQlE:1OVA4GdIuLNW4qMUfzasKE" http://localhost:8080/config/app-pay/dev
