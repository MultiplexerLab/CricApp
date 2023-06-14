package com.multiplexer.cricapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        com.multiplexer.cricapp.models.countries.Data::class,
        com.multiplexer.cricapp.models.continents.Data::class,
        com.multiplexer.cricapp.models.teams.Data::class,
        com.multiplexer.cricapp.models.leagues.Data::class,
    ], version = 9
)
abstract class DataBase : RoomDatabase() {
    abstract fun cricketDao(): CricketDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getDatabase(context: Context): DataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, DataBase::class.java, "cricket_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}