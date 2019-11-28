package DS_DrawSomething

import java.io.{DataInputStream, DataOutputStream}
import java.net.{ServerSocket, Socket}
import java.sql.ResultSet
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Server extends App with data {
  val server = new ServerSocket(6000)
  //Database.createTable()
  //Database.insertIntoTable()
  while(true){
    val client: Socket = server.accept()
    val f = Future({
      val is = new DataInputStream(client.getInputStream())
      val os = new DataOutputStream(client.getOutputStream())
      var line: String = is.readLine()
       println(s"Check if item name is correct for : $line")
      //get the row that satisfy the query
      val result: ResultSet = statement.executeQuery(s"SELECT name FROM drawingItems WHERE name LIKE '%$line%'")
      val results = Iterator.from(0).takeWhile(_ => result.next()).map(_ => result.getString("name")).toList
      os.writeBytes(s"${results.head}\n")
      client.close()
    })
  }

}
