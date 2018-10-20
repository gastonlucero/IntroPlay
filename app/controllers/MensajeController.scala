package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

class MensajeController @Inject()(cc: ControllerComponents,
                                  mensajeRandom: MensajeRandom) extends AbstractController(cc) {


  def mensajeAleatorio = Action {
    Ok(mensajeRandom.mensaje())
  }

}


@Singleton
class MensajeRandom {
  def mensaje() = s"Mensaje Aleatorio ${System.currentTimeMillis()}"
}