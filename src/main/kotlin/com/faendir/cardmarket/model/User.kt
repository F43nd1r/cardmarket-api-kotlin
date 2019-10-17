package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 30.09.19
 */
data class User(override val idUser: Int,
                override val username: String,
                val registrationDate: String,
                override val isCommercial: UserType,
                val isSeller : Boolean,
                override val name: Name,
                val address: Address,
                override val phone: String?,
                override val email: String,
                override val vat: String?,
                override val riskGroup: RiskGroup,
                override val reputation: Reputation,
                val shipsFast: Int,
                override val sellCount: Int,
                override val soldItems: Int,
                override val avgShippingTime: Int,
                override val onVacation: Boolean) : BaseUser {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idUser == (other as User).idUser)

    override fun hashCode(): Int = idUser
}