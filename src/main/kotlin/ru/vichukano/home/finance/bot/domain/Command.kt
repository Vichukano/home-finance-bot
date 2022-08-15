package ru.vichukano.home.finance.bot.domain

enum class Command(val value: String) {
    HELP("/help"), CONFIRM("/save"), REPORT("/report")
}