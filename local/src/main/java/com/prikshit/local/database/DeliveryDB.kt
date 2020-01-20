package com.prikshit.local.database

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prikshit.local.model.DeliveryLocal

@Database(
    entities = [DeliveryLocal::class],
    version = 1,
    exportSchema = false
)
abstract class DeliveryDB : RoomDatabase() {
    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "delivery_ng.db"
        @Volatile
        private var INSTANCE: DeliveryDB? = null

        fun getInstance(@NonNull context: Context): DeliveryDB {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            DeliveryDB::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getDeliveryDao(): DeliveryDao
}