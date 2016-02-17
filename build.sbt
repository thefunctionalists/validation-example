organization := "com.thefunctionalists"

name := "DroidValidation"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.5",
  "org.specs2" %% "specs2-core" % "2.4.17" % "test"
)
