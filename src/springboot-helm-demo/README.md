# Springboot Helm demo

1. Build the project and create an image

```
mvn clean install
```
2. Tag am image
```
docker tag <IMAGE_ID> ebd622/springboot-helm-demo:<TAG>
```

3. Tag am image
```
docker push ebd622/springboot-helm-demo:<TAG>
```
