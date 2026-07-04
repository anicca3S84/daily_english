package com.codework.dailyenglish.data.dictionary.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentWordSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(entity: RecentWordSearchEntity)

    @Query("SELECT * FROM recent_word_searches ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<RecentWordSearchEntity>>

    @Query("DELETE FROM recent_word_searches WHERE wordId = :wordId")
    suspend fun deleteRecentSearch(wordId: String)

    @Query("DELETE FROM recent_word_searches")
    suspend fun clearAll()
}