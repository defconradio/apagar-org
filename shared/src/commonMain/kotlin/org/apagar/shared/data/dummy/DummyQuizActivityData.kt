package org.apagar.shared.data.dummy

import org.apagar.shared.domain.model.QuizActivity

object DummyQuizActivityData {
    private val firstNames = listOf(
        "Luis", "Carla", "Pedro", "Emily", "María", "James", "Camila", "Sofía",
        "Diego", "Ana", "Mateo", "Olivia", "Valentina", "Daniel", "Emma", "Nicolás",
        "Isabella", "Alexander", "Javier", "Charlotte", "Lucía", "William", "Alejandro", "Mia",
        "Sebastián", "Sophia", "Andrés", "Benjamin", "Paula", "Henry", "Miguel", "Harper",
        "Elena", "Ethan", "Ricardo", "Amelia", "Gabriel", "Evelyn", "Fernando", "Lucas",
        "Victoria", "Samuel", "Inés", "David", "Thomas", "Daniela", "Michael", "Martín",
        "Julia", "Robert"
    )

    private val surnames = listOf(
        "Rodríguez", "Pérez", "González", "Martín", "Herrera", "Fernández",
        "García", "Silva", "López", "Gómez"
    )

    private val names = firstNames.flatMap { firstName ->
        surnames.map { surname -> "$firstName $surname" }
    }

    private val countryCodes = listOf("VE", "AR", "US", "ES")

    fun recentActivities(count: Int = 16): List<QuizActivity> {
        val normalizedCount = count.coerceIn(1, names.size)

        return names
            .shuffled()
            .take(normalizedCount)
            .mapIndexed { index, name ->
                val countryCode = countryCodes[(index * 7 + name.length) % countryCodes.size]
                val cents = (23400..833488).random()
                QuizActivity(name, countryCode, cents / 100.0)
            }
    }

    fun randomActivity(): QuizActivity {
        val name = "${firstNames.random()} ${surnames.random()}"
        val countryCode = countryCodes.random()
        val cents = (23400..833488).random()
        return QuizActivity(name, countryCode, cents / 100.0)
    }
}
