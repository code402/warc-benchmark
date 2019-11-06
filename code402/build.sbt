name := "code402"

organization := "com.cldellow"

version := "0.0.1"

scalaVersion := "2.12.8"

//Define dependencies. These ones are only required for Test and Integration Test scopes.
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
  "dk.brics" % "automaton" % "1.12-1",
  "commons-io" % "commons-io" % "2.6",
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.475",
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.475",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.475",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.475",
  "de.siegmar" % "fastcsv" % "1.0.3",
  "com.hankcs" % "aho-corasick-double-array-trie" % "1.2.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8",
  "io.dropwizard.metrics" % "metrics-core" % "4.0.0",
  "io.dropwizard.metrics" % "metrics-json" % "4.0.0",
  "org.nanohttpd" % "nanohttpd" % "2.3.1",
  "org.apache.commons" % "commons-compress" % "1.18",
  "com.cldellow" %% "bayes" % "0.0.1",
  "com.cldellow" %% "warc-framework" % "0.0.1",
  "com.cldellow" %% "warc-service" % "0.0.1",
  "com.github.luben" % "zstd-jni" % "1.4.3-1",
  "com.amazonaws" % "aws-java-sdk-cloudformation" % "1.11.475",
  "commons-cli" % "commons-cli" % "1.4"
)

resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"

// For Settings/Task reference, see http://www.scala-sbt.org/release/sxr/sbt/Keys.scala.html

// Compiler settings. Use scalac -X for other options and their description.
// See Here for more info http://www.scala-lang.org/files/archive/nightly/docs/manual/html/scalac.html 
scalacOptions ++= List("-feature","-deprecation", "-unchecked", "-Xlint")

// ScalaTest settings.
// Ignore tests tagged as @Slow (they should be picked only by integration test)
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "org.scalatest.tags.Slow", "-u","target/junit-xml-reports", "-oD", "-eS")

useGpg := true

// needed so jft doesn't get loaded 2x in same jvm
//fork := true

//coverageEnabled := true


