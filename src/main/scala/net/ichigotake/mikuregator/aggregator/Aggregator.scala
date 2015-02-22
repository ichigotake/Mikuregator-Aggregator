package net.ichigotake.mikuregator.aggregator

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request._
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

class Aggregator(dryRun: Boolean = false) {
  
  final def isDryRun = dryRun

  def aggregate():
  Unit = {
    val aggregators: List[RepositoryAggregator] = List(new GithubAggregator())
    for (a <- aggregators) {
      a.aggregate().foreach(r => {
        println(s"${r.fullName}")
      })
    }
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
  
  def request(page: Int = 1): ListBuffer[AggregatedGithubRepository] = {
    val repositories = ListBuffer[AggregatedGithubRepository]()
    val client = new OkHttpClient()
    val req = new Builder()
      .url(s"https://api.github.com/search/repositories?q=mikutter&sort=updated&sort=desc&per_page=50&page=$page")
      .build()
    // TODO: Error handling
    val response = client.newCall(req).execute().body().string()
    implicit val formats = Serialization.formats(NoTypeHints)
    val res = read[GithubResponse](response)
    for (r <- res.items) {
      if (!isUpdated(r)) {
        return repositories
      }
      val item = AggregatedGithubRepository(id = r.id, fullName = r.full_name, isPrivate = r.`private`)
      repositories.insert(repositories.size, item)
    }
    repositories
  }

  def isUpdated(repository: GithubRepository): Boolean = {
    true
  }
  
}
