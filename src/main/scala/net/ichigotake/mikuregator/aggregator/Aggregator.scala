package net.ichigotake.mikuregator.aggregator

import scala.collection.mutable

case class Aggregator(token: String) {

  def aggregate(): Unit = {
    for (a <- ApiClient(token).getAll) {
      AggregateAction(apiClient = a, perPage = 20).aggregate() match {
        case Left(e) => System.err.println(e.getMessage)
        case Right(b) => System.out.println("Success: " + b)
      }
    }
  }
}

case class AggregateAction(apiClient: ApiRequestAction, perPage: Int) {

  def aggregate(): Either[Error, Response] = {
    val requestBody = RequestBody(perPage = 20)
    val body = Body(repositories = List(), authors = Set())
//    paging(requestBody = requestBody, body = body, response = None)
    paging(requestBody = requestBody, body = body, response = null)
  }

//  def paging(requestBody: RequestBody, body: Body, response: Option[Response): Either[Error, Response] = {
  def paging(requestBody: RequestBody, body: Body, response: Response): Either[Error, Response] = {
    val hasNextPage = response.pagingState.hasNextPage
    var repositories = response.repositories
    val authors = mutable.Set[Author]()
    apiClient.action(response.pagingState)
      .fold(
        e => Left(e),
        r => {
          for (r <- r) {
            repositories = r :: repositories
            authors.add(r.author)
          }
          val next: Response = Response(
            repositories = repositories,
            authors = authors.toSet,
            pagingState = response.pagingState.nextPage(repositories.size)
          )
          if (!hasNextPage) {
            return Right(next)
          }
          paging(requestBody = requestBody, body = body, response = next)
        }
    )
  }

}
