docker build -t workshop .
docker network create distribuidos
docker run --network=distribuidos --name redis -d redis
docker run --network=distribuidos --name pythohn-app -d -p 5000:5000 --rm workshop
 
docker volume create dataredis
docker run --network=distribuidos --volume=dataredis --name redis -d redis redis-server --save 60 1 --loglevel warning
# redis-server --save 60 1 --loglevel warning, this command is used to execute inside redis

