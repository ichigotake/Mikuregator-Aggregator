package net.ichigotake.mikuregator.aggregator

import java.text.SimpleDateFormat
import java.util.Locale

object Main {

  def main(args: Array[String]): Unit = {
    val aggregatedRepositories = new Aggregator().aggregate(List(new GithubAggregator()))
    printFeeds(aggregatedRepositories)
  }

  def printFeeds(repositories: List[AggregatedRepository]):
  Unit = {
    println( s"""
                |  <rss version="2.0">
                |  <channel>
                |    <title>An example RSS feed</title>
                |    <description>La dee daah</description>
                |    <link>http://www.example.com/rss</link>
                |    <lastBuildDate>Mon, 05 Oct 2012 11:12:55 =0100 </lastBuildDate>
                |   <pubDate>Tue, 06 Oct 2012 09:00:00 +0100</pubDate>
                |
                """.stripMargin)
    for (repository <- repositories.reverse.slice(0, 20)) {
      val pushedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH).parse(repository.pushedAt)
      val pubDate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(pushedAt)
      println(s"""
                 |  <item>
                 |    <title>${repository.fullName}</title>
                 |    <description>${repository.description}</description>
                 |    <link>https://github.com/${repository.fullName}</link>
                 |    <guid isPermaLink="false">${repository.id}</guid>
                 |    <pubDate>$pubDate</pubDate>
                 |  </item>""".stripMargin)
    }
    
    println(s"""
               |  </channel>
               |</rss>""".stripMargin)
    
  }

}