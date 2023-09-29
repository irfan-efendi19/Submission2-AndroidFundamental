package com.dicoding.githubuser.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubuser.data.entity.UserFavoriteEntity

@Database(entities = [UserFavoriteEntity::class], version = 1)
abstract class FavDatabase : RoomDatabase() {

    abstract fun userDao(): GithubDAO

    companion object {
        @Volatile
        private var INSTANCE: FavDatabase? = null
        fun getInstance(context: Context): FavDatabase {
            if (INSTANCE == null) {
                synchronized(FavDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavDatabase::class.java, "fav_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as FavDatabase
        }
    }

}