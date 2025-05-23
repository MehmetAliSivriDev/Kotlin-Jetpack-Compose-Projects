package com.example.movieapp.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies


@Composable
fun MovieRow(movie: Movie, onItemClick: (String) -> Unit = {}){
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
//            .height(130.dp)
            .clickable {
                onItemClick(movie.id)
            },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Surface (
                modifier = Modifier.padding(12.dp).size(100.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ){
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(movie.images[0])
                            .crossfade(true)
                            .transformations(CircleCropTransformation())
                            .build()
                    ),
                    contentDescription = "Movie Poster",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(120.dp)
                )
//                Icon(imageVector = Icons.Default.AccountBox, contentDescription = "MovieImage")
            }
            Column(modifier= Modifier.padding(4.dp)) {
                Text(movie.title, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ))
                Text("Director: ${movie.director}", style = MaterialTheme.typography.bodyMedium)
                Text("Released: ${movie.year}", style = MaterialTheme.typography.bodyMedium)

                AnimatedVisibility(
                    visible = expanded
                ) {
                    Column {
                        Text(buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(color = Color.DarkGray,
                                    fontSize = 13.sp)
                            ){
                                append("Plot: ")
                            }
                            withStyle(
                                style = SpanStyle(color = Color.DarkGray,
                                    fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            ){
                                append(movie.plot)
                            }
                        }, modifier = Modifier.padding(6.dp))

                        HorizontalDivider(
                            modifier = Modifier.padding(3.dp)
                        )

                        Text(text = "Director: ${movie.director}",
                            style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Actors: ${movie.actors}",
                            style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Rating: ${movie.rating}",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Icon(imageVector = if(expanded) Icons.Filled.KeyboardArrowUp else
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down Arrow",
                    modifier = Modifier.size(25.dp).clickable {
                        expanded = !expanded
                    },
                    tint = Color.DarkGray)
            }
        }



    }
}