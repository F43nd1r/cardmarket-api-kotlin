package com.faendir.cardmarket.model

enum class UserType(val value: String) {
    PRIVATE("private"),
    COMMERCIAL("commercial"),
    POWERSELLER("powerseller");

    override fun toString(): String {
        return value
    }

}