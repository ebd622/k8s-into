# Introduction to Helm

##### Table of Contents  
* [Why do we need Helm](#Why-do-we-need-Helm)
* [What is Helm](#What-is-Helm)
* [Arcitecture](#Arcitecture)
    * [Helm 2](#Helm-2)
    * [Helm 3](#Helm-3)
* [Sharing Helm Charts](#sharing-helm-charts)
* [Work with Helm](#Work-with-Helm)
   * [Creating your first Helm chart](#Creating-your-first-Helm-chart)
   * [API Demo App architecture](#api-demo-app-architecture)
   * [Clone the git repo](#clone-the-git-repo)
   * [Test your templates before installing](#Test-your-templates-before-installing)
   * [Install a chart](#Install-a-chart)
   * [List releases](#List-releases)
   * [Run tests to check the deployment](#Run-tests-to-check-the-deployment)
   * [Values and parameters](#Values-and-parameters)
   * [Keep track on releases](#Keep-track-on-releases)
   * [Trigger deployment change when config changes](#Trigger-deployment-change-when-config-changes)
   * [Control Flows ](#Control-Flows)
* [Use Cases](#Use-Cases)
   * [Use case 1: use the same chart to deploy different app-versions](#use-case-1-use-the-same-chart-to-deploy-different-app-versions)
   * [Use case 2: use different values to deploy a chart in T, A and P](#use-case-2-use-different-values-to-deploy-a-chart-in-t-a-and-p)
* [Assignment](#Assignment)


## Why do we need Helm
In Kubernetes we describe everything what we do with yaml-files. These yaml-fails represent objects such as deployments, pods, services and so on. The objects are represented in *declarative* way. It means we tell Kubernetes exactly what we need and Kubernetes makes it happen. 

But managing all yaml-files can sometimes become a bit complicated. We may have many different yaml-files needed to deploy our applications/services to a Kubernetes cluster. All these yaml-s makes up our application and its infrastructure. 

It is important to notice here that all the yaml-s are not **reusable**. It means that if we want to build another application - we will have to copy/paste all the yaml-s and then change what is needed. If we need to deploy more applications we may end up with lots of yaml-s and lots of duplication. 

In over time these yaml-s may become hard to manage. And this is where Helm comes in.

## What is Helm
Helm is a package manager for Kubernetes. It is maintained by the [CNCF](https://cncf.io/) - in collaboration with  Microsoft,  Google,  [Bitnami](https://bitnami.com/) and the Helm contributor community. We can think of Helm as *apt*, *yum* or *Homebrew* for Kubernetes.

Helm allows us to bring all yaml-files together in what's called a ***Chart***. A chart can have a *name*, a *description* and a *version*. 

A chart groups all yaml-s together in a folder called ***templates***. To make this chart reusable we have the ability to inject values as parameters. We can deploy a chart and inject a name which we can use in our deployment, for example APP-1. We can reuse this chart and deploy it into the same Kubernetes cluster or somewhere else with another name - APP-2. 

Once we have this chart we can effectively reuse all yaml-s by injecting parameters into it. This gives us greater flexibility as we can reuse the chart among many applications and micro-services. 

*Injecting parameters in a chart makes our chart **generic***.


## Arcitecture

First we have to distinguish Helm 2 and Helm 3. 

### Helm 2
In  the version 2 Helm installation comes in two parts: a ***Helm Client*** (CLI) and a server called ***Tiller***:

<img src="images/Helm2-architecture.PNG" width="60%">


This architecture offers additional valuable feature which is **Release Management**. 

*Release Management*<br/>
Whenever you create or change deployment Tiller will store a copy of each configuration for future reference. In this way Tiller creates a history and keeps track of all chart executions. This allows to rollback to a previous revision in a case of any issue with a new deployment.

But Helm 2 architecture has a big caveat because Tiller has too much power inside of a Kubernetes cluster:
* Tiller can create, updated, delete components;
* Tiller has too much permissions within a cluster. 

This makes a big security issue. This is a reason why Tiller has been removed in Helm 3.


### Helm 3
Helm 3 uses the client-only architecture:

<img src="images/Helm3-architecture.PNG" width="60%">

Removing Tiller has solved a security concern making Helm more secure. But this also makes *Release Management* feature more challenging. 

## Sharing Helm Charts

One of the powerful feature of Helm is *sharing Charts via repositories*. This is one of a reason why Helm became so popular. 

Normally when we use Helm a generic work flow looks like this:

* Create your own Helm Charts;
* Push them into a Helm Repository (public or private ) to share it with others;
* Download and use existing charts when you need them;

Commonly used deployments like *DB Apps, ElasticSearch, Monitoring Apps* have a complex setup. But they all have charts already available in some Helm repository. 

There are many different public repositories available for us, here are just a few of them:

* https://charts.helm.sh/stable
* https://bitnami.com/stacks/helm

Using a simple Helm `install` command we can reuse a configuration that someone else has already made without additional effort from our side. Sometimes that "someone" is even a company that created an application.

As an example let's install the monitoring tool Prometheus & Grafana on our local Kubernetes cluster.

```
helm search repo list
```

Add the repository of stable charts:
```
helm repo add stable https://charts.helm.sh/stable
```

Search for all stable charts in the repo:
```
helm search repo stable
```

Search for the Prometheus chart:
```
helm search repo stable/prometheus
```

We will install the `Prometheus Operator for Kubernetes` that provides easy monitoring definitions for Kubernetes services and deployment and management of Prometheus instances:
```
helm install --generate-name stable/prometheus-operator
```

Once the chart is installed, let's check it and also Kubernetes objects: 

```
helm list
kubectl get pods
kubectl get svc
```

Now Pometheus & Grafana services are available within a cluster (`ClusterIP` is the default Kubernetes service), therefore they can not be accessed outside of cluster.

In order to access the web GUI from outside of cluster, we need to change default `ClusterIP` services to `NodePort`:

```
kubectl edit svc prometheus-operator-xxxxxxx-grafana
```

Let's check the updated service and find a NodePort number assigned by the cluster:
```
$ kubectl get svc prometheus-operator-xxxxxxx-grafana

NAME                                      TYPE      CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
prometheus-operator-xxxxxxx-grafana   NodePort   10.107.36.148      <none>        80:30393/TCP   6m36s
```
(Note: a port number is assigned by a Kubernetes cluster, it can be always different)

Now we can access Grafana from our local environment:
```
http://<CLUSTER_IP>:30393
```

Default credentials for Grafana:
```
username: admin
password: prom-operator
```

The same exercise can be done with Prometheus: edit the service `prometheus-operator-xxxxxx-prometheus` to replace `ClusterIP` with `NodePort`, check a port number and request Prometheus GUI from your browser.


## Work with Helm

### Creating your first Helm chart 

Helm chart is a *unit of deployment*, it is made up with a set of yaml files. We can have a chart for microservices or any software like Redis, MySQL, Prometeus and so on.

Use the following command to create a chart called `example-app`:

```
helm create example-app
```

This will create a new folder `example-app`:

```
└── example-app							
    ├── charts						# Charts that this chart depends on
    ├── Chart.yaml					# Information about your chart (Chart name, version, description)
    ├── templates					# The template files
    │   ├── deployment.yaml
    │   ├── _helpers.tpl
    │   ├── ingress.yaml
    │   ├── NOTES.txt
    │   ├── serviceaccount.yaml
    │   ├── service.yaml
    │   └── tests					# The test files (optional, can be removed)
    │       └── test-connection.yaml
    └── values.yaml					# The default values for your templates
```    
### API Demo App architecture
Further we will  play with a very simpe API Demo App.

<img src="images/api-demo-app-arch.svg" width="90%" >

The API exposes just one end point `/api/info` to retrieve a configuration details of the App. The details are provided in JSON-format.

It is a spring-boot based applicarion. It is dockerized, the image has been pushed into the Docker Hub.

There are three different versions of the API, returned JSON is also different for every version. The versions have their own [tags](https://hub.docker.com/r/ebd622/springboot-helm-demo/tags?page=1&ordering=last_updated) in the Docker Hub.


springboot-helm-demo:[1.0.0](https://hub.docker.com/layers/ebd622/springboot-helm-demo/1.0.0/images/sha256-3862163f6cb7f1dab75049d8fe07bd5add8097b81e77f56c5ee26edea1641008?context=explore)
```
{
app_version: "1.0.0",
environment: "test",
db_conf: "jdbc:mysql://server-t:3306/",
db_user: "user-t"
}
```
springboot-helm-demo:[2.0.0](https://hub.docker.com/layers/ebd622/springboot-helm-demo/2.0.0/images/sha256-a673be10e9819a442659fd4f8908f85e2aef5230b43f87f5eb9b42d9b9fb0339?context=explore)
```
{
app_version: "2.0.0",
environment: "test",
	db_properties: {
		db_conf: "jdbc:mysql://server-t:3306/",
		db_user: "user-t"
	}
}
```
springboot-helm-demo:[3.0.0](https://hub.docker.com/layers/ebd622/springboot-helm-demo/3.0.0/images/sha256-fe203c0d23b006bb3062d8eb4460fa6ee643dde0fe45ef9509337420d9c6cd5c?context=explore)
```
{
app_version: "3.0.0",
environment: "test",
	db_properties: {
		db_conf: "jdbc:mysql://server-t:3306/",
		db_user: "user-t"
	},
	kafka_consumer: {
		bootstrap_servers: "none",
		group_id: "none",
		key_deserializer: "none",
		value_deserializer: "none"
	}
}
```

### Clone the git repo

First let's create clone the git repo, swith to `master` and change a folder:

```
git clone https://github.com/ebd622/k8s-into.git
cd k8s-into/src/api-demo-chart/
```

Let's go ahead and see what Helm allows us to do.

### Test your templates before installing 

Before installing a chart it always makes sense to test rendering of templates 

```
helm template api-demo-chart .
```

Helm will generate all yaml-s without deploying them to a cluster. 

Helm `template` command is a really powerful command which allows us to test our templates by generating row kubernetes yaml-s. If anything is wrong with the templates then the command will generate an error.

### Install a chart

To Install  the chart run the command `install`:
```
helm install api-demo-chart .
```
Helm will install everything what is a part of the chart to a Kubernetes cluster.

Now you can access your API via URL:
```
http://<CLUSTER_IP>:30000/api/info
```

### List releases

Every time Helm deploys a chart it will create a release where it tracks the revision. The command `list` can be used to check revisions:

```
helm list
```

You can check all the components deployed into a cluster:

```
kubectl get all
```
### Run tests to check the deployment

When your chart is deployed you run the `test` command to check it:

```
helm test api-demo-chart
```

### Values and parameters

Values file and the parameter injection allows us to inject values into our chart making it reusable. 

[api-deployment.yaml](https://github.com/ebd622/k8s-into/blob/master/src/api-demo-chart/templates/api-deployment.yaml):
``` javascript 
    spec:
      containers:
      - name: api-app
        image: {{.Values.apiApp.image.repository}}:{{.Values.apiApp.image.tag}}
        ports:
        - containerPort: 80
        envFrom:
        - configMapRef:
            name: api-config
```

[values.yaml](https://github.com/ebd622/k8s-into/blob/master/src/api-demo-chart/values.yaml):
```
apiApp:
  image:
    repository: ebd622/springboot-helm-demo
    tag: 1.0.0
```

If you upgrade your values you are allowed to override them at the time of deployment. You can inject your custom values file with the command `upgrade`:

```
helm upgrade api-demo-chart . --values values.yaml
```

Helm will also upgrade our release.

When you simply need to override one or two values in your deployment you can do it over th command line:

```
helm upgrade api-demo-chart . --set apiApp.image.tag=3.0.0
```

In this case you can use a command line instead of using a values-file.

So, you can use this templating engine and notations to replace pretty  much anything you like making you chart more generic. 

For example, you can create a generic chart to allow your teams to deploy their microservices and share the same chart among many services. You will only need to use different `values.yaml` file.

### Keep track on releases

Every time when you create/upgrade your deployment Helm will create a new release.

The command `history` is used to see all releases:

```
helm history api-demo-chart
```

Run the command `rollback` if you want to rollback to one of the previous releases:
```
helm rollback api-demo-chart 2
```

### Trigger deployment change when config changes

By default, a deployment will not rollout new pods when a configmap changes. 

Some applications read configuration at start up and deployment may need to rollout new pods when a configuration changes. But when a configmap changes with a new values - the pod will not automatically restart to pick up these changes. 

We can use Helm's powerful template generation capability to overcome this issue. It allows us to forcefully rollout  new pods when a configmap changes. <br/>
With Helm we can trigger a change on deployment that will tell Kubernetes to forcefully rollout new pods and they will pick up the new configmap. 

To trigger a new deployment we need to detect changes on a configmap. For this we need to add `annotations` on a deployment object (in the section `spec` related to a replicaset):

```
    metadata:
      name: api-demo-pod
      annotations:
        checksum/config: {{include (print $.Template.BasePath "/api-config.yaml") . | sha256sum}}
```
This will create a checksum value of the `api-config.yaml`. Whenever this confg is changed the encoding of the statement will be different. <br/>
Here we use the `include` function, print the contents of the script and pipe it to a checksum function. Every time when configmap changes the checksum function result will be different. It will create a unique annotation on the deployment which will forcefully trigger a change in kubernetes. 

Here you can find more on [Functions and Pipelines](https://helm.sh/docs/chart_template_guide/functions_and_pipelines/#helm) in Helm.


### Control Flows 
Helm also allows to Control Flows. We can have `if` and `else` statements to generate more complex yaml based of values we pass in. We also can also set `defaults`.

Let's say we want to enforce things like **cpu** and **memory** limits. We can do it like allow the users to set their own limits or fall back to defaults if they are not supplied. To allow this capability we can use `if` and `else` statements in Helm which are really straightforward.

Checkout the branch `control_flow`

[values.yaml](https://github.com/ebd622/k8s-into/blob/control_flow/src/api-demo-chart/values.yaml):

```
deployment:
  resourcse:
    limits:
      memory: 512Mi
      cpu: 500m

```
(Meaning of `cpu` and `memory` units can be found in [Resource units in Kubernetes](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#resource-units-in-kubernetes)) <br/>

[api-deployment.yaml](https://github.com/ebd622/k8s-into/blob/control_flow/src/api-demo-chart/templates/api-deployment.yaml):

```
        {{- if .Values.deployment}}
        resources:
          limits:
            memory: {{.Values.deployment.resourcse.limits.memory | default "256Mi" | quote}}
            cpu: {{.Values.deployment.resourcse.limits.cpu | default "300m" | quote}}
          {{- else}}
          limits:
            memory: "128Mi"
            cpu: "100m"
        {{- end }}
```

When installing the chart Helm will process the logic and render a piece of code in `api-deployment.yam` into the following statement:

```
        resources:
          limits:
            memory: "256Mi"
            cpu: "500m"
```
Here you can find more on [Flow Control](https://helm.sh/docs/chart_template_guide/control_structures/#helm) in Helm.

## Use Cases

Let's go ahead and make our chart even more generic.

### Use case 1: use the same chart to deploy different app-versions

One think that we can do to make our chart more generic and reusable is to inject custom names for all kubernetes objects in yaml-s.

Check out the branch `use-case_1`:
```
git checkout use-case_1
```


Let's add a `name` of the application into `values.yaml`:
```javascript
apiApp:
  name: api-demo-v1
  ...
```

This will make sure that none of the objects clash and every time we deploy a new chart we can supply a specific `values.yaml` file for that application.

Now we can use `{{.Values.apiApp.name}}` in our yaml-s:


[api-deployment.yaml](https://github.com/ebd622/k8s-into/blob/use-case_1/src/api-demo-chart/templates/api-deployment.yaml):
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{.Values.apiApp.name}}
  labels:
    app: {{.Values.apiApp.name}}
...
```

[api-service.yaml](https://github.com/ebd622/k8s-into/blob/use-case_1/src/api-demo-chart/templates/api-service.yaml)
```
apiVersion: v1
kind: Service
metadata:
  name: {{.Values.apiApp.name}}
  labels:
    name: {{.Values.apiApp.name}}
    app: {{.Values.apiApp.name}}
...
```

[api-config.yaml](https://github.com/ebd622/k8s-into/blob/use-case_1/src/api-demo-chart/templates/api-config.yaml)
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{.Values.apiApp.name}}
...
```
Now our chart is more generic and we can deploy multiple instances of the chart.

For example we have two value-files with different names:

* [values-v1.yaml](https://github.com/ebd622/k8s-into/blob/use-case_1/src/api-demo-chart/values-v1.yaml):  with the application name `api-demo-v1`
* [values-v2.yaml](https://github.com/ebd622/k8s-into/blob/use-case_1/src/api-demo-chart/values-v2.yaml):  with the application name `api-demo-v2`

Let's install and check the chart.

Install the instance using `values-v1.yaml`:

```
helm install api-demo-chart-v1 . --values values-v1.yaml
```

Install the instance using `values-v2.yaml`:

```
helm install api-demo-chart-v2 . --values values-v2.yaml
```

We can see two instances of the same chart deployed into the cluster:

```
$ helm ls
NAME                    NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                   APP VERSION
api-demo-chart-v1       default         1               2021-02-21 21:13:41.2995337 +0100 CET   deployed        api-demo-chart-0.1.0    1.16.0
api-demo-chart-v2       default         1               2021-02-21 21:14:42.5354706 +0100 CET   deployed        api-demo-chart-0.1.0    1.16.0

```

### Use case 2: use different values to deploy a chart in T, A and P

Check out the branch `use-case_2`:

```
git checkout use-case_2
```
Different `values-x.yaml` files can be used to deploy the application in defferent environment. <br/>
For example, we may have environment specific value-files to deploy the application into T, A and P: 
* [values-t.yaml](https://github.com/ebd622/k8s-into/blob/use-case_2/src/api-demo-chart/values-t.yaml) - configuration values for Test
* [values-a.yaml](https://github.com/ebd622/k8s-into/blob/use-case_2/src/api-demo-chart/values-a.yaml) - configuration values for Acceptance
* [values-p.yaml](https://github.com/ebd622/k8s-into/blob/use-case_2/src/api-demo-chart/values-p.yaml) - configuration values for Prod

At the same time we will use "generic" template-files to deploy the appication.

Run the commands to install the chart using different values:

```
helm install api-demo-chart-t . --values values-t.yaml
helm install api-demo-chart-a . --values values-a.yaml
helm install api-demo-chart-p . --values values-p.yaml
```

When the application is installed you can also run tests:

```
helm test api-demo-chart-t
helm test api-demo-chart-a
helm test api-demo-chart-p
```

## Assignment

Let's imagine you have implemented additional functionality in your API and now need to connect API to Kafka. For this you have to update your Helm Chart to apply a configuration for Kafka consumer. 

##### What needs to be done:
* update the existing chart and apply new config-parameters for Kafka consumer;
* install the updated chart;
* make a call to `api/info` to check new configuration

##### Step-by-step implementation:
- Change `values.yaml`: upgrade the image-tag up to `3.0.0`
- Change `api-config.yaml` and `values.yaml`: add new data:
	* BOOTSTRAP_SERVERS = kafka-broker1:9092
	* GROUP_ID = API_GROUP_SEARCH
	* KEY_DESERIALIZER = StringDeserializerKey
	* VALUE_DESERIALIZER = StringDeserializerValue

- Install the chart;
- Check the deployment (List installed charts, make sure that a deployed `pod` is up and running) on a cluster;
- Check the updated configuration from a browser (`http://<CLUSTER_IP>:30000/api/info`). The expected result should be like this:

```
{
"app_version": "3.0.0",
"environment": "test",
	"db_properties": {
		"db_conf": "jdbc:mysql://server-t:3306/",
		"db_user": "user-t"
	},
	"kafka_consumer": {
		"bootstrap_servers": "kafka-broker1:9092",
		"group_id": "API_GROUP_SEARCH",
		"key_deserializer": "StringDeserializerKey",
		"value_deserializer": "StringDeserializerValue"
	}
}
```

- Change the value of `GROUP_ID` (either in `values.yaml` or in command line)
	* GROUP_ID = API_GROUP_SEARCH_INDEX

- Make sure that a new configuration is picked up by the API (see [Trigger deployment change when config changes](#trigger-deployment-change-when-config-changes));
- Check the new configuration in a browser;
- Check a `history`  of releases;
- Roll back a release to a previous revision;
- Check the configuration in a browser (should be `GROUP_ID: API_GROUP_SEARCH`);
- Uninstall the chart

(Checkout the branch `assignment` to see a solution)
