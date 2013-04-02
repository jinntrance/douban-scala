import AssemblyKeys._ // put this at the top of the file

organization := "com.douban"

name := "scala-api"

version := "2.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit")

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
