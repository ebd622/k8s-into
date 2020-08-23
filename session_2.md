# Kubernetes Services, Controllers and Deployment


## Services

According to the Kubernetes [documentation](https://kubernetes.io/docs/concepts/services-networking/service/) - a Service is an abstraction which defines a logical set of Pods and a policy by which to access them (sometimes this pattern is called a micro-service).

Services help us to connect kubernetes applications together with other applications users. 

<img src="images/serv1.PNG" width="80%">

For example, our application is a group of pods - front-end, back-end, DB. In Kubernetes world PODs don't communicate directly to each other, services are used for this.
The services enable [loose coupling](https://www.linkedin.com/pulse/loose-coupling-microservice-architecture-patrick-van-vuuren/) between micro-services in our application. 


### Use Case example

Let's look at one use case for the services.

We deployed our web-application as POD to a cluster - how can an external user can access a page?

<img src="images/serv2_0.PNG" width="60%">


The existing setup:
* A cloud has one node with the IP 192.168.1.2;
* A user laptop is on the same network and has IP  192.168.1.10;
* The internal POD network is in the range 10.244.0.0;
* The POD is IP 10.244.0.2;

It is not possible to access (ping) the POD from the user's laptop because they are in different networks.

What are the options to see the web-page?

It is possible to SSH from the laptop to the Node and run access the POD **from the Node** like this:

```
ssh 192.168.1.2
curl http://10.244.0.2
```

This will work but this is not what we really want.

We want to access the POD directly from the laptop running the command like this:

```
curl http://192.168.1.2
```

So, we need something that will help us to access the **POD through the Node** from the laptop. 

<img src="images/serv2.PNG" width="60%">

This is where a Kubernetes Service comes to help. A Service listens a request on a Node-port and forward the request to a POD. This type service called **NodePort**


### Services Types

<img src="images/serv3.PNG" width="60%">

* *NodePort*: the service makes an **internal port of POD** available on a **Node**;
* *ClusterIP*: a service creates a virtual IP inside a cluster to enable communication of different services (such as set of front-end servers to a set of back-end servers);
* *LoadBalancer*: it provides a load-balancing for an application in supported cloud providers;

### Service NodePort

<img src="images/serv4.PNG" width="80%">

* Node, Service and POD have their own IP addresses;
* Service forwards requests to the TargerPort (80);
* NodePort (30008) is used to access the web-service externally; 
* In a service-definition file: if targetPort is not provided - it is assumed to be the same as a port;
* In a service-definition file: if **nodePort** is not provided - a  free port in a (range 30000-32767) will be automatically allocated;

#### Create a service

As any Kuberneted object, a service can be created with a definition file (declarative way):

<img src="images/serv5.PNG" width="80%">

* Services CANNOT connect to PODs via POD-IP addresses (because POD-IPs are not static and changed every time when POD is created and destroyed);
* Services are connected to PODs via POD-labels (POD-labels are still the same when POD is created/deleted);

#### Commands to create, get and request a service

Create a service from a definition-file:
```
kubectl create -f service-definition.yaml
```

Get created services:
```
kubectl get services
```

<img src="images/serv6.PNG" width="80%">

1. Link a service to a POD;
2. See that a service has been created;
3. Request (externally) a web-service via a node-IP address (192.168.1.2)


Service can be mapped NOT only to one POD. There are different cases:
* Service is mapped to one POD (the example above);
* Service is mapped to **many PODs** running on **the same Node**;
* Service is mapped to **many PODs** running on **different Nodes**;


#### Service is mapped to many PODs running on the same Node

This is use case example:

<img src="images/serv7.PNG" width="60%">

* Service is mapped to PODs via labels during creation of a service;
* Service automatically selects three pods as end-points to forward requests coming from users;
* you don’t need to make any additional configuration to make this happen!
* Service use a random algorithm to select a POD to forward requests;
* Here service is running as a load balancer to distribute load across different PODs;


#### Service is mapped to many PODs running on different Nodes

<img src="images/serv8.PNG" width="60%">

* Kubernetes automatically creates a service that spreads across all the nodes in a cluster and maps targetPort-s in PODs to the same service port;
* In that way you can access an application using an IP of any node in a cluster and using the same port (in the example it is 3008);
* When PODs are created/deleted a service is automatically updated;
* Normally when a service once created you don’t have to do anything with it; 


### Service CLusterIP

ClusterIP services provide a single interface to communicate one **group of PODs** with **another group** (can be also one-to-one POD):

<img src="images/serv9.PNG" width="60%">

#### Defifnition file

Here is the example of the ClusterIP service definition file:

<img src="images/serv10.PNG" width="80%">

* **targerPort** - is a port where back-end is exposed;
* **port** - is a port where service is exposed; 
* for linking a service to a set of PODs we use selectors (labels in a POD);


#### Create and check

The following command is used to create a service from a definition-file (i.e from a manifest):

```
kubectl create -f service-definition.yaml
```

<img src="images/serv11.PNG" width="80%">

It is also possible to create a service exposing  a running pod with the command:

```
kubectl expose pod nginx --port=90 --target-port=80 --type=ClusterIP
```
* **type** is a type service (ClusterIP, NodePort or LoadBalancer). Default type is ClusterIP

More details regarding the "expose" command can be found in the [documentation](https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#expose).



## Replication Controller

Kubernetes Controllers are the brain behind Kubernetes. Controllers are processes that monitor kubernetes objects and respond accordingly. Let's consider one controller in particular - Replication Controller.

Replication Controller is used to provide customers with

* High Availability
* Load Balancing and Scaling


### High Availability

When an application crashes for some reason the POD will fail. It means that users will no longer be able to access our application. To prevent users from losing access to our application, we would like to have more than one instance or POD running at the same time. 


That way if one fails we still have our application running on the other one. The replication controller helps to run and control multiple instances of a single POD in the kubernetes cluster thus providing **High Availability**.

<img src="images/RC_1.PNG" width="80%">

Replica Controller works also for a single POD - if you have a single POD, the replication controller can help by automatically bringing up a new POD when the existing one fails. 
Thus the replication controller ensures that the specified number of PODs are running at all times. Even if it’s just 1 or 100.


### Load Balancing and Scaling

Another reason we need replication controller is to create multiple PODs to share the load across them. When the number of users increase we deploy additional POD to balance the load across the pods. 

If the demand further increases and If we were to run out of resources on the first node, we could deploy additional PODs across other nodes in the cluster. 

<img src="images/RC_2.PNG" width="80%">

As you can see, the replication controller spans across multiple nodes in the cluster. It helps us balance the load across multiple pods on different nodes.


### ReplicaController vs ReplicaSet

There are two similar terms: **Replication Controller** and **Replica Set**. Both have the same purpose but they are not the same. Replication
Controller is the older technology that is being replaced by Replica Set. Replica set is the new recommended way to setup replication. 

This is how a ReplicaSet is created:

<img src="images/RC_3.PNG" width="40%">

A section "selector" is a required section for ReplicaSet. This is a major difference between Replication Controller and Replica Set - in Replication Controller the section "selector" is NOT required but still available.PODs.

The ReplicaSet selector also provides many other options for matching labels that were not available in a replication controller.


### Labels and Selectors

What is the deal with Labels and Selectors? Why do we label our PODs and objects in kubernetes? Let us look at a simple scenario. Say we deployed 3 instances of our frontend web application as 3 PODs:

<img src="images/RC_4.PNG" width="100%">

We would like to create a replica set to ensure that we have 3 active PODs at anytime. This is one of the use cases of replica sets. 

You can use it to monitor existing pods, if you have them already created. In case they were not created, the replica set will create them for you. The role of the replicaset is to monitor the pods and if any of them were to fail, deploy new ones. The replica set is a process that monitors the pods. 

But how does the replicaset *know what pods to monitor*? There could be 100s of other PODs in the cluster running different application. This is
were labelling our PODs comes in handy. 

We could now provide these labels as a filter for replicaset. Under the selector section we use the **matchLabels** filter and provide the same label that we used while creating the pods. This way the replicaset knows which pods to monitor.

The same concept labels/selectors is used in many other places in Kubernetes.

### Scale

How can we scale replicas? There are different ways to do this:

* Update the number of replicas in the definition file and run the kubectl **replace** command:

```
kubectl replace -f replica-definition.yml
```

* run the kubectl **scale** command:

```
kubectl scale --replicas=3 <replica-set-name>
```

## Deployments

Deployment is a kubernetes object that comes higher than POD and ReplicaSet in the hierarchy. The deployment provides us with capabilities to upgrade the underlying instances seamlessly using rolling updates, undo changes, and pause and resume changes to deployments.

<img src="images/deployment_1.PNG" width="100%">

The following use cases are described in details in the [Kubernetes documentation](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#use-case):

* Create a Deployment to rollout a ReplicaSet. The ReplicaSet creates Pods in the background. Check the status of the rollout to see if it succeeds or not.
* Declare the new state of the Pods by updating the PodTemplateSpec of the Deployment. A new ReplicaSet is created and the Deployment manages moving the Pods from the old ReplicaSet to the new one at a controlled rate. Each new ReplicaSet updates the revision of the Deployment.
* Rollback to an earlier Deployment revision if the current state of the Deployment is not stable. Each rollback updates the revision of the Deployment.
* Scale up the Deployment to facilitate more load.
* Pause the Deployment to apply multiple fixes to its PodTemplateSpec and then resume it to start a new rollout. Use the status of the Deployment as an indicator that a rollout has stuck.
* Clean up older ReplicaSets that you don't need anymore.

### Create Deployment
The deployment definition file is very similar to ReplicaSet, just replace "ReplicaSet" with "Deployment":

<img src="images/deployment_2.PNG" width="100%">

So far there hasn’t been much of a difference between replicaset and deployments, except for the fact that deployments created a new kubernetes object. We will see how to take advantage of the deployment later.


### Rollout and Versioning

Let’s look into Rollouts and Versioning in a deployment. 

<img src="images/deployment_3.PNG" width="100%">

Whenever you create a new deployment or upgrade the images in an existing deployment it triggers a Rollout. A **rollout** is the process of
deploying or upgrading your application containers. When you first create a deployment, it triggers a rollout. 

A new rollout creates a new Deployment revision (let’s call it Revision 1). In the future when the application is upgraded – a new rollout is triggered and a new deployment revision is created - Revision 2. 

This helps us keep track of the changes made to our deployment and enables us to rollback to a previous version of deployment if necessary.

To see the status of your rollout - run the command:

```
kubectl rollout status <deployment-name>
```

To see the revisions and history of rollout run the command:

```
kubectl rollout history <deployment-name>
```

### Deployment Strategy

 There are two types of deployment strategies:
 * Recreate
 * Rolling update

<img src="images/deployment_4.PNG" width="100%">

#### Recreate
 
The first strategy is to destroy all the old application instances and then create newer versions of application instances. Meaning first, destroy, for example, the 5 running instances and then deploy 5 new instances of the new application version. 

The problem with this as you can imagine, is that during the period after the older versions are down and before any newer version is up, the application is down and inaccessible to users. This strategy is known as the **Recreate** strategy, and this is NOT the *default* deployment strategy. 

#### Rolling update

The second strategy is were we do not destroy all of them at once. Instead we take down the older version and bring up a newer version one by one. This way the application never goes down and the upgrade is seamless.

if you do not specify a strategy while creating the deployment, it will assume it to be Rolling Update. In other words, RollingUpdate is the default Deployment Strategy.

There are a few ways to update a deployment:
* Modify a deployment-definition yaml-file and the command:

```
kubectl apply f deployment-definition.yml
```

* Run the command to set a new image:

```
kubectl set image <deployment-name> deployment nginx=nginx:1.9.1
```

* Run the command to edit a running deployment:

```
kubectl edit deployment <deployment-name>
```

The difference between the recreate and rollingupdate strategies can also be seen when you view the deployments in detail. Run the command to see detailed information regarding the deployments: 

```
kubectl describe deployment <deployment-name>
```

You will notice when the Recreate strategy was used the events indicate that the old replicaset was scaled down to 0 first and the new replica set scaled up to 5. 

However when the RollingUpdate strategy was used the old replica set was scaled down one at a time simultaneously scaling up the new replica set one at a time.



### Upgrades

<img src="images/upgrades.PNG" width="100%">



### Rollback

<img src="images/rollback.png" width="100%">






