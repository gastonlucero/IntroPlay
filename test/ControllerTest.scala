
import controllers.{MensajeController, MensajeRandom}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, PlaySpecification}


class ControllerTest extends PlaySpecification {

  "MensajeController" should {

    "Retornar un mensaje aleatorio" in {
      val mensajeRandom = new MensajeRandom()
      val controller = new MensajeController(stubControllerComponents(), mensajeRandom)
      val result = controller.mensajeAleatorio(FakeRequest())
      contentAsString(result) must contain("Mensaje Aleatorio ")
    }
  }
}
