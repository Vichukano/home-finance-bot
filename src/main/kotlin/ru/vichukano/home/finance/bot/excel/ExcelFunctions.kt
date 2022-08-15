package ru.vichukano.home.finance.bot.excel

import mu.KotlinLogging
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.vichukano.home.finance.bot.domain.FinanceCategory.*
import ru.vichukano.home.finance.bot.domain.FinanceInfo
import java.io.FileInputStream
import java.time.LocalDate

private val log = KotlinLogging.logger {}

private enum class CategoryCell(val value: String, val rowIndex: Int, val colIndex: Int) {
    FOOD_CELL(FOOD.value, 4, 3),
    AUTOMOBILE_CELL(AUTOMOBILE.value, 5, 3),
    HEALTH_CELL(HEALTH.value, 6, 3),
    OTHER_CREDIT_CELL(OTHER_CREDIT.value, 7, 3),
    SALARY_CELL(SALARY.value, 4, 1),
    PREPAID_CELL(PREPAID.value, 5, 1),
    OTHER_DEBIT_CELL(OTHER_DEBIT.value, 6, 1)
}

fun workbook(name: String): Workbook {
    return try {
        FileInputStream(name).use { WorkbookFactory.create(it) }
    } catch (e: Exception) {
        log.info {"Create new book"}
        XSSFWorkbook()
    }
}

fun Workbook.addFinanceInfo(financeInfo: FinanceInfo) {
    val currMonth = LocalDate.now().month.name
    val sheet = if (this.isSheetPresent(currMonth)) this.getSheet(currMonth) else this.newSheetWithHeader()
    val category = CategoryCell.values().first { c -> c.value == financeInfo.category }
    val cellToInsert =
        sheet.getRow(category.rowIndex).getCell(category.colIndex - 1) ?: sheet.getRow((category.rowIndex))
            .createCell(category.colIndex - 1)
    cellToInsert.setCellValue(cellToInsert.numericCellValue + financeInfo.amount)
}

fun Workbook.isSheetPresent(sheetName: String): Boolean = this.getSheet(sheetName) != null

fun Workbook.newSheetWithHeader(): Sheet {
    val now = LocalDate.now()
    val sheet = this.createSheet(now.month.name)
    sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 3))
    sheet.addMergedRegion(CellRangeAddress(0, 0, 4, 5))
    sheet.addMergedRegion(CellRangeAddress(1, 2, 0, 1))
    sheet.addMergedRegion(CellRangeAddress(1, 2, 2, 3))
    sheet.isSelected = true
    val formula = this.creationHelper.createFormulaEvaluator()
    val headerStyle = this.createCellStyle().also { style ->
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
    }
    sheet.createRow(0).also { row ->
        row.rowStyle = headerStyle
        row.createCell(0).also { it.setCellValue("${now.month} - ${now.year}") }
        row.createCell(4).also { it.setCellValue("Итоги") }
    }
    sheet.createRow(1).also { row ->
        row.rowStyle = headerStyle
        row.createCell(0).also { it.setCellValue("Доход") }
        row.createCell(2).also { it.setCellValue("Расход") }
        row.createCell(4).also { it.setCellValue("Доход") }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "SUM(A5:A30)"
            formula.evaluate(it)
        }
    }
    sheet.createRow(2).also { row ->
        row.rowStyle = headerStyle
        row.createCell(4).also { it.setCellValue("Расход") }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "SUM(C5:C30)"
            formula.evaluate(it)
        }
    }
    sheet.createRow(3).also { row ->
        row.rowStyle = headerStyle
        row.createCell(0).also { it.setCellValue("Сумма") }
        row.createCell(1).also { it.setCellValue("Категория") }
        row.createCell(2).also { it.setCellValue("Сумма") }
        row.createCell(3).also { it.setCellValue("Категория") }
        row.createCell(4).also { it.setCellValue("Профит") }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "F2-F3"
            formula.evaluate(it)
        }
    }
    for (category in CategoryCell.values()) {
        val row = sheet.getRow(category.rowIndex) ?: sheet.createRow(category.rowIndex)
        row.createCell(category.colIndex).also { it.setCellValue(category.value) }
    }
    for (i in 0..4) {
        sheet.autoSizeColumn(i, true)
    }
    return sheet
}
