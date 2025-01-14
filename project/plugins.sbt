import sbt.Resolver.sonatypeRepo

resolvers ++= Seq(sonatypeRepo("snapshots"), sonatypeRepo("releases"))

addSbtPlugin("com.47deg"        % "sbt-org-policies" % "0.11.3")
addSbtPlugin("com.47deg"        % "sbt-microsites"   % "0.9.0")
addSbtPlugin("com.eed3si9n"     % "sbt-buildinfo"    % "0.9.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-site"         % "1.3.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages"      % "0.6.2")
addSbtPlugin("org.tpolecat"     % "tut-plugin"       % "0.6.10")
addSbtPlugin("org.scalameta"    % "sbt-mdoc"         % "1.2.10")
