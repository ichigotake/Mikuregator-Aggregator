package net.ichigotake.mikuregator.aggregator

object Main {

  def main(args: Array[String]): Unit = {
    Aggregator("token").aggregate()
  }

}