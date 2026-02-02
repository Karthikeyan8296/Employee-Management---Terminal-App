import Memory.data
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

fun main() {
    //it blocks the thread, and complete the thread works, and it comes back
    runBlocking {
        while (true) {
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
                6 -> {
                    println("Thank you for logging!")
                    break
                }

                else -> println("Sorry Please select one of these to continue")
            }
        }
    }
}

//Local storage
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


//serialization
suspend fun saveData() = withContext(Dispatchers.IO) {
    //if we are using default values in data classes, use factory
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    //for collection, we must specify the type
    val type = Types.newParameterizedType(
        MutableMap::class.java,
        Int::class.javaObjectType,
        Employee::class.java
    )
    //pass the type inside, adapter
    val adapter = moshi.adapter<MutableMap<Int, Employee>>(type)

    val converter = adapter.toJson(data)

    val file = File("data.json")
    file.writeText(converter)
}

//deserialization
suspend fun loadData() = withContext(Dispatchers.IO) {
    val file = File("data.json")

    //if the file doesn't exist at all
    if (!file.exists()) {
        data = mutableMapOf()
        return@withContext
    }

    val json = file.readText()

    //if there is no data in file
    if (json.isEmpty()) {
        data = mutableMapOf()
        return@withContext
    }

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val type = Types.newParameterizedType(
        MutableMap::class.java,
        Int::class.javaObjectType,
        Employee::class.java
    )

    val adapter = moshi.adapter<MutableMap<Int, Employee>>(type)

    data = adapter.fromJson(json) ?: mutableMapOf()
}

data class Employee(
    val id: Int, val name: String, val role: Role = Role.NO_ROLE, val salary: Int
)

enum class Role {
    MANAGER, DEVELOPER, INTERN, NO_ROLE
}

//database operation
suspend fun addEmployee() = withContext(Dispatchers.IO) {
    //NAME
    println("Employee name?")
    val inputName = readln().trim()
    //need to cover the name in small case, and add . for initial edge cases
    if (inputName.isEmpty() || !inputName.all { it.isLetter() }) {
        println("Invalid input")
        return@withContext
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
            return@withContext
        }
    }

    //ID
    println("Employee id, only in numbers")
    val id = readln().toIntOrNull() ?: run {
        println("Invalid input")
        return@withContext
    }

    //SALARY
    println("Employee Salary")
    val salary = readln().toIntOrNull() ?: run {
        println("Invalid input")
        return@withContext
    }

    val employee = Employee(name = inputName, id = id, salary = salary, role = roleSelected)
    data[id] = employee
    println("Employee created successfully")

    saveData()
    Memory.showData()
    return@withContext
}

//calculation operation
suspend fun listAllEmployee() = withContext(Dispatchers.Default) {
    Memory.showData()
    return@withContext
}


//calculation operation
suspend fun findEmployeeRole() = withContext(Dispatchers.Default) {
    println("find employee by role")
    println("1. MANAGER \n2. DEVELOPER \n3. INTERN")
    val role = readln().toIntOrNull()
    when (role) {
        1 -> {
            val result = data.filter { it.value.role == Role.MANAGER }
            if (result.isEmpty()) {
                println("No Employee found on Manager Role!")
                return@withContext
            } else {
                data.filter { it.value.role == Role.MANAGER }.forEach { println("Manager Role: $it") }
            }
        }

        2 -> {
            val result = data.filter { it.value.role == Role.DEVELOPER }
            if (result.isEmpty()) {
                println("No Employee found on Developer Role!")
                return@withContext
            }
            data.filter { it.value.role == Role.DEVELOPER }.forEach { println("Developer Role: $it") }
        }

        3 -> {
            val result = data.filter { it.value.role == Role.INTERN }
            if (result.isEmpty()) {
                println("No Employee found on Intern Role")
                return@withContext
            } else {
                data.filter { it.value.role == Role.INTERN }.forEach { println("Intern Role: $it") }
            }
        }

        else -> {
            println("Invalid role selected")
            return@withContext
        }
    }
    return@withContext
}


//database operations
suspend fun deleteEmployeeById() = withContext(Dispatchers.IO) {
    println("Enter the Employee ID, that you need to delete")
    val deleteId = readln().toIntOrNull()

    if (deleteId == null) {
        println("Failed to delete Employee")
        return@withContext
    } else {
        val result = data.filter { it.key == deleteId }
        if (result.isEmpty()) {
            println("No Employee Id like this found")
            return@withContext
        } else {
            data.remove(deleteId).also { println("Employee Id deleted successfully") }
            saveData()
            return@withContext
        }
    }
}

//calculation operation
suspend fun filterBySalary() = withContext(Dispatchers.Default) {
    println("Select which type of filter you want:")
    println("1. Exact salary Amount \n2. Salary between min X and max Y")
    val enterAmount = readln().toIntOrNull()

    when (enterAmount) {
        1 -> filterByExactAmount()
        2 -> filterAmountMaxMin()
        else -> {
            println("Invalid option")
            return@withContext
        }
    }
}

suspend fun filterByExactAmount() = withContext(Dispatchers.Default) {
    println("Enter the exact salary")
    val exact = readln().toIntOrNull()
    if (exact == null) {
        println("Invalid salary")
        return@withContext
    } else {
        val result = data.filter { it.value.salary == exact }
        if (result.isEmpty()) {
            println("No salary like this found")
            return@withContext
        } else {
            result.forEach {
                println(it)
                return@withContext
            }
        }
    }
}

suspend fun filterAmountMaxMin() = withContext(Dispatchers.Default) {
    println("Enter the Minimum Salary")
    val min = readln().toIntOrNull()
    println("Enter the Maximum Salary")
    val max = readln().toIntOrNull()

    if (min == null || max == null) {
        println("Invalid inputs")
        return@withContext
    } else {
        val result = data.filter { it.value.salary in min..max }
        if (result.isEmpty()) {
            println("No salary like this found")
            return@withContext
        } else {
            result.forEach {
                println(it)
                return@withContext
            }
        }
    }
}