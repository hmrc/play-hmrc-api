import sbt._

object LibraryDependencies {

  val bootstrapPlayVersion = "9.11.0"

  val play30: Seq[ModuleID] = Seq(
    "org.playframework" %% "play"                      % "3.0.0",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-30" % bootstrapPlayVersion
  )

  val test30: Seq[ModuleID] =
    Seq(
      "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapPlayVersion
    ).map(_ % Test)

}
