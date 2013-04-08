<<<<<<< HEAD
import AssemblyKeys._ 
=======
import sbt._
import AssemblyKeys._ // put this at the top of the file
>>>>>>> 3781d97f186d3199dadcb1931d02bb8f21c8d2b8

organization := "com.douban"

name := "scala-api"

version := "2.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-unchecked", "-deprecation","-feature", "-Xcheckinit","-target:jvm-1.7")

autoScalaLibrary := false

libraryDependencies ++= Seq(
	"com.thoughtworks.paranamer" % "paranamer" % "latest.release",
	"org.scala-lang" % "scalap" % "2.10.1",
	"org.scalatest" %% "scalatest" % "latest.release" % "test"
	)

seq(ProguardPlugin.proguardSettings :_*)

proguardOptions ++= Seq(
	"-dontobfuscate","-dontpreverify","-dontoptimize",
	"-dontwarn scala.**","-dontwarn org.apache.**","-dontwarn com.thoughtworks.**","-dontwarn ch.epfl.lamp.compiler.**",
	"-dontnote scala.**","-dontnote com.thoughtworks.**",
	"-keep class com.douban.**"
)

publishTo := Some(Resolver.file("file",  new File( "./repo/releases" )) )

assemblySettings

test in assembly := {}
