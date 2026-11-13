package ba.untz.attendance1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ba.untz.attendance1.ui.theme.Attendance1Theme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Attendance1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfessorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CheckInRow(
    checkIn: CheckIn,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box {
            Avatar(initials = checkIn.student.initials())
            if (checkIn.status is CheckInStatus.Late) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = checkIn.student.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            StatusBadge(status = checkIn.status)
        }


        Text(
            text = checkIn.timeText(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}






@Preview(name = "On time", showBackground = true)
@Composable
fun CheckInCardOnTimePreview() {
    Attendance1Theme {
        CheckInRow(checkIn = sampleCheckIns[0])
    }
}

@Preview(name = "Late", showBackground = true)
@Composable
fun CheckInCardLatePreview() {
    Attendance1Theme {
        CheckInRow(checkIn = sampleCheckIns[1])
    }
}

// 3. Preview za status "Absent" (Selma)
@Preview(name = "Absent", showBackground = true)
@Composable
fun CheckInCardAbsentPreview() {
    Attendance1Theme {
        CheckInRow(checkIn = sampleCheckIns[4])
    }
}

@Composable
fun AttendanceSummary(
    checkIns: List<CheckIn>,
    modifier: Modifier = Modifier
) {
    val onTimeCount = checkIns.count { it.status is CheckInStatus.OnTime }
    val lateCount = checkIns.count { it.status is CheckInStatus.Late }
    val absentCount = checkIns.count { it.status is CheckInStatus.Absent }

    Text(
        text = "On time: $onTimeCount · Late: $lateCount · Absent: $absentCount",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AttendanceSummaryPreview() {
    AttendanceSummary(checkIns = sampleCheckIns)
}

@Preview(showBackground = true)
@Composable
fun AttendanceSummaryMiddlePreview() {
    AttendanceSummary(
        checkIns = sampleCheckIns.filter { it.window == Window.MIDDLE }
    )
}

@Composable
fun Avatar(
    initials: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun StatusBadge(
    status: CheckInStatus,
    modifier: Modifier = Modifier
) {
    Text(
        text = status.label(),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        color = status.color(), // <- Ovdje primijenjena nova funkcija
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
fun CheckInStatus.color(): Color {
    return when (this) {
        is CheckInStatus.OnTime -> MaterialTheme.colorScheme.primary
        is CheckInStatus.Late -> MaterialTheme.colorScheme.error
        is CheckInStatus.Absent -> MaterialTheme.colorScheme.outline
    }
}
@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CheckInRowPreview() {
    Attendance1Theme {
        Surface {
            CheckInRow(checkIn = sampleCheckIns[1]) // Amir Imširović (Late)
        }
    }
}
@Composable
fun WindowControls(
    modifier: Modifier = Modifier
) {
    var openWindow by remember { mutableStateOf<Window?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Open: ${openWindow?.name?.lowercase() ?: "none"}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Window.entries.forEach { w ->
                Button(
                    onClick = {
                        openWindow = if (openWindow == w) null else w
                        Log.d("RMAS", "now: ${openWindow?.name?.lowercase() ?: "none"}")
                    },
                    enabled = openWindow != w,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = w.name.lowercase())
                }
            }
        }
    }
}
@Composable
fun CheckInCard(
    checkIn: CheckIn,
    allCheckIns: List<CheckIn>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        CheckInRow(checkIn = checkIn)

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 64.dp, top = 4.dp, bottom = 8.dp)
            ) {
                val studentCheckIns = sampleCheckIns.filter { it.student == checkIn.student }

                studentCheckIns.forEach { item ->

                    Text(
                        text = "${item.window.name.lowercase()}: ${item.status.label()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WindowControls(
    openWindow: Window?,
    onOpen: (Window?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Open: ${openWindow?.name?.lowercase() ?: "none"}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Window.entries.forEach { w ->
                Button(
                    onClick = {
                        val nextWindow = if (openWindow == w) null else w
                        onOpen(nextWindow)
                    },
                    enabled = openWindow != w,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = w.name.lowercase())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WindowControlsMiddlePreview() {
    Attendance1Theme {
        WindowControls(
            openWindow = Window.MIDDLE,
            onOpen = {}
        )
    }
}
@Composable
fun ProfessorScreen(
    modifier: Modifier = Modifier
) {
    var openWindow by rememberSaveable { mutableStateOf<Window?>(null) }
    var checkIns by remember { mutableStateOf(sampleCheckIns) }
    val currentCheckIns = checkIns.filter { it.window == openWindow }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            WindowControls(
                openWindow = openWindow,
                onOpen = { openWindow = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (openWindow == null) {

            } else {
                AttendanceSummary(
                    checkIns = currentCheckIns
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    currentCheckIns.forEach { checkIn ->
                        CheckInCard(
                            checkIn = checkIn,
                            allCheckIns = checkIns
                        )
                    }
                }
            }
        }
    }
}