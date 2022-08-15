package ru.vichukano.home.finance.bot.handler

import ru.vichukano.home.finance.bot.excel.ExcelDao
import java.io.File

class ReportHandler(private val dao: ExcelDao) : UpdateHandler<HandlerContext, File> {

    override fun handle(update: HandlerContext): File {
        val foundFile = dao.getDataByUserId(update.userId)
        if (!foundFile.exists()) {
            throw IllegalArgumentException("Report not found for user id: ${update.userId}")
        }
        return foundFile
    }

}