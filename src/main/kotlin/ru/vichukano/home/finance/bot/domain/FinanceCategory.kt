package ru.vichukano.home.finance.bot.domain

import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.CREDIT
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.DEBIT

enum class FinanceCategory(val value: String, val category: DebitCreditCategory) {
    SALARY_VI("Зарплата Витя", DEBIT),
    SALARY_JU("Зарплата Юля", DEBIT),
    PREPAID_VI("Аванс Витя", DEBIT),
    PREPAID_JU("Аванс Юля", DEBIT),
    CASHBACK("Кэшбэк", DEBIT),
    GIFTS_FOR_US("Подарки", DEBIT),
    OTHER_DEBIT("Другой доход", DEBIT),
    FOOD("Продукты", CREDIT),
    AUTOMOBILE("Автомобиль", CREDIT),
    HEALTH("Здоровье", CREDIT),
    HOME_COMMON("ЖКХ, тел, интернет", CREDIT),
    MORTGAGE("Ипотека", CREDIT),
    BABY("Сад", CREDIT),
    TRAINING("Секции", CREDIT),
    BEAUTY("Красота", CREDIT),
    GIFTS("Подарки", CREDIT),
    FUN("Развлечения", CREDIT),
    INVESTMENTS("Инвестиции", CREDIT),
    TRANSPORT("Общ транспорт", CREDIT),
    HOME("Дом", CREDIT),
    INSURANCE("Страховка", CREDIT),
    OTHER_CREDIT("Другой расход", CREDIT);

    companion object {
        fun categoryByValue(value: String): FinanceCategory = FinanceCategory.values().first { it.value == value }
    }
}