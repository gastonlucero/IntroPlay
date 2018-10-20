package play

import play.api.libs.functional.syntax._
import play.api.libs.json._


object JsonParsersEjemplos extends App {

  case class Persona(nombre: String, edad: Int)

  println(">>>Parseos simples de Json\n")
  val jsonObject: JsObject = Json.obj("nombre" -> "Maria", "edad" -> 35)
  println(s"Json.obj $jsonObject")
  val read: Reads[String] = (JsPath \ "nombre").read[String]
  val nombre = jsonObject.as[String](read)
  println(s"Read de un campo $nombre")


  /**
    * Reads / Writes
    */
  println("\n>>>Parseos usando Reads / Writes \n")
  implicit val PersonaReads: Reads[Persona] = (
    (JsPath \ "nombre").read[String] and
      (JsPath \ "edad").read[Int]) (Persona.apply _)

  println(">>>Reads")
  val persona = Json.parse("""{ "nombre": "Maria", "edad": 35 }""").as[Persona]
  println(s"Json.parse con casting a Persona $persona")

  val json: JsValue = Json.parse("""{ "nombre": "Maria", "edad": 35 }""")
  println(s"Json.parse como JsValue $json")
  val personaResultado: JsResult[Persona] = Json.fromJson[Persona](json)
  println(s"Json.fromJson $json")
  val personaGet: Persona = personaResultado.get
  println(s"Reads Json.fromJson.get $personaGet")

  println(">>>Writes")
  implicit val personaWrites: Writes[Persona] = (
    (JsPath \ "nombre").write[String] and
      (JsPath \ "edad").write[Int]
    ) (unlift(Persona.unapply))

  val jsonPersona: JsValue = Json.toJson[Persona](persona)
  println(s"Writes Json.toJson $jsonPersona")

}
