# Case Study
This is a sample implementation for Saving account services with deposit and withdraw feature. This is designed using microservice architecture.  This application consists of following services 
* API Gateway
* Eureka
* OAuth
* Savings

Docker compose is used to orchestrate the application services.

## Setup
Following commands to work with the docker-compose:

Start
```
sh start
```

Stop
```
sh stop
```

Restart
```
sh restart
```

To monitor api gateway logs:
```shell
docker logs -f kadmos_gateway_1
```

## API Testing

Following users are seeded by default for jump start:

| *User* | *Password* |
|--------|:----------:|
| john   |    john    |
| jack   |    jack    |

Get JWT token
---
Use the following api to get JWT token to execute all the APIs.
```shell
response=$(curl --request POST \
  --url 'http://localhost:9180/oauth/token?username=john&password=john&grant_type=password&scope=read%20write' \
  --header 'Authorization: Basic Y2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: application/x-www-form-urlencoded' | jq '.')
  
access_token=$(echo "$response" | jq '.access_token' | tr -d '"' )
```

If you want to create your own user use following APIs:

Create new user for authentication
---
```shell
curl --request POST \
  --url http://localhost:9180/users \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer $access_token' \
  --data '{
    "userName": "swey",
    "password": "swey",
    "roles": "ROLE_USER"
  }' | jq '.'
```

Get existing user
---
```shell
curl --request GET \
  --url http://localhost:9180/api/users/aea5d679-c62e-47d9-bc62-79b4036aa90f \
  --header 'Authorization: Bearer $access_token' | jq '.'
```

Get balance of savings account
---
```shell
curl --request GET \
  --url http://localhost:8080/savings/accounts/c166f572-8379-4811-92f4-210c0d2c5a81/balance \
  --header 'Authorization: Bearer $access_token' | jq '.'
```

Increase balance of savings account
---
```shell
curl --request POST \
  --url http://localhost:8080/savings/accounts/c166f572-8379-4811-92f4-210c0d2c5a81/balance \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer $access_token' \
  --data '{
    "operationType":"DEPOSIT",
    "balance": 1.00
    }' | jq '.'
```

Decrease balance of savings account
---
```shell
curl --request POST \
  --url http://localhost:8080/savings/accounts/c166f572-8379-4811-92f4-210c0d2c5a81/balance \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer $access_token' \
  --data '{
    "operationType":"WITHDRAW",
    "balance": 1.00
    }' | jq '.'
```


## Enhancements


### Scale your API Gateway
API gateway is the gatekeeper for access to APIs, securing and managing traffic between API consumers and the applications that expose those APIs. The API gateway typically handles authentication and authorization, request routing to backends, rate limiting to avoid overloading systems and protect against DDoS attacks, offloading SSL/TLS traffic to improve performance, and handling errors or exceptions.

There are two options:
* Centralized, Edge Gateway
* Two-Tier Gateway
* Microgateway
* Per-Pod Gateways
* Sidecar Gateways and Service Mesh

### Microgateway
A microgateway gateway pattern builds on the two‑tier approach by providing a dedicated gateway to individual teams, which not only helps them manage traffic between services, but lets them make changes without impacting other applications.

This pattern enables the following capabilities at the edge:
* SSL/TLS termination
* Routing
* Rate limiting 
Organizations then add individual microgateways for each service that manage:
* Load balancing
* Service discovery
* Authentication per API

### NGINX Kubernetes Gateway
Evolution of an Ingress controller, NGINX Kubernetes Gateway addresses the challenges of enabling multiple teams to manage Kubernetes infrastructures in modern customer environments. It also helps simplify deployment and administration by delivering many capabilities without the need to implement Custom Resources(CRDs).

### Monitor uptime so you can sleep at night?
Expose Prometheus metrics like health end point from each service and create alert to email or slack channel when
* Service is down
* API error rate increase
* CPU and Memory usage thresh hold(75%) reached etc.

e.g: http://localhost:8081/actuator/metrics/
