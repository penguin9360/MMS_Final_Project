

# Backend Deployment



## (1) Create a Google Cloud Platform Project

For deploying the backend code, we should firstly create a Google Cloud Platform Project.

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/creat_project.png?raw=true)



## (2) Firebase Storage Deployment

After we have created a project in GCP, we can create a project in firebase for deploying our Android App. When we open the creating project page, there will be a button to directly link to the GCP project. We can click this button.

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/firebase.png)

After creating projetc, we can set a storage bucket in firebase.

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/firebase_store.png)

Choose the location and click 'Done'

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/firebase_store_1.png)

As the default set, an authentication is needed to connect firebase from frontend. 

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/firebase_store2.png)

But in order to test the program more easily, we temporally allow all access request. So the rule should be changed as followed.

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/firebase_store3.png)

## (3) Cloud Function Deployment

Once the fire store bucket is created, there will be a bucket in GCP storage. We can now deploy the cloud function. The function should be triggered once there are new files cheated in the selected storage bucket.

![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/cloud_function.png)

The entry point is $process$ function, and the compiler is python 3.9. The .py file and requirement file will be handed on with this report.
![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/cloud_fucntion1.png)
![creat_project](https://github.com/penguin9360/MMS_Final_Project/blob/main/Backend/Figures/cloud_function2.png)
