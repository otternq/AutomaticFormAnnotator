Automatic Form Annotator
======================


Table of Contents
-----
- [About](#about)
- [Form Discovery](#form-discovery)
- [Form Submission](#form-submission)
- [Requirements](#requirements)
- [Platform](#platform)
- [References](#references)

About
-----
This is a semester long team project for [CS 360](http://wiki.cs.uidaho.edu/index.php/CS_360) (Intro to Databases) at the University of Idaho


Form Discovery
---
A set of classes and scripts that will find and evaludate forms to store the information required to submit the form automatically

###Tagging Forms###
Allow a user to provide context to the system, classifying the type of data this form will allow access to

Form Submission
----
Uses stored information about forms to allow users to submit values for each form element and then stores and displays
 the resulting HTML content for immediate viewing and later analysis.

Requirements
----
Automatic Form Annotator requires the [jSoup](http://jsoup.org/) .jar to be installed in /war/WEB-INF/lib/ this .jar is not stored in this GitHub repository and must be added manually

Platform
-----

<b>Language</b>: Java<br />
<b>Hosting</b>: Google App Engine

###Dependencies###

####jSoup####
jSoup is a library that allows HTML parsing based on CSS style selection. It can be downloaded [here](http://jsoup.org/).

####JDO####
We chose JDO as our storage mechanism so that we were not restricted to the Google App Engine DataStore storage engine.
Using JDO allows the system to write its class structure without thought of storage engine.

####GSON####
Google's JSON library. Can be found [here](http://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.2-release.zip&can=2&q=)

References
------
- [Google App Engine - JDO Documentation](https://developers.google.com/appengine/docs/java/datastore/jdo/creatinggettinganddeletingdata)
- [Oracle/Java - JDO Documentation](http://www.oracle.com/technetwork/java/index-jsp-135919.html)
- [jsoup Documentation](http://jsoup.org/apidocs/)