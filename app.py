from flask import Flask
from redis import Redis
import os

app = Flask(__name__)
redis = Redis(host='REDISAPP_SERVICE_HOST', port=6379)

@app.route('/')
def hello():
    count = redis.incr('hits')
    return 'Hola mundo! He sido visto  {} veces.\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)

