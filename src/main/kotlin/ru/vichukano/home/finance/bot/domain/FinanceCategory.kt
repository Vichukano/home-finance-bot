package ru.vichukano.home.finance.bot.domain

import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.CREDIT
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.DEBIT

enum class FinanceCategory(val value: String, val category: DebitCreditCategory) {
    SALARY("Зарплата", DEBIT),
    PREPAID("Аванс", DEBIT),
    FOOD("Еда", CREDIT),
    AUTOMOBILE("Автомобиль", CREDIT),
    HEALTH("Здоровье", CREDIT),
    OTHER_DEBIT("Другой доход", DEBIT),
    OTHER_CREDIT("Другой расход", CREDIT)
}