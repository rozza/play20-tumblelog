import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "tumblelog"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.1",
    "org.webjars" % "webjars-play" % "0.1",
    "org.webjars" % "bootstrap" % "2.1.1")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId")

}

