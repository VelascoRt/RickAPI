package com.example.rickapi.services

import com.example.rickapi.models.Character
import com.example.rickapi.models.CharacterList
import retrofit2.http.GET
import retrofit2.http.Path

interface RickService {

    @GET("character")
    suspend fun getCharacters() : CharacterList

    @GET("character/{id}")
    suspend fun getCharacterByID(@Path("id") id:Int) : Character

}