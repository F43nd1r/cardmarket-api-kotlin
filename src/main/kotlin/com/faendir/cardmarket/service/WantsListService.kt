package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*

/**
 * @author lukas
 * @since 09.10.19
 */
class WantsListService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getWantsLists(): List<WantsList> = get("wantslist").submit("wantslist") ?: emptyList()

    fun createWantsList(game: Game, name: String) = post("wantslist").body {
        "wantslist" {
            element("name", name)
            element("idGame", game.idGame)
        }
    }.submit() ?: Unit

    fun getWantsList(wantsList: WantsList) = getWantsList(wantsList.idWantslist)!!

    fun getWantsList(id: Int): WantsList? = get("wantslist/$id").submit("wantslist")

    fun changeName(wantsList: WantsList, name: String): WantsList? = put("wantslist/${wantsList.idWantslist}").body {
        element("action", "editWantslist")
        element("name", name)
    }.submit("wantslist")

    fun addItems(wantsList: WantsList, vararg items: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}").body {
        element("action", "addItem")
        items.forEach {
            when (it) {
                is MetaproductWant -> "metaproduct"{
                    element("idMetaproduct", it.idMetaproduct)
                    element("count", it.count)
                    element("minCondition", it.minCondition)
                    element("wishPrice", it.wishPrice)
                    element("mailAlert", it.mailAlert)
                    element("idLanguage", it.languages)
                    element("isFoil", it.isFoil)
                    element("isAltered", it.isAltered)
                    element("isSigned", it.isSigned)
                    element("isFirstEd", it.isFirstEd)
                }
                is ProductWant -> "product" {
                    element("idProduct", it.idProduct)
                    element("count", it.count)
                    element("minCondition", it.minCondition)
                    element("wishPrice", it.wishPrice)
                    element("mailAlert", it.mailAlert)
                    element("idLanguage", it.languages)
                    element("isFoil", it.isFoil)
                    element("isAltered", it.isAltered)
                    element("isSigned", it.isSigned)
                    element("isFirstEd", it.isFirstEd)
                }
                else -> throw IllegalArgumentException()
            }
        }
    }.submit("wantslist")

    fun editItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}").body {
        element("action", "editItem")
        "want" {
            element("idWant", wantsListItem.idWant)
            element("count", wantsListItem.count)
            element("minCondition", wantsListItem.minCondition)
            element("wishPrice", wantsListItem.wishPrice)
            element("mailAlert", wantsListItem.mailAlert)
            element("idLanguage", wantsListItem.languages)
            element("isFoil", wantsListItem.isFoil)
            element("isAltered", wantsListItem.isAltered)
            element("isSigned", wantsListItem.isSigned)
            element("isFirstEd", wantsListItem.isFirstEd)
        }
    }.submit("wantslist")

    fun deleteItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}")
            .body {
                element("action", "deleteItem")
                "want" {
                    element("idWant", wantsListItem.idWant)
                }
            }.submit("wantslist")

    fun deleteWantsList(wantsList: WantsList) = delete("wantslist/${wantsList.idWantslist}").submit() ?: Unit
}