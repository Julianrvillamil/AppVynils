package com.misw.appvynills.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misw.appvynills.database.dao.AlbumDao
import com.misw.appvynills.database.dao.ArtistDao
import com.misw.appvynills.database.dao.CollectorDao
import com.misw.appvynills.database.entity.AlbumEntity
import com.misw.appvynills.database.entity.AlbumPerformerCrossRef
import com.misw.appvynills.database.entity.ArtistEntity
import com.misw.appvynills.database.entity.CollectorAlbumEntity
import com.misw.appvynills.database.entity.CollectorCommentEntity
import com.misw.appvynills.database.entity.CollectorEntity
import com.misw.appvynills.database.entity.CollectorPerformerEntity
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
        CollectorEntity::class,
        CollectorAlbumEntity::class,
        CollectorPerformerEntity::class,
        CollectorCommentEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class VinylRoomDatabase: RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun collectorDao(): CollectorDao

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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS collectors (
                    id INTEGER PRIMARY KEY NOT NULL,
                    name TEXT NOT NULL,
                    telephone TEXT NOT NULL,
                    email TEXT NOT NULL
                )
            """
                )
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS collector_comments (
                    id INTEGER PRIMARY KEY NOT NULL,
                    description TEXT NOT NULL,
                    rating INTEGER NOT NULL,
                    collectorId INTEGER NOT NULL,
                    FOREIGN KEY(collectorId) REFERENCES collectors(id) ON DELETE CASCADE
                )
            """
                )
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS collector_performers (
                    id INTEGER PRIMARY KEY NOT NULL,
                    name TEXT NOT NULL,
                    image TEXT NOT NULL,
                    description TEXT NOT NULL,
                    birthDate TEXT,
                    creationDate TEXT,
                    collectorId INTEGER NOT NULL,
                    FOREIGN KEY(collectorId) REFERENCES collectors(id) ON DELETE CASCADE
                )
            """
                )
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS collector_albums (
                    id INTEGER PRIMARY KEY NOT NULL,
                    price INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    collectorId INTEGER NOT NULL,
                    FOREIGN KEY(collectorId) REFERENCES collectors(id) ON DELETE CASCADE
                )
            """
                )
                // Agrega explícitamente el índice en collectorId
                db.execSQL(
                    """
                CREATE INDEX index_collector_albums_collectorId ON collector_albums(collectorId)
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}