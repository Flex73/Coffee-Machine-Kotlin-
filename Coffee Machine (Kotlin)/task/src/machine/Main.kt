package machine

enum class Coffee(val coffee: Int) {
    ESPRESSO(1),
    LATTE(2),
    CAPPUCCINO(3),
}

enum class Actions(val action: String) {
    FILL("fill"),
    BUY("buy"),
    TAKE("take"),
    REMAINING("remaining"),
    EXIT("exit"),
}

const val ESPRESSO_WATER = 250
const val ESPRESSO_BEANS = 16
const val ESPRESSO_COST = 4

const val LATTE_WATER = 350
const val LATTE_MILK = 75
const val LATTE_BEANS = 20
const val LATTE_COST = 7

const val CAPPUCCINO_WATER = 200
const val CAPPUCCINO_MILK = 100
const val CAPPUCCINO_BEANS = 12
const val CAPPUCCINO_COST = 6

data class MachineState(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int)

fun main() {
    val state = MachineState(400, 540, 120, 9, 550)
    while (true) {
        val input = readln().uppercase()

        val action = try {
            Actions.valueOf(input)
        } catch (e: IllegalArgumentException) {
            println("Invalid action: $input")
            return
        }

        when (action) {
            Actions.BUY -> {
                val (newWater, newMilk, newBeans, newMoney) = buyCoffee(
                    state.water,
                    state.milk,
                    state.beans,
                    state.money
                )
                if (newWater == state.water
                    && newMilk == state.milk
                    && newBeans == state.beans
                    && newMoney == state.money) {
                    continue
                }
                if (newWater >= 0 && newMilk >= 0 && newBeans >= 0 && newMoney >= 0) {
                    println("I have enough resources, making you a coffee!")
                    state.water = newWater
                    state.milk = newMilk
                    state.beans = newBeans
                    state.cups -= 1
                    state.money = newMoney
                } else {
                    var resource = ""

                    if (newWater < 0) {
                        resource = "water"
                    } else if (newMilk < 0) {
                        resource = "milk"
                    } else if (newBeans < 0) {
                        resource = "beans"
                    } else if (newMoney < 0) {
                        resource = "money"
                    }
                    println("Sorry, not enough $resource!")
                }
            }

            Actions.FILL -> {
                val (newWater, newMilk, newBeans, newCups) = fillMachine(
                    state.water,
                    state.milk,
                    state.beans,
                    state.cups
                )
                state.water = newWater
                state.milk = newMilk
                state.beans = newBeans
                state.cups = newCups
            }

            Actions.TAKE -> {
                state.money = takeMoney(state.money)
            }

            Actions.REMAINING -> {
                printCoffeeMachineState(state)
            }

            Actions.EXIT -> {
                break
            }
        }
    }
}

fun printCoffeeMachineState(state: MachineState) {
    println("""
        The coffee machine has:
        ${state.water} ml of water
        ${state.milk} ml of milk
        ${state.beans} g of coffee beans
        ${state.cups} disposable cups
        ${'$'}${state.money} of money
    """.trimIndent())
}

fun buyCoffee(water: Int, milk: Int, beans: Int, money: Int): List<Int> {
    println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
    val input = readln()

    if (input == "back") {
        return listOf(water, milk, beans, money)
    }

    val coffeeType = Coffee.values().find { it.coffee == input.toInt() } ?: run {
        println("Invalid input: $input")
        return listOf(water, milk, beans, money)
    }

    return when (coffeeType) {
        Coffee.ESPRESSO -> {
            val (newWater, newBeans, newMoney) = cookEspresso(water, beans, money)
            listOf(newWater, milk, newBeans, newMoney)
        }
        Coffee.LATTE -> {
            val (newWater, newMilk, newBeans, newMoney) = cookLatte(water, milk, beans, money)
            listOf(newWater, newMilk, newBeans, newMoney)
        }
        Coffee.CAPPUCCINO -> {
            val (newWater, newMilk, newBeans, newMoney) = cookCappuccino(water, milk, beans, money)
            listOf(newWater, newMilk, newBeans, newMoney)
        }
    }
}

fun cookEspresso(water: Int, beans: Int, cost: Int): List<Int> {
    val newWater = water - ESPRESSO_WATER
    val newBeans = beans - ESPRESSO_BEANS
    val newCost = cost + ESPRESSO_COST
    return listOf(newWater, newBeans, newCost)
}

fun cookLatte(water: Int, milk: Int, beans: Int, cost: Int): List<Int> {
    val newWater = water - LATTE_WATER
    val newMilk = milk - LATTE_MILK
    val newBeans = beans - LATTE_BEANS
    val newCost = cost + LATTE_COST
    return listOf(newWater, newMilk, newBeans, newCost)
}

fun cookCappuccino(water: Int, milk: Int, beans: Int, cost: Int): List<Int> {
    val newWater = water - CAPPUCCINO_WATER
    val newMilk = milk - CAPPUCCINO_MILK
    val newBeans = beans - CAPPUCCINO_BEANS
    val newCost = cost + CAPPUCCINO_COST
    return listOf(newWater, newMilk, newBeans, newCost)
}

fun fillMachine(water: Int, milk: Int, beans: Int, cups: Int): List<Int> {
    println("Write how many ml of water you want to add:")
    val waterAdd = readln().toInt() + water
    println("Write how many ml of milk you want to add:")
    val milkAdd = readln().toInt() + milk
    println("Write how many grams of coffee beans you want to add:")
    val beansAdd = readln().toInt() + beans
    println("Write how many disposable cups you want to add:")
    val cupsAdd = readln().toInt() + cups

    return listOf(waterAdd, milkAdd, beansAdd, cupsAdd)
}

fun takeMoney(money: Int): Int {
    println("I gave you $money")
    return 0
}
