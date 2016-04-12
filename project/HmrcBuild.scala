import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import SbtAutoBuildPlugin._
  
  val nameApp = "play-hmrc-api"

  val appDependencies = {
    import Dependencies._

    Seq(
      Compile.microserviceBootstrap,
      Compile.playConfig,

      Test.hmrctest,
      Test.scalaTest,
      Test.pegdown
    )
  }

  lazy val simpleReactiveMongo = Project(nameApp, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      autoSourceHeader := false,
      scalaVersion := "2.11.8",
      libraryDependencies ++= appDependencies,
      resolvers += Resolver.typesafeRepo("releases"),
      crossScalaVersions := Seq("2.11.8")
    )
}

object Dependencies {

  object Compile {
    val microserviceBootstrap = "uk.gov.hmrc" %% "microservice-bootstrap" % "4.2.1" % "provided"
    val playConfig = "uk.gov.hmrc" %% "play-config" % "2.0.1" % "provided"
  }

  sealed abstract class Test(scope: String) {
    val hmrctest = "uk.gov.hmrc" %% "hmrctest" % "1.6.0" % scope
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % scope
  }

  object Test extends Test("test")

  object IntegrationTest extends Test("it")

}
