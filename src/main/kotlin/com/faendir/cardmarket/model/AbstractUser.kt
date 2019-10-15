package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonCreator

/**
 * @author lukas
 * @since 30.09.19
 */
abstract class AbstractUser {
    abstract val idUser: Int
    abstract val username: String
    abstract val isCommercial: UserType
    abstract val riskGroup: RiskGroup
    abstract val reputation: Reputation
    abstract val sellCount: Int
    abstract val soldItems: Int
    abstract val avgShippingTime: Int
    abstract val onVacation: Boolean
    abstract val name: Name
    abstract val email: String
    abstract val phone: String?
    abstract val vat: String?
}

enum class RiskGroup(val value: Int) {
    NONE(0),
    LOW(1),
    HIGH(2);

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(s: String): RiskGroup? {
            val value = s.toInt()
            return values().find { it.value == value }
        }
    }
}

enum class Reputation(val value: Int) {
    NONE(0),
    OUTSTANDING(1),
    VERY_GOOD(2),
    GOOD(3),
    AVERAGE(4),
    BAD(5);

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(s: String): Reputation? {
            val value = s.toInt()
            return values().find { it.value == value }
        }
    }
}

data class Name(val company: String?, val firstName: String?, val lastName: String?)