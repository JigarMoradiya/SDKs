package com.jigar.me.data.repositories

import com.jigar.me.data.api.SafeApiCall
import com.jigar.me.data.local.db.exam.ExamHistoryDB
import com.jigar.me.data.local.db.inapp.purchase.InAppPurchaseDB
import com.jigar.me.data.local.db.inapp.sku.InAppSKUDB
import com.jigar.me.data.local.db.sudoku.SudokuDB
import com.jigar.me.data.model.dbtable.exam.ExamHistory
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.utils.sudoku.SudukoConst
import io.reactivex.Single
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val inAppPurchaseDB: InAppPurchaseDB,
    private val inAppSKUDB: InAppSKUDB,
    private val examHistoryDB: ExamHistoryDB
) : SafeApiCall {

    fun getInAppPurchase() = inAppPurchaseDB.getInAppPurchase()
    fun getInAppSKUDetailLive(sku : String) = inAppSKUDB.getInAppSKUDetailLive(sku)
    fun getInAppSKU() = inAppSKUDB.getInAppSKU()
    fun getInAppSKUDetail(sku : String) = inAppSKUDB.getInAppSKUDetail(sku)

    suspend fun saveExamResultDB(data: ExamHistory) = examHistoryDB.insert(data)
    fun getExamHistoryList(examType: String) = examHistoryDB.getExamHistoryList(examType)
}