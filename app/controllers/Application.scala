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
    Ok(views.html.index(Post.findAll.toList))
  }

  def getPost(slug: String) = Action {
    Post.findOne(MongoDBObject("slug" -> slug)).map(post =>
      Ok(views.html.post(post, BlogForm.commentForm))
    ).getOrElse(NotFound)
  }

  def addComment(slug: String) = Action { implicit request =>
    Post.findOne(MongoDBObject("slug" -> slug)).map(post =>
      BlogForm.commentForm.bindFromRequest.fold(
        commentForm => BadRequest(views.html.post(post, commentForm)),
        comment => {
          Post.addComment(post, comment)
          Redirect(routes.Application.getPost(slug = post.slug)).flashing(
            "message" -> "Comment added")
        })
    ).getOrElse(NotFound)
  }

  def addPost = BasicAuth("admin", "password") {
    Action {
      Ok(views.html.addPost(BlogForm.postForm))
    }
  }

  def addPostSubmit = BasicAuth("admin", "password") {
    Action { implicit request =>
      BlogForm.postForm.bindFromRequest.fold(
        blogForm => BadRequest(views.html.addPost(blogForm)),
        post => {
          Post.insert(post)
          Redirect(routes.Application.index).flashing(
            "message" -> "User Registered!")
        })
    }
  }

}