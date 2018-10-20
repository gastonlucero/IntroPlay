import org.specs2.execute.Results
import play.api.test._

class RoutesTest extends PlaySpecification with Results {


  "Application" should {

    "Comprobar que la aplicación se accede desde el browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port)
      browser.pageSource must contain("Insertar Dispositivo")
    }

    "Enviar un mensaje 404 en un bad request" in new WithApplication {
      route(app, FakeRequest(GET, "/noexisto")) must beSome.which(status(_) == NOT_FOUND)
    }

    "Redireccionar al index" in new WithApplication {
      val home = route(app, FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }

    "Validar la ruta dispositivo/todos" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/dispositivo/todos"))
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
    }

  }
}
