package forms
import java.util.Date

import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._

import models._

package object BlogForm {
  val postForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "slug" -> nonEmptyText,
      "body" -> nonEmptyText)
      ((title, slug, body) => Post.fromMap(
        Map("title" -> title,
            "slug" -> slug,
            "body" -> body)))
      ((post: Post) => Some((post.title, post.slug, post.body))))

  val commentForm = Form(
    mapping(
      "body" -> nonEmptyText,
      "author" -> nonEmptyText)
      ((body, author) => Comment(body=body, author=author))
      ((comment: Comment) => Some((comment.body, comment.author)))
    )
}

