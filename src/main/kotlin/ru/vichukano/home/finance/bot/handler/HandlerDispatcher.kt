package ru.vichukano.home.finance.bot.handler

import mu.KotlinLogging
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import ru.vichukano.home.finance.bot.domain.Command
import ru.vichukano.home.finance.bot.domain.UserInfo
import ru.vichukano.home.finance.bot.domain.UserState
import ru.vichukano.home.finance.bot.excel.ExcelDao
import ru.vichukano.home.finance.bot.store.UserStore

class HandlerDispatcher(dao: ExcelDao) : UpdateHandler<Update, Validable> {
    private val log = KotlinLogging.logger {}
    private val startHandler = StartHandler()
    private val debitCreditHandler = DebitCreditHandler()
    private val categoryHandler = CategoryHandler()
    private val amountHandler = AmountHandler()
    private val confirmHandler = ConfirmHandler(dao)
    private val reportHandler = ReportHandler(dao)

    override fun handle(update: Update): Validable {
        log.debug { "Receive update: $update" }
        val id = userId(update)
        val text = message(update)
        val user = UserStore.userStore[id] ?: UserInfo(id, UserState.START)
        val context = HandlerContext(id, text)
        if (text == Command.REPORT.value) {
            val report = reportHandler.handle(context)
            val out = SendDocument().apply {
                chatId = chatId(update)
                document = InputFile(report)
            }
            log.debug { "Document out: $out" }
            return out
        }
        val answer = when {
            text == Command.HELP.value -> "Полезное сообщение о помощи"
            text == Command.CONFIRM.value -> confirmHandler.handle(context)
            user.state == UserState.START -> startHandler.handle(context)
            user.state == UserState.CHOOSE_DEBIT_CREDIT -> debitCreditHandler.handle(context)
            user.state == UserState.CHOSE_CATEGORY -> categoryHandler.handle(context)
            user.state == UserState.INPUT_AMOUNT -> amountHandler.handle(context)
            else -> "Я не знаю такую команду"
        }
        val out = SendMessage().apply {
            this.text = answer
            chatId = chatId(update)
        }
        log.debug { "Message out: $out" }
        return out
    }
}