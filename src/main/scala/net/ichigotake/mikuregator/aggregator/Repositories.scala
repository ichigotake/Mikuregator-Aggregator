package net.ichigotake.mikuregator.aggregator

import java.net.URL

case class GithubResponse(total_count: Int, incomplete_results: Boolean, items: List[GithubRepository])
case class GithubRepository(id: Int, full_name: String, name: String, `private`: Boolean, fork: Boolean)
case class GithubAuthor(id: Int, login: String, avatar_url: String)

case class Repository(
  name: String,
  fullName: String,
  description: String,
  repositoryUrl: URL,
  author: Author,
  updatedAtMs: Long
)

trait AggregatedRepository {
  def id: Int
  def fullName: String
  def isPrivate: Boolean
  
}
case class AggregatedGistRepository(id: Int, fullName: String, isPrivate: Boolean) extends AggregatedRepository
case class AggregatedGithubRepository(id: Int, fullName: String, isPrivate: Boolean) extends AggregatedRepository

trait Author
trait User extends Author
trait Organization extends Author

