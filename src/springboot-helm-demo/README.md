(under construction)

# Springboot Helm demo

This is a simple Springboot API-application developed for the [Helm-demo](https://github.com/ebd622/k8s-into/blob/master/session_5.md) session.

The application exposes the end point:

```
curl localhost:8080/api/info
```



### Build an impage and push it to DockerHub

1. Build the project and create an image

```
mvn clean install
```
2. Tag the created image
```
docker tag <IMAGE_ID> ebd622/springboot-helm-demo:<TAG>
```

3. Push the images to Docker Hub
```
docker push ebd622/springboot-helm-demo:<TAG>
```
