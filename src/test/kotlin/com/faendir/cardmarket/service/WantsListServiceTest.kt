package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * @author lukas
 * @since 15.10.19
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class WantsListServiceTest {
    private val config = CardmarketApiConfiguration.get(emptyArray())!!
    private val service = WantsListService(config)
    private val marketplaceService = MarketplaceService(config)

    @Test
    fun wantsListsItems() {
        val name = "TestWantsList"
        service.createWantsList(Game.MTG, name)
        val wantsLists = service.getWantsLists()
        assertThat(wantsLists, anyElement(has(WantsList::name, equalTo(name))))
        val wantsList = wantsLists.find { it.name == name }!!
        assertThat(wantsList.items, isEmpty)
        val product = marketplaceService.findProduct("Nexus of Fate")[0]
        val metaproduct = marketplaceService.findMetaproducts("Crucible of Worlds")[0]
        service.addItems(wantsList, product.toWant(), metaproduct.toWant())
        assertThat(service.getWantsLists(), anyElement(has(WantsList::name, equalTo(name)) and has(WantsList::items, isEmpty)))
        val items = service.getWantsList(wantsList).items
        assertThat(items, hasSize(equalTo(2)))
        assertThat(items, anyElement(isA(has(ProductWant::idProduct, equalTo(product.idProduct)))))
        assertThat(items, anyElement(isA(has(MetaproductWant::idMetaproduct, equalTo(metaproduct.idMetaproduct)))))
        service.editItem(wantsList, items.filterIsInstance<ProductWant>()[0].copy(count = 2))
        service.editItem(wantsList, items.filterIsInstance<MetaproductWant>()[0].copy(isFoil = true))
        assertThat(service.getWantsList(wantsList).items, anyElement(isA(has(ProductWant::count, equalTo(2)))))
        assertThat(service.getWantsList(wantsList).items, anyElement(isA(has(MetaproductWant::isFoil, equalTo(true)))))
        service.deleteItem(wantsList, service.getWantsList(wantsList).items.filterIsInstance<ProductWant>()[0])
        assertThat(service.getWantsList(wantsList).items, !anyElement(isA(has(ProductWant::idProduct, equalTo(product.idProduct)))))
        service.deleteWantsList(wantsList)
        assertThat(service.getWantsLists(), !anyElement(has(WantsList::name, equalTo(name))))
    }

    @Test
    fun wantsListsName() {
        val name = "TestWantsList 2"
        service.createWantsList(Game.MTG, name)
        var wantsLists = service.getWantsLists()
        assertThat(wantsLists, anyElement(has(WantsList::name, equalTo(name))))
        val wantsList = wantsLists.find { it.name == name }!!
        val newName = "TestWantsList 3"
        service.changeName(wantsList, newName)
        wantsLists = service.getWantsLists()
        assertThat(wantsLists, anyElement(has(WantsList::name, equalTo(newName))))
        assertThat(wantsLists, !anyElement(has(WantsList::name, equalTo(name))))
        service.deleteWantsList(wantsList)
        wantsLists = service.getWantsLists()
        assertThat(wantsLists, !anyElement(has(WantsList::name, equalTo(newName))))
        assertThat(wantsLists, !anyElement(has(WantsList::name, equalTo(name))))
    }
}