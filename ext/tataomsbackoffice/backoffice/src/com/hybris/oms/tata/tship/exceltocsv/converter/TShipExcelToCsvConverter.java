package com.hybris.oms.tata.tship.exceltocsv.converter;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.tata.tship.exceltocsv.pojo.LogisticPartner;
import com.hybris.oms.tata.tship.exceltocsv.pojo.PriorityMaster;
import com.hybris.oms.tata.tship.exceltocsv.service.DefaultTshipExcelToCsvService;
import com.hybris.oms.tata.tship.exceltocsv.util.TshipExcelConstants;
import com.hybris.oms.tata.tship.exceltocsv.util.TshipLogisticPartnerUtil;
import com.hybris.oms.tata.tship.exceltocsv.util.TshipRowCount;
import com.hybris.oms.tata.tship.exceltocsv.validator.TshipLogisticPartnerValidator;
import com.hybris.oms.tata.tship.exceltocsv.validator.TshipPriorityMasterValidator;


/**
 *
 * This class takes the input of excel and process the required validaions, if there are no validations then the output
 * will be generated in CSV file in particular location,else validation messages can be seen in validation file.
 *
 */
public class TShipExcelToCsvConverter
{
	private static final Logger LOG = LoggerFactory.getLogger(TShipExcelToCsvConverter.class);

	@Resource(name = "tshipExcelToCsvService")
	private DefaultTshipExcelToCsvService tshipExcelToCsvService;
	@Resource(name = "logisticPartnerValidator")
	private TshipLogisticPartnerValidator logisticPartnerValidator;
	@Resource(name = "priorityMasterValidator")
	private TshipPriorityMasterValidator priorityMasterValidator;
	@Resource(name = "priorityMaster")
	private PriorityMaster priorityMaster;
	@Autowired
	private CommonI18NService commonI18NService;

	/**
	 * @param priorityMaster
	 *           the priorityMaster to set
	 */
	public void setPriorityMaster(final PriorityMaster priorityMaster)
	{
		this.priorityMaster = priorityMaster;
	}

	/**
	 * @param tshipExcelToCsvService
	 *           the tshipExcelToCsvService to set
	 */
	public void setTshipExcelToCsvService(final DefaultTshipExcelToCsvService tshipExcelToCsvService)
	{
		this.tshipExcelToCsvService = tshipExcelToCsvService;
	}

	/**
	 * @param priorityMasterValidator
	 *           the priorityMasterValidator to set
	 */
	public void setPriorityMasterValidator(final TshipPriorityMasterValidator priorityMasterValidator)
	{
		this.priorityMasterValidator = priorityMasterValidator;
	}

	/**
	 * @param logisticPartnerValidator
	 *           the logisticPartnerValidator to set
	 */
	public void setLogisticPartnerValidator(final TshipLogisticPartnerValidator logisticPartnerValidator)
	{
		this.logisticPartnerValidator = logisticPartnerValidator;
	}

	//

