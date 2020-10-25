# Assignment

Create a kubernetes implementation of the famous [Voting App](https://dockerlabs.collabnix.com/play-with-docker/example-voting-app/) example normally used as a demo application for Docker.

## Arcitecture

The deployment includes two different front-end applications: `Vting app` and `Result app`, `Redis` queue, `Worker app` and `DB`:


![GitHub Logo](/images/voting_app_1.svg)

* The voting-app is a front-end web app in Python which lets you vote between two options;
* Redis is a queue which collects new votes;
* The worker-app is a [java](https://github.com/dockersamples/example-voting-app/blob/master/worker/src/main/java/worker/Worker.java) application which consumes votes and stores them in DB;
* The DB is a Postgres database;
* The result-app is a Node.js webapp which shows the results of the voting in real time;

## Sequence diagrams

<p align="center">
  <img src="/images/Making_Vote.png">
</p>

<p align="center">
  <img src="/images/Getting_results.png">
</p>

## Front-end applications

### Voting app

Use the following URL to open the Voting-app in a browser:
```
http://192.168.99.100:31781
```

For voting you can also make a POST request using `curl`:

```
curl -X POST http://192.168.99.100:31781 -d "vote=a"
```
- `vote=a`: vote for a cat
- `vote=b`: vote for a dog



### Result app:
Use the following URL to open the Result-app in a browser:

```
http://192.168.99.100:31364
```

## Steps

#### 1. Create voting-service
- `name`: voting-service
- `type`: NodePort
- `port`: 80
- `targetPort`: 80
- `nodePort`: 31781
- `labels`: ...
- `selector`: ...

(Check the solution: [voting-app-service.yml](https://github.com/ebd622/k8s-into/blob/master/src/voting-app/voting-app-service.yml))

#### 2. Create voting-app-deployment
- `name`: voting-app-deployment
- `replicas`: 3
- `image`: dockersamples/examplevotingapp_vote
- `labels`: ...
- `selector`: ...

(Check the solution: [voting-app-deployment.yml](https://github.com/ebd622/k8s-into/blob/master/src/voting-app/voting-app-deployment.yml))

#### 3. Create redis service
- `name`: redis-service
- `type`: NodePort
- `port`: 6379
- `targetPort`: 6379
- `labels`: ...
- `selector`: ...

(Check the solution: [redis-service.yml](https://github.com/ebd622/k8s-into/blob/master/src/voting-app/redis-service.yml))

#### 4. Create redis-deployment
- `name`: redis-deployment
- `replicas`: 1
- `image`: redis
- `containerPort`: 6379
- `labels`: ...
- `selector`: ...

(Check the solution: [redis-deployment.yml](https://github.com/ebd622/k8s-into/blob/master/src/voting-app/redis-deployment.yml))


#### 5. Create worker-app-deployment
TODO

Worker app [java](https://github.com/dockersamples/example-voting-app/blob/master/worker/src/main/java/worker/Worker.java) version

#### 6. Create result-service
- `name`: result-service
- `type`: NodePort
- `port`: 80
- `targetPort`: 80
- `nodePort`: 31364
- `labels`: ...
- `selector`: ...

#### 7. Create result-app-deployment
TODO

#### 8. Create db service
TODO

#### 9. Create postgres-deployment
TODO





