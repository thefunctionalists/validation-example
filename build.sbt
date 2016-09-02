organization := "com.thefunctionalists"

name := "DroidValidation"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.5",
  "org.typelevel" %% "cats" % "0.7.0",
  "org.specs2" %% "specs2-core" % "3.8.4" % "test"
)
