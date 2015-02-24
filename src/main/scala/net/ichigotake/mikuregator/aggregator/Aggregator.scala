package net.ichigotake.mikuregator.aggregator

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request._
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

class Aggregator {
  
  def aggregate(aggregators: List[RepositoryAggregator]): List[AggregatedRepository] = {
    aggregators
      .map(a => a.aggregate())
      .flatten(r => r)
      .filter(r => !r.isPrivate)
  }

}

trait RepositoryAggregator {
  
  def aggregate(): List[AggregatedRepository]
  
}

class GithubAggregator extends RepositoryAggregator {
  
  override def aggregate(): List[AggregatedRepository] = {
    val aggregated = ListBuffer[AggregatedRepository]()
    val b = new Breaks
    b.breakable {
      var page = 1
      while (true) {
        var res = request(page = page)
        if (res.isEmpty) {
          b.break()
        }
        aggregated.insertAll(aggregated.size, res)
        page += 1
      }
    }
    aggregated.toList
  }
  
  def request(page: Int = 1): List[AggregatedGithubRepository] = {
    val client = new HttpClient()
    // TODO: Error handling
    val response = client.searchGithubRepository(page)
    response.items
      .filter(r => isUpdated(r))
      .map(r => convert(r))
  }
  
  private def convert(r: GithubRepository): AggregatedGithubRepository = {
    AggregatedGithubRepository(id = r.id, fullName = r.full_name, description = r.description, isPrivate = r.`private`, pushedAt = r.pushed_at)
  }
  

  private def isUpdated(repository: GithubRepository): Boolean = {
    true
  }

}

class HttpClient {
  
  def searchGithubRepository(page: Int): GithubResponse = {
    val client = new OkHttpClient()
    val req = new Builder()
      .url(s"https://api.github.com/search/repositories?q=mikutter&sort=updated&sort=desc&per_page=50&page=$page")
      .build()
    // TODO: Error handling
    val response = client.newCall(req).execute().body().string()
    implicit val formats = Serialization.formats(NoTypeHints)
    read[GithubResponse](response)
  }
  
}
