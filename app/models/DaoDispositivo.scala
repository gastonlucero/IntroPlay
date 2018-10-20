package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile
import slick.lifted

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
  * El acceso a la base de datos lo realizamos con un Singleton, en donde utilizamos las clases que nos brinda Play para
  * integrarse con slick, leyendo simplemente las configuraciones del archivo de propiedades e indicando el profile de la
  * bd que utilizaremos.
  * Al extender del trait HasDatabaseConfigProvider y tener inyectado DatabaseConfigProvider, tnemos acceso a un variable
  * llamada db , que es nuestra conexión directa a la bd
  **/
@Singleton
class DaoDispositivo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile] with Configuraciones {

  import profile.api._

  val esquema = config.getString(NombreEsquema)
  val nombreTablaDispositivo = config.getString(SchemaDispositivo)

  class TablaDispositivo(tag: Tag) extends Table[Dispositivo](tag, Some(esquema), nombreTablaDispositivo) {
    def id = column[String]("id")

    def imei = column[String]("imei")

    def marca = column[String]("marca")

    def modelo = column[String]("modelo")

    def ultimaPosicion = column[Option[String]]("ultima_posicion")

    def * = (id, imei, marca, modelo, ultimaPosicion) <> (Dispositivo.tupled, Dispositivo.unapply)

    def pk = primaryKey("dispositivo_pk", id)
  }

  val tablaDispositivo = {
    val table = lifted.TableQuery[TablaDispositivo]
    logger.info(s"Creando esquema para la tabla ${config.getString(SchemaDispositivo)}")
    Try {
      val action = table.schema.create
      Await.result(db.run(action), 10 seconds) //En este paso si usamos await, porque necesitamos el esquema
    } match {
      case Success(s) => logger.info(s"Esquema creado para la tabla ${table.schema.createStatements.mkString}")
      case Failure(f) => logger.warn("El esquema ya existe", f)
    }
    table
  }

  def obtenerTodos(): Future[Seq[Dispositivo]] = {
    logger.info(s"Obtener todos sql = ${tablaDispositivo.result.statements.mkString}")
    db.run(tablaDispositivo.result)
  }

  def insertar(dispositivo: Dispositivo): Future[Dispositivo] = {
    logger.info("Insertando dispositivo")
    val nuevo: Future[Dispositivo] = db.run(tablaDispositivo returning tablaDispositivo += dispositivo)
    nuevo
  }

  def actualizar(dispositivoActualizado: Dispositivo) = {
    val cantidadActualizada: Future[Int] = db.run(tablaDispositivo.insertOrUpdate(dispositivoActualizado))
    cantidadActualizada
  }

  def obtenerPorImei(imei: String): Future[Seq[Dispositivo]] = {
    db.run(tablaDispositivo.filter(_.imei === imei).result)
  }

}
