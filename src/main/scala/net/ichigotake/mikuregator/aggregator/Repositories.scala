package net.ichigotake.mikuregator.aggregator

import java.net.URL

case class GithubResponse(total_count: Int, incomplete_results: Boolean, items: List[GithubRepository])
case class GithubRepository(id: Int, full_name: String, name: String, description: String, `private`: Boolean, fork: Boolean, pushed_at: String)
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
  def description: String
  def isPrivate: Boolean
  def pushedAt: String
}
case class AggregatedGistRepository(id: Int, fullName: String, description: String, isPrivate: Boolean, pushedAt: String) extends AggregatedRepository
case class AggregatedGithubRepository(id: Int, fullName: String, description: String, isPrivate: Boolean, pushedAt: String) extends AggregatedRepository

trait Author
trait User extends Author
trait Organization extends Author

