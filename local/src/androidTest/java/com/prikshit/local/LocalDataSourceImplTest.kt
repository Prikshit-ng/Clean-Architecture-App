package com.prikshit.local

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.prikshit.data.repository.LocalDataSource
import com.prikshit.local.database.DeliveryDB
import com.prikshit.local.database.DeliveryDao
import com.prikshit.local.mapper.DeliveryDataMapper
import com.prikshit.local.source.LocalDataSourceImpl
import com.prikshit.local.utils.TestDataGenerator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocalDataSourceImplTest {

    private val deliveryDataMapper = DeliveryDataMapper()

    private lateinit var deliveryDao: DeliveryDao

    private lateinit var deliveryDB: DeliveryDB
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        deliveryDB = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            DeliveryDB::class.java
        ).build()
        deliveryDao = deliveryDB.getDeliveryDao()
        val mockDeliveries = TestDataGenerator.generateDeliveryList()
        deliveryDao.saveDeliveries(mockDeliveries)
        localDataSource = LocalDataSourceImpl(
            deliveryDB,
            deliveryDataMapper,
            deliveryDao
        )
    }

    @After
    fun closeDb() {
        deliveryDB.close()
    }

    @Test
    fun saveDeliveries() {
        val mockDeliveries = TestDataGenerator.generateDeliveryList()
        deliveryDao.getDeliveryCount().test()
            .assertSubscribed()
            .assertValue {
                mockDeliveries.size == it
            }
    }

    @Test
    fun updateDelivery() {
        val deliveryLocal = TestDataGenerator.generateDeliveryList()[0]
        deliveryLocal.isFav = true
        deliveryDao.updateDelivery(deliveryLocal).test().assertComplete()
    }

    @Test
    fun clearDB() {
        deliveryDB.clearAllTables()
        deliveryDao.getDeliveryCount().test()
            .assertSubscribed()
            .assertValue {
                0 == it
            }
    }
}