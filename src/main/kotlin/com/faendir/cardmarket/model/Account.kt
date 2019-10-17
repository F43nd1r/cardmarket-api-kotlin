package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

/**
 * @author lukas
 * @since 30.09.19
 */
data class Account(override val idUser: Int,
                   override val username: String,
                   val country: String,
                   override val isCommercial: UserType,
                   val maySell: Boolean,
                   val sellerActivation: SellerActivation,
                   override val riskGroup: RiskGroup,
                   override val reputation: Reputation,
                   val shipsFast: ShippingSpeed,
                   override val sellCount: Int,
                   override val soldItems: Int,
                   override val avgShippingTime: Int,
                   override val onVacation: Boolean,
                   @JsonProperty("idDisplayLanguage")
                   val displayLanguage: DisplayLanguage,
                   override val name: Name,
                   val homeAddress: Address,
                   override val email: String,
                   @JsonProperty("phoneNumber")
                   override val phone: String?,
                   override val vat: String?,
                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
                   val registerDate: OffsetDateTime,
                   val isActivated: Boolean,
                   val moneyDetails: MoneyDetails,
                   val bankAccount: BankAccount,
                   val articlesInShoppingCart: Int,
                   val unreadMessages: Int) : BaseUser {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idUser == (other as Account).idUser)

    override fun hashCode(): Int = idUser
}

enum class SellerActivation(val value: Int) {
    NONE(0),
    PENDING(1),
    PROCESSED(2),
    ACTIVE(3);

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(s: String): SellerActivation {
            val value = s.toInt()
            return values().find { it.value == value }!!
        }
    }
}

enum class DisplayLanguage(val value: Int) {
    ENGLISH(1),
    FRENCH(2),
    GERMAN(3),
    SPANISH(4),
    ITALIAN(5);

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(s: String): DisplayLanguage? {
            val value = s.toInt()
            return values().find { it.value == value }
        }
    }
}

enum class ShippingSpeed(val value: Int) {
    UNKNOWN(-1),
    NORMAL(0),
    VERY_FAST(1),
    FAST(2);

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(s: String): ShippingSpeed? {
            val value = s.toInt()
            return values().find { it.value == value }
        }
    }
}

data class MoneyDetails(val totalBalance: Double, val moneyBalance: Double, val bonusBalance: Double, val unpaidAmount: Double, val providerRechargeAmount: Double)

data class BankAccount(val accountOwner: String, val iban: String, val bic: String, val bankName: String)