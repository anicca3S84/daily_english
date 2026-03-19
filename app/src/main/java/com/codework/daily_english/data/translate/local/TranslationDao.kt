package com.codework.daily_english.data.translate.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codework.daily_english.data.translate.local.entity.TranslationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(entity: TranslationEntity)

    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<TranslationEntity>>
}