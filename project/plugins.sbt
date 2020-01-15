resolvers ++= Seq(
  Resolver.url(
    "hmrc-sbt-plugin-releases",
    url("https://dl.bintray.com/hmrc/sbt-plugin-releases")
  )(Resolver.ivyStylePatterns),
  "Typesafe Releases".at("https://repo.typesafe.com/typesafe/releases/"),
  "HMRC Releases".at("https://dl.bintray.com/hmrc/releases")
)

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "2.5.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "2.1.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "1.0.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "0.19.0")
