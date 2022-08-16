package ru.vichukano.home.finance.bot

import mu.KotlinLogging
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.vichukano.home.finance.bot.excel.ExcelDao

private val log = KotlinLogging.logger {}
fun main(args: Array<String>) {
    log.info { "Start home-finance-bot" }
    val botName = args[0]
    val token = args[1]
    val pathToStore = args[2]
    TelegramBotsApi(DefaultBotSession().javaClass).registerBot(HomeFinanceBot(botName, token, ExcelDao(pathToStore)))
}
