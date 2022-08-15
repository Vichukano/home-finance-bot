package ru.vichukano.home.finance.bot.excel

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.vichukano.home.finance.bot.domain.FinanceInfo
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

internal class ExcelFunctionsKtTest {
    private val temp = System.getProperty("java.io.tmpdir") + "/"
    private val bookName = "test.xlsx"

    @Test
    fun shouldCreateSaveAndLoadNewWorkbook() {
        val book = workbook(temp + bookName)
        book.newSheetWithHeader()
        FileOutputStream(temp + bookName).use { book.write(it) }

        val loaded = workbook(temp + bookName)

        val now = LocalDate.now()
        Assertions.assertThat(loaded.isSheetPresent(now.month.toString())).isTrue
        Assertions.assertThat(loaded.getSheet(now.month.toString()).getRow(0).getCell(0).stringCellValue)
            .isEqualTo("${now.month} - ${now.year}")
        Files.deleteIfExists(Path.of(temp + bookName))
    }

    @Test
    fun shouldAddAmount() {
        val now = LocalDate.now()
        val book = workbook("test.xlsx")
        val debitInfo = FinanceInfo("Зарплата", 30000.0)
        val debitInfoOnce = FinanceInfo("Зарплата", 10000.0)
        val creditInfo = FinanceInfo("Здоровье", 5000.0)

        book.addFinanceInfo(debitInfo)
        book.addFinanceInfo(debitInfoOnce)
        book.addFinanceInfo(creditInfo)

        Assertions.assertThat(book.getSheet(now.month.name).getRow(4).getCell(0).numericCellValue)
            .isEqualTo(debitInfo.amount + debitInfoOnce.amount)
        Assertions.assertThat(book.getSheet(now.month.name).getRow(6).getCell(2).numericCellValue)
            .isEqualTo(creditInfo.amount)
    }
}
