name := "play-hmrc-api"

val scala3_3 = "3.3.4"

ThisBuild / scalaVersion := scala3_3
ThisBuild / majorVersion := 8
ThisBuild / isPublicArtefact := true
ThisBuild / organization := "uk.gov.hmrc"
ThisBuild / scalacOptions += "-Wconf:src=src_managed/.*:s" // silence all warnings on autogenerated files

lazy val library = (project in file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .aggregate(
    play30
  )

lazy val play30 = Project("play-hmrc-api-play-30", file("play-30"))
  .settings(
    scalaVersion := scala3_3,
    libraryDependencies ++= LibraryDependencies.play30 ++ LibraryDependencies.test30,
    sharedSources
  )

def sharedSources = Seq(
  Compile / unmanagedSourceDirectories += baseDirectory.value / "../shared/src/main/scala",
  Compile / unmanagedResourceDirectories += baseDirectory.value / "../shared/src/main/resources",
  Test / unmanagedSourceDirectories += baseDirectory.value / "../shared/src/test/scala",
  Test / unmanagedResourceDirectories += baseDirectory.value / "../shared/src/test/resources"
)
