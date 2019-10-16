package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * @author lukas
 * @since 14.10.19
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AccountServiceTest {
    private val service = AccountService(CardmarketApiConfiguration.get())

    @Test
    fun getAccount() {
        val account = service.getAccount()!!
        assertEquals(65310125, account.idUser)
        assertEquals("KotlinTest", account.username)
        assertEquals("D", account.country)
        assertEquals(UserType.PRIVATE, account.isCommercial)
        assertTrue(account.maySell)
        assertEquals(SellerActivation.ACTIVE, account.sellerActivation)
        assertEquals(RiskGroup.LOW, account.riskGroup)
        assertEquals(Reputation.NONE, account.reputation)
        assertEquals(ShippingSpeed.UNKNOWN, account.shipsFast)
        assertEquals(0, account.sellCount)
        assertEquals(0, account.soldItems)
        assertEquals(0, account.avgShippingTime)
        assertFalse(account.onVacation)
        assertEquals(DisplayLanguage.ENGLISH, account.displayLanguage)
        assertEquals(Name(null, "Max", "Mustermann"), account.name)
        assertEquals(Address("Max Mustermann", null, "Müllerweg 1", "10115", "Berlin", "D"), account.homeAddress)
        assertEquals("lukas.morawietz+cardmarket-sandbox@gmail.com", account.email)
        assertNull(account.phone)
        assertNull(account.vat)
        assertEquals(OffsetDateTime.of(2019, 10, 14, 15, 23, 54, 0, ZoneOffset.UTC), account.registerDate)
        assertTrue(account.isActivated)
    }

    @Test
    fun setOnVacation() {
        service.setOnVacation(true)
        assertTrue(service.getAccount()!!.onVacation)
        service.setOnVacation(false)
        assertFalse(service.getAccount()!!.onVacation)
    }

    @Test
    fun setDisplayLanguage() {
        service.setDisplayLanguage(DisplayLanguage.GERMAN)
        assertEquals(service.getAccount()!!.displayLanguage, DisplayLanguage.GERMAN)
        service.setDisplayLanguage(DisplayLanguage.ENGLISH)
        assertEquals(service.getAccount()!!.displayLanguage, DisplayLanguage.ENGLISH)
    }

    @Test
    fun messageThreads() {
        service.getMessageThreads().forEach { service.deleteMessageThread(it.partner.idUser) }
        assertTrue(service.getMessageThreads().isEmpty())
        val partner = 662949
        service.sendMessage(partner, "Test Message")
        assertEquals(1, service.getMessageThreads().size)
        val messages = service.getMessageThread(partner)!!.messages
        assertEquals(1, messages.size)
        assertEquals("Test Message", service.getMessage(partner, messages[0].idMessage)!!.messages[0].text)
        service.sendMessage(partner, "Test Message 2")
        assertEquals(2, service.getMessageThread(partner)!!.messages.size)
        service.deleteMessage(partner, messages[0].idMessage)
        assertEquals(1, service.getMessageThread(partner)!!.messages.size)
        service.deleteMessageThread(partner)
        assertTrue(service.getMessageThreads().isEmpty())
    }

    @Test
    fun findMessages() {
        assertTrue(service.findMessages(unread = true).isEmpty())
    }
}