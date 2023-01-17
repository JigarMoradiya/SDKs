package com.jigar.me.data.local.db.inapp.sku

import androidx.lifecycle.LiveData
import com.android.billingclient.api.ProductDetails
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InAppSKUDB @Inject constructor(private val dao: InAppSKUDao) {
    suspend fun saveInAppSKU(data: MutableList<ProductDetails>) = withContext(Dispatchers.IO) {
        dao.insertOrUpdate(data)
    }
    fun getInAppSKU(): LiveData<List<InAppSkuDetails>> {
        return dao.getInAppSku()
    }
    fun getInAppSKUDetailLive(sku : String): LiveData<List<InAppSkuDetails>> {
        return dao.getInAppSkuDetailLive(sku)
    }
    suspend fun getInAppSKUDetail(sku : String): List<InAppSkuDetails> {
        return dao.getInAppSkuDetail(sku)
    }
}
