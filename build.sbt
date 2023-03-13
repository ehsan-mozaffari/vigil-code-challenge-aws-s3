import Dependencies._
logLevel := Level.Info

name    := "vigil code challenge aws s3"
version := "1.0.0"

val scala3Version = "3.2.2"
scalaVersion := scala3Version

scalacOptions ++= Seq(
  "-J-target:jvm-19", // It is no preventing not to compile scala with other java version, it just shows the byte code java version is jdk 19
  "-J-release 19",
  "-J-enable-preview"
)

javacOptions ++= Seq(
  "--release 19",
  "--enable-preview"
)

javaOptions ++= Seq(
  "--enable-preview"
)

// checks the specific version of java version to run the project
initialize := {
  val _ = initialize.value // Needed to run previous initialization.
  assert(scala.util.Properties.isJavaAtLeast("19"), "Java 19 is required!")
}

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

lazy val services = project
  .in(file("services"))
  .settings(scalaVersion := scala3Version)
  .settings(libraryDependencies ++= lib.zio.core)
  .aggregate(svParserService)
  .dependsOn(svParserService)

addCommandAlias("run", "; services/run")

lazy val util = project
  .in(file("util"))
  .settings(
    libraryDependencies ++= common.core,
    scalaVersion := scala3Version
  )

lazy val svParserService  = project
  .in(file("services/sv/parser/service"))
  .settings(
    libraryDependencies ++= common.core,
    scalaVersion := scala3Version
  ).dependsOn(util)

