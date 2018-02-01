import Exceptions.MyException
import akka.actor.{Actor, ActorLogging}

class ChildActor extends Actor with ActorLogging {

  var counter: Int = _

  override def preStart(): Unit = {
    log.debug("Initializing")
    println("Child: Hello")
    counter = 0
  }

  def receive: PartialFunction[Any, Unit] = {
    case "ping" =>
      log.debug("ping")
      println(s"Child: Count = $counter")
      counter += 1
    case "exception" =>
      log.debug("exception")
      throw new MyException()
  }

  override def postStop(): Unit = {
    log.debug("graceful shutdown")
    println(s"Child: we managed to count to $counter")
    println("Child: Good bye")
  }
}
