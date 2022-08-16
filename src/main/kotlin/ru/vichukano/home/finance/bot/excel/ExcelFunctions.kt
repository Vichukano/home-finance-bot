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
    HOME_COMMON_CELL(HOME_COMMON.value, 7, 3),
    MORTGAGE_CELL(MORTGAGE.value, 8, 3),
    BABY_CELL(BABY.value, 9, 3),
    TRAINING_CELL(TRAINING.value, 10, 3),
    BEAUTY_CELL(BEAUTY.value, 11, 3),
    GIFTS_CELL(GIFTS.value, 12, 3),
    FUN_CELL(FUN.value, 13, 3),
    INVESTMENTS_CELL(INVESTMENTS.value, 14, 3),
    TRANSPORT_CELL(TRANSPORT.value, 15, 3),
    HOME_CELL(HOME.value, 16, 3),
    INSURANCE_CELL(INSURANCE.value, 17, 3),
    OTHER_CREDIT_CELL(OTHER_CREDIT.value, 18, 3),
    SALARY_VI_CELL(SALARY_VI.value, 4, 1),
    SALARY_JU_CELL(SALARY_JU.value, 5, 1),
    PREPAID_VI_CELL(PREPAID_VI.value, 6, 1),
    PREPAID_JU_CELL(PREPAID_JU.value, 7, 1),
    CASHBACK_CELL(CASHBACK.value, 8, 1),
    GIFTS_FOR_US_CELL(GIFTS_FOR_US.value, 9, 1),
    OTHER_DEBIT_CELL(OTHER_DEBIT.value, 10, 1)
}

fun workbook(name: String): Workbook {
    return try {
        FileInputStream(name).use { WorkbookFactory.create(it) }
    } catch (e: Exception) {
        log.info { "Create new book" }
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
    val headerStyle = this.createCellStyle().also { style ->
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.borderBottom = BorderStyle.MEDIUM
        style.borderLeft = BorderStyle.MEDIUM
        style.borderRight = BorderStyle.MEDIUM
        style.borderTop = BorderStyle.MEDIUM
        style.fillPattern = FillPatternType.SOLID_FOREGROUND
        style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
    }
    val formula = this.creationHelper.createFormulaEvaluator()
    sheet.createRow(0).also { row ->
        row.createCell(0).also {
            it.setCellValue("${now.month} - ${now.year}")
            it.cellStyle = headerStyle
        }
        row.createCell(4).also {
            it.setCellValue("Итоги")
            it.cellStyle = headerStyle
        }
        row.createCell(5).also { it.cellStyle = headerStyle }
    }
    sheet.createRow(1).also { row ->
        row.createCell(0).also {
            it.setCellValue("Доход")
            it.cellStyle = headerStyle
        }
        row.createCell(2).also {
            it.setCellValue("Расход")
            it.cellStyle = headerStyle
        }
        row.createCell(4).also {
            it.setCellValue("Доход")
            it.cellStyle = headerStyle
        }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "SUM(A5:A30)"
            it.cellStyle = headerStyle
            formula.evaluate(it)
        }
    }
    sheet.createRow(2).also { row ->
        row.createCell(4).also {
            it.setCellValue("Расход")
            it.cellStyle = headerStyle
        }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "SUM(C5:C30)"
            formula.evaluate(it)
            it.cellStyle = headerStyle
        }
    }
    sheet.createRow(3).also { row ->
        row.createCell(0).also {
            it.setCellValue("Сумма")
            it.cellStyle = headerStyle
        }
        row.createCell(1).also {
            it.setCellValue("Категория")
            it.cellStyle = headerStyle
        }
        row.createCell(2).also {
            it.setCellValue("Сумма")
            it.cellStyle = headerStyle
        }
        row.createCell(3).also {
            it.setCellValue("Категория")
            it.cellStyle = headerStyle
        }
        row.createCell(4).also {
            it.setCellValue("Профит")
            it.cellStyle = headerStyle
        }
        row.createCell(5, CellType.FORMULA).also {
            it.cellFormula = "F2-F3"
            formula.evaluate(it)
            it.cellStyle = headerStyle
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
