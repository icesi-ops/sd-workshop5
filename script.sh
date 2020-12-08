#!/bin/bash
echo "Desplegando app web"
echo "."
echo ".."
kubectl create -f python_deploy.yml
echo "..."
echo "App web en python desplegada"
echo "Desplegando base de datos"
echo "."
echo ".."
kubectl create -f redis_deploy.yml
echo "..."
echo "Base de datos redis desplegada"
echo "La aplicación está lista para ser probada en la ip publica del balanceador que se muestra a continuación"
kubectl get svc
