# spring-and-amazon-s3
Create a server side application for interacting with a file system (Amazon S3) and MySQL using Java Spring.


This application is an advanced version of the application constructed in the following reopsitory since there was no file system involved - https://github.com/yatharthranjan/Spring-Mysql-Test
A file system is necessary because heroku has an ephermeral file system.

You can clone this and run this application on the localhost or run it on heroku just like in the link above.
But however you run it you need a few things-

* Create an account on Amazon Web Services(AWS) for using Amazon S3 here - http://docs.aws.amazon.com/AmazonS3/latest/gsg/SigningUpforS3.html
* Once you account is active, you will need to create your security credentials as stated here - http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/signup-create-iam-user.html
* Now you will have to set-up the credentials and your desired region in the environment. If you are using the localhost to run your application, follow the instrunctions here according to your Operating System - http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html 
* If you are running on heroku follow the instructions here under S3 Setup->Credentials - https://devcenter.heroku.com/articles/s3
* Now that you have set up everything, create a new bucket in the S3 management console and name it - `spring-mysql-test`
* Now place some files inside this bucket so you can download them.
* You can now change the type of content, according to the content in the bucket, in the MainController.java file here- 


![Alt text](/images/Screen2.png "Main Page")



You can find all the types supported here - https://docs.spring.io/spring/docs/3.0.x/javadoc-api/org/springframework/http/MediaType.html
* Now you can run the application on localhost as - $ gradle bootRun
If there are any errors just check the stacktrace. If running on heroku skip this step.
* You can go to your browser and run the application according to the mappings in MainController.java
For example, you can download and display a file in your browser by with the following command -

```
http://localhost:8080/demo/viewpic?fname=image3.png&key=Screenshot_20170618-214818.png

```
Here, 'image3.png' can be replaced by the file you want to store the downloaded file in and 'Screenshot_20170618-214818.png' should be replaced by the key of your file in the bucket.

* For uploading a file put it in the root folder of this repository like 'me' file in this screenshot - 

![Alt text](/images/Screen1.png "Main Page")


* Then change the name of the file in the MainController.java as it is in the directory - 

![Alt text](/images/Screen3.png "Main Page")


Now you can upload the file easily using the following URl - 

``` 
  http://localhost:8080/demo/upload
```
