resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)
resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin(
  sys.env.get("PLAY_VERSION") match {
    case Some("2.8") => "com.typesafe.play" % "sbt-plugin" % "2.8.7"
    case Some("2.7") => "com.typesafe.play" % "sbt-plugin" % "2.7.9"
    case _           => "com.typesafe.play" % "sbt-plugin" % "2.6.25"
  }
)

addSbtPlugin("uk.gov.hmrc"      % "sbt-auto-build"             % "3.0.0"  )
addSbtPlugin("uk.gov.hmrc"      % "sbt-play-cross-compilation" % "2.0.0"  )
addSbtPlugin("com.timushev.sbt" % "sbt-updates"                % "0.5.2"  )
