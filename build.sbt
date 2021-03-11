import uk.gov.hmrc.SbtArtifactory

name := "play-hmrc-api"

lazy val library = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := "2.12.13",
    majorVersion := 6,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= LibraryDependencies(),
    resolvers += Resolver.jcenterRepo,
    PlayCrossCompilation.playCrossCompilationSettings
  )
