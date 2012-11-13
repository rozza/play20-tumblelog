package controllers
import java.util.Date

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.mongodb.casbah.Imports._
import com.novus.salat._
import models._
import forms._

object Application extends Controller {

  def index = Action {
    val posts = Blog.all
    Ok(views.html.index(posts))
  }

  def getPost(slug: String) = Action {
    Blog.getPost(slug).map(post =>
      Ok(views.html.post(post))).getOrElse(NotFound)
  }
  

  def addPost() = Action { implicit request =>
    BlogForm.form.bindFromRequest.fold(
      form => BadRequest(views.html.addPost(form)),
      value => {
        Blog.insert(value)
        Redirect(routes.Application.index).flashing(
          "message" -> "User Registered!")
      })
  }
}