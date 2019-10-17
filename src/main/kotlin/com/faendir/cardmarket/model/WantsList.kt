package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author lukas
 * @since 09.10.19
 */
data class WantsList(val idWantslist: Int,
                     val game: Game,
                     val name: String,
                     val itemCount: Int,
                     @JsonProperty("item")
                     val items: List<WantsListItem> = emptyList()) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idWantslist == (other as WantsList).idWantslist)

    override fun hashCode(): Int = idWantslist
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(JsonSubTypes.Type(value = ProductWant::class, name = "product"),
        JsonSubTypes.Type(value = MetaproductWant::class, name = "metaproduct"))
abstract class WantsListItem {
    abstract val idWant: String
    abstract val count: Int
    abstract val wishPrice: Double
    abstract val fromPrice: Double
    abstract val mailAlert: Boolean
    abstract val type: String
    @get:JsonProperty("idLanguage")
    abstract val languages: List<Int>
    abstract val minCondition: Condition
    abstract val isFoil: Boolean?
    abstract val isSigned: Boolean?
    abstract val isAltered: Boolean?
    abstract val isFirstEd: Boolean?
}

data class ProductWant(override val idWant: String,
                       override val count: Int,
                       override val wishPrice: Double,
                       override val fromPrice: Double,
                       override val mailAlert: Boolean,
                       override val idProduct: Int,
                       val product: Product,
                       override val languages: List<Int>,
                       override val minCondition: Condition,
                       override val isFoil: Boolean?,
                       override val isSigned: Boolean?,
                       override val isAltered: Boolean?,
                       override val isFirstEd: Boolean?) : WantsListItem(), HasProductId {
    override val type = "product"
}

fun Product.toWant(): ProductWant = ProductWant("", 1, 0.0, 0.0, false, this.idProduct, this, emptyList(), Condition.POOR, null, null, null, null)

data class MetaproductWant(override val idWant: String,
                           override val count: Int,
                           override val wishPrice: Double,
                           override val fromPrice: Double,
                           override val mailAlert: Boolean,
                           override val idMetaproduct: Int,
                           val metaproduct: Metaproduct,
                           override val languages: List<Int>,
                           override val minCondition: Condition,
                           override val isFoil: Boolean?,
                           override val isSigned: Boolean?,
                           override val isAltered: Boolean?,
                           override val isFirstEd: Boolean?) : WantsListItem(), HasMetaproductId {
    override val type = "metaproduct"
}

fun Metaproduct.toWant() : MetaproductWant = MetaproductWant("", 1, 0.0, 0.0, false, this.idMetaproduct, this, emptyList(), Condition.POOR, null, null, null, null)