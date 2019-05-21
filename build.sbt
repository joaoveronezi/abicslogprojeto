import com.typesafe.sbteclipse.plugin.EclipsePlugin._
 
EclipseKeys.withSource := true

name := "baldededados"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.35"

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "2.4.0"

libraryDependencies += "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += filters

libraryDependencies += "net.sf.jasperreports" % "jasperreports" % "6.0.0"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.json" % "json" % "20160212"

resolvers += "Jasper" at "http://jasperreports.sourceforge.net/maven2"

resolvers +=  "olap4j" at "http://jaspersoft.artifactoryonline.com/jaspersoft/third-party-ce-artifacts"

//libraryDependencies += "de.congrace" % "exp4j" % "0.3.11"

//libraryDependencies += "net.objecthunter" % "exp4j" % "0.4.5"

libraryDependencies += "com.udojava" % "EvalEx" % "1.1"

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

libraryDependencies += "org.apache.poi" % "poi" % "3.14"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator