package com.codework.dailyenglish.data.dictionary.remote

import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.remote.response.PageDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DictionaryApi {
    @GET("vocabularies/search")
    suspend fun searchWord(@Query("q") query: String): BaseResponse<PageDto<WordDto>>

    @GET("vocabularies/saved")
    suspend fun getSavedWords(): BaseResponse<PageDto<WordDto>>

    @POST("vocabularies/{id}/toggle-save")
    suspend fun toggleSaveVocabulary(
        @Path("id") id: String
    ): BaseResponse<Void>
}