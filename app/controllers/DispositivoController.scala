package controllers

import java.util.UUID
import javax.inject._

import models.Modelos._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import play.twirl.api.Html

import scala.concurrent.{ExecutionContext, Future}

class DispositivoController @Inject()(dao: DaoDispositivo, cc: MessagesControllerComponents)
                                     (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val dispositivoForm: Form[CrearDispositivoForm] = Form {
    mapping(
      "imei" -> nonEmptyText,
      "marca" -> nonEmptyText,
      "modelo" -> nonEmptyText
    )(CrearDispositivoForm.apply)(CrearDispositivoForm.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.index(dispositivoForm))
  }

  def obtenerTodos = Action.async {
    implicit request =>
      dao.obtenerTodos().map { dispositivos =>
        Ok(Json.toJson(dispositivos))
      }
  }

  def insertar = Action.async {
    implicit request =>
      val dispositivo: Dispositivo = Json.fromJson[Dispositivo](request.body.asJson.get).get
      dao.insertar(dispositivo).map {
        resultado => Ok(Json.toJson(resultado))
      }
  }

  def actualizar = Action.async {
    implicit request =>
      val dispositivo: Dispositivo = Json.fromJson[Dispositivo](request.body.asJson.get).get
      dao.actualizar(dispositivo).map {
        resultado => Ok(Json.obj("Actualizado" -> dispositivo))
      }
  }

  def obtenerPorImei(imei: String) = Action.async {
    implicit request =>
      dao.obtenerPorImei(imei).map {
        dispositivos => Ok(Json.toJson(dispositivos))
      }
  }

  def templateHtml() = Action {
    implicit request =>
      val html = views.html.main("Hola")(Html("twirl"))
      Ok(html)
  }


  def insertarForm = Action.async { implicit request =>
    dispositivoForm.bindFromRequest.fold(
      //Si hay errores al leer el form, se retorna el error
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      //Si no hay errores, leemos los datos del form , y ejecutamos la accion de guardado
      dispositivoForm => {
        val nuevoDispositivo = Dispositivo(id = UUID.randomUUID().toString,
          imei = dispositivoForm.imei, marca = dispositivoForm.marca,
          modelo = dispositivoForm.modelo)
        dao.insertar(nuevoDispositivo).map { _ =>
          //Despues de insertado se redirige al metodo obtenerTodos -> que es el index en nuestro archivos routes
          Redirect(routes.DispositivoController.obtenerTodos()).flashing("success" -> "dispositivo.creado")
        }
      }
    )
  }

}

