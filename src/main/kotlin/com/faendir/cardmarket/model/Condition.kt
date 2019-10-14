package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author lukas
 * @since 01.10.19
 */
enum class Condition(@JsonValue val value: String) {
    MINT("MT"),
    NEAR_MINT("NM"),
    EXCELLENT("EX"),
    GOOD("GD"),
    LIGHT_PLAYED("LP"),
    PLAYED("PL"),
    POOR("PO");

    override fun toString(): String {
        return value
    }
}