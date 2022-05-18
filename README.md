## sd-workshop5
Maria Diomar Ordoñez Ordoñez - A00355009
## app.py
### Deployment de app.py
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: app
      namespace: app-namespace
      labels:
       app: app
    spec:
      selector:
        matchLabels:
             app: app
      template:
        metadata:
          labels:
            app: app
        spec: 
          containers:
          - name: app
            image: ordonezmaria1/intro
            ports: 
              - containerPort: 5000
### Service LoadBalancer de app.py
    apiVersion: v1
    kind: Service
    metadata:
      name: app
      namespace: app-namespace
    spec:
      type: LoadBalancer
      selector:
        app: app
      ports:
       - protocol: TCP
         port: 5000
         targetPort: 5000
## redis
### Deployment redis
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: redis
      namespace: app-namespace
      labels:
        app: redis
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: redis
      template:
        metadata:
          labels:
            app: redis
        spec:
          containers:
          - name: redis
            image: "redis:alpine"
            ports:
            - containerPort: 6379
### Service redis
    apiVersion: v1
    kind: Service
    metadata:
      name: redis
      namespace: app-namespace
    spec:
      selector:
       app: redis
      ports:
       - protocol: TCP
         port: 6379
         targetPort: 6379
 ## Screenshot
 ![image](https://user-images.githubusercontent.com/47904094/169085508-2e4de12c-4042-49f9-9557-365bfc7274a1.png)
 ![image](https://user-images.githubusercontent.com/47904094/169085869-7a9d886a-67d7-47fa-884e-b7626e077d9c.png)


