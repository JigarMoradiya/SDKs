package com.jigar.me.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import com.jigar.kotlin.data.local.db.Migrations.MIGRATION_1_2
import com.jigar.kotlin.data.local.db.Migrations.MIGRATION_2_3
import com.jigar.me.BuildConfig
import com.jigar.me.data.api.AppApi
import com.jigar.me.data.api.RemoteDataSource
import com.jigar.me.data.local.db.AppDatabase
import com.jigar.me.data.local.db.exam.ExamHistoryDB
import com.jigar.me.data.local.db.exam.ExamHistoryDao
import com.jigar.me.data.local.db.inapp.purchase.InAppPurchaseDB
import com.jigar.me.data.local.db.inapp.purchase.InAppPurchaseDao
import com.jigar.me.data.local.db.inapp.sku.InAppSKUDB
import com.jigar.me.data.local.db.inapp.sku.InAppSKUDao
import com.jigar.me.data.local.db.sudoku.SudukoAnswerStatusDao
import com.jigar.me.data.local.db.sudoku.SudukoDao
import com.jigar.me.data.local.db.sudoku.SudukoLevelDao
import com.jigar.me.data.local.db.sudoku.SudokuDB
import com.jigar.me.data.local.db.sudoku.SudukoPlayDao
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.data.pref.PreferenceInfo
import com.jigar.me.data.pref.PreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    // Preferences
    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String = Constants.PREF_NAME

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper = appPreferencesHelper

    /*
   Local Room Database
   */
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppConstants.DB_NAME)
            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
            .build()

    @Provides
    fun providesInAppSKUDao(db: AppDatabase): InAppSKUDao = db.inAppSKUDao()
    @Provides
    fun providesInAppSKUDB(dao: InAppSKUDao): InAppSKUDB = InAppSKUDB(dao)

    @Provides
    fun providesInAppPurchaseDao(db: AppDatabase): InAppPurchaseDao = db.inAppPurchaseDao()
    @Provides
    fun providesInAppPurchaseDB(dao: InAppPurchaseDao): InAppPurchaseDB = InAppPurchaseDB(dao)

    @Provides
    fun providesExamHistoryDao(db: AppDatabase): ExamHistoryDao = db.examHistoryDao()
    @Provides
    fun providesExamHistoryDB(dao: ExamHistoryDao): ExamHistoryDB = ExamHistoryDB(dao)

    @Provides
    fun providesSudukoDao(db: AppDatabase): SudukoDao = db.sudokuDao()
    @Provides
    fun providesSudukoLevelDao(db: AppDatabase): SudukoLevelDao = db.sudokuLevelDao()
    @Provides
    fun providesSudukoAnswerStatusDao(db: AppDatabase): SudukoAnswerStatusDao = db.sudokuAnswerStatusDao()
    @Provides
    fun providesSudukoPlayDao(db: AppDatabase): SudukoPlayDao = db.sudokuPlayDao()

    @Singleton
    @Provides
    fun providesSudokuDB(sudokuDao: SudukoDao,
                              sudokuLevelDao: SudukoLevelDao,
                              sudokuPlayDao: SudukoPlayDao,
                              sudokuAnswerStatusDao: SudukoAnswerStatusDao): SudokuDB = SudokuDB(sudokuDao,sudokuLevelDao,sudokuPlayDao,sudokuAnswerStatusDao)

    @Singleton
    @Provides
    fun provideAppApi(@ApplicationContext context: Context,remoteDataSource: RemoteDataSource): AppApi {
        return remoteDataSource.buildApi(AppApi::class.java, context, BuildConfig.FAN_MODULE)
    }


//    @Binds
//    @IntoMap
//    @Singleton
//    @Provides
//    @WorkerKey(Sudoku4WorkManager::class)
//    internal fun bindCreateSudoku4WorkerManager(factory: Sudoku4WorkManager.Factory): ChildWorkerFactory = factory
}


@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)



