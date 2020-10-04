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
TODO
