package net.ichigotake.mikuregator.aggregator

object DataStorage {
  def get: List[DataStorageAction] = List(
    new JsonStorage(),
    new SqliteStorage()
  )
}

trait DataStorageAction {
  def putRepositories(authors: List[Repository]): Option[Error]
  def putAuthors(authors: List[Author]): Option[Error]
}

class JsonStorage extends DataStorageAction {
  override def putRepositories(authors: List[Repository]): Option[Error] = None

  override def putAuthors(authors: List[Author]): Option[Error] = None
}

class SqliteStorage extends DataStorageAction {
  override def putRepositories(authors: List[Repository]): Option[Error] = None

  override def putAuthors(authors: List[Author]): Option[Error] = None
}

sealed trait DataStorageError extends Error
final case class ConvertJsonError(message: String) extends DataStorageError
final case class SqliteError(message: String) extends DataStorageError
