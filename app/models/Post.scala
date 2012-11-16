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
trait Post {
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

object Post extends ModelCompanion[Post, ObjectId] {
  val collection = mongoCollection("blog")
  val dao = new SalatDAO[Post, ObjectId](collection = collection) {}

  def findFromSlug(slug: String): Option[Post] = findOne(MongoDBObject("slug" -> slug))

  def addComment(post: Post, comment: Comment): Unit = {
    val updateQuery = $push ("comments" -> MongoDBObject("body" -> comment.body, "author" -> comment.author ))
    dao.update (MongoDBObject("_id" -> post.id), updateQuery)
  }
}
