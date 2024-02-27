# Workshop4

# STEPS TO ACTIVATE THE SERVICES DISCOVERER (CONSUL)

• First you must install the dnsmasq service that allows you to have a DNS service.
• Then you must create a file (preferably with the name 10-consul) in the etc/dnsmasq.d directory where you must put the server with the loopback address and port like this

_Server=/consul/127.0.0.1#8600_

• Then the service must be restarted
• Finally, the resolv.conf file must be modified to allow DNS to resolve services found on the host machine, as follows

_name server 127.0.0.1_

To confirm that the DNS service is resolving the IP address of each microservice, use the command dig <service name>.service.consul (in the terminal), and on the screen enter the local address localhost:8500 through the browser


