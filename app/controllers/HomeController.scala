package controllers

import play.api.libs.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Circe {

  case class User(name: String, age: Int)
  
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
  val user = User("Dan", 18)

    Ok(user.asJson)
  }
}
