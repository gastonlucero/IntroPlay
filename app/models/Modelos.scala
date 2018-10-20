package models

import java.util.UUID

import play.api.libs.json._


/**
  * Unico punto donde escribimos nuestros pareadores
  */
object Modelos {

  /**
    * Utilizamos el metodo Json.WithDefaultValues, ya que nuestras clases tienen atributos opcionales,
    * entonces evitamos errores al momento de leer el json que llega de un request
    * */
  implicit val dispositivoFormat = Json.using[Json.WithDefaultValues].format[Dispositivo]
  implicit val mensajeFormat = Json.using[Json.WithDefaultValues].format[Mensaje]
}

/** Modelo de clases */
case class Dispositivo(id: String = UUID.randomUUID().toString, imei: String, marca: String, modelo: String, ultimaPosicion: Option[String] = None)

case class Mensaje(id: Option[Long] = None, texto: String, dispositivoImei: String)

/**
  * Formulario para crear dispositivos
  */
case class CrearDispositivoForm(imei: String, marca: String, modelo: String)