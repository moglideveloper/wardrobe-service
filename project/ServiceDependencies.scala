import com.logicovercode.base_plugin.FluentStyleSbt.autoImport.{ContainerDefinitionExtension, DockerContainer}
import com.logicovercode.bsbt.BuilderStyleBuild.autoImport._
import com.logicovercode.fsbt.commons.{MicroService, SbtServiceDescription}
import com.logicovercode.wdocker.{DockerReadyChecker, VolumeMapping}

object ServiceDependencies {

  val dbService = MicroService(dbContainerDescription())

  private def dbContainerDescription(): SbtServiceDescription = {

    val postgresDbConfMap = mapFromConfFile(CURRENT_DIRECTORY / "13-http-endpoints-resources" / "clothing_db.conf")

    DockerContainer("postgres", "latest", Option("clothes-db"))
      .withEnv(s"POSTGRES_PASSWORD=${postgresDbConfMap("POSTGRES_PASSWORD")}")
      .withPorts(5432 -> postgresDbConfMap("CLOTHING_DB_PORT").toInt)
      .withNetworkMode( DockerNetwork("aws-net") )
      .withVolumes(
        VolumeMapping(CURRENT_DIRECTORY_PATH  + "/postgres-db-init-scripts", "/docker-entrypoint-initdb.d/")
      )
      .withReadyChecker(
        DockerReadyChecker.LogLineContains(
          "PostgreSQL init process complete"
        )
      )
      .configurableAttributes(15, 5)
  }
}
