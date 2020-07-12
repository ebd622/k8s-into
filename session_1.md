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

#### Master vs Worker

<img src="images/master_worker.PNG" width="80%">


