import sbt._

object AppDependencies {

  import play.core.PlayVersion
  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    "com.typesafe.play" %% "play" % PlayVersion.current % "provided",
    "uk.gov.hmrc" %% "microservice-bootstrap" % "6.9.0" % "provided"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scalatest" %% "scalatest" % "2.2.6" % scope,
        "org.pegdown" % "pegdown" % "1.5.0" % scope,
        "com.github.tomakehurst" % "wiremock" % "2.2.2" % scope excludeAll ExclusionRule(organization = "org.apache.httpcomponents"),
        "uk.gov.hmrc" %% "hmrctest" % "2.3.0" % scope,
        "org.mockito" % "mockito-all" % "1.9.5" % "test"
      )
    }.test
  }

  def apply() = compile ++ Test()
}

