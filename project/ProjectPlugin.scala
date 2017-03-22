import sbt.Keys.{scalaVersion, _}
import sbt._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositeKeys._
import sbtorgpolicies._
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.ScriptedPlugin.{scriptedDependencies, scriptedLaunchOpts}
import sbtbuildinfo.BuildInfoPlugin.autoImport.{buildInfoKeys, buildInfoPackage, BuildInfoKey}

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val pluginSettings = Seq(
      sbtPlugin := true,
      resolvers ++= Seq("jgit-repo" at "http://download.eclipse.org/jgit/maven"),
      addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.0"),
      addSbtPlugin("com.typesafe.sbt" % "sbt-site"    % "1.2.0"),
      addSbtPlugin("org.tpolecat"     % "tut-plugin"  % "0.4.8"),
      libraryDependencies ++= Seq(
        "com.lihaoyi"           %% "scalatags" % "0.6.0",
        "org.scalactic"         %% "scalactic" % "3.0.0",
        "net.jcazevedo"         %% "moultingyaml" % "0.4.0",
        "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.7",
        %%("scalatest")         % "test",
        %%("scalacheck")        % "test"
      )
    )

    lazy val testScriptedSettings =
      ScriptedPlugin.scriptedSettings ++ Seq(
        scriptedDependencies := (compile in Test) map { (analysis) =>
          Unit
        },
        scriptedLaunchOpts := {
          scriptedLaunchOpts.value ++
            Seq(
              "-Xmx2048M",
              "-XX:ReservedCodeCacheSize=256m",
              "-XX:+UseConcMarkSweepGC",
              "-Dplugin.version=" + version.value,
              "-Dscala.version=" + scalaVersion.value
            )
        }
      )

    lazy val buildInfoSettings = Seq(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := "microsites"
    )

    lazy val micrositeSettings = Seq(
      micrositeName := "sbt-microsites",
      micrositeDescription := "An sbt plugin to create awesome microsites for your project",
      micrositeBaseUrl := "sbt-microsites",
      micrositeDocumentationUrl := "/sbt-microsites/docs/",
      micrositeGithubOwner := "47deg",
      micrositeGithubRepo := "sbt-microsites",
      micrositeHighlightTheme := "color-brewer",
      includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
    )

    lazy val jsSettings = sharedJsSettings ++ Seq(
      scalaVersion := "2.11.8",
      crossScalaVersions := Seq("2.11.8"),
      skip in packageJSDependencies := false,
      libraryDependencies ++= Seq(
        "org.scala-js"        %%% "scalajs-dom"       % "0.9.0",
        "be.doeraene"         %%% "scalajs-jquery"    % "0.9.0",
        "com.lihaoyi"         %%% "upickle"           % "0.4.1",
        "org.scala-exercises" %%% "evaluator-client"  % "0.1.2-SNAPSHOT",
        "com.lihaoyi"         %%% "scalatags"         % "0.6.0",
        "org.querki"          %%% "jquery-facade"     % "1.0-RC6",
        "org.denigma"         %%% "codemirror-facade" % "5.11-0.7",
        "com.fortysevendeg"   %%% "github4s"          % "0.9.0",
        "fr.hmil"             %%% "roshttp"           % "2.0.0-RC1"
      ),
      resolvers ++= Seq(
        Resolver.url(
          "bintray-sbt-plugin-releases",
          url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
          Resolver.ivyStylePatterns),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.bintrayRepo("denigma", "denigma-releases")
      ),
      jsDependencies ++= Seq(
        "org.webjars" % "jquery" % "2.1.3" / "2.1.3/jquery.js",
        ProvidedJS / "codemirror.js",
        ProvidedJS / "clike.js" dependsOn "codemirror.js"
      )
    )

  }

  override def projectSettings =
    Seq(
      name := "sbt-microsites",
      description := "An sbt plugin to create awesome microsites for your project",
      scalaVersion := "2.10.6",
      crossScalaVersions := Seq("2.10.6"),
      scalaOrganization := "org.scala-lang"
    ) ++ shellPromptSettings
}