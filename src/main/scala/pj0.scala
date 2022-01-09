import java.sql.DriverManager
import java.sql.Connection
import java.util.Scanner
object pj0 {


  def main(args: Array[String]):Unit = {
    
    var n=1
    
    println("Please enter any string to begin this Test Grade Program (Include 'q' in the string to quit).")
    var scanner =new Scanner(System.in)
    var runProgram= scanner.nextLine()

    while (runProgram.contains("q")==false)
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
              val statement2 = connection.createStatement()
              val resultSet2 = statement2.executeUpdate("INSERT INTO Gradesheet(StudentNumber, StudentName, Period, Grade) VALUES ("+n+", "+inputName+", "+inputPeriod+", "+inputScore+")" )
              // Change query to your table
              
              println()
              println("Please indicate if you would like to view your current Test Grade Spreadsheet by enter a string with at least 3 'y' characters.  Otherwise, enter any other string to continue")
                  var seeSpreadSheet= scanner.nextLine()
              if (seeSpreadSheet.contains('y')==true) {
                Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM Gradesheet")
            //  "statement.executeUpdate
              while ( resultSet.next() ) {
                print(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3)+ " " + resultSet.getString(4))
                println()
              }
              }
            
              println("Please indicate if you made a mistake in this student's grade to redo it by entering a string with at least 3 'b' characters 'b' stands for 'bad' grade input.  Otherwise, enter any other string to continue.")
              var mistake= scanner.nextLine()
            if (mistake.contains('b')==true)
              {Class.forName(driver)
              connection = DriverManager.getConnection(url, username, password)
                val statement3 = connection.createStatement()
                val resultSet3 = statement3.executeUpdate("DELETE FROM Gradesheet WHERE StudentNumber = "+n+" ")
                n-=1
                println("This record has been dropped.")
                }
                n+=1
              println("Please enter a string with at least 3 'q' characters to continue this Test Grade Program (enter 00' to quit).")
          
                  
          
      
          
       runProgram= scanner.nextLine()
          
     connection.close()
      }
       catch {
             case e: Exception => e.printStackTrace
            }
          } 
  }
            


}