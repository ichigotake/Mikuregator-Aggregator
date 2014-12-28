package net.ichigotake.mikuregator.aggregator

case class ApiClient(token: String) {
  def getAll: List[ApiRequestAction] = List(gist, gitHub)
  def gist: ApiRequestAction = GistApiRequest(token)
  def gitHub: ApiRequestAction = GitHubApiRequest(token)
}

trait ApiRequestAction {
  def action(pagingState: PagingState): Either[Error, List[Repository]]
}

case class GitHubApiRequest(token: String) extends ApiRequestAction {
  override def action(pagingState: PagingState): Either[Error, List[Repository]] = Right(List())
}

case class GistApiRequest(token: String) extends ApiRequestAction {

  override def action(pagingState: PagingState): Either[Error, List[Repository]] = {
    scrape(pagingState)
      .fold(
        e => Left(e),
        r => Right(r.map(f => get(f.name).right.get)) // TODO: Left... :(
      )
  }

  def scrape(pagingState: PagingState): Either[Error, List[ScrapedGistRepository]] = Right(List())

  def get(name: String) : Either[Error, Repository] = Left(GistApiLimitError("Failed"))

}

case class Response(repositories: List[Repository], authors: Set[Author], pagingState: PagingState) {
  def nextPage(f: => RequestBody) => {

  }

}
case class Body(repositories: List[Repository], authors: Set[Author])
case class RequestBody(perPage: Int)

case class PagingState(perPage: Int, page: Int, itemCount: Int, hasNextPage: Boolean) {
  def nextPage(itemCount: Int): PagingState = PagingState(perPage, page+1, itemCount, hasNextPage)
}

sealed trait AggregateError extends Error
case class EmptyResponseError(message: String) extends AggregateError
case class AuthenticationError(message: String) extends AggregateError
case class GistApiLimitError(message: String) extends AggregateError
case class ScrapingGistError(message: String) extends AggregateError
case class GitHubApiLimitError(message: String) extends AggregateError
