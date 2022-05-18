FROM alpine

RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
RUN python3 -m ensurepip
RUN pip3 install --no-cache --upgrade pip setuptools

COPY . /app
RUN pip install -r /app/requirements.txt

EXPOSE 8080

CMD ["/usr/bin/python", "/app/app.py"]
