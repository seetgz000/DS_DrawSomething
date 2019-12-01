package DS_DrawSomething

import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket

import Client.client

object Client extends App {
  var client = new Socket( "127.0.0.1", 6000 )
  val is = new DataInputStream(client.getInputStream() )
  val os = new DataOutputStream( client.getOutputStream() )
  println("Enter the item name")
  os.writeBytes(s"${scala.io.StdIn.readLine("Enter item: ")}\n")
  var line: String = is.readLine();
  println(s"Item name is $line")
  client.close()
}
