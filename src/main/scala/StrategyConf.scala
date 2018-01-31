import Main.MyException
import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{OneForOneStrategy, SupervisorStrategy, SupervisorStrategyConfigurator}
import scala.concurrent.duration._

class StrategyConf extends SupervisorStrategyConfigurator{
  override def create(): SupervisorStrategy = OneForOneStrategy(0, 0.seconds) {
    case _: MyException => Escalate
  }
}
