import sbt._
import play.core.PlayVersion

object LibraryDependencies {

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "com.typesafe.play"      %% "play-ws"                   % PlayVersion.current
      ),
      play26 = Seq(
        "uk.gov.hmrc"            %% "bootstrap-backend-play-26" % "4.1.0"
      ),
      play27 = Seq(
        "uk.gov.hmrc"            %% "bootstrap-backend-play-27" % "4.1.0"
      ),
      play28 = Seq(
        "uk.gov.hmrc"            %% "bootstrap-backend-play-28" % "4.1.0"
      )
    )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "com.typesafe.play"      %% "play-test"                 % PlayVersion.current % Test,
        "org.mockito"            % "mockito-core"               % "3.8.0"             % Test
      ),
      play26 = Seq(
        "uk.gov.hmrc"            %% "hmrctest"                  % "3.10.0-play-26"    % Test,
        "org.scalatestplus.play" %% "scalatestplus-play"        % "3.1.3"             % Test
      ),
      play27 = Seq(
        "uk.gov.hmrc"            %% "service-integration-test"  % "1.1.0-play-27"     % Test,
        "org.scalatestplus.play" %% "scalatestplus-play"        % "4.0.3"             % Test,
        "org.pegdown"            %  "pegdown"                   % "1.6.0"             % Test
      ),
      play28 = Seq(
        "uk.gov.hmrc"            %% "service-integration-test"  % "1.1.0-play-28"     % Test,
        "org.scalatestplus.play" %% "scalatestplus-play"        % "5.0.0"             % Test,
        "com.vladsch.flexmark"   %  "flexmark-all"              % "0.35.10"           % Test
      )
    )

  def apply(): Seq[ModuleID] = compile ++ test
}
