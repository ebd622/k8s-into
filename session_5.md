(under construction)
# Introduction to Helm

- What is Helm
- Why do we need Helm
- Helm Architecture
- Creating your first Helm chart 
- Helm templating
- Values and parameters
- Installing and Upgrading a chart
- Control flows
- Helm Repositories

## What is Helm
TODO

## Why do we need Helm
In Kubernetes we describe everything what we do with yaml-files. These yaml-fails represent objects such as deployments, pods, services and so on. The objects are represented in **declarative** way. It means we tell Kubernetes exactly what we need and Kubernetes makes it happen. 

But managing all yaml-files can sometimes become a bit complicated. We may have many different yaml-files needed to deploy our applications/services to a Kubernetes cluster. All these yaml-s makes up our application and its infrastructure. 

It is important to notice here that all the yaml-s are not **reusable**. It means that if we want to build another application - we will have to copy/paste all the yaml-s and then change what is needed.


## Arcitecture

TODO


