# sd-workshop5

1. Change the haproxy.cfg Configuration
Make necessary changes to the haproxy.cfg configuration file according to your project requirements.

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/2a941e15-9991-4b4c-ab94-8efc956fac0c)


2. Build the Load Balancer Image
Build the load balancer image using the provided Dockerfile.

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/9647a9d9-eb38-4ba5-9586-34b994962386)

3. Run the Load Balancer Image
Run the previously built load balancer image with the following command:

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/ee64cf10-cd6b-47b2-96ac-7b347b1c6492)

4. Run the data store of express gateway:

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/cb3982bc-abc4-46d5-874f-5340ac846c89)


5. Configure the Gateway.config.yml File
Navigate to the appgw directory inside the repository and configure the Gateway.config.yml file according to your specifications.

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/2d1a1c67-9c2d-4976-9771-8ae8d31e5dfd)

6. Run the Express Gateway Container
After configuring the Gateway.config.yml file, run the following command from the location of that file:

docker run -d --name express-gateway --network distributed -v "$(pwd)":/var/lib/eg -p 8080:8080 -p 9876:9876 express-gateway

7. Enter the Console of the Express Gateway Container
Access the console of the express-gateway container to create a user and generate credentials with key-auth. Use the following command to enter the container's console:

docker exec -it express-gateway bash

Then, create a user and generate credentials with key-auth.

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/1367c681-0791-4978-b35a-19902d8e69bc)


8. Perform Functionality Tests
After setting up the environment, perform the necessary tests to ensure the functionality of the Gateway API and the load balancer.

![image](https://github.com/SGutierrez-11/sd-workshop5/assets/69949511/4941f328-404f-480c-82ab-b75cb4a2b3a5)


Note:
The key generated for key-auth is: 19w70xPkBCnefdcXq7DGBZ:0QHnHOj48hhV00fymofzW9. Make sure to copy it for making subsequent requests.
