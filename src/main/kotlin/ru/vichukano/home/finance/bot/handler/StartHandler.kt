package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.CREDIT
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.DEBIT
import ru.vichukano.home.finance.bot.domain.UserInfo
import ru.vichukano.home.finance.bot.domain.UserState
import ru.vichukano.home.finance.bot.store.UserStore

class StartHandler : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        val user = UserStore.userStore[update.userId] ?: UserInfo(
            id = update.userId,
            state = UserState.CHOOSE_DEBIT_CREDIT,
            financeInfo = mutableListOf()
        )
        val updated = user.copy(state = UserState.CHOOSE_DEBIT_CREDIT)
        UserStore.userStore[updated.id] = updated
        return "Выбери категорию:\n\n${CREDIT.value} ${CREDIT.rusValue}\n\n${DEBIT.value} ${DEBIT.rusValue}\n"
    }

}