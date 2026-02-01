import Memory.data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun main() {
    loadData()

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
        if (data.isEmpty()) {
            println("No Employee data!")
        } else {
            data.forEach {
                println("Employee data: ${it.value}")
            }
        }
    }
}

fun saveData() {
    val gson = Gson()
    val converter = gson.toJson(data)

    val file = File("data.json")
    file.writeText(converter)
}

fun loadData() {
    val file = File("data.json")
    if (!file.exists()) return

    val json = file.readText()
    val gson = Gson()

    val type = object : TypeToken<MutableMap<Int, Employee>>() {}.type
    Memory.data = gson.fromJson(json, type) ?: mutableMapOf()
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
    //need to cover the name in small case, and add . for initial edge cases
    if (inputName.isEmpty() || !inputName.all { it.isLetter() }) {
        println("Invalid input")
        return main()
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
            return main()
        }
    }

    //ID

    println("Employee id, only in numbers")
    val id = readln().toIntOrNull() ?: run {
        println("Invalid input")
        return main()
    }

    //SALARY
    println("Employee Salary")
    val salary = readln().toIntOrNull() ?: run {
        println("Invalid input")
        return main()
    }

    val employee = Employee(name = inputName, id = id, salary = salary, role = roleSelected)
    data[id] = employee
    println("Employee created successfully")

    saveData()
    Memory.showData()
    main()
}

fun listAllEmployee() {
    Memory.showData()
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

    if (deleteId == null || Memory.data.remove(deleteId) == null) {
        println("Delete Employee failed")
        main()
    } else {
        data.remove(deleteId).also { println("Employee data deleted successfully") }
        saveData()
        main()
    }
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
    if (exact == null) {
        println("Invalid salary")
        main()
    } else {
        val result = data.filter { it.value.salary == exact }
        if (result.isEmpty()) {
            println("No salary like this found")
            main()
        } else {
            result.forEach {
                println(it)
                main()
            }
        }
    }
}

fun filterAmountMaxMin() {
    println("Enter the Minimum Salary")
    val min = readln().toIntOrNull()
    println("Enter the Maximum Salary")
    val max = readln().toIntOrNull()

    if (min == null || max == null) {
        println("Invalid inputs")
        main()
    } else {
        val result = data.filter { it.value.salary in min..max }
        if (result.isEmpty()) {
            println("No salary like this found")
        } else {
            result.forEach {
                println(it)
                main()
            }
        }
    }
}