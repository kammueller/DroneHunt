name := """MediaSaturnHackaTUM"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "commons-codec" % "commons-codec" % "1.10",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.apache.httpcomponents" % "httpclient" % "4.2.3",
  "org.apache.httpcomponents" % "httpcore" % "4.2.2",
  "log4j" % "log4j" % "1.2.16",
  "xml-apis" % "xml-apis" % "1.4.01",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "mysql" % "mysql-connector-java" % "5.1.18"
)

