package ba.untz.attendance1

data class Student(val id: String, val name: String)

enum class Window(val label: String){
    START("start"),
    MIDDLE("middle"),
    END("end")
}
enum class StatusKind(val label: String) {
    ON_TIME("On time"),
    LATE("Late"),
    ABSENT("Absent")
}

sealed interface CheckInStatus{
    data object OnTime: CheckInStatus
    data class Late(val minutes: Int): CheckInStatus
    data object Absent: CheckInStatus
}

data class CheckIn(
    val student: Student,
    val window: Window,
    val minutes: Int,
    val status: CheckInStatus
)

fun Student.initials() : String =
    name.split(" ")
        .filter { it.isNotBlank() }
        .map { it.first().uppercaseChar() }
        .joinToString("")

fun CheckInStatus.label() : String = when(this) {
    CheckInStatus.OnTime -> "On time"
    is CheckInStatus.Late-> "$minutes min late"
    CheckInStatus.Absent -> "Absent"
}

fun CheckIn.timeText(): String {
    if (status is CheckInStatus.Absent) return "-"
    val hours = minutes / 60
    val mins = minutes % 60
    return "%02d:%02d".format(hours, mins)
}

val sampleCheckIns = listOf(
    CheckIn(Student("IB101", "Amina Bajrektarević"), Window.START, 481, CheckInStatus.OnTime),
    CheckIn(Student("IB102", "Amir Imširović"), Window.START, 492, CheckInStatus.Late(12)),
    CheckIn(Student("IB103", "Faris Ćosić"), Window.START, 480, CheckInStatus.OnTime),
    CheckIn(Student("IB104", "Hedija Šišić"), Window.START, 505, CheckInStatus.Late(25)),
    CheckIn(Student("IB105", "Selma Lugavić"), Window.START, 0, CheckInStatus.Absent),
    CheckIn(Student("IB106", "Elma Đonlić"), Window.START, 483, CheckInStatus.OnTime),

    CheckIn(Student("IB101", "Amina Bajrektarević"), Window.MIDDLE, 601, CheckInStatus.OnTime),
    CheckIn(Student("IB102", "Amir Imširović"), Window.MIDDLE, 602, CheckInStatus.OnTime),
    CheckIn(Student("IB103", "Faris Ćosić"), Window.MIDDLE, 608, CheckInStatus.Late(8)),
    CheckIn(Student("IB104", "Hedija Šišić"), Window.MIDDLE, 600, CheckInStatus.OnTime),
    CheckIn(Student("IB105", "Selma Lugavić"), Window.MIDDLE, 615, CheckInStatus.Late(15)),
    CheckIn(Student("IB106", "Elma Donlić"), Window.MIDDLE, 603, CheckInStatus.OnTime),

    CheckIn(Student("IB101", "Amina Bajrektarević"), Window.END, 690, CheckInStatus.OnTime),
    CheckIn(Student("IB102", "Amir Imširović"), Window.END, 0, CheckInStatus.Absent),
    CheckIn(Student("IB103", "Faris Ćosić"), Window.END, 691, CheckInStatus.OnTime)
)



