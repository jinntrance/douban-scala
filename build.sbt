import sbt._

organization := "com.douban"

name := "scala-api"

version := "2.4.7"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/jinntrance/douban-scala"))

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.6")

scalacOptions ++= Seq("-unchecked", "-deprecation","-feature", "-Xcheckinit","-Xelide-below","INFO")

autoScalaLibrary := false

publishMavenStyle := true

publishArtifact in Test := false

useGpg := true

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.6",
	"com.google.code.gson" % "gson" % "2.3.1",
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
	"org.testng" % "testng" % "6.8.21" % "test"
	)

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>git@github.com:jinntrance/douban-scala.git</url>
    <connection>scm:git:git@github.com:jinntrance/douban-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jinntrance</id>
      <name>Joseph J.C. Tang</name>
      <url>http://www.josephjctang.com</url>
    </developer>
  </developers>)


