package ru.vichukano.home.finance.bot.domain

data class FinanceInfo(
    val category: String,
    var amount: Double = 0.0
)
