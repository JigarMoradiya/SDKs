package com.jigar.me.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `ExamHistory` (`id` INTEGER NOT NULL, `examTotalTime` INTEGER NOT NULL, `examType` TEXT NOT NULL, `examDetails` TEXT NOT NULL,`examBeginners` TEXT NOT NULL, `addedOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `Suduko` (`id` INTEGER NOT NULL, `cellPosition` TEXT NOT NULL, `cellValue` TEXT NOT NULL, `roomID` TEXT NOT NULL,`notes` TEXT NOT NULL, `addedOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE `Suduko_Play` (`id` INTEGER NOT NULL, `cellPosition` TEXT NOT NULL, `cellValue` TEXT NOT NULL, `roomID` TEXT NOT NULL,`notes` TEXT NOT NULL, `level` TEXT NOT NULL, `defaultSet` TEXT NOT NULL, `valueStatus` TEXT NOT NULL, `addedOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE `Suduko_AnswerStatus` (`id` INTEGER NOT NULL, `cellPosition` TEXT NOT NULL, `cellValue` TEXT NOT NULL, `roomID` TEXT NOT NULL,`otherCellPosition` TEXT NOT NULL, `addedOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE `Suduko_Level` (`id` INTEGER NOT NULL, `level` TEXT NOT NULL, `status` TEXT NOT NULL, `roomID` TEXT NOT NULL,`playTime` TEXT NOT NULL, `addedOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        }
    }

}