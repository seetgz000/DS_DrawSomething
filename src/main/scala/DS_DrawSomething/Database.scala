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
      "CREATE TABLE drawingItems " +
        "(name VARCHAR(255) not NULL, " +
        " PRIMARY KEY (name))"
    statement.executeUpdate(makeTable)
  }

  def insertIntoTable (): Unit ={
    val item1 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Apple')")
    val item2 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Superman')")
    val item3 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Chocolate')")
    val item4 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Jeans')")
    val item5 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Ears')")
    val item6 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Bottle')")
    val item7 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Computer')")
    val item8 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Rat')")
    val item9 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Lion')")
    val item10 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Door')")
    val item11 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Escalator')")
    val item12 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Red')")
    val item13 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Paper')")
    val item14 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Pencil')")
    val item15 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Fan')")
    val item16 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Chair')")
    val item17 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Table')")
    val item18 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Glass')")
    val item19 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Watch')")
    val item20 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Coffee')")
    val item21 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Tree')")
    val item22 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Bag')")
    val item23 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Sandwich')")
    val item24 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Cheese')")
    val item25 = statement.executeUpdate("INSERT INTO drawingItems VALUES ('Shoes')")

  }

}