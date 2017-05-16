package com.rockspin.bargainbits.data.repository.storage

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by valentin.hinov on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class BBObjectStoreTest {

    @Mock
    lateinit var mockPrimitiveStore: PrimitiveStore

    private lateinit var objectStore: BBObjectStore

    companion object {
        data class TestClass(val text: String, val number: Int)

        val TEST_LIST = listOf(TestClass("one", 1), TestClass("two", 2))
        val TEST_LIST_JSON = Gson().toJson(TEST_LIST)!!
    }

    @Before
    fun setUp() {
        objectStore = BBObjectStore(Gson(), mockPrimitiveStore)
    }

    @Test
    fun testStoreList_storesValueAsJson() {
        objectStore.storeList("KEY", TEST_LIST)
        verify(mockPrimitiveStore).storeString("KEY", TEST_LIST_JSON)
    }

    @Test
    fun testGetStoredList_returnsEmptyListForMissingValue() {
        assertThat(objectStore.getStoredList("KEY", TestClass::class.java)).isEmpty()
    }

    @Test
    fun testGetStoredList_returnsItemDeserializedFromJson() {
        `when`(mockPrimitiveStore.getStoredString("KEY")).thenReturn(TEST_LIST_JSON)

        assertThat(objectStore.getStoredList("KEY", TestClass::class.java)).isEqualTo(TEST_LIST)
    }
}