package models

import play.api.Play.current
import java.util.Date

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import mongoContext._
import se.radley.plugin.salat._

case class Blog(
  id: ObjectId = new ObjectId,
  created_at: Date = new Date(),
  title: String,
  slug: String,
  body: String,
  comments: List[Comment])

case class Comment(
  created_at: Date = new Date(),
  body: String,
  author: String)

object Blog extends ModelCompanion[Blog, ObjectId] {
  val collection = mongoCollection("blog")
  val dao = new SalatDAO[Blog, ObjectId](collection = collection) {}

  def all(): List[Blog] = dao.find(MongoDBObject.empty).toList
  def getPost(slug: String): Option[Blog] = dao.findOne(MongoDBObject("slug" -> slug))


}
