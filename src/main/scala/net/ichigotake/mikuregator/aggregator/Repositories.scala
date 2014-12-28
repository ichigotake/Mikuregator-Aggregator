package net.ichigotake.mikuregator.aggregator

import java.net.URL

trait Repository {
  def name: String
  def fullName: String
  def description: String
  def repositoryUrl: URL
  def author: Author
  def updatedTimestamp: Long
}

trait ScrapedGistRepository {
  def name: String
}

trait Author {
  def nickname: String
  def profileImageUrl: URL
}

