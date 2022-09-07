package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.DebitCreditCategory
import ru.vichukano.home.finance.bot.domain.FinanceCategory
import ru.vichukano.home.finance.bot.domain.FinanceInfo
import ru.vichukano.home.finance.bot.domain.UserState
import ru.vichukano.home.finance.bot.store.UserStore

class CategoryHandler : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        val rawMessage = update.message.removePrefix("/")
        val category = FinanceCategory.valueOf(rawMessage)
        return when {
            FinanceCategory.values().map { it.name }.contains(rawMessage) -> {
                val user = UserStore.userStore[update.userId]
                    ?: throw IllegalStateException("User with id: ${update.userId} not found")
                val updated = user.copy(
                    state = UserState.INPUT_AMOUNT
                ).apply {
                    financeInfo.add(FinanceInfo(
                        isDebit = category.category == DebitCreditCategory.DEBIT,
                        category = category.value
                    ))
                }
                UserStore.userStore[updated.id] = updated
                return "Введите сумму"
            }
            else -> "Нажмите на верную категорию"
        }
    }

}