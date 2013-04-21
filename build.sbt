import sbt._

organization := "com.douban"

name := "scala-api"

version := "2.1"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-unchecked", "-deprecation","-feature", "-Xcheckinit")

autoScalaLibrary := false

libraryDependencies ++= Seq(
	"com.google.code.gson" % "gson" % "latest.release",
	"org.scalatest" %% "scalatest" % "latest.release" % "test"
	)

publishTo := Some(Resolver.file("file",  new File( "./repo/releases" )) )
