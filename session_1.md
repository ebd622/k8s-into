# Chapter time 1

### Container Orchectration
Container orchestration is all about managing the lifecycles of containers, especially in large, dynamic environments.

Basic questions to be addressed:
* What if your application relies on other containers such as database or messaging services or
other backend services?
* What if the number of users increase and you need to scale your application? You would also like to scale down when the load decreases

<img src="images/ochestration.PNG" width="80%">

To enable these functionalities you need an underlying platform with a set of resources. The platform needs to orchestrate the connectivity between the
containers and automatically scale up or down based on the load. This whole process of automatically deploying and managing containers is known as **Container Orchestration**.

#### Orchestration Technologies:
* Docker Swarm
* Kubernentes
* MESOS



### Kubernetes Architecture
What is a Kubernetes cluster?

<img src="images/arch_1.PNG" width="80%">

* A cluster is a set of nodes grouped together. 
* A node (also known as a minion in the past) is a machine physical or virtual on which  kubernetes is installed. 
*

#### Components

<img src="images/components.PNG" width="70%">


* **API server** acts as the front end for kubernetes. The users, management devices, Command line interfaces all talk to the API server to interact with the kubernetes cluster.


* **ETCD key store**. [ETCD](https://etcd.io) is a distributed reliable key value store used by kubernetes to store all data used to manage the cluster. 
  * When you have multiple nodes and multiple masters in your cluster, **etcd** stores all that information on all the nodes in the cluster in a distributed manner. 

* **Scheduler** is responsible for distributing work or containers across multiple nodes. It looks for newly created containers and assigns them to Nodes.

* **Controllers** are the brain behind orchestration. They are responsible for noticing and responding when nodes, containers or endpoints goes down. The controllers makes decisions to bring up new containers in such cases.

* **Kubelet** is the agent that runs on each node in the cluster. The agent is responsible for making sure that the containers are running on the nodes as expected.

* **Container Runtime** is a container hosting platform. This doesn’t habe to be docker, there are other container runtime alternatives available such as *Rocket* or *CRIO*

#### Master vs Worker
So, there are two types of servers – Master and Worker(s) and a set of components that make up Kubernetes.
But how are these components distributed across different types of servers. In other words, how does one server become a master and the other slave?

<img src="images/master_worker.PNG" width="80%">

* The worker node is were the containers are hosted;
* The worker nodes have the kubelet agent that is responsible for interacting with the master;
* The master server has the **API server** and that is what makes it a master;
* All the information gathered are stored in a key value store on the Master;
* The master also has the controller manager and the scheduler;

There are also other components as well, but we will stop there. The idea now is to understand what components constitute the master and
worker nodes. 

#### kubectl CLI

*Kubectl CLI* is the kube command line tool or **kubectl** or kube control as it is also called. The **kubectl**  is used to deploy and manage applications on a kubernetes cluster, to get cluster information, get the status of nodes in the cluster and many other things.

Examples:

```
kubectl cluster-info
```
The command is used to view information about the cluster


```
kubectl get nodes
```
The command is used to list all the nodes of the cluster

### Kubernetes Concepts
POD. What is a POD?

A POD is the smallest object, that you can create in Kubernetes.
Kubernetes does not deploy containers directly on the worker nodes. The containers are encapsulated into a POD. A POD is a single instance of an application. 

Here we see the simplest case were you have a single node kubernetes cluster with a single instance of your application running in a single docker container encapsulated in a POD.

<img src="images/pod_1.PNG" width="80%">

What if the number of users accessing your application increase and you need to scale your application? We create a new POD with a new instance of the same application.

What if the user base FURTHER increases and your current node has no sufficient capacity? Then you deploy additional PODs on a new node in the cluster. You will have a new node added to the cluster to expand the cluster’s physical capacity.

<img src="images/pod_2.PNG" width="80%">
