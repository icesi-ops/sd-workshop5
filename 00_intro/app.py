from flask import Flask
from redis import Redis
import os

app = Flask(__name__)
redis = Redis(
    host=os.environ["REDIS_DB_SERVICE_HOST"],
    port=int(os.environ["REDIS_DB_SERVICE_PORT"])
)

@app.route('/')
def hello():
    count = redis.incr('hits')
    return 'Hello World! I have been seen {} times.\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
