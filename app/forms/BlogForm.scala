package forms
import java.util.Date

import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

import models._

package object BlogForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "slug" -> nonEmptyText,
      "body" -> nonEmptyText)
   ((title, slug, body) => Blog.fromMap(Map("title" -> title, "slug" -> slug, "body" -> body)))
   ((blog: Blog) => Some((blog.title, blog.slug, blog.body)))
  )
}

