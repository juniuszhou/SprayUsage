name := "SprayUsage"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.10"

libraryDependencies += "io.spray" % "spray-can_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-json_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-httpx_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-client_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-util_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-servlet_2.10" % "1.3.1"

libraryDependencies += "io.spray" % "spray-io_2.10" % "1.3.1"
