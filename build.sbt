import PlayCrossCompilation._
import uk.gov.hmrc.SbtArtifactory

name := "play-hmrc-api"
val scalaVer: String = "2.12.8"
lazy val library = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    scalaVersion := scalaVer,
    majorVersion := 4,
    crossSbtVersions := List("1.3.4"),
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= LibraryDependencies(),
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo
    ),
    resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
  )
