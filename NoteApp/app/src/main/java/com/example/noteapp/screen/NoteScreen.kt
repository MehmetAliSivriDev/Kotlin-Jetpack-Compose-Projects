package com.example.noteapp.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.components.NoteButton
import com.example.noteapp.components.NoteInputText
import com.example.noteapp.model.Note
import com.example.noteapp.util.formatDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(modifier: Modifier = Modifier,
               notes: List<Note>,
               onAddNote: (Note) -> Unit,
               onRemoveNote: (Note) -> Unit,
               ){

    var title by remember {
        mutableStateOf("")
    }

    var descripton by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(modifier = modifier.padding(6.dp)) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Icon")
            },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDADFE3)),
                modifier = Modifier
        )

        Column(modifier = Modifier.fillMaxWidth(),
             horizontalAlignment = Alignment.CenterHorizontally){
            NoteInputText(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 10.dp,
                    bottom = 5.dp,
                    start = 5.dp,
                    end = 5.dp,
                ),
                text = title,
                label = "Title",
                onTextChange = {
                    if(it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) title = it
                }
            )
            NoteInputText(
                modifier = Modifier.fillMaxWidth().padding(
                    vertical = 5.dp,
                    horizontal = 5.dp
                ),
                text = descripton,
                label = "Add a note",
                onTextChange = {
                    if(it.all { char ->
                        char.isLetter() || char.isWhitespace()
                        }) descripton = it
                }
            )
            NoteButton(
                modifier = Modifier.width(200.dp).padding(
                    vertical = 5.dp
                ),
                text = "Save",
                onClick = {
                    if(title.isNotEmpty() && descripton.isNotEmpty()){
                        //Save/Add to the list
                        onAddNote(Note(
                            title = title,
                            description = descripton
                        ))

                        Toast.makeText(context,"Note added", Toast.LENGTH_SHORT).show()

                        title = ""
                        descripton = ""
                    }
                }
            )

        }
        HorizontalDivider(
            modifier = Modifier.padding(10.dp)
        )
        LazyColumn {
            items(notes){ note ->
                NoteRow(
                    note = note,
                    onNoteClicked = {onRemoveNote(note)},
                )
            }
        }

    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit,
){
    Surface(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 33.dp,
                    bottomStart = 33.dp
                )
            )
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        color = Color(0xFFDFE6EB),
        tonalElevation = 6.dp,
        shadowElevation = 1.5.dp

    ) {
        Column(
            modifier = modifier
                .clickable {
                    onNoteClicked(note)
                }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = note.title,style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ))
            Text(text = note.description,style = MaterialTheme.typography.labelMedium)
            Text(text = formatDate(note.entryDate.time) ,style = MaterialTheme.typography.bodyMedium)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview(){
    NoteScreen(
        notes = emptyList(),
        onAddNote = {},
        onRemoveNote = {})
}