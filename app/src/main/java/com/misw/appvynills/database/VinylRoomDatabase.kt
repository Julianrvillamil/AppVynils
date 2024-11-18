package com.misw.appvynills.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misw.appvynills.database.dao.AlbumDao
import com.misw.appvynills.database.dao.ArtistDao
import com.misw.appvynills.database.entity.AlbumEntity
import com.misw.appvynills.database.entity.AlbumPerformerCrossRef
import com.misw.appvynills.database.entity.ArtistEntity
import com.misw.appvynills.database.entity.CommentEntity
import com.misw.appvynills.database.entity.PerformerEntity
import com.misw.appvynills.database.entity.TrackEntity

@Database(
    entities = [
        AlbumEntity::class,
        TrackEntity::class,
        PerformerEntity::class,
        CommentEntity::class,
        AlbumPerformerCrossRef::class,
        ArtistEntity::class,

    ],
    version = 2,
    exportSchema = false
)
abstract class VinylRoomDatabase: RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao

    companion object {
        @Volatile
        private var INSTANCE: VinylRoomDatabase? = null

        // Define las migraciones aquí
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Realiza cambios necesarios en la base de datos
                // Ejemplo: Alteraciones o creación de nuevas tablas
                //database.execSQL("ALTER TABLE albums ADD COLUMN some_new_column TEXT DEFAULT '' NOT NULL")
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS artists (
                            id INTEGER PRIMARY KEY NOT NULL,
                            name TEXT NOT NULL,
                            description TEXT NOT NULL,
                            image TEXT NOT NULL,
                            birthDate TEXT
                        )
                        """
                )
            }
        }

        fun getDatabase(context: Context): VinylRoomDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinylRoomDatabase::class.java,
                    "vinyls_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}