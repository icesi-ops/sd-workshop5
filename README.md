# WorkShop 5
Configuración del balanceador de cargas y del ApiGateway para los microservicios desplegados con anterioridad

## Balanceador de Cargas
Haciendo uso del haproxy, generamos el balanceador de carga que nos ayudará a repartir el flujo de trabajo entre múltiples instancias del mismo microservicio


En el archivo haproxy.cfg, completamos la configuración de los endpoints necesarios para nuestro desarrollo, agregando las rutas en el frontend, backend y els ervidor de dns que responde a las peticiones.

````
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
````

## ApiGateway con Express-Gateway y Redis

Para configurar el apigateway debemos modificar el archivo gateway.config.yml, agregando los endpoints con sus rutas, las url de acceso del lado del balanceador de cargas, es importante recordar que el dominio "loadbalancer" en esta url corresponde al nombre de la imagen con la que se construyó el contenedor.

Además este archivo contiene las politicas de acceso y seguridad que se usarán para consumir la información de los microservicios.

````
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
````

Luego de modificar la configuración en el archivo anterior, dentro de la ruta pay-app-spring-microservices/appgw/ generamos la base de datos redis y lanzamos el contenedor del gateway:

- #### Para identificar las características, necesitamos un almacenamiento de datos como redis 
````
docker run --network distribuidos -d --name express-gateway-data-store \
                -p 6379:6379 \
                redis:alpine
````

- #### Correr el comando dentro del directorio de appgw o cambiar la ruta del volumen apuntando a la carpeta donde esté contenido el archivo gateway.config.yml
````
docker run -d --name express-gateway \
    --network distribuidos \
    -v .:/var/lib/eg \
    -p 8080:8080 \
    -p 9876:9876 \
    express-gateway
````
- Para conectarnos al contenedor del gateway usamos el comando 
```` 
docker exec -it express-gateway sh
````
- Crear usuario que tendrá acceso a los microservicios
````
eg users create
````

- Asignamos la llave de autenticación al usuario que acabamos de crear

````
eg credentials create -c jhorman -t key-auth -q
````

- Guardamos la llave de autenticación que se genera

- Hacemos curl a los endpoint del API con las credenciales

````
curl -H "Authorization: apiKey (Auth-Key)" http://localhost:8080/config/app-pay/dev
````
