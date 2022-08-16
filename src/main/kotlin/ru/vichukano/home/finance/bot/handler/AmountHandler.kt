package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.Command
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.CREDIT
import ru.vichukano.home.finance.bot.domain.DebitCreditCategory.DEBIT
import ru.vichukano.home.finance.bot.domain.UserState
import ru.vichukano.home.finance.bot.store.UserStore

class AmountHandler : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        return try {
            val amount = update.message.toDouble()
            val user = UserStore.userStore[update.userId]
                ?: throw IllegalStateException("User with id: ${update.userId} not found")
            val updated = user.copy(state = UserState.CHOOSE_DEBIT_CREDIT).also {
                it.financeInfo.find { fi -> fi.amount == 0.0 }
                    .also { financeInfo ->
                        financeInfo?.let { fin -> fin.amount = amount }
                    }
            }
            UserStore.userStore[updated.id] = updated
            "Выбери категорию:\n\n${CREDIT.value} ${CREDIT.rusValue}\n\n${DEBIT.value} ${DEBIT.rusValue}\n\n" +
                    "или нажми:\n\n${Command.CONFIRM.value}\n\nчтобы сохранить отчет и завершить работу"
        } catch (e: NumberFormatException) {
            "Введите сумму в верном формате. Пример:\n10000\n200.50"
        }
    }

}