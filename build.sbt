import PlayCrossCompilation._
import uk.gov.hmrc.SbtArtifactory

name := "play-hmrc-api"

val scalaVer: String = "2.11.12"

lazy val library = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 3,
    scalaVersion := scalaVer,
    crossScalaVersions := Seq(scalaVer),
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= LibraryDependencies(),
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases")
    ),
    playCrossCompilationSettings
  )
