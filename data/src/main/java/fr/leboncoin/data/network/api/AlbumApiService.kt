package fr.leboncoin.data.network.api

import fr.leboncoin.data.network.model.AlbumDto
import retrofit2.Response
import retrofit2.http.GET

interface AlbumApiService {
    
    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): Response<List<AlbumDto>>
}