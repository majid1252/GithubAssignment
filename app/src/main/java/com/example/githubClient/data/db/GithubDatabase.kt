package com.example.githubClient.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubClient.data.model.GithubBaseUser

@Database(
    entities = [GithubBaseUser::class],
    version = 1
)
abstract class GithubDatabase: RoomDatabase() {

    abstract val githubUserDao : GithubUserDao

    companion object {

        @Volatile
        private var INSTANCE: GithubDatabase? = null

        fun getInstance(context: Context): GithubDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GithubDatabase::class.java, "github.db"
            )
                .build()
    }
}