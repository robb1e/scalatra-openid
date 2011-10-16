resolvers ++= Seq(
  "Guardian GitHub Release" at "http://guardian.github.com/maven/repo-releases",
  "Guice Maven" at "http://guice-maven.googlecode.com/svn/trunk" // required for openid dependency
)

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "7.4.1.v20110513" % "jetty",
  "org.fusesource.scalate" % "scalate-core" % "1.4.1",
  "org.scalatra" %% "scalatra" % "2.0.1",
  "org.openid4java" % "openid4java-consumer" % "0.9.6"
)

seq(webSettings :_*)