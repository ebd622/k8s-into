(under construction)

# Springboot Helm demo

1. Build the project and create an image

```
mvn clean install
```
2. Tag the created image
```
docker tag <IMAGE_ID> ebd622/springboot-helm-demo:<TAG>
```

3. Push images to the Docker Hub
```
docker push ebd622/springboot-helm-demo:<TAG>
```
