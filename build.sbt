import sbt._

organization := "com.douban"

name := "scala-api"

version := "2.4.5"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/jinntrance/douban-scala"))

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.4","2.11.0")

scalacOptions ++= Seq("-unchecked", "-deprecation","-feature", "-Xcheckinit","-Xelide-below","INFO")

autoScalaLibrary := false

publishMavenStyle := true

publishArtifact in Test := false

useGpg := true

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.0",
	"com.google.code.gson" % "gson" % "2.2.4",
	"org.scalatest" % "scalatest_2.11.0-RC1" % "2.1.0" % "test",
	"org.testng" % "testng" % "6.8.8" % "test"
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
      <url>http://crazyadam.diandian.com</url>
    </developer>
  </developers>)


