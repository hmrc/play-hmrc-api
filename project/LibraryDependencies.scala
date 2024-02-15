import sbt._

object LibraryDependencies {

  val play28: Seq[ModuleID] = Seq(
    "com.typesafe.play" %% "play"                      % "2.8.21",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % "5.24.0"
  )

  val play29: Seq[ModuleID] = Seq(
    "com.typesafe.play" %% "play"                      % "2.9.0",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-29" % "8.4.0"
  )

  val play30: Seq[ModuleID] = Seq(
    "org.playframework" %% "play"                      % "3.0.0",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-30" % "8.4.0"
  )

  val test28: Seq[ModuleID] =
    Seq(
      "com.typesafe.play"      %% "play-test"          % "2.8.21",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0",
      "com.vladsch.flexmark"   % "flexmark-all"        % "0.36.8"
    ).map(_ % Test)

  val test29: Seq[ModuleID] =
    Seq(
      "com.typesafe.play"      %% "play-test"          % "2.9.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.0",
      "com.vladsch.flexmark"   % "flexmark-all"        % "0.64.6"
    ).map(_ % Test)

  val test30: Seq[ModuleID] =
    Seq(
      "org.playframework"      %% "play-test"          % "3.0.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1",
      "com.vladsch.flexmark"   % "flexmark-all"        % "0.64.6"
    ).map(_ % Test)

}
