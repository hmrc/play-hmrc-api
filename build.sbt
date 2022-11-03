
name := "play-hmrc-api"

lazy val library = (project in file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := "2.13.8",
    majorVersion := 7,
    isPublicArtefact := true,
    libraryDependencies ++= LibraryDependencies(),
    resolvers += Resolver.jcenterRepo,
    crossScalaVersions := Seq("2.12.14", "2.13.8"),
    PlayCrossCompilation.playCrossCompilationSettings
  )
