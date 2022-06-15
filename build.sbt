
name := "play-hmrc-api"

lazy val library = (project in file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := "2.12.14",
    majorVersion := 7,
    isPublicArtefact := true,
    libraryDependencies ++= LibraryDependencies(),
    resolvers += Resolver.jcenterRepo,
    PlayCrossCompilation.playCrossCompilationSettings
  )
