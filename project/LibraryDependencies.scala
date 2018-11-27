import sbt._

object LibraryDependencies {

  private val play25Version = "2.5.19"
  private val play26Version = "2.6.20"

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
      ),
      play25 = Seq(
        "com.typesafe.play" %% "play" % play25Version,
        "com.typesafe.play" %% "play-ws" % play25Version,
        "uk.gov.hmrc" %% "bootstrap-play-25" % "3.2.0" % "provided"
      ),
      play26 = Seq(
        "com.typesafe.play" %% "play" % play26Version,
        "com.typesafe.play" %% "play-ws" % play26Version,
        "com.typesafe.play" %% "play-guice" % play26Version,
        "uk.gov.hmrc" %% "bootstrap-play-26" % "0.32.0" % Provided
      )
    )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "com.outworkers" %% "util-samplers" % "0.40.0" % Test,
        "org.scalacheck" %% "scalacheck"    % "1.13.4" % Test,
        "org.scalamock"  %% "scalamock"     % "4.1.0"  % Test,
        "org.scalatest"  %% "scalatest"     % "3.0.5"  % Test,
        "uk.gov.hmrc"    %% "hmrctest"      % "3.0.0" % Test,
        "org.mockito"    %  "mockito-core"  % "2.11.0" % Test
      ),
      play25 = Seq(
        "com.typesafe.play"      %% "play-test"          % play25Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1"       % Test
      ),
      play26 = Seq(
        "com.typesafe.play"      %% "play-test"          % play26Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"       % Test
      )
    )

  def apply() = compile ++ test
}

