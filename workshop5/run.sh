#!/bin/bash
#Primero incie el nodo de minikube
echo -----------------------------------
echo Se estan desplegando los recursos

kubectl apply -f NWork.yaml
kubectl apply -f DeployApp.yaml
kubectl apply -f DeployRedis.yaml

#IR al namespace para hacer pruebas
echo -----------------------------------
echo Redirigiendo al NS

kubectl config set-context --current --namespace=work5
