# Scalatra OpenID Example

In this project, I'll try to outline a few different OpenID libraries that work on the JVM and I'll be using Scalatra as my web routing library and a simple User model to hold state.  My primary objective has been integrating with Google Apps so that I can back authentication against a domain which Google Apps provides services for.

This project uses SBT 0.10.1 and Scala 2.8.1

## Running

SBT is included here, so just run

	./sbt
	
Then run the command

	jetty-run

## openid4java

Pretty straight forward to include, although there is a dependency on Google Guice for which I had to add a new repository.

To include the Google Guice repository

	"Guice Maven" at "http://guice-maven.googlecode.com/svn/trunk"

To include the dependency

	"org.openid4java" % "openid4java-consumer" % "0.9.6"
