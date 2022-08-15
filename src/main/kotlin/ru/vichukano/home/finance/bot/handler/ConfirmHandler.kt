package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.domain.Command
import ru.vichukano.home.finance.bot.excel.ExcelDao
import ru.vichukano.home.finance.bot.store.UserStore
import java.time.LocalDate

class ConfirmHandler(private val dao: ExcelDao) : UpdateHandler<HandlerContext, String> {

    override fun handle(update: HandlerContext): String {
        val removed = UserStore.userStore.remove(update.userId)
        removed?.let {
            dao.populateData(it)
        }
        return """Внесены данные:
            |Дата: ${LocalDate.now()}
            |Финансы:
            |******************************
            |${removed?.financeInfo?.joinToString(separator = "\n") { "[${it.category}: ${it.amount}]" }}
            |******************************
            |
            |
            |Нажми  ${Command.REPORT.value}  чтобы получить excel отчет
        """.trimMargin()
    }

}