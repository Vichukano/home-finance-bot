package ru.vichukano.home.finance.bot.domain

enum class DebitCreditCategory(val value: String, val rusValue: String) {
    DEBIT("/debit", "Доход"), CREDIT("/credit", "Расход")
}
