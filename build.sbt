val bootDeps = springBootLatestStableRelease
val doobieDeps = DoobieDependencies(doobie_0_12_1)

val build = SBuild("com.mindera", "wardrobe-service", "0.0.1")
  .sourceDirectories("01-internal", "11-business-logic", "12-db-logic", "13-http-endpoints", "14-app")
  .testSourceDirectories("11-business-logic-spec")
  .resourceDirectories("13-http-endpoints-resources")
  .dependencies(
    bootDeps.starter_parent(), bootDeps.starter_web(), //dependencies for http end point
    jackson_module_scala(), //dependencies for json
    doobieDeps.core(), doobieDeps.postgres(), doobieDeps.specs2(), //dependencies for doobie db
    better_files() //other dependencies
  )
  .testDependencies( scalatest() )
  .scalaVersions(scala_2_13_MaxVersion)
  .javaCompatibility(Jdk11)
  .services( ServiceDependencies.dbService ) //dependent services

idePackagePrefix := Some("com.mindera") //this is organization level package declaration
packMain := Map("wardrobe-service" -> "com.mindera.wardrobe.main.WardrobeApp")

val wardrobeServiceProject = ( project in file(".") )
  .settings( build.settings )
  .enablePlugins(PackPlugin, FluentStyleSbt) //PackPlugin is for packaging, FluentStyleSbt is for starting db service as docker container
