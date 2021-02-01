# Voting-app with Helm

This is an example of how to deploy a [Voting-app](https://github.com/ebd622/k8s-into/blob/master/session_4.md) using a Helm chart. 

**Prerequisites:** The package manager [Helm](https://helm.sh/) needs to be installed in your environment to deploy the example. Check out [Installing Helm](https://helm.sh/docs/intro/install/) for details.

## Step-by-step deployment

**1. Clone the repository**
```
git clone https://github.com/ebd622/k8s-into.git
cd src/voting-app-chart
```

**2. Install the chart**

Run the following command just to see generated templates (no deployment):

```
helm install voting-app --dry-run .
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
        - voting-service:80
```
The containers use `wget` to check whether `voting-app` and `resul-app` are up and running. 

**5. Uninstall the chart**
```
helm uninstall voting-app
```
