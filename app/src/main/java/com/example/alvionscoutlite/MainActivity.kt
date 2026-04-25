package com.example.alvionscoutlite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlvionScoutTheme {
                AlvionScoutApp()
            }
        }
    }
}

data class ScoutNote(
    val locationName: String,
    val shootIdea: String,
    val lightingNote: String,
    val category: String,
    val x: Int,
    val y: Int,
    val actionType: String,
    val dateTime: String
)

@Composable
fun AlvionScoutTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = Color(0xFFD4AF37),
        secondary = Color(0xFFC9A227),
        background = Color(0xFF0B0B0B),
        surface = Color(0xFF151515),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun AlvionScoutApp() {
    var lastX by remember { mutableFloatStateOf(0f) }
    var lastY by remember { mutableFloatStateOf(0f) }
    var tapCount by remember { mutableIntStateOf(0) }
    var actionType by remember { mutableStateOf("No touch yet") }

    var locationName by remember { mutableStateOf("") }
    var shootIdea by remember { mutableStateOf("") }
    var lightingNote by remember { mutableStateOf("") }

    val categories = listOf(
        "Portrait",
        "Car",
        "Landscape",
        "Product",
        "Fashion",
        "Street"
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val savedNotes = remember { mutableStateListOf<ScoutNote>() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "ALVION SCOUT LITE",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Photography scouting tool for saving composition points and shoot ideas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Scouting Area",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tap or drag inside the area to mark a composition point.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(
                                    color = Color(0xFF202020),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        lastX = offset.x
                                        lastY = offset.y
                                        tapCount++
                                        actionType = "Tap"
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, _ ->
                                        lastX = change.position.x
                                        lastY = change.position.y
                                        actionType = "Drag"
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Touch here",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "X: ${lastX.toInt()}")
                            Text(text = "Y: ${lastY.toInt()}")
                            Text(text = "Taps: $tapCount")
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = "Last action: $actionType")
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "New Scouting Note",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        OutlinedTextField(
                            value = locationName,
                            onValueChange = { locationName = it },
                            label = { Text("Location name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = shootIdea,
                            onValueChange = { shootIdea = it },
                            label = { Text("Shoot idea") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = lightingNote,
                            onValueChange = { lightingNote = it },
                            label = { Text("Lighting note") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Box {
                            OutlinedButton(
                                onClick = {
                                    categoryMenuExpanded = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Category: $selectedCategory")
                            }

                            DropdownMenu(
                                expanded = categoryMenuExpanded,
                                onDismissRequest = {
                                    categoryMenuExpanded = false
                                }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(category)
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            categoryMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val formatter = SimpleDateFormat(
                                    "dd.MM.yyyy HH:mm",
                                    Locale.getDefault()
                                )

                                val note = ScoutNote(
                                    locationName = locationName.ifBlank {
                                        "Unnamed location"
                                    },
                                    shootIdea = shootIdea.ifBlank {
                                        "No idea entered"
                                    },
                                    lightingNote = lightingNote.ifBlank {
                                        "No lighting note"
                                    },
                                    category = selectedCategory,
                                    x = lastX.toInt(),
                                    y = lastY.toInt(),
                                    actionType = actionType,
                                    dateTime = formatter.format(Date())
                                )

                                savedNotes.add(0, note)

                                locationName = ""
                                shootIdea = ""
                                lightingNote = ""
                            }
                        ) {
                            Text("Save Scout Note")
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saved Notes (${savedNotes.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedButton(
                        onClick = {
                            savedNotes.clear()
                        }
                    ) {
                        Text("Clear All")
                    }
                }
            }

            if (savedNotes.isEmpty()) {
                item {
                    Text(
                        text = "No scouting notes saved yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                items(savedNotes) { note ->
                    ScoutNoteCard(note = note)
                }
            }
        }
    }
}

@Composable
fun ScoutNoteCard(note: ScoutNote) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D1D1D)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = note.locationName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(text = "Idea: ${note.shootIdea}")
            Text(text = "Lighting: ${note.lightingNote}")
            Text(text = "Category: ${note.category}")
            Text(text = "Touch position: X ${note.x}, Y ${note.y}")
            Text(text = "Action: ${note.actionType}")
            Text(text = "Saved: ${note.dateTime}")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val encodedLocation = Uri.encode(note.locationName)
                    val mapUri = Uri.parse("geo:0,0?q=$encodedLocation")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

                    context.startActivity(mapIntent)
                }
            ) {
                Text("Open in Google Maps")
            }
        }
    }
}