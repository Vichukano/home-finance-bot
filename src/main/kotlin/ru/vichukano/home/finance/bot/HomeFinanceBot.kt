package ru.vichukano.home.finance.bot

import mu.KotlinLogging
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.vichukano.home.finance.bot.excel.ExcelDao
import ru.vichukano.home.finance.bot.handler.HandlerDispatcher
import ru.vichukano.home.finance.bot.handler.chatId
import ru.vichukano.home.finance.bot.handler.userId

class HomeFinanceBot(private val name: String, private val token: String, dao: ExcelDao) : TelegramLongPollingBot() {
    private val log = KotlinLogging.logger {}
    private val dispatcher = HandlerDispatcher(dao)

    override fun getBotToken() = this.token

    override fun getBotUsername() = this.name

    override fun onUpdateReceived(update: Update?) {
        update?.let {
            try {
                log.info { "Receive update from chatId: ${chatId(update)} and userId: ${userId(update)}" }
                val out = dispatcher.handle(it)
                if (out is SendDocument) {
                    execute(out)
                } else {
                    execute(out as SendMessage)
                }
                log.info { "Successfully executed answer: $out" }
            } catch (e: Exception) {
                log.error(e) {}
            }
        }
    }
}