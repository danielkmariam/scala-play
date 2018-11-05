package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PlaceController @Inject()(cc:ControllerComponents) extends AbstractController(cc)  {

  case class Location(lat: Double, long: Double)

  case class Place(name: String, location: Location)

  object Place {

    var list: List[Place] = {
      List(
        Place(
          "Sandleford",
          Location(51.377797, -1.318965)
        ),
        Place(
          "Watership Down",
          Location(51.235685, -1.309197)
        )
      )
    }

    def save(place: Place) = {
      list = list ::: List(place)
    }
  }

  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "lat").write[Double] and
    (JsPath \ "long").write[Double]
  )(unlift(Location.unapply))

  implicit val placeWrites: Writes[Place] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "location").write[Location]
  )(unlift(Place.unapply))

  def listPlaces = Action {
    val json = Json.toJson(Place.list)
    Ok(json)
  }

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
    (JsPath \ "long").read[Double]
  )(Location.apply _)

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "location").read[Location]
  )(Place.apply _)

  def savePlace = Action(parse.json) { request =>
    val placeResult = request.body.validate[Place]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      place => {
        Place.save(place)
        Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
      }
    )
  }

}
