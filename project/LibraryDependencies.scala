import sbt._

object LibraryDependencies {

  private val play26Version = "2.6.20"
  private val play27Version = "2.7.9"

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(),
      play26 = Seq(
        "com.typesafe.play" %% "play-ws"           % play26Version,
        "uk.gov.hmrc"       %% "bootstrap-backend-play-26" % "3.2.0" % Provided
      ),
      play27 = Seq(
        "com.typesafe.play" %% "play-ws"                   % play27Version,
        "uk.gov.hmrc"       %% "bootstrap-backend-play-27" % "3.2.0" % Provided
      )
    )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest" %% "scalatest"   % "3.0.5"  % Test,
        "org.mockito"   % "mockito-core" % "2.11.0" % Test
      ),
      play26 = Seq(
        "com.typesafe.play"      %% "play-test"          % play26Version   % Test,
        "uk.gov.hmrc"            %% "hmrctest"           % "3.9.0-play-26" % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"         % Test
      ),
      play27 = Seq(
        "com.typesafe.play"      %% "play-test"                % play27Version    % Test,
        "uk.gov.hmrc"            %% "service-integration-test" % "0.13.0-play-27" % Test,
        "org.scalatestplus.play" %% "scalatestplus-play"       % "3.1.2"          % Test,
        "org.pegdown"            % "pegdown"                   % "1.6.0"          % Test
      )
    )

  def apply(): Seq[ModuleID] = compile ++ test
}
