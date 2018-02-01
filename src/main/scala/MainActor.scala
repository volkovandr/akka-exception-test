import Exceptions.{MyException, TerminatedException}
import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, OneForOneStrategy, Props, SupervisorStrategy}
import scala.concurrent.duration._

class MainActor extends Actor with ActorLogging {

  var child: ActorRef = _
  var ticker: Cancellable = _
  var working: Boolean = _

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => SupervisorStrategy.Escalate
  }

  override def preStart(): Unit = {
    log.debug("Initializing")
    println("Main: Hello")
    child = context.actorOf(Props(new ChildActor), "child")
    working = false
  }

  def receive: PartialFunction[Any, Unit] = {
    case "start" =>
      log.debug("start")
      if(!working) {
        ticker = context.system.scheduler.schedule(1.second, 1.second, child, "ping")(context.dispatcher)
        working = true
      } else {
        log.warning("Triggered start when working already")
      }
    case "main exception" =>
      log.debug("main exception")
      println("Main: will throw an error in 3 seconds")
      Thread.sleep(3000)
      throw new MyException
    case "child exception" =>
      log.debug("child exception")
      println("Main: will tell the child to throw an error in 3 seconds")
      Thread.sleep(3000)
      child ! "exception"
    case "terminate" =>
      log.debug("terminate")
      println("Main: Terminating abnormally")
      throw new TerminatedException
    case "shutdown" =>
      log.debug("shutdown")
      println("Main: will trigger graceful shutdown from within the actor")
      context.system.terminate()
  }

  override def postStop(): Unit = {
    log.debug("graceful shutdown")
    println("Main: Good bye!")
  }
}
