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
    val posts = Blog.findAll
    Ok(views.html.index(posts.toList))
  }

  def getPost(slug: String) = Action {
    Blog.findOne(MongoDBObject("slug" -> slug)).map(post =>
      Ok(views.html.post.blogpost(post, BlogForm.commentForm))
    ).getOrElse(NotFound)
  }

  def addComment(slug: String) = Action { implicit request =>
    Blog.findOne(MongoDBObject("slug" -> slug)).map(post =>
      BlogForm.commentForm.bindFromRequest.fold(
        commentForm => BadRequest(views.html.post.blogpost(post, commentForm)),
        comment => {
          Blog.addComment(post, comment)
          Redirect(routes.Application.getPost(slug = post.slug)).flashing(
            "message" -> "Comment added")
        })
    ).getOrElse(NotFound)
  }

  def addPost(postType: Option[String]) = BasicAuth("admin", "password") {
    Action {
      val form = getForm(postType)
      Ok(views.html.addPost(BlogForm.blogForm, postType))
    }
  }

  def addPostSubmit(postType: Option[String]) = BasicAuth("admin", "password") {
    Action { implicit request =>
      BlogForm.blogForm.bindFromRequest.fold(
        blogForm => BadRequest(views.html.addPost(blogForm, postType)),
        post => {
          BlogPost.insert(post)
          Redirect(routes.Application.index).flashing(
            "message" -> "User Registered!")
        })
    }
  }

  def getForm(postType: Option[String]): Form[_] = postType match {
      case Some("blogPost") => BlogForm.blogForm
      case _ => BlogForm.blogForm
    }
}