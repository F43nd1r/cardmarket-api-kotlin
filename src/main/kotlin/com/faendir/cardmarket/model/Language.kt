package com.faendir.cardmarket.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = LanguageDeserializer::class)
enum class Language(val idLanguage: Int, val languageName: String) {
    ENGLISH(1, "English"),
    FRENCH(2, "French"),
    GERMAN(3, "German"),
    SPANISH(4, "Spanish"),
    ITALIAN(5, "Italian"),
    S_CHINESE(6, "S-Chinese"),
    JAPANESE(7, "Japanese"),
    PORTUGUESE(8, "Portuguese"),
    RUSSIAN(9, "Russian"),
    KOREAN(10, "Korean"),
    T_CHINESE(11, "T-Chinese")
}

internal class LanguageDeserializer : StdDeserializer<Language>(Language::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Language {
        val node: JsonNode = p.codec.readTree(p)
        val id = node.get("idLanguage").asInt()
        val name = (node.get("languageName")).asText()
        val language = Language.values().find { it.idLanguage == id }
        require(language != null) { "Unknown language id $id" }
        require(language.languageName == name) { "Expected ${language.languageName} for $id but was $name" }
        return language
    }
}