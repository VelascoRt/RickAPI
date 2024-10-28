package com.example.rickapi.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickapi.models.Character
import com.example.rickapi.models.Location
import com.example.rickapi.models.Origin
import com.example.rickapi.services.RickService
import com.example.rickapi.ui.theme.RickAPITheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun CharacterDetailScreen(id:Int,innerPaddingValues: PaddingValues) {
    val scope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(false)
    }
    var character by remember {
        mutableStateOf(Character(created = "", episode = listOf(""), gender = "", id = 0, image = "",
            Location(name = "", url = ""), name = "Rick", Origin(name = "", url = ""),
            species = "", status = "", type = "", url = ""))
    }
    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val characterService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RickService::class.java)
                isLoading = true
                character = characterService.getCharacterByID(id)
                Log.i("CharacterDetailScreen",character.toString())
                isLoading = false
            }catch (e:Exception) {
                character = Character(created = "", episode = listOf(""), gender = "", id = 0, image = "",
                    Location(name = "", url = ""), name = "Rick", Origin(name = "", url = ""),
                    species = "", status = "", type = "", url = "")
                Log.i("API_ERROR",e.toString())
                isLoading = false
            }

        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column  (
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val color = when (character.status) {
                "Alive" -> Color.Green
                "Dead" -> Color.Red
                else -> Color.Gray
            }

            AsyncImage(model = character.image, contentDescription = character.name, alignment = Alignment.Center, modifier = Modifier.padding(15.dp))
            Text(text = character.name, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
            Row {
                Text(text = "${character.gender} - ${character.status} : ${character.species}", textAlign = TextAlign.Center, fontSize = 12.sp)
                Box (modifier = Modifier
                    .padding(2.dp).offset(y = 4.dp, x = 2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)) {}
            }
            Text(text = "Location : ${character.location.name}")
            Text(text = "Origin : ${character.origin.name}")
            Text(text = "Episodes : ${character.episode}")
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CharacterDetailScreenPreview() {
    RickAPITheme {
        CharacterDetailScreen(id = 1, innerPaddingValues = PaddingValues(0.dp))
    }
}