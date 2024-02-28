# Workshop5

By: Samuel Guerrero

1. Configure the haproxy.cfg file

2. Build the docker image for loadBalancer

```
docker build -t loadbalancer:0.0.1 .
```

3. Run the recently built image

```
docker run -d -p 80:80 -p 1936:1936 --network distribuidos --name loadbalancer loadbalancer:0.0.1
```
4. Run the database por the application gateway 

```
docker run --network distribuidos -d --name express-gateway-data-store -p 6379:6379 redis:alpine
``` 

5. Edit the gateway.config.yml file

6. Run in this location the express gateway
```
docker run -d --name express-gateway --network distribuidos -v .:/var/lib/eg -p 8080:8080 -p 9876:9876 express-gateway
```
7. Create a user inside the cointainers termianl with the following command
```
eg users create
```
8. Generate a authentication key for that user with the following command

```
eg credentials create -c username -t key-auth -q
```

9. After use these 2 commands to validate everything

```
curl -H "Authorization: apiKey ${keyId}:${keySecret}" http://localhost:8080/config/app-pay/dev
curl localhost:80/config/app-pay/dev
```


## Evidence

![Consul](/assets/curl.png)
![Consul](/assets/loadbalancer.png)
![Consul](/assets/express.png)
