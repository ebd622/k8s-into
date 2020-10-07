# Assignment

Create a kubernetes implementation of the famous [Voting App](https://github.com/dockersamples/example-voting-app) example

## Arcitecture

![GitHub Logo](/images/voting_app_1.svg)

Task: deploy all micriservices according to the architechture

## Requests
Voting app:
```
http://192.168.99.100:31781
```

Result app:
```
http://192.168.99.100:31364
```

POST request to Voting app:
```
curl -X POST http://192.168.99.100:31781 -d "vote=a"
```
- `vote=a`: vote for a cat
- `vote=b`: vote for a dog


## Steps

#### 1. Create voting-service
- `name`: voting-service
- `type`: NodePort
- `port`: 80
- `targetPort`: 80
- `nodePort`: 31781
- `labels`: ...
- `selector`: ...


#### 2. Create voting-app-deployment
TODO

#### 3. Create redis service
TODO

#### 4. Create redis-deployment
TODO

#### 5. Create worker-app-deployment
TODO

Worker app [java](https://github.com/dockersamples/example-voting-app/blob/master/worker/src/main/java/worker/Worker.java) version

#### 6. Create result-service
TODO

#### 7. Create result-app-deployment
TODO

#### 8. Create db service
TODO

#### 9. Create postgres-deployment
TODO





