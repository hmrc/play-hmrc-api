import sbt._
import play.core.PlayVersion

object LibraryDependencies {

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "com.typesafe.play"      %% "play-ws"                   % PlayVersion.current
      ),
      play28 = Seq(
        "uk.gov.hmrc"            %% "bootstrap-backend-play-28" % "5.24.0"
      )
    )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "com.typesafe.play"      %% "play-test"                 % PlayVersion.current % Test,
        "org.mockito"            % "mockito-core"               % "3.8.0"             % Test
      ),
      play28 = Seq(
        "uk.gov.hmrc"            %% "service-integration-test"  % "1.3.0-play-28"     % Test,
        "org.scalatestplus.play" %% "scalatestplus-play"        % "5.0.0"             % Test,
        "com.vladsch.flexmark"   %  "flexmark-all"              % "0.35.10"           % Test
      )
    )

  def apply(): Seq[ModuleID] = compile ++ test
}
