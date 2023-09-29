package com.dicoding.githubuser.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.githubuser.data.entity.UserFavoriteEntity

@Dao
interface GithubDAO {
    @Query("SELECT  * FROM favorite_user")
    fun getAllFavorite(): LiveData<List<UserFavoriteEntity>>

    @Query("SELECT * FROM favorite_user WHERE username = :username")
    fun getUserInfo(username: String): LiveData<List<UserFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: UserFavoriteEntity)

    @Update
    fun update(fav: UserFavoriteEntity)

    @Delete
    fun delete(fav: UserFavoriteEntity)

}