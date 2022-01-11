import java.sql.DriverManager
import java.sql.Connection
import java.util.Scanner
import scala.collection.mutable.ArrayBuffer
import java.io.PrintWriter
import java.io.File
import java.util.Calendar
object pj0 {

//log.write(Calenter.getInstance().getTimeInMillies + Executing "")

  def main(args: Array[String]):Unit = {
    val log = new PrintWriter(new File("query.log"))
    var n=1
    var arrBuff= new ArrayBuffer[Int]()
    arrBuff+=0

    
    println("Please enter any string to begin this Test Grade Program (Include 'q' in the string to quit).")
    var scanner =new Scanner(System.in)
    var runProgram= scanner.nextLine()

    while (runProgram!="q")
    { println(runProgram)
          
          
          println("Please enter the name for this person's test paper.")
        var inputName= scanner.nextLine()
          println("Please enter the class period for this person's test paper (class periods are 1-6).")
          var inputPeriod= scanner.nextInt()
          scanner.nextLine()
          println("Please enter a number 0-100 to indicate this person's points the test paper.")
          var inputScore= scanner.nextInt()
          scanner.nextLine()
          inputName= "'" + inputName + "'"

          // connect to the database named "mysql" on the localhost
          val driver = "com.mysql.jdbc.Driver"
          val url = "jdbc:mysql://localhost:3306/Demodatabase" // Modify for whatever port you are running your DB on
          val username = "root"
          val password = "FUzzy26!" // Update to include your password

          var connection:Connection = null

          try {
              // make the connection
              Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
              // val statement = connection.createStatement()
              // val resultSet = statement.executeQuery("SELECT * FROM Gradesheet")
              // create the statement, and run the select query
                val statement4 = connection.createStatement()
                val resultSet4 = statement4.executeQuery("SELECT * FROM Gradesheet")
                   log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'SELECT * FROM Gradesheet;' \n")
                // log.write(Calenter.getInstance().getTimeInMillies + Executing "SELECT * FROM Gradesheet")
            //  "statement.executeUpdate
              while ( resultSet4.next() ) {
              arrBuff+=  resultSet4.getString(1).toInt}
              n=arrBuff.max
              n += 1
           //  {arrBuff += resultSet4.getInt(i)}
            //  println(arrBuff)
              println(n)
              val statement2 = connection.createStatement()
              val resultSet2 = statement2.executeUpdate("INSERT INTO Gradesheet(StudentNumber, StudentName, Period, Grade) VALUES ("+n+", "+inputName+", "+inputPeriod+", "+inputScore+")" )
           //   log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'SELECT * FROM Orders WHERE OrderID = "+orderID+";'\n")
             log.write(Calendar.getInstance().getTimeInMillis  + " -  Executing 'INSERT INTO Gradesheet(StudentNumber, StudentName, Period, Grade) VALUES ("+n+", "+inputName+", "+inputPeriod+", "+inputScore+");' \n")
              // Change query to your table
            
              println()
              println("Please indicate if you would like to view your current Test Grade Spreadsheet by enter a string with at least 3 'y' characters.  Otherwise, enter any other string to continue")
                  var seeSpreadSheet= scanner.nextLine()
              if (seeSpreadSheet.contains('y')==true) {
                Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM Gradesheet")
                log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'SELECT * FROM Gradesheet;' \n")
                // log.write(Calenter.getInstance().getTimeInMillies + Executing "SELECT * FROM Gradesheet")
            //  "statement.executeUpdate
              while ( resultSet.next() ) {
                print(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3)+ " " + resultSet.getString(4))
                println()
              }
              }
              println("Please indicate if you made a mistake in this student's grade to redo it by entering a string with at least 3 'b' characters 'b' stands for 'bad' grade input.  Otherwise, enter any other string to continue." +
                "There is also an option to truncate the entire table in the event that more than the previous row needs to be delete.  Please enter 'truncate' to truncate the entire table")
              var mistake= scanner.nextLine()
            if (mistake.contains('b')==true)
              {Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
                val statement3 = connection.createStatement()
                val resultSet3 = statement3.executeUpdate("DELETE FROM Gradesheet WHERE StudentNumber = "+n+" ")
                 log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'DELETE FROM Gradesheet WHERE StudentNumber = "+n+"' \n")
                n-=1
                println("This record has been dropped.")
                }
                if (mistake.contains("truncate")==true)
               { Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
                val statement5 = connection.createStatement()
                val resultSet5 = statement5.executeUpdate("TRUNCATE Gradesheet")
                 log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'TRUNCATE Gradesheet' \n")
                 println("The Gradesheet has been truncated.")
                for (i <- 0 to arrBuff.length-1)
                  {arrBuff(i)=0}

                 n=0
              }
                n+=1
              println("Please enter any string to continue to enter the next student's grade (enter 'q' to quit).")
          
                  
          
      
          
       runProgram= scanner.nextLine()
          
     connection.close()
     log.close()
      }
       catch {
             case e: Exception => e.printStackTrace
             log.write("Error! " + e.getMessage())
            }
          } 
  }
            


}