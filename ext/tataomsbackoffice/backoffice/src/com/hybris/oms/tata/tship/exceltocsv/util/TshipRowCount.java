package com.hybris.oms.tata.tship.exceltocsv.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;


/**
 *
 * This class is used to find the total row count of the given excel file.
 *
 */
public class TshipRowCount
{

	/**
	 * This method gets total row count of the excel.
	 *
	 * @param sheet
	 * @return
	 */
	public int determineRowCount(final XSSFSheet sheet)
	{
		int lastRowIndex = -1;
		if (sheet.getPhysicalNumberOfRows() > 0)
		{
			// getLastRowNum() actually returns an index, not a row number
			lastRowIndex = sheet.getLastRowNum();
			// now, start at end of spreadsheet and work our way backwards until
			// we find a row having data
			for (; lastRowIndex >= 0; lastRowIndex--)
			{
				final Row row = sheet.getRow(lastRowIndex);
				if (!isRowEmpty(row))
				{
					break;
				}
			}
		}
		return lastRowIndex;
	}

	/**
	 * Checks whether every row is empty or not.
	 *
	 * @param row
	 * @return
	 */
	public boolean isRowEmpty(final Row row)
	{
		if (row == null)
		{
			return true;
		}
		final int cellCount = row.getLastCellNum() + 1;
		for (int i = 0; i < cellCount; i++)
		{
			final String cellValue = getCellValue(row, i);
			if (cellValue != null && cellValue.length() > 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * This method gets each cell value of that particular row.
	 *
	 * @param row
	 * @param columnIndex
	 * @return
	 */
	public String getCellValue(final Row row, final int columnIndex)
	{
		final DataFormatter evaluator = new DataFormatter();
		String cellValue;
		final Cell cell = row.getCell(columnIndex);
		if (cell == null)
		{
			// no data in this cell
			cellValue = null;
		}
		else
		{
			if (cell.getCellType() != Cell.CELL_TYPE_FORMULA)
			{
				// cell has a value, so format it into a string
				cellValue = evaluator.formatCellValue(cell);
			}
			else
			{
				// cell has a formula, so evaluate it
				cellValue = evaluator.formatCellValue(cell);
			}
		}
		return cellValue;
	}

	/**
	 * This method is used to find columnIndex of surfaceMode.
	 *
	 * @param sheet
	 * @param cellContent
	 * @return
	 */
	public int findSurfaceModeIndex(final XSSFSheet sheet, final String cellContent)
	{
		for (final Row row : sheet)
		{
			for (final Cell cell : row)
			{
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				{
					if (cell.getRichStringCellValue().getString().trim().equals(cellContent))
					{
						return cell.getColumnIndex();
					}
				}
			}
		}
		return 0;
	}
}
