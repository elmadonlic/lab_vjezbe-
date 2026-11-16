package ba.untz.attendance1

import org.junit.Assert.assertEquals
import org.junit.Test

class LectureUiStateTest {
    private val base = LectureUiState(
        lecture = sampleLectures.last(),
        checkIns = sampleCheckIns
    )

    @Test
    fun noOpenWindow_showsAll15CheckIns() {
        assertEquals(15, base.shown.size)
    }

    @Test
    fun middle_hasSixCheckIns() {
        val state = base.copy(openWindow = Window.MIDDLE)
        assertEquals(6, state.inWindow.size)
    }

    @Test
    fun middleAndOnlyLate_returnsFarisAndSelma() {
        val state = base.copy(openWindow = Window.MIDDLE, onlyLate = true)
        assertEquals(listOf("Faris Ćosić", "Selma Lugavić"), state.shown.map { it.student.name })
    }

    @Test
    fun middleAndQueryAmi_returnsAminaAndAmir() {
        val state = base.copy(openWindow = Window.MIDDLE, query = "ami")
        assertEquals(listOf("Amina Bajrektarević", "Amir Imširović"), state.shown.map { it.student.name })
    }
}