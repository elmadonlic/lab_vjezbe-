package ba.untz.attendance1
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

class LectureViewModel(private val lectureId: String) : ViewModel() {

    private val initialLecture = sampleLectures.find { it.id == lectureId } ?: sampleLectures.first()

    private val _uiState = MutableStateFlow(
        LectureUiState(
            lecture = initialLecture,
            checkIns = sampleCheckIns
        )
    )
    val uiState: StateFlow<LectureUiState> = _uiState.asStateFlow()

    init {
        Log.d("Lab6", "ViewModel created: $lectureId")
    }

    fun openWindow(window: Window?) {
        _uiState.update { it.copy(openWindow = window) }
    }

    fun setQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }
    fun setOnlyLate(onlyLate: Boolean) {
        _uiState.update { it.copy(onlyLate = onlyLate) }
    }

    fun addCheckIn(checkIn: CheckIn) {
        _uiState.update { it.copy(checkIns = it.checkIns + checkIn) }
    }

    override fun onCleared() {
        Log.d("Lab6", "ViewModel cleared: $lectureId")
        super.onCleared()
    }
}