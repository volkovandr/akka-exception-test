import akka.actor.{ActorSystem, Props}

object Entrypoint {

  def main(args: Array[String]): Unit = {
    println("Main thread starts")
    val system = ActorSystem("Main")
    val mainActor = system.actorOf(Props(new MainActor), "SuperActor")

    mainActor ! "start"

    if(args.length == 0) {
      println("The app will run forever and can be terminated with Ctrl+C and the exit code 130 is exected")
    } else {

      if (args(0) == "main-exception") {
        println("The app should throw an exception in the main actor and terminate with exit code 0")
        mainActor ! "main exception"
      }

      if (args(0) == "child-exception") {
        println("The app should throw an exception in the child actor and terminate with exit code 0")
        mainActor ! "child exception"
      }

      if (args(0) == "terminate") {
        println("The app should terminate immediately with exit code 0")
        mainActor ! "terminate"
      }

      if (args(0) == "shutdown") {
        println("The app should shut down gracefully with exit code 0")
        mainActor ! "shutdown"
      }
    }

    println("Main thread ends")
  }

}
