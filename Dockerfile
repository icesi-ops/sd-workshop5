FROM python:3.9-alpine
ADD . /app
WORKDIR /app
RUN pip install -r requirements.txt
CMD ["python", "count-app.py"]
