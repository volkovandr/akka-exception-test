import akka.actor.Status.Success
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props, Terminated}

import scala.util
import scala.util.{Failure, Try}


object Main {

  class MyException extends Exception {}

  class testActor extends Actor {

    var counter = 0

    def receive: PartialFunction[Any, Unit] = {
      case "delayed exception" =>
        println("will throw an error in 3 seconds")
        Thread.sleep(3000)
        throw new MyException
      case "stop" =>
        println("Terminating normally")
        System.exit(0)
      case "terminate" =>
        println("Terminating abnormally")
        System.exit(1)
      case "start" =>
        Thread.sleep(1000)
        counter += 1
        println(s"counter = $counter")
        self ! "start"
    }

    override def postStop(): Unit = {
      println("The actor is stopped!")
      super.postStop()
    }
  }


  def main(args: Array[String]): Unit = {
    println("Main thread starts")
    val system = ActorSystem("Main")
    val testActor = system.actorOf(Props(new testActor), "SuperActor")
    @volatile var systemTerminated = false
    val mainThread = Thread.currentThread()

    system.registerOnTermination({
      if(!systemTerminated) {
        systemTerminated = true
        System.exit(2)
      }
    })

    sys.addShutdownHook({
      if(!systemTerminated)
      {
        systemTerminated = true
        system.terminate()
      }
      mainThread.join()
    })


    testActor ! "start"

    if(args.length == 0) {
      println("The app will run forever and can be terminated with Ctrl+C and the exit code 130 is exected")
    } else {

      if (args(0) == "stop") {
        println("The app will stop \"normally\" with exit code 0 now")
        testActor ! "stop"
      }

      if (args(0) == "terminate") {
        println("The app should terminate itself \"abnormally\" with exit code 1")
        testActor ! "terminate"
      }

      if (args(0) == "exception") {
        println("The app should throw an exception and terminate with exit code 2")
        testActor ! "delayed exception"
      }
    }

    Thread.sleep(100)
    println("Main thread ends")

  }

}
