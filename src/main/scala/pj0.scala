//Author: Leif Riggs
//Date: 1/11/2022
//Purpose: To provide a Grade Sheet Update program to input student scores for a graded assignment

//import all the libraries
import java.sql.DriverManager
import java.sql.Connection
import java.util.Scanner
import scala.collection.mutable.ArrayBuffer
import java.io.PrintWriter
import java.io.File
import java.util.Calendar
object pj0 {

  def main(args: Array[String]):Unit = {   //begin main method
    val log = new PrintWriter(new File("query.log")) //assign a variable to action log to track activity in the terminal during program execution
    var n=1 // this variable is used for uniquely numbering each student record
    var arrBuff= new ArrayBuffer[Int]() // this array buffer is used to help append an existing gradesheet so the first new input will be numbered correctly
    arrBuff+=0 // start the array buffer to have an element equal to 0 so that it is not empty when taking the MAX later in the program

    
    println("Please enter any string to begin this Gradesheet Program (enter 'q' to quit).")  //prompt to enter into the program loop
    var scanner =new Scanner(System.in)
    var runProgram= scanner.nextLine()  //input any string besides 'q' to enter the program

    while (runProgram!="q")  //loop condition to either continue entering grades or to end the program
    { 
     println("Please enter the name for this students's test paper.") // prompt to enter student name
     var inputName= scanner.nextLine() // scanner to accept input for student name
     println("Please enter the class period for this student's test paper (class periods are 1-6).")  //prompt to enter student class period
     var inputPeriod= scanner.nextInt() //scanner to accept input for student class period
     scanner.nextLine() // must use nextLine scanner when scannning an integer
     println("Please enter a number 0-100 to indicate this student's points the test paper.") //prompt to input grade score 
     var inputScore= scanner.nextInt()  // scanner to accept input for student test score
     scanner.nextLine()
     inputName= "'" + inputName + "'" // include single quotes for student name for mysql syntax

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
        /* In this next section, consideration is taken as to how to select the correct value for n which is the StudentNumber on the gradesheet.  
        The purpose of this is to either start the student number at 1 if the gradesheet starts blank, or to start at the next consecutive number
        if the gradesheet already has some records.  This is done by first SELECTING the entire gradesheet, and then by using a while loop to save
        the first entry of each row into the array buffer.  The variable n is then used to take the max of this array buffer.  Lastly 1 is added to this 
        max which either increases it from 0 to 1, or it increases it from the last record number to the current record number. */
        val statement4 = connection.createStatement()
        val resultSet4 = statement4.executeQuery("SELECT * FROM Gradesheet")
        log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'SELECT * FROM Gradesheet;' \n")
              
        while ( resultSet4.next() ) {
          arrBuff+=  resultSet4.getString(1).toInt
          }
        n=arrBuff.max
        n += 1

        /* In this next section, the inputs from the above prompts for StudentName, Period, and Grade are used to INSERT into the Gradesheet
        by using executeUpdate  */
        val statement2 = connection.createStatement()
        val resultSet2 = statement2.executeUpdate("INSERT INTO Gradesheet(StudentNumber, StudentName, Period, Grade) VALUES ("+n+", "+inputName+", "+inputPeriod+", "+inputScore+")" )
        log.write(Calendar.getInstance().getTimeInMillis  + " -  Executing 'INSERT INTO Gradesheet(StudentNumber, StudentName, Period, Grade) VALUES ("+n+", "+inputName+", "+inputPeriod+", "+inputScore+");' \n")
        println()
        /* In this next section, the user is prompted to enter 'y' if a printout of the current gradesheet is desired.  To do this, a mysql SELECT statement is used within 
        executeQuery.  A while loop is then ran on the results to print each indexed string from the resultSet variable assigned to the SELECT statement. */
        println("Please indicate if you would like to view your current Test Grade Spreadsheet by entering 'y'.  Otherwise, please enter any other string to continue.")
        var seeSpreadSheet= scanner.nextLine()
        if (seeSpreadSheet.contains('y')==true) {
          Class.forName(driver)
          connection = DriverManager.getConnection(url, username, password)
          val statement = connection.createStatement()
          val resultSet = statement.executeQuery("SELECT * FROM Gradesheet")
          log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'SELECT * FROM Gradesheet;' \n")
              
          while ( resultSet.next() ) {
            print(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3)+ " " + resultSet.getString(4))
            println()
            }
        }

        /* In this next section, the user will be able to either delete the last student record, and the user will also have the ability to truncate the entire table and 
        start a new gradesheet.  This is done with either a DELETE clause or TRUNCATE clause within excuteUpdate commands.  These changes to the table will only happen if specific user
        input is given based on the prompts.  Otherwise, the blocks of code are skipped, and this will lead to another loop of user input for the next student grade in the Gradesheet. 
        
        */
        println("Please indicate if you made a mistake in this student's grade, and that you would like redo it by entering 'b' ('b' stands for 'bad' grade input).") 
        println("Otherwise, enter any other string to continue.")
        println("There is also an option to truncate the entire table in the event that more than the previous row needs to be deleted.  Please enter 'truncate' to truncate the entire table")
        var mistake= scanner.nextLine()
        if (mistake.contains('b')==true) {
          Class.forName(driver)
          connection = DriverManager.getConnection(url, username, password)
          val statement3 = connection.createStatement()
          val resultSet3 = statement3.executeUpdate("DELETE FROM Gradesheet WHERE StudentNumber = "+n+" ")
          log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'DELETE FROM Gradesheet WHERE StudentNumber = "+n+"' \n")
          n-=1
          println("This record has been dropped.")
          }
        if (mistake.contains("truncate")==true) {
          Class.forName(driver)
          connection = DriverManager.getConnection(url, username, password)
          val statement5 = connection.createStatement()
          val resultSet5 = statement5.executeUpdate("TRUNCATE Gradesheet")
          log.write(Calendar.getInstance().getTimeInMillis + " - Excecuting 'TRUNCATE Gradesheet' \n")
          println("The Gradesheet has been truncated.")// Note that within this conditional block of code, this for loop sets all the values in the array buffer to 0.  
          for (i <- 0 to arrBuff.length-1) // This is because a new Gradesheet table needs the first student entry to go back to 1
          
            {arrBuff(i)=0}

          n=0
        }
        n+=1 // always increase the student number by 1 for each loop
        println("Please enter any string to continue to enter the next student's grade (enter 'q' to quit).")  
        runProgram= scanner.nextLine()  //input 'q' to quit the program
          
        connection.close()
   
      }
      catch {
        case e: Exception => e.printStackTrace
        log.write("Error! " + e.getMessage())
      }

    }

  log.close() //close the activity log after the program is finished running
  println("The activity log can now be checked before the program closes.  Please enter any string to close the program.")
  var inputName= scanner.nextLine()
  println("Thank you for using this GradeSheet Program of Leif Riggs.")
  }
}