This is supposed to show how an application using Akka actors
will behave if an exception is happened inside an actor, or if the user
presses Ctrl+C or the actor system is terminated from inside an actor.

To use it simply build it with `sbt assembly` and then run it with
`java -jar target/scala-2.12/akka-exception-test-assembly-0.1.jar`

additionally you may give it one of the parameters:
- *shutdown*  to let it stop "normally" by itself;
- *terminate* to let it stop "abnormally" by itself;
- *main-exception* to let it throw an exception in the main actor and then crash.
- *child-exception* to let it throw an exception in the child actor and then crash.

Check exit codes after each run with `echo $?`. In fact the exit code is always 0 except for the case when it was terminated by Ctrl+C, where it is 130