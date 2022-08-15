package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.CREDIT
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.DEBIT
import ru.vichukano.home.finance.bot.domain.FinanceCategory
import ru.vichukano.home.finance.bot.domain.UserState
import ru.vichukano.home.finance.bot.store.UserStore

class DebitCreditHandler : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        return when (update.message) {
            DEBIT.value -> {
                val user = UserStore.userStore[update.userId]
                    ?: throw IllegalArgumentException("User with id: ${update.userId} is absent")
                val updated = user.copy(state = UserState.CHOSE_CATEGORY)
                UserStore.userStore[updated.id] = updated
                return FinanceCategory.values()
                    .asList()
                    .filter { it.category == DEBIT }
                    .joinToString(separator = "\n\n") { "/$it  ${it.value}" }
            }
            CREDIT.value -> {
                val user = UserStore.userStore[update.userId]
                    ?: throw IllegalArgumentException("User with id: ${update.userId} is absent")
                val updated = user.copy(state = UserState.CHOSE_CATEGORY)
                UserStore.userStore[updated.id] = updated
                return FinanceCategory.values()
                    .asList()
                    .filter { it.category == CREDIT }
                    .joinToString(separator = "\n\n") { "/$it  ${it.value}" }
            }
            else -> "Не верная категрия!\nВыбери одну из:\n\n${CREDIT.value}\n\n${DEBIT.value}"
        }
    }

}

