package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*

/**
 * @author lukas
 * @since 09.10.19
 */
class WantsListService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getWantsLists(): List<WantsList> = get("wantslist", "wantslist") ?: emptyList()

    fun createWantsList(game: Game, name: String) = post("wantslist", "wantslist" to arrayOf("name" to name, "idGame" to game.idGame))

    fun getWantsList(wantsList: WantsList) = getWantsList(wantsList.idWantslist)!!

    fun getWantsList(id: Int): WantsList? = get("wantslist/$id", "wantslist")

    fun changeName(wantsList: WantsList, name: String): WantsList? = put("wantslist/${wantsList.idWantslist}", "wantslist", "action" to "editWantslist", "name" to name)

    fun addItems(wantsList: WantsList, vararg items: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}", "wantslist", "action" to "addItem",
            *(items.map {
                when (it) {
                    is MetaproductWant -> "metaproduct" to arrayOf(
                            "idMetaproduct" to it.idMetaproduct,
                            "count" to it.count,
                            "minCondition" to it.minCondition,
                            "wishPrice" to it.wishPrice,
                            "mailAlert" to it.mailAlert,
                            "idLanguage" to it.languages,
                            "isFoil" to it.isFoil,
                            "isAltered" to it.isAltered,
                            "isSigned" to it.isSigned,
                            "isFirstEd" to it.isFirstEd
                    )
                    is ProductWant -> "product" to arrayOf(
                            "idProduct" to it.idProduct,
                            "count" to it.count,
                            "minCondition" to it.minCondition,
                            "wishPrice" to it.wishPrice,
                            "mailAlert" to it.mailAlert,
                            "idLanguage" to it.languages,
                            "isFoil" to it.isFoil,
                            "isAltered" to it.isAltered,
                            "isSigned" to it.isSigned,
                            "isFirstEd" to it.isFirstEd
                    )
                    else -> throw IllegalArgumentException()
                }
            }.toTypedArray()))

    fun editItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}", "wantslist", "action" to "editItem",
            "want" to arrayOf(
                    "idWant" to wantsListItem.idWant,
                    "count" to wantsListItem.count,
                    "minCondition" to wantsListItem.minCondition,
                    "wishPrice" to wantsListItem.wishPrice,
                    "mailAlert" to wantsListItem.mailAlert,
                    "idLanguage" to wantsListItem.languages,
                    "isFoil" to wantsListItem.isFoil,
                    "isAltered" to wantsListItem.isAltered,
                    "isSigned" to wantsListItem.isSigned,
                    "isFirstEd" to wantsListItem.isFirstEd
            ))

    fun deleteItem(wantsList: WantsList, wantsListItem: WantsListItem): WantsList? = put("wantslist/${wantsList.idWantslist}", "wantslist", "action" to "deleteItem",
            "want" to ("idWant" to wantsListItem.idWant))

    fun deleteWantsList(wantsList: WantsList)= delete("wantslist/${wantsList.idWantslist}")

}