fun main(){
    println("Welcome to Employee Management!!")

    //Add
    println("1. Add Employee")

    //List
    println("2. List All the Employee")

    //Find
    println("3. Find Employee by Role")

    //Filter
    println("4. Filter Employee by Salary")

    //Delete
    println("5. Delete Employee by Id")

    println("Select your work to do! by numbers from 1 - 5")

    val i = readlnOrNull()?.toInt() ?: 0

    when(i){
        1 -> println("Add is selected")
        2 -> println("List all is selected")
        3 -> println("find by role is selected")
        4 -> println("Filter salary is selected")
        5 -> println("delete is selected")
    }
}