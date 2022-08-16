package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.Command
import ru.vichukano.home.finance.bot.domain.FinanceCategory
import ru.vichukano.home.finance.bot.excel.ExcelDao
import ru.vichukano.home.finance.bot.store.UserStore
import java.time.LocalDate

class ConfirmHandler(private val dao: ExcelDao) : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        val removed = UserStore.userStore.remove(update.userId)
        removed?.let {
            dao.populateData(it)
        }
        val info = removed?.financeInfo
            ?.sortedBy { FinanceCategory.categoryByValue(it.category).category.rusValue }
            ?.joinToString(separator = "\n") {
                "[${FinanceCategory.categoryByValue(it.category).category.rusValue}: ${it.category}: ${it.amount}]"
            }
        return """Внесены данные:
            |Дата: ${LocalDate.now()}
            |Финансы:
            |******************************
            |$info
            |******************************
            |
            |
            |Нажми  ${Command.REPORT.value}  чтобы получить excel отчет
        """.trimMargin()
    }

}