package ru.vichukano.home.finance.bot.domain

data class UserInfo(
    val id: String,
    val state: UserState,
    val financeInfo: MutableList<FinanceInfo> = mutableListOf()
)