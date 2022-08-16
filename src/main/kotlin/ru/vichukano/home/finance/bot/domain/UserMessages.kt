package ru.vichukano.home.finance.bot.domain

sealed interface UserMessages {
    fun text(): String
}

data class TextMessage(val text: String) : UserMessages {
    override fun text() = text
}

data class CommandMessage(val text: String) : UserMessages {
    override fun text() = text
}
