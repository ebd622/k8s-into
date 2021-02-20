# Voting-app with Helm

This is an example of how to deploy a [Voting-app](https://github.com/ebd622/k8s-into/blob/master/session_4.md) using a Helm chart. 

**Prerequisites:** The package manager [Helm](https://helm.sh/) needs to be installed in your environment to deploy the example. Check out [Installing Helm](https://helm.sh/docs/intro/install/) for details.

## Step-by-step deployment

**1. Clone the repository**
```
git clone https://github.com/ebd622/k8s-into.git
cd k8s-into/src/api-demo-chart/
```

**2. Install the chart**

Run the following command just to see generated templates (no deployment):

```
helm install voting-app --dry-run .
```
or use `helm template` command to render chart templates locally and display the output:
```
helm template voting-app .
```

By default Helm will pick up the file `values.yaml` to get deployment parameters. You can provide another file using the flag `-f` if you want to provide another values-file:

```
helm install -f my-values.yaml voting-app --dry-run .
```

Install the chart:
```
helm install voting-app .
```
A chart with the name voting-app will be installed.
 

**3. Get info about a deployment**

List all the deployments:
```
helm ls
```
Get a status of the deployment:

```
helm status voting-app
```

Get deployment history of the chart:
```
helm history voting-app
```
You can also run kubectl command to check kubernetes deployments and pods

```
kubectl get deployment,pod
```
When all pods are up and running the expected result should be like this:


```javascript
NAME                                    READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/postgres-deployment     1/1     1            1           5m58s
deployment.apps/redis-deployment        1/1     1            1           5m58s
deployment.apps/result-app-deployment   1/1     1            1           5m58s
deployment.apps/api-deployment   1/1     1            1           5m58s
deployment.apps/worker-app-deployment   1/1     1            1           5m58s

NAME                                         READY   STATUS    RESTARTS   AGE
pod/postgres-deployment-77df95cdd7-nmx56     1/1     Running   0          5m58s
pod/redis-deployment-5d7988b4bb-zlj9q        1/1     Running   0          5m58s
pod/result-app-deployment-7cdc94dfcd-75wn4   1/1     Running   0          5m58s
pod/api-deployment-678c67fc7-q7fjh    1/1     Running   0          5m58s
pod/worker-app-deployment-767d5b67ff-jwb5d   1/1     Running   0          5m58s
```


**4. Run tests**

```
helm test voting-app
```

A test-pod includes two containers, both are created from the image `busybox`:

```javascript
  containers:
    - name: test-result-app
      image: busybox
      command: [wget]
      args:
      - -O-
      - result-service:80
    - name: test-voting-app
      image: busybox
      command: [wget]
      args:
        - -O-
        - api-service:80
```
The containers use `wget` to check whether `voting-app` and `resul-app` are up and running. 

**5. Uninstall the chart**
```
helm uninstall voting-app
```
After running the command all the kubernetes objects, earlier created with the Helm chart, will be removed from the cluster.
