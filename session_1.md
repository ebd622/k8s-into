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

<img src="images/master_worker.PNG" width="80%">


