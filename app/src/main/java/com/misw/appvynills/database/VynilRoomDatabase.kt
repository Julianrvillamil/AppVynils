package com.misw.appvynills.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misw.appvynills.database.dao.AlbumDao
import com.misw.appvynills.database.entity.AlbumEntity
import com.misw.appvynills.database.entity.AlbumPerformerCrossRef
import com.misw.appvynills.database.entity.CommentEntity
import com.misw.appvynills.database.entity.PerformerEntity
import com.misw.appvynills.database.entity.TrackEntity

@Database(
    entities = [
        AlbumEntity::class,
        TrackEntity::class,
        PerformerEntity::class,
        CommentEntity::class,
        AlbumPerformerCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VynilRoomDatabase: RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: VynilRoomDatabase? = null

        fun getDatabase(context: Context): VynilRoomDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VynilRoomDatabase::class.java,
                    "vinyls_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}