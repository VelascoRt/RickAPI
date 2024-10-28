package com.example.rickapi.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rickapi.models.Character
import com.example.rickapi.services.RickService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(innerPaddingValues: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var characters by remember {
        mutableStateOf(listOf<Character>())
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                isLoading = true
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val rickService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RickService::class.java)
                val characterList = rickService.getCharacters()
                characters = characterList.results
                Log.i("HomeScreenResponse", characterList.toString())
                isLoading = false
            } catch (e:Exception) {
                characters = listOf()
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
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize().background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            items(characters) {
                Card {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.background(Color.White)
                            .clickable { navController.navigate("detail/${it.id}") }
                    ){
                        AsyncImage(model = it.image,
                            modifier = Modifier.fillMaxWidth(),
                            contentDescription = it.name)
                        Text(text = it.computedName,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        //
                        val color = when (it.status) {
                            "Alive" -> Color.Green
                            "Dead" -> Color.Red
                            else -> Color.Gray
                        }
                        Box (modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)) {  }
                    }
                }
            }
        }
    }
}