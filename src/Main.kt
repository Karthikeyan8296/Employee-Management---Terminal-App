import Memory.data
import com.google.gson.Gson
import java.io.File

fun main() {
    println("======================================")
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

    println("======================================")

    println("Select your work to do! by numbers from 1 - 6")

    val i = readln().toIntOrNull()

    when (i) {
        1 -> addEmployee()
        2 -> listAllEmployee()
        3 -> findEmployeeRole()
        4 -> filterBySalary()
        5 -> deleteEmployeeById()
        6 -> return
        else -> println("Sorry Please select one of these to continue")
    }
}

object Memory {
    var data = mutableMapOf<Int, Employee>()

    fun showData() {

        if(data.isEmpty()) {
            println("No Employee data!")
        }else{
            data.forEach {
                println("Employee data: ${it.value}")
            }
        }
    }
}

fun saveTheEmployeeData(){
    val gson = Gson()
    val converter = gson.toJson(data)

    val file = File("data.json")
    file.writeText(converter)
}

data class Employee(
    val id: Int?,
    val name: String,
    val role: Role = Role.NO_ROLE,
    val salary: Int?
)

enum class Role {
    MANAGER,
    DEVELOPER,
    INTERN,
    NO_ROLE
}

fun addEmployee() {
    //NAME
    println("Employee name?")
    val inputName = readln().trim()
    var name = ""
    if (inputName.isNotEmpty() && inputName.all { it.isLetter() }) {
        name = inputName
    } else {
        println("Invalid input")
        main()
    }

    //ROLE
    println("Employee role? \n1. MANAGER \n2. DEVELOPER \n3. INTERN")
    val role = readln().toIntOrNull()
    val roleSelected = when (role) {
        1 -> Role.MANAGER
        2 -> Role.DEVELOPER
        3 -> Role.INTERN
        else -> {
            println("Invalid role")
            main()
        }
    }

    //ID
    println("Employee id")
    val id = readln().toIntOrNull()
    if (id == null) {
        println("invalid id")
        main()
    }

    //SALARY
    println("Employee Salary")
    val salary = readln().toIntOrNull()
    if (salary == null) {
        println("invalid salary")
        main()
    }


    val employee = Employee(name = name, id = id, salary = salary, role = roleSelected as Role)
    data[id as Int] = employee
    println("Employee created successfully")

    saveTheEmployeeData()
    Memory.showData()
    main()
}

fun listAllEmployee() {
    Memory.showData()
    saveTheEmployeeData()
    main()
}

fun findEmployeeRole() {
    println("find employee by role")
    println("1. MANAGER \n2. DEVELOPER \n3. INTERN")
    val role = readln().toIntOrNull()

    when (role) {
        1 -> data.filter { it.value.role == Role.MANAGER }.forEach { println("Manager Role: $it") }
        2 -> data.filter { it.value.role == Role.DEVELOPER }.forEach { println("Developer Role: $it") }
        3 -> data.filter { it.value.role == Role.INTERN }.forEach { println("Intern Role: $it") }
        else -> println("Invalid role selected")
    }
    main()
}

fun deleteEmployeeById() {
    println("Enter the Employee ID, that you need to delete")
    val deleteId = readln().toIntOrNull()

    Memory.data.remove(key = deleteId as Int).also { println("Employee deleted successFully") }
    saveTheEmployeeData()
    main()
}

fun filterBySalary() {
    println("Select which type of filter you want:")
    println("1. Exact salary Amount \n2. Salary between min X and max Y")
    val enterAmount = readln().toIntOrNull()

    when (enterAmount) {
        1 -> filterByExactAmount()
        2 -> filterAmountMaxMin()
        else -> {
            println("Invalid option")
            main()
        }
    }
}

fun filterByExactAmount() {
    println("Enter the exact salary")
    val exact = readln().toIntOrNull()
    if(exact == null){
        println("Invalid salary")
        main()
    }else{
        Memory.data.filter { it.value.salary == exact }.forEach { println(it) }
        main()
    }
}

fun filterAmountMaxMin() {
    println("Enter the Minimum Salary")
    val min = readln().toIntOrNull()
    println("Enter the Maximum Salary")
    val max = readln().toIntOrNull()

    if(min == null || max == null){
        println("Invalid inputs")
        main()
    }else{
        Memory.data.filter { it.value.salary in min..<max }.forEach { println(it) }
        println("Salary fetched successfully!")
        main()
    }
}