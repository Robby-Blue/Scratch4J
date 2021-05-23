# Scratch4J
Scratch4J is a Java library for interacting with the [scratch.mit.edu](htpps://scratch.mit.edu) website. 

## Getting started
Logging into an account
```java
Scratch4j scratch4j = new Scratch4j();
ScratchSession session = scratch4j.login("Username", "Password");
```

### Cloud
Connecting to a project's cloud server and changing a variable
```java
ScratchCloudSession cloudSession = session.startCloudSession(projectid);
cloudSession.setVariable("variable", "123");
```
Listening for cloud variable changes
```java
cloudsession.addListener(event -> {
	System.out.println(event.getVariable() + " was changed to " + event.getValue());
});
```
