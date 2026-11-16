package ba.untz.attendance1


data class Lecture(
    val id: String,
    val number: Int,
    val title: String
)

val sampleLectures = listOf(
    Lecture("RMAS-1", 1, "Introduction to Android"),
    Lecture("RMAS-2", 2, "Kotlin basics"),
    Lecture("RMAS-3", 3, "First Compose UI"),
    Lecture("RMAS-4", 4, "State"),
    Lecture("RMAS-5", 5, "Lists and navigation")
)