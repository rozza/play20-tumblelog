package controllers
import java.util.Date

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.mongodb.casbah.Imports._
import com.novus.salat._

import controllers.Secured._
import models._
import forms._


object Application extends Controller {

  def index = Action {
    val posts = Blog.all
    Ok(views.html.index(posts))
  }

  def getPost(slug: String) = Action {
    Blog.getPost(slug).map(post =>
      Ok(views.html.post(post, BlogForm.commentForm))
    ).getOrElse(NotFound)
  }

  def addComment(slug: String) = Action { implicit request =>
    Blog.getPost(slug).map(post =>
      BlogForm.commentForm.bindFromRequest.fold(
        commentForm => BadRequest(views.html.post(post, commentForm)),
        comment => {
          Blog.addComment(post, comment)
          Redirect(routes.Application.getPost(slug = post.slug)).flashing(
            "message" -> "Comment added")
        })
    ).getOrElse(NotFound)
  }

  def addPost = BasicAuth("admin", "password") {
    Action {
      Ok(views.html.addPost(BlogForm.blogForm))
    }
  }

  def addPostSubmit = BasicAuth("admin", "password") {
    Action { implicit request =>
      BlogForm.blogForm.bindFromRequest.fold(
        blogForm => BadRequest(views.html.addPost(blogForm)),
        post => {
          Blog.insert(post)
          Redirect(routes.Application.index).flashing(
            "message" -> "User Registered!")
        })
    }
  }
}