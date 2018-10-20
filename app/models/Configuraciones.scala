package models

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

trait Configuraciones {
  val logger = LoggerFactory.getLogger("Datahack")

  lazy val config = {
    logger.info("Cargando configuraciones de application.conf")
    ConfigFactory.load("application.conf") // o .load()
  }


  /** Constantes en donde definimos como buscar en las configuraciones */

  val NombreEsquema = "slick.dbs.default.db.schema.nombre"
  val SchemaDispositivo = "slick.dbs.default.db.schema.tablaDispositivo"
  val SchemaMensajes = "slick.dbs.default.db.schema.tablaDispositivo"

}
