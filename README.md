# sd-workshop5
## To run:
## First, run redis deployment with:
kubectl create -f redis-deployment.yml
## Then, run python app deployment with:
kubectl create -f python-deployment.yml
## Then, find your external IP with:
kubectl get service pythonbalancer
## Finally, access your app at port :8000
