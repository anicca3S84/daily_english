package com.codework.dailyenglish.data.vocabulary.remote

import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.remote.response.PageDto
import com.codework.dailyenglish.data.vocabulary.remote.dto.VocabTopicDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VocabularyApi {
    @GET("vocab-topics")
    suspend fun getVocabTopics(
    ): BaseResponse<List<VocabTopicDto>>

    @GET("vocabularies/{topicPublicId}/topics")
    suspend fun getVocabulariesByTopic(
        @Path("topicPublicId") topicPublicId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<PageDto<WordDto>>

}