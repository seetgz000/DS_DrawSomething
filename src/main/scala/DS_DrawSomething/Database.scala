package DS_DrawSomething

import java.sql.{Connection, DriverManager}

import Database.statement

trait data{
  // connect to the database named "mysql" on port 3306 of localhost
  val url = "jdbc:mysql://localhost:3306/MySql"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "root"
  var connection:Connection = _
  Class.forName(driver)
  connection = DriverManager.getConnection(url, username, password)
  val statement = connection.createStatement

}

object Database extends  data{
  def createTable(): Unit = {
    val makeTable : String =
      "CREATE TABLE items " +
        "(name VARCHAR(255) not NULL, " +
        " PRIMARY KEY (name))"
    statement.executeUpdate(makeTable)
  }

  def insertIntoTable (): Unit ={
    val item1 = statement.executeUpdate("INSERT INTO items VALUES ('Apple')")
    val item2 = statement.executeUpdate("INSERT INTO items VALUES ('Superman')")
    val item3 = statement.executeUpdate("INSERT INTO items VALUES ('Chocolate')")
    val item4 = statement.executeUpdate("INSERT INTO items VALUES ('Jeans')")
    val item5 = statement.executeUpdate("INSERT INTO items VALUES ('Ears')")
  }

}