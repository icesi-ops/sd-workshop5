# sd-workshop5

To execute the app we start generating the the required yml files.
First we need a name space:

```
apiVersion: v1
kind: Namespace
metadata:
name: w2
```

Next the redis database, we create a deployment and a service:

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app
  namespace: w2
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
        image: li1408/compose:v1
        ports:
        - containerPort: 5000
---
apiVersion: v1
kind: Service
metadata:
  name: loadbalancer
  namespace: w2
spec:
  type: LoadBalancer
  selector:
    app: app
  ports:
    - protocol: TCP
      port: 5000
      targetPort: 5000
```
The last step is creating the python app, we create a deployment and a service:

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: py-app-deploy
  namespace: w2
  labels:
   app: py-app
spec:
  replicas: 2 #Se que deberián ser 6 pero me traba el pc
  selector:
    matchLabels:
         app: py-app
  template:
    metadata:
      labels:
        app: py-app
    spec:
      containers:
      - name: py-app
        image: 'nelsonq2424/dockerpython2'
        ports:
          - containerPort: 5000
---
apiVersion: v1
kind: Service
metadata:
 name: py-app-service
 namespace: w2
 labels:
  app: py-app
spec:
 type: LoadBalancer
#  externalIPs:
#   - 192.168.49.2
 selector:
  app: py-app
 ports:
  - protocol: TCP
    port: 5000
    targetPort: 5000
```
Now we need the commands execute the app:

```
  kubectl create -f ns.yml
  kubectl create -f redis_app_deployment.yml
  kubectl create -f py_app_deployment.yml
```

Evidence of the app working:

```
  kubectl get all
```

![imagen](https://user-images.githubusercontent.com/44851531/169149789-6fbdff12-d274-4fae-9684-7108d7af2fc3.png)

![imagen](https://user-images.githubusercontent.com/44851531/169148871-bcc5886c-a9c3-4bf1-bc0c-09188691c905.png)

