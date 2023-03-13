import sbt._ // For understanding Dependencies object as an sbt file

object Dependencies {

  private object ver {

    val zio     = "2.0.10"
    val logback = "1.4.5"
  }

  object lib {

    object zio {
      val core:    Seq[ModuleID] = Seq("dev.zio" %% "zio" % ver.zio)
      val streams: Seq[ModuleID] = Seq("dev.zio" %% "zio-streams" % ver.zio)

    }

    object log {
      val logbackClassic: Seq[ModuleID] = Seq("ch.qos.logback" % "logback-classic" % ver.logback)
    }
  }

  object common {
    val core: Seq[ModuleID] = (Nil ++
      lib.zio.core           ++
      lib.zio.streams        ++
      lib.log.logbackClassic ++
      Nil)
      .map(library =>
        library withSources () withJavadoc () // Download source and Java Doc without IDE plugin
      )
  }
}
