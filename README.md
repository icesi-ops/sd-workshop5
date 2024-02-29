# sd-workshop5 


# <b> *Training Microservices* </b>



## <b> STEPS </b> 📄

Configuring Load Balancer and ApiGateway for previously deployed microservices

Load Balancer
Using haproxy, we set up the load balancer to distribute the workload among multiple instances of the same microservice.

In the haproxy.cfg file, we complete the configuration of the necessary endpoints for our development, adding routes in the frontend, backend, and the DNS server that responds to requests.

```
defaults
   timeout connect 5s
   timeout client 1m
   timeout server 1m

frontend stats
   bind *:1936
   mode http
   stats uri /
   stats show-legends
   no log

frontend http_front
   bind *:80
   mode http
   acl url_config path_beg /config
   use_backend config_back if url_config
   acl url_invoice path_beg /invoice
   use_backend app_invoice if url_invoice
   acl url_pay path_beg /pay
   use_backend app_pay if url_pay
   acl url_transaction path_beg /transaction
   use_backend app_transaction if url_transaction

backend config_back
    mode http
    balance roundrobin
    http-request set-path "%[path,regsub(^/config/,/)]"
    server appconfig app-config.service.consul:8888 resolvers consul resolve-prefer ipv4 check
backend app_invoice
    mode http
    balance roundrobin
    http-request set-path "%[path,regsub(^/invoice/,/)]"
    server appinvoice app-invoice.service.consul:8006 resolvers consul resolve-prefer ipv4 check
backend app_pay
    mode http
    balance roundrobin
    http-request set-path "%[path,regsub(^/pay/,/)]"
    server apppay app-pay.service.consul:8010 resolvers consul resolve-prefer ipv4 check
backend app_transaction
    mode http
    balance roundrobin
    http-request set-path "%[path,regsub(^/transaction/,/)]"
    server apptran app-transaction.service.consul:8082 resolvers consul resolve-prefer ipv4 check

resolvers consul
    nameserver consul consul:8600
    accepted_payload_size 8192
    hold valid 5s 

```
ApiGateway with Express-Gateway and Redis
To configure the API Gateway, we need to modify the gateway.config.yml file, adding endpoints with their routes and the access URLs from the load balancer side. It's important to remember that the domain "loadbalancer" in this URL corresponds to the name of the image used to build the container.

Additionally, this file contains the access and security policies that will be used to consume information from the microservices.

```
http:
  port: 8080
admin:
  port: 9876
  host: localhost
apiEndpoints:
  appconfig:
    host: localhost
    paths: ['/config','/config/*']
  appinvoice:
    host: localhost
    paths: ['/invoice','/invoice/*']
  apppay:
    host: localhost
    paths: ['/pay','/pay/*']
  apptransaction:
    host: localhost
    paths: ['/transaction','/transaction/*']
serviceEndpoints:
  appconfig:
   url: 'http://loadbalancer/config/'
  appinvoice:
   url: 'http://loadbalancer/invoice/'
  apppay:
   url: 'http://loadbalancer/pay/'
  apptransaction:
   url: 'http://loadbalancer/transaction/'
policies:
  - basic-auth
  - cors
  - expression
  - key-auth
  - log
  - oauth2
  - proxy
  - rate-limit
pipelines:
  default:
    apiEndpoints:
      - appconfig
      - appinvoice
      - apppay
      - apptransaction
    policies:
      - key-auth:
      - proxy:
          - action:
              serviceEndpoint: appconfig 
              changeOrigin: true
              prependPath:  false
              ignorePath:   false
              stripPath:    false
          - action:
              serviceEndpoint: appinvoice 
              changeOrigin: true
              prependPath:  false
              ignorePath:   false
              stripPath:    false
          - action:
              serviceEndpoint: apppay
              changeOrigin: true
              prependPath:  false
              ignorePath:   false
              stripPath:    false
          - action:
              serviceEndpoint: apptransaction 
              changeOrigin: true
              prependPath:  false
              ignorePath:   false
              stripPath:    false
```

After modifying the configuration in the previous file, within the pay-app-spring-microservices/appgw/ directory, we set up the Redis database and launch the gateway container:

To identify the characteristics, we need a data storage like redis

```
docker run --network distributed -d --name express-gateway-data-store \
                -p 6379:6379 \
                redis:alpine
```
Run the command inside the appgw directory or change the volume path to point to the folder containing the gateway.config.yml file

```
docker run -d --name express-gateway \
    --network distributed \
    -v .:/var/lib/eg \
    -p 8080:8080 \
    -p 9876:9876 \
    express-gateway
```

To connect to the gateway container, use the command:

```
docker exec -it express-gateway sh
```

Create a user that will have access to the microservices:

```
eg users create
```

Assign the authentication key to the user you just created:

```
eg credentials create -c jhorman -t key-auth -q
```

Save the generated authentication key.

Make a curl request to the API endpoints with the credentials:

```
curl -H "Authorization: apiKey (Auth-Key)" http://localhost:8080/config/app-pay/dev
```

## <b> Built with </b> 🛠


+ [Docker](https://www.docker.com/) - Docker is an open platform for developing, shipping, and running applications.


## **Versioned** 📌

<div style="text-align: left">
    <a href="https://git-scm.com/" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/2ae2a900d2f041da66e950e4d48052658d850630/icons/git/git-original.svg" height="60" width = "60" alt="Git"></a> 
    <a href="https://github.com/" target="_blank"> <img src="https://img.icons8.com/fluency-systems-filled/344/ffffff/github.png" height="60" width = "60" alt="GitHub"></a>
</div>




## <b> Made by </b>


+ [Camilo González Velasco](https://github.com/camilogonzalez7424 "Camilo G.")
+ [Icesi-Ops](https://github.com/icesi-ops")




<br>



[![forthebadge](https://forthebadge.com/images/badges/docker-container.png)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)

