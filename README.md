# StuChat

# Introduction

For this app, we created a multi-functional and responsive messaging android application that is similar to the popular WhatsApp. We were able to use Firebase as our main database to store login information (First name, last name, email, password, profile image, and phone number), used fragments to main different functions of the app (Contacts, Chats, Call, public accounts, and Users' profile), and also used all resources available on Android studio (xml's, drawables, different libraries, etc) to create a functional messaging app. 

# Objectives

We were to create a messaging application similar to WhatsApp that included login authentication, multiple account information, contacts list from the address book, public accounts, camera, and microphone for messaging. 



# Approaches

For the student logins/passwords/phone numbers we used the FireBase database. For the rest of the application, we implemented fragments which allow us to make and combine several "fragments" in one activity to build a multi-panel UI. Not only are we able to build a multi-panel UI, but we are also able to use each fragment more than once in multiple activities. We also used adapters for the messaging and contacts portion so we are able to connect UI components with the source of the data. Finally, we created models for chats, contacts, users so that we can use them anywhere in the project.


# Workflow

The below figure showcase our workflow for the app. We are using Android studio for the frontend whereas Firebase is used as a backend to store the data.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/workflow.jpg)

# Welcome to StuChat

This screen is where the app starts. The users will be able to register, sign in, or reset the password.
 
![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/1.jpg)

# Login and Registration
Implemented by using Firebase database to stored and retrieve the user's first name, last name, email, password, phone number, and also checked for authentication. We implemented the error handling to avoid any crashing.

## Registration 

The users will be able to use register using their email addresses. 

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/2.jpg)

## Login

By using the email address and the password, they will be able to use the StuChat app.

## Reset 
Let users reset their passwords by sending a link to their emails. 

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/3.jpg)

# Public

Shows a list of people that are registered to the app and you are able to message them.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/4.jpg)

The user will be able to pick anyone and chat.

**Sending a message**

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/5.jpg)

**Receiving a message**

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/6.jpg)


# Chats
Shows ongoing chats and you are able to chat back to other users. we have some features on it like using the Mic to do speech to text.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/7.jpg)


# Chats
show from the toolbar the call function where users can have a call.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/20.jpg)

# Contacts
Shows a list of contacts pulled directly from the phones address book, and shows if this contact has an account by showing active statues.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/8.jpg)


# User Profile
Shows the user profile and updating the data

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/9.jpg)

# Microphone
Able to use the built-in microphone to do speech-to-text to create messages.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/10.jpg)

# Camera
Able to use the built-in camera to save a profile picture

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/11.jpg)

# Online/Offline

Able to see if the user is either online or offline.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/12.jpg)


# Implementing
Note: Screenshots do not represent the entire code. 

The below screenshot is a function for the signup. We used a function to create a user with email and password from the firebase.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/13.JPG)

The below code is for error handling 

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/14.JPG)

The below code is the source code for the chat.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/15.JPG)

Also, this is for the user model.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/16.JPG)


Below is where we have the authentication data in Firebase.

![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/17.jpg)


![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/18.jpg)


![image](https://github.com/Consolefun/Lab2_StuChat/blob/master/Documentation/19.jpg)

# Evaluation and Discussion

Using a messaging app is fantastic in the sense that you save time. Unlike emails, you do not have to wait for responses from other people where you have to wait for messages to download from a server. Using a messaging application cuts down that time, by allowing users to communicate with each other in real-time.