	/**
	 * This method reads the input from excel file and loads the logisticPartner names, then validates priority masters
	 * data (i.e) all the priorities of air and surface. if validation of prioritymaster is successfull then validates
	 * all the logisticPartner data and then writes the output to csv file.
	 *
	 * @param deliveryMode
	 * @param inputFile
	 * @param outputFile
	 * @param validationFile
	 * @param fileNameTimeStamp
	 */
	public void convertExcelToCsv(final String deliveryMode, final File inputFile, final File outputFile,
			final File validationFile, final String fileNameTimeStamp)
	{

		LOG.info("inside Convert Excel TO CSV");
		 FileWriter writer=null;
		XSSFWorkbook wBook = null;
		XSSFSheet sheet = null;
		ArrayList<Integer> logisticPartnerIndexes = null;
		final Map<Integer, String> logisticPartners = new HashMap<Integer, String>();

		try
		{
			final File outputPath = new File(outputFile.getParent());
			if (!outputPath.exists())
			{
				outputPath.mkdirs();
			}
			if (!outputFile.exists())
			{
				outputFile.createNewFile();
			}
			final File validationPath = new File(validationFile.getParent());
			if (validationPath != null)
			{
				validationPath.mkdirs();
			}
			if (!validationFile.exists())
			{
				validationPath.createNewFile();
			}
			writer = new FileWriter(validationFile);
			wBook = new XSSFWorkbook(new FileInputStream(inputFile));
			logisticPartnerIndexes = new ArrayList<Integer>();
			final TshipRowCount rowCount = new TshipRowCount();
			//final int numberOfSheets = wBook.getNumberOfSheets();
			sheet = wBook.getSheetAt(0);
			final int rowcount = rowCount.determineRowCount(sheet);
			//appending of header data to csv file.
			tshipExcelToCsvService.appendHeaderData(deliveryMode);
			for (final Row row : sheet)
			{
				final int rowNumber = row.getRowNum() + 1;
				//surfaceModeLastIndex = sheet.getRow(3).getLastCellNum();
				if (row.getRowNum() == 1)
				{
					final int lastColumnIndex = Integer.parseInt(TshipExcelConstants.LAST_COLUMN_INDEX);
					final int columnIndex = Integer.parseInt(TshipExcelConstants.COD_COLUMN_INDEX);
					//store logistic partner names in a MAP
					for (int i = columnIndex; i < lastColumnIndex; i = i + 9)
					{
						final String logisticPartnerName = TshipLogisticPartnerUtil
								.findLogisticPartnerName(getCellData(row.getCell(i)));
						logisticPartners.put(i, logisticPartnerName);
						logisticPartnerIndexes.add(i);//keep track of starting index of logistic partner data

					}
				}

				if (row.getRowNum() >= 3 && row.getRowNum() <= rowcount)
				{

					//Get Priority Master first
					//	final PriorityMaster priorityMaster = new PriorityMaster();
					//final AirPriorityMaster airPriorityMaster = new AirPriorityMaster();

					for (int i = 0; i < logisticPartnerIndexes.get(0); i++)
					{
						//final Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
						final String data = getCellData(row.getCell(i));
						switch (i)
						{
							case 0:
								final String pincodeResult = data.substring(0, data.length() - 1);
								final boolean result = isNumeric(pincodeResult);
								//This condition is for checking whether pincode is numeric or not
								if (result)
								{
									final Double dou = Double.valueOf(pincodeResult);
									final int pincode = dou.intValue();
									final int length = (int) (Math.log10(pincode) + 1);
									//This condition is for pincode should have only 6 digitis
									if (length == 6)
									{
										priorityMaster.setPincode(pincode + TshipExcelConstants.ADD_COMMA);
									}
									else
									{
										writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
												+ TshipExcelConstants.EMPTY_SPACE + TshipExcelConstants.PINCODE
												+ TshipExcelConstants.NEW_LINE);
									}
								}
								else
								{
									writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
											+ TshipExcelConstants.EMPTY_SPACE + TshipExcelConstants.PINCODE + TshipExcelConstants.NEW_LINE);
								}
								break;
							case 1:
								priorityMaster.getAirPriorityMaster().setAirPrepaidPriority1(data);
								break;
							case 2:
								priorityMaster.getAirPriorityMaster().setAirPrepaidPriority2(data);
								break;
							case 3:
								priorityMaster.getAirPriorityMaster().setAirPrepaidPriority3(data);
								break;
							case 4:
								priorityMaster.getAirPriorityMaster().setAirPrepaidPriority4(data);
								break;
							case 5:
								priorityMaster.getAirPriorityMaster().setAirPrepaidPriority5(data);
								break;
							case 6:
								priorityMaster.getAirPriorityMaster().setAirCodPriority1(data);
								break;
							case 7:
								priorityMaster.getAirPriorityMaster().setAirCodPriority2(data);
								break;
							case 8:
								priorityMaster.getAirPriorityMaster().setAirCodPriority3(data);
								break;
							case 9:
								priorityMaster.getAirPriorityMaster().setAirCodPriority4(data);
								break;
							case 10:
								priorityMaster.getAirPriorityMaster().setAirCodPriority5(data);
								break;
							case 11:
								priorityMaster.getSurfacePriorityMaster().setSurfacePrepaidPriority1(data);
								break;
							case 12:
								priorityMaster.getSurfacePriorityMaster().setSurfacePrepaidPriority2(data);
								break;
							case 13:
								priorityMaster.getSurfacePriorityMaster().setSurfacePrepaidPriority3(data);
								break;
							case 14:
								priorityMaster.getSurfacePriorityMaster().setSurfacePrepaidPriority4(data);
								break;
							case 15:
								priorityMaster.getSurfacePriorityMaster().setSurfaceCodPriority1(data);
								break;
							case 16:
								priorityMaster.getSurfacePriorityMaster().setSurfaceCodPriority2(data);
								break;
							case 17:
								priorityMaster.getSurfacePriorityMaster().setSurfaceCodPriority3(data);
								break;
							case 18:
								priorityMaster.getSurfacePriorityMaster().setSurfaceCodPriority4(data);
								break;
							case 19:
								final String city = data;
								if (city.length() == 1)
								{
									writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
											+ TshipExcelConstants.EMPTY_SPACE + TshipExcelConstants.EMPTY_SPACE + TshipExcelConstants.CITY
											+ TshipExcelConstants.NEW_LINE);
								}
								else
								{
									priorityMaster.setCity(data);
								}
								break;
							case 20:
								final String state = data.substring(0, data.length() - 1).toLowerCase();
								//lgoic to get state codes
								final Map<String, String> stateCodes = new HashMap<>();
								final List<RegionModel> statecodes = commonI18NService.getAllRegions();
								for (final RegionModel code : statecodes)
								{
									stateCodes.put(code.getName().toLowerCase(), code.getIsocode());
								}

								final String stateResult = stateCodes.get(state);
								if (stateResult == null)
								{
									writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
											+ TshipExcelConstants.EMPTY_SPACE + state + TshipExcelConstants.EMPTY_SPACE
											+ TshipExcelConstants.STATE_VALIDATION + TshipExcelConstants.NEW_LINE);
								}
								else
								{
									final String replaceResult = stateResult.replace("IN-", "");
									priorityMaster.setState(replaceResult + ",");
								}
								break;
							default:
								break;
						}//switch end
					} //for
					  //validate Priority Master data
					if (priorityMasterValidator.validate(priorityMaster, writer, rowNumber))
					{// if priority mastr is correct validate lp data

						LOG.info("inside priorty Master");
						final ArrayList<LogisticPartner> logisticPartners1 = new ArrayList<LogisticPartner>();
						for (int i = 0; i < logisticPartnerIndexes.size(); i++)
						{
							final int index = logisticPartnerIndexes.get(i);//11

							//create Logistic Partner
							final LogisticPartner logisticPartner = new LogisticPartner();

							//store logicticPartner starting index from excel sheet into logistic partner object
							logisticPartner.setIndex(index);
							//get the name of logistic Partner and store in logistic partner object
							logisticPartner.setName(logisticPartners.get(index));
							int propertiesPopulated = 0;
							for (int j = index; j < index + 9; j++)
							{
								switch (propertiesPopulated)
								{
									case 0:
										logisticPartner.setCod(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 1:
										logisticPartner.setPrepaidLimit(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 2:
										logisticPartner.setCodLimit(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 3:
										logisticPartner.setCarea(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 4:
										logisticPartner.setCscrcd(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 5:
										logisticPartner.setCloctype(getCellData(row.getCell(j)));
										propertiesPopulated++;
										break;
									case 6:
										logisticPartner.setNewzone(getCellData(row.getCell(j)));
										//Get the configured default values for tatPrepaid,tatCOD and set it here.
										//The default value should be a valid integer. Zero is an acceptable value.
										final String tatPrepaidValue = TshipExcelConstants.TAT_PREPAID;
										final String tatCodValue = TshipExcelConstants.TAT_COD;
										if (StringUtils.isNotEmpty(tatPrepaidValue))
										{
											logisticPartner.setTatPrepaid(tatPrepaidValue + TshipExcelConstants.ADD_COMMA);
										}
										else
										{
											logisticPartner.setTatPrepaid("0" + TshipExcelConstants.ADD_COMMA);
										}

										if (StringUtils.isNotEmpty(tatCodValue))
										{
											logisticPartner.setTatCode(tatCodValue + TshipExcelConstants.ADD_COMMA);
										}
										else
										{
											logisticPartner.setTatCode("0" + TshipExcelConstants.ADD_COMMA);
										}
										propertiesPopulated++;
										break;
									case 7:
										logisticPartner.setFormRequired(getCellData(row.getCell(j)) + TshipExcelConstants.ADD_COMMA);
										propertiesPopulated++;
										break;
									case 8:
										logisticPartner.setPickUp(getCellData(row.getCell(j)));
										propertiesPopulated = 0;
										logisticPartners1.add(logisticPartner);//add whole details of Logistic Partner to list
										break;
									default:
										break;
								}
							} //for
						} //for
						  //
						  //	System.out.println("lp " + logisticPartners1);
						  //Now validate all Logistic Partner Data
						try
						{
							logisticPartnerValidator.validateLogisticPartners(deliveryMode, priorityMaster, logisticPartners1, writer,
									rowNumber);
						}
						catch (final Exception e)
						{
							writer.flush();
						} //TISPRDT-969 :it will continue to the loop end of the  logistics
					} //if PM validated
				} //reading rows while loop
			} //sheet reading for loop
			writer.flush();
			tshipExcelToCsvService.appendFooterData(deliveryMode, fileNameTimeStamp);
			tshipExcelToCsvService.writeData(tshipExcelToCsvService.getData(), outputFile, validationFile);

		} //try
		catch (final Exception e)
		{

		LOG.info("Error while Lodaing Tship Bulk Upload File "+e.getMessage());
		} //catch
		finally //TISPRDT-969:to write the error logistics data in case any exception with validation
		{
			try
			{
				writer.flush();
				writer.close();
			}
			catch (final IOException e)
			{
				LOG.info("Error while closing the writer obejct"+e.getMessage());
			}

		}

	}

	/**
	 * This method handles two types of input data, one is with formulas and other is without formula.
	 *
	 * @param cell
	 * @return
	 */
	public String getCellData(final Cell cell)
	{
		String result = "";
		if (cell != null)
		{
			switch (cell.getCellType())
			{
				case Cell.CELL_TYPE_BOOLEAN:
					result = (cell.getBooleanCellValue() + TshipExcelConstants.ADD_COMMA);
					break;
				case Cell.CELL_TYPE_NUMERIC:
					result = (cell.getNumericCellValue() + TshipExcelConstants.ADD_COMMA);
					break;
				case Cell.CELL_TYPE_STRING:
					result = (cell.getStringCellValue() + TshipExcelConstants.ADD_COMMA);

					break;
				case Cell.CELL_TYPE_BLANK:
					result = ("" + ",");
					break;
				case Cell.CELL_TYPE_FORMULA:
					switch (cell.getCachedFormulaResultType())
					{
						case Cell.CELL_TYPE_STRING:
							result = (cell.getStringCellValue() + TshipExcelConstants.ADD_COMMA);
							break;
						case Cell.CELL_TYPE_NUMERIC:
							result = (cell.getNumericCellValue() + TshipExcelConstants.ADD_COMMA);
							break;
						case Cell.CELL_TYPE_BLANK:
							result = ("" + ",");
							break;
						default:
							result = (cell + "");
					}
					break;
				default:
					result = (cell + "");

			}// switch end
		}

		return result;
	}//


	/**
	 *
	 * @param pincode
	 * @return
	 */
	public boolean isNumeric(final String pincode)
	{
		return pincode.matches("[-+]?\\d*\\.?\\d+");
	}




	//end
}