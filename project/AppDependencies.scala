import sbt._

object AppDependencies {

  import play.core.PlayVersion
  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    "com.typesafe.play" %% "play" % PlayVersion.current % "provided",
    "uk.gov.hmrc" %% "bootstrap-play-25" % "3.2.0" % "provided"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "uk.gov.hmrc" %% "hmrctest" % "3.0.0" % scope,
        "org.mockito" % "mockito-core" % "2.11.0" % "test"
      )
    }.test
  }

  def apply() = compile ++ Test()
}

