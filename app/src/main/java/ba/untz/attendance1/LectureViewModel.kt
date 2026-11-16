package ba.untz.attendance1


// Da je shown obicno polje konstruktora, moglo bi ostati zastarjelo i neuskladjeno s ostalim poljima ako se promijeni filter, a zaboravi azurirati shown.
// Za primjer: korisnik upise query = "ami", ViewModel napravi copy(query = "ami"), ali zaboravi ponovo filtrirati shown, pa na ekranu u search polju pise "ami", dok lista i dalje prikazuje sve studente.
data class LectureUiState(
    val lecture: Lecture,
    val openWindow: Window? = null,
    val checkIns: List<CheckIn> = emptyList(),
    val query: String = "",
    val onlyLate: Boolean = false
) {
    val inWindow: List<CheckIn>
        get() = if (openWindow == null) checkIns else checkIns.filter { it.window == openWindow }

    val shown: List<CheckIn>
        get() = inWindow.filter { checkIn ->
            val matchesQuery = checkIn.student.name.contains(query, ignoreCase = true)
            val matchesLate = !onlyLate || checkIn.status is CheckInStatus.Late
            matchesQuery && matchesLate
        }
}