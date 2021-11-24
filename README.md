# Workshop 5

# 1️⃣ Build the app as a Docker Image in DockerHub

```bash
# Build the image
docker build -t python-counter-app .

# Tag the image
docker tag python-counter-app camilaleniss/python-counter-app

# Login to docker with your docker Id
docker login

# Push the image to docker hub
docker push camilaleniss/python-counter-app
```

# 2️⃣ Running the app

```bash
# Start the Docker service
sudo systemctl start docker

# Start minikube for local deployment
minikube start

# Apply the configuration in the yml of the path 
kubectl apply -f ./
```

# 3️⃣ Test the app

```bash
# Make a request to the url defined for the python-counter-app service
# This is the load balancer that will show that is load balancing trough three pods
curl $(minikube service python-counter-app --url)
```