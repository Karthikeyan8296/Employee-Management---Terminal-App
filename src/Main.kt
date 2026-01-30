fun main(){
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
        2 -> listAllEmployee()
        3 -> findEmployeeRole()
        4 -> filterBySalary()
        5 -> deleteEmployeeById()
        6 -> return
        else -> println("Sorry Please select one of these to continue")
    }
}

object Memory{
    var data = mutableListOf<Employee>()

    fun showData(){
        data.forEach {
            println("your data: $it")
        }
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

    val employee = Employee(name = name, id = id, salary = salary, role = roleSelected)

    Memory.data.add(employee)

    println("Employee created successfully")

    Memory.showData()

    main()
}

fun listAllEmployee(){
    Memory.showData()
    main()
}

fun findEmployeeRole(){
    println("find employee by role")
    println("\n1. MANAGER \n2. DEVELOPER \n3. INTERN")
    val role = readln().toInt()

    when(role){
        1 -> Memory.data.filter { it.role == Role.MANAGER }.forEach { println("Manager Role: $it") }
        2 -> Memory.data.filter { it.role ==  Role.DEVELOPER}.forEach { println("Developer Role: $it") }
        3 -> Memory.data.filter { it.role == Role.INTERN}.forEach { println("Intern Role: $it") }
    }
    main()
}

fun deleteEmployeeById(){
    println("Enter the Employee ID, that you need to delete")
    val deleteId = readln().toInt()

    if(Memory.data.removeIf { it.id == deleteId }){
        println("Employee deleted successfully!!")
    }else{
        println("No Employee ID exist!!")
    }
    main()
}

fun filterBySalary(){
    println("Select which type of filter you want:")
    println("1. Exact salary Amount \n2. Salary between min X and max Y")
    val enterAmount = readln().toInt()

    when(enterAmount){
        1 -> filterByExactAmount()
        2 -> filterAmountMaxMin()
        else -> {
            println("Invalid salary")
            main()
        }
    }
}

fun filterByExactAmount(){
    println("Enter the exact salary")
    val exact = readln().toInt()
    Memory.data.filter { it.salary == exact }.forEach { println(it) }
    main()
}

fun filterAmountMaxMin(){
    println("Enter the Minimum Salary")
    val min = readln().toInt()
    println("Enter the Maximum Salary")
    val max = readln().toInt()

    Memory.data.filter { it.salary in min..<max }.forEach { println(it) }
    println("Salary fetched successfully!")
    main()
}