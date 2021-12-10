---
typora-root-url: ./Figures
---

# Backend Deployment



## (1) Create a Google Cloud Platform Project

For deploying the backend code, we should firstly create a Google Cloud Platform Project.

![creat_project](/creat_project.png)



## (2) Firebase Storage Deployment

After we have created a project in GCP, we can create a project in firebase for deploying our Android App. When we open the creating project page, there will be a button to directly link to the GCP project. We can click this button.



After creating projetc, we can set a storage bucket in firebase.



Choose the location and click 'Done'



As the default set, an authentication is needed to connect firebase from frontend. 



But in order to test the program more easily, we temporally allow all access request. So the rule should be changed as followed.



## (3) Cloud Function Deployment

Once the fire store bucket is created, there will be a bucket in GCP storage. We can now deploy the cloud function. The function should be triggered once there are new files cheated in the selected storage bucket.



The entry point is $process$ function, and the compiler is python 3.9. The .py file and requirement file will be handed on with this report.