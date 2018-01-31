This is supposed to show how an application using Akka actors
will behave if an exception is happened inside an actor, or if the user
presses Ctrl+C or if System.exit() is called inside actor.

To use it simply build it with `sbt assembly` and then run it with
`java -jar target/scala-2.12/akka-exception-test-assembly-0.1.jar`

additionally you may give it one of the parameters:
- *stop* to let it stop "normally" by itself;
- *terminate* to let it stop "abnormally" by itself;
- *exception* to let it throw an exception and then crash.

Check exit codes after each run with `echo $?`