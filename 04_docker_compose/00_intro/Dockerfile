FROM python
LABEL mail: javierandrestorresreyes@gmail.com
COPY . /code
WORKDIR /code
RUN pip3 install -r requirements.txt
EXPOSE 5000
CMD ["python3", "app.py"]
