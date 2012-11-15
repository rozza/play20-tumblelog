package models

import play.api.Play.current
import java.util.Date

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import mongoContext._
import se.radley.plugin.salat._

@Salat
trait Blog {
  val id: ObjectId
  val created_at: Date
  val title: String
  val slug: String
  val body: String
  val comments: List[Comment]
}

case class Comment(
  created_at: Date = new Date(),
  body: String,
  author: String)

object Blog extends ModelCompanion[Blog, ObjectId] {
  val collection = mongoCollection("blog")
  val dao = new SalatDAO[Blog, ObjectId](collection = collection) {}

  def getPost(slug: String): Option[Blog] = dao.findOne(MongoDBObject("slug" -> slug))

  def addComment(post: Blog, comment: Comment): Unit = {
    val updateQuery = $push ("comments" -> MongoDBObject("body" -> comment.body, "author" -> comment.author ))
    dao.update (MongoDBObject("_id" -> post.id), updateQuery)
  }
}

case class BlogPost (
  id: ObjectId = new ObjectId,
  created_at: Date = new Date(),
  title: String,
  slug: String,
  body: String,
  comments: List[Comment] = List()
) extends Blog

object BlogPost extends ModelCompanion[BlogPost, ObjectId] {
  val collection = mongoCollection("blog")
  val dao = new SalatDAO[BlogPost, ObjectId](collection = collection) {}
}
