package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import com.faendir.cardmarket.util.leafs
import com.faendir.cardmarket.util.tree

/**
 * @author lukas
 * @since 09.10.19
 */
class WantsListService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getWantsLists(): List<WantsList> = get("wantslist").submit("wantslist") ?: emptyList()

    fun createWantsList(game: Game, name: String) = post("wantslist").body(tree("wantslist") {
        node(leafs("name", name))
        node(leafs("idGame", game.idGame.toString()))
    }).submit() ?: Unit

    fun getWantsList(wantsList: WantsList) = getWantsList(wantsList.idWantslist)!!

    fun getWantsList(id: Int): WantsList? = get("wantslist/$id").submit("wantslist")

    fun changeName(wantsList: WantsList, name: String): WantsList? = put("wantslist/${wantsList.idWantslist}")
            .body(leafs("action", "editWantslist"), leafs("name", name)).submit("wantslist")

    fun addItems(wantsList: WantsList, vararg items: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}").body(leafs("action", "addItem"),
            *(items.map {
                when (it) {
                    is MetaproductWant -> tree("metaproduct") {
                        node(leafs("idMetaproduct", it.idMetaproduct.toString()))
                        node(leafs("count", it.count.toString()))
                        node(leafs("minCondition", it.minCondition.toString()))
                        node(leafs("wishPrice", it.wishPrice.toString()))
                        node(leafs("mailAlert", it.mailAlert.toString()))
                        node(leafs("idLanguage", it.languages.toString()))
                        node(leafs("isFoil", it.isFoil.toString()))
                        node(leafs("isAltered", it.isAltered.toString()))
                        node(leafs("isSigned", it.isSigned.toString()))
                        node(leafs("isFirstEd", it.isFirstEd.toString()))
                    }
                    is ProductWant -> tree("product") {
                        node(leafs("idProduct", it.idProduct.toString()))
                        node(leafs("count", it.count.toString()))
                        node(leafs("minCondition", it.minCondition.toString()))
                        node(leafs("wishPrice", it.wishPrice.toString()))
                        node(leafs("mailAlert", it.mailAlert.toString()))
                        node(leafs("idLanguage", it.languages.toString()))
                        node(leafs("isFoil", it.isFoil.toString()))
                        node(leafs("isAltered", it.isAltered.toString()))
                        node(leafs("isSigned", it.isSigned.toString()))
                        node(leafs("isFirstEd", it.isFirstEd.toString()))
                    }
                    else -> throw IllegalArgumentException()
                }
            }.toTypedArray())).submit("wantslist")

    fun editItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}").body(leafs("action", "editItem"),
            tree("want") {
                node(leafs("idWant", wantsListItem.idWant.toString()))
                node(leafs("count", wantsListItem.count.toString()))
                node(leafs("minCondition", wantsListItem.minCondition.toString()))
                node(leafs("wishPrice", wantsListItem.wishPrice.toString()))
                node(leafs("mailAlert", wantsListItem.mailAlert.toString()))
                node(leafs("idLanguage", wantsListItem.languages.toString()))
                node(leafs("isFoil", wantsListItem.isFoil.toString()))
                node(leafs("isAltered", wantsListItem.isAltered.toString()))
                node(leafs("isSigned", wantsListItem.isSigned.toString()))
                node(leafs("isFirstEd", wantsListItem.isFirstEd.toString()))
            }).submit("wantslist")

    fun deleteItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}")
            .body(leafs("action", "deleteItem"), tree("want") { node(leafs("idWant", wantsListItem.idWant)) }).submit("wantslist")

    fun deleteWantsList(wantsList: WantsList) = delete("wantslist/${wantsList.idWantslist}").submit() ?: Unit

}