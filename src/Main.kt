import javax.xml.crypto.Data

fun main(){
    println("Welcome to Employee Management!!!")

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

    //Exit
    println("6. Exit")

    println("Select your work to do! by numbers from 1 - 6")

    val i = readln().toInt()

    when(i){
        1 -> addEmployee()
        2 -> println("List all is selected")
        3 -> println("find by role is selected")
        4 -> println("Filter salary is selected")
        5 -> println("delete is selected")
        6 -> return
        else -> println("Sorry Please select one of these to continue")
    }
}

fun memory(data: List<Employee>){
    data.forEach {
        println("your data: $it")
    }
}

data class Employee(
    val id: Int,
    val name: String,
    val role: Role = Role.DEVELOPER,
    val salary: Int
)

enum class Role{
    MANAGER,
    DEVELOPER,
    INTERN,
    NO_ROLE
}

fun addEmployee(){
    //name
    println("Employee name?")
    val name = readln()

    println("Employee role? \n1. MANAGER \n2. DEVELOPER \n3. INTERN")
    val role = readln().toInt()
    val roleSelected = when(role){
        1 -> Role.MANAGER
        2 -> Role.DEVELOPER
        3 -> Role.INTERN
        else -> Role.NO_ROLE
    }

    println("Employee id")
    val id = readln().toInt()

    println("Employee Salary")
    val salary = readln().toInt()

    val employee = listOf<Employee>(Employee(name = name, id = id, salary = salary, role = roleSelected))

    memory(data = employee)

    println("Cool! wanna add some?")
    continues()
}



fun continues(){
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

    //Exit
    println("6. Exit")

    println("Select your work to do! by numbers from 1 - 6")

    val i = readln().toInt()

    when(i){
        1 -> addEmployee()
        2 -> println("List all is selected")
        3 -> println("find by role is selected")
        4 -> println("Filter salary is selected")
        5 -> println("delete is selected")
        6 -> return
        else -> println("Sorry Please select one of these to continue")
    }
}