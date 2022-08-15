package ru.vichukano.home.finance.bot.excel

import mu.KotlinLogging
import ru.vichukano.home.finance.bot.domain.UserInfo
import java.io.File
import java.io.FileOutputStream

class ExcelDao(private val pathToStore: String) {
    private val log = KotlinLogging.logger {}
    private val fileSuffix = "-data.xlsx"

    fun populateData(userInfo: UserInfo) {
        log.debug { "Start to populate data for user info: $userInfo" }
        val pathToBook = "$pathToStore/${userInfo.id}$fileSuffix"
        val book = workbook(pathToBook)
        userInfo.financeInfo.forEach { book.addFinanceInfo(it) }
        FileOutputStream(pathToBook).use { book.write(it) }
        log.debug { "Finish to populate data" }
    }

    fun getDataByUserId(userId: String): File {
        return File("$pathToStore/$userId$fileSuffix")
    }
}
