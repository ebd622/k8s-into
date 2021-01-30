# Voting-app with Helm

This is an example of how to deploy a Voting-app using a Helm chart

**1. Clone the repository**
```
git clone https://github.com/ebd622/...
cd ...
```

**2. Install the chart**

Run the following command just to see generated templates (no deployment):

```
helm install voting-app --dry-run .
```

Install the chart:
```
helm install crud-app .
```
A chart with the name crud-app will be deployed
 

**3. Get info about deployment**

List all the deployments:
```
helm ls
```
Get deployment history:
```
helm history crud-app
```

**4. Run tests**

```
helm test crud-app
```

**5. Uninstall the chart**
```
helm uninstall crud-app
```
