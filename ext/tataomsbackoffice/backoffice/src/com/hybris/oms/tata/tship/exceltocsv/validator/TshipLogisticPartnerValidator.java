package com.hybris.oms.tata.tship.exceltocsv.validator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.hybris.oms.tata.tship.exceltocsv.pojo.LogisticPartner;
import com.hybris.oms.tata.tship.exceltocsv.pojo.PriorityMaster;
import com.hybris.oms.tata.tship.exceltocsv.service.DefaultTshipExcelToCsvService;
import com.hybris.oms.tata.tship.exceltocsv.util.TshipExcelConstants;


/**
 *
 * This class validates all logistic Partners data, if any of the fields of the logisticpartner fails to have manditory
 * conditions, it will raise those validation message in validation file.
 *
 */
public class TshipLogisticPartnerValidator
{
	@Resource(name = "tshipExcelToCsvService")
	private DefaultTshipExcelToCsvService tshipExcelToCsvService;

	//

	/**
	 * @param tshipExcelToCsvService
	 *           the tshipExcelToCsvService to set
	 */
	public void setTshipExcelToCsvService(final DefaultTshipExcelToCsvService tshipExcelToCsvService)
	{
		this.tshipExcelToCsvService = tshipExcelToCsvService;
	}


	/**
	 * This method validates all the columns of the logisticPartner and then append data to output file.
	 *
	 * @param deliveryMode
	 * @param priorityMaster
	 * @param logisticPartners
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	public void validateLogisticPartners(final String deliveryMode, final PriorityMaster priorityMaster,
			final ArrayList<LogisticPartner> logisticPartners, final FileWriter writer, final int rowNumber) throws IOException
	{

		for (final LogisticPartner logisticPartner : logisticPartners)
		{
			validateCod(priorityMaster, logisticPartner, writer, rowNumber);
			validatePrepaidLimit(priorityMaster, logisticPartner, writer, rowNumber);
			validateCodLimit(priorityMaster, logisticPartner, writer, rowNumber);
			validateCareatoNewzone(priorityMaster, logisticPartner, writer);
			validateFormRequired(priorityMaster, logisticPartner, writer, rowNumber);
			validatePickup(priorityMaster, logisticPartner, writer, rowNumber);
			assigningOfCodPriority(priorityMaster, logisticPartner, writer);
			assigningOfPrepaidPriority(priorityMaster, logisticPartner, writer);
			tshipExcelToCsvService.addLogisticPartnerData(deliveryMode, logisticPartner, priorityMaster);
		}
	}


	/**
	 * This method validates if logistic partner is present in priorities then corresponding logistic partner cod column
	 * should have yes, if not it would raise validation error in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateCod(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner, final FileWriter writer,
			final int rowNumber) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		if (logisticPartner.getIndex() >= surfaceModeStartingIndex)
		{
			if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4(), logisticPartner, writer,
						rowNumber);
			}
			else
			{
				transform(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1(),
						priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2(),
						priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3(),
						priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4(), logisticPartner);
			}
		} // if condition for surface mode validation of logisticPartners
		else
		{
			if (priorityMaster.getAirPriorityMaster().getAirCodPriority1().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority1(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority2().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority2(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority3().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority3().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority3(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority4().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority4(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority5().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority5().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority5(), logisticPartner, writer,
						rowNumber);
			}
			else
			{
				transform(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1(),
						priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2(),
						priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3(),
						priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4(),
						priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5(), logisticPartner);
			}
		}
	}


	/**
	 * This method validates if logistic partner is present in priorities then corresponding logistic partner cod limit
	 * should have value, if not it would raise validation error in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateCodLimit(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		if (logisticPartner.getIndex() >= surfaceModeStartingIndex)
		{
			if (!priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().isEmpty()
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1(), logisticPartner,
						writer, rowNumber);
			}
			else if (!priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2().isEmpty()
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2(), logisticPartner,
						writer, rowNumber);
			}
			else if (!priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3().isEmpty()
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3(), logisticPartner,
						writer, rowNumber);
			}
			else if (!priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4().isEmpty()
					&& priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4(), logisticPartner,
						writer, rowNumber);
			}
			else if (!logisticPartner.getName().equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4()))
			{
				if (StringUtils.isEmpty(logisticPartner.getCodLimit()) || !isNumeric(logisticPartner.getCodLimit().substring(0, logisticPartner.getCodLimit().length()-1))) // TISPRD-649: Non Numeric Check and Not Present in any  Priority
				{
					logisticPartner.setCodLimit("0.0" + TshipExcelConstants.ADD_COMMA);
				}
				else
				{
					logisticPartner.setCodLimit(logisticPartner.getCodLimit());
				}	
			}
		}
		else
		{
			if (priorityMaster.getAirPriorityMaster().getAirCodPriority1().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority1(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority2().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority2(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority3().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority3().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority3(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority4().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority4(), logisticPartner, writer,
						rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirCodPriority5().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirCodPriority5().equalsIgnoreCase(logisticPartner.getName()))
			{
				validateCodLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirCodPriority5(), logisticPartner, writer,
						rowNumber);
			}
			else if (!logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority1())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority2())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority3())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority4())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority5()))
			{
				if (StringUtils.isEmpty(logisticPartner.getCodLimit()) || !isNumeric(logisticPartner.getCodLimit().substring(0, logisticPartner.getCodLimit().length()-1))) // TISPRD-649: Non Numeric Check and Not Present in any  Priority
				{
					logisticPartner.setCodLimit("0.0" + TshipExcelConstants.ADD_COMMA);
				}
				else
				{
					logisticPartner.setCodLimit(logisticPartner.getCodLimit());
				}
			}
		}

	}

	/**
	 * This method validates if logistic partner is present in priorities then corresponding logistic partner prepaid
	 * limit should have value, if not it would raise validation error in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validatePrepaidLimit(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		if (logisticPartner.getIndex() >= surfaceModeStartingIndex)
		{
			if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1(),
						logisticPartner, writer, rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2(),
						logisticPartner, writer, rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3(),
						logisticPartner, writer, rowNumber);
			}
			else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4().length() > 1
					&& priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4(),
						logisticPartner, writer, rowNumber);
			}
			else if (!logisticPartner.getName()
					.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1())
					&& !logisticPartner.getName()
							.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2())
					&& !logisticPartner.getName()
							.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3())
					&& !logisticPartner.getName()
							.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4()))
			{
				if (StringUtils.isEmpty(logisticPartner.getPrepaidLimit()) || !isNumeric(logisticPartner.getPrepaidLimit().substring(0, logisticPartner.getPrepaidLimit().length()-1))) // TISPRD-649: Non Numeric Check , Not given in any  Priority
				{
					logisticPartner.setPrepaidLimit("0.0" + TshipExcelConstants.ADD_COMMA);
				}
				else
				{
					logisticPartner.setPrepaidLimit(logisticPartner.getPrepaidLimit());
				}
			}
		}
		else
		{
			if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1(), logisticPartner,
						writer, rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2(), logisticPartner,
						writer, rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3(), logisticPartner,
						writer, rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4(), logisticPartner,
						writer, rowNumber);
			}
			else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5().length() > 1
					&& priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5().equalsIgnoreCase(logisticPartner.getName()))
			{
				validatePrepaidLimitOfPriorities(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5(), logisticPartner,
						writer, rowNumber);
			}

			else if (!logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4())
					&& !logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5()))
			{
				if (StringUtils.isEmpty(logisticPartner.getPrepaidLimit()) || !isNumeric(logisticPartner.getPrepaidLimit().substring(0, logisticPartner.getPrepaidLimit().length()-1))) // TISPRD-649: Non Numeric Check , Not given in any  Priority
				{
					logisticPartner.setPrepaidLimit("0.0" + TshipExcelConstants.ADD_COMMA);
				}
				else
				{
					logisticPartner.setPrepaidLimit(logisticPartner.getPrepaidLimit());
				}
			}
		}
	}

	/**
	 * This method takes what ever the input in carea,cscrcd,cloctype,newzone these columns and write these to csv file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @throws IOException
	 */
	private void validateCareatoNewzone(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer) throws IOException
	{
		logisticPartner.setCarea(logisticPartner.getCarea());
		logisticPartner.setCscrcd(logisticPartner.getCscrcd());
		logisticPartner.setCloctype(logisticPartner.getCloctype());
		logisticPartner.setNewzone(logisticPartner.getNewzone());
	}



	/**
	 * This method takes what ever the column input is and display in csv file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateFormRequired(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		final String formReuired = logisticPartner.getFormRequired();
		//here i am adding two commas because return flag is removed in 2.3
		//so as return flag is blank so added extra comma to this.
		if (formReuired.equalsIgnoreCase("YES,,"))
		{
			logisticPartner.setFormRequired("Y,,");
		}
		else if (formReuired.equalsIgnoreCase("NO,,"))
		{
			logisticPartner.setFormRequired("N,,");
		}
		else if (formReuired.equals(",,"))
		{
			logisticPartner.setFormRequired("N,,");
		}
		else if (!formReuired.equalsIgnoreCase("YES,") && !formReuired.equalsIgnoreCase("NO,") && formReuired.length() > 1)
		{
			writer.write(
					TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
							+ logisticPartner.getName() + TshipExcelConstants.FORM_REQUIRED_VALIDATION + TshipExcelConstants.NEW_LINE);
		}
	}



	/**
	 * This method takes input from pickup and writes into the csv file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param row
	 * @throws IOException
	 */
	private void validatePickup(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		final String pickUp = logisticPartner.getPickUp();
		if (pickUp.equalsIgnoreCase("YES,"))
		{
			logisticPartner.setPickUp("Y,");
		}
		else if (pickUp.equalsIgnoreCase("NO,"))
		{
			logisticPartner.setPickUp("N,");
		}
		else if (pickUp.equals(","))
		{
			logisticPartner.setPickUp("Y,");
		}
		else if (!pickUp.equalsIgnoreCase("YES,") && !pickUp.equalsIgnoreCase("NO,") && pickUp.length() > 1)
		{
			writer.write(
					TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
							+ logisticPartner.getName() + TshipExcelConstants.PICKUP_VALIDATION + TshipExcelConstants.NEW_LINE);
		}
		logisticPartner.setPickUp(logisticPartner.getPickUp());
	}
	//}

	/**
	 *
	 * This method checks priority names and assign p001 for priority1 logistic partner and p002 for priority2 logistic
	 * partner. for eg: codPriority1 : GATI and codPriority2 is FEDEX. so in out put file while wrting the data of GATI
	 * CodPriority column will have P001 and Fedex will have P002.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @throws IOException
	 */
	private void assigningOfCodPriority(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		if (logisticPartner.getIndex() < surfaceModeStartingIndex)
		{
			if (priorityMaster.getAirPriorityMaster().getAirCodPriority1().length() > 1)
			{
				if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority1()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY1);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority2()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY2);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority3()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY3);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority4()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY4);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirCodPriority5()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY5);
				}
				else
				{
					logisticPartner.setCodPriority(TshipExcelConstants.ADD_COMMA);
				}
			} //if condition
			else
			{
				logisticPartner.setCodPriority(TshipExcelConstants.ADD_COMMA);
			}
		} // if condition for surface mode
		else
		{
			if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().length() > 1)
			{
				if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY1);

				} //if condition for checking priority equal or not with logisticpartner name
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY2);
				}
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY3);

				} //if condition for checking priority equal or not with logisticpartner name
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4()))
				{
					logisticPartner.setCodPriority(TshipExcelConstants.CONST_PRIORITY4);
				}
				else
				{
					logisticPartner.setCodPriority(TshipExcelConstants.ADD_COMMA);
				}
			} // if condition
			else
			{
				logisticPartner.setCodPriority(TshipExcelConstants.ADD_COMMA);
			}
		} //else
	}

	/**
	 *
	 * This method checks priority names and assign p001 for priority1 logistic partner and p002 for priority2 logistic
	 * partner. for eg: prepaidPriority1 : GATI and prepaidPriority2 is FEDEX. so in out put file while wrting the data
	 * of GATI prepaidPriority column will have P001 and Fedex will have P002.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @throws IOException
	 */
	private void assigningOfPrepaidPriority(final PriorityMaster priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		if (logisticPartner.getIndex() < surfaceModeStartingIndex)
		{
			if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1().length() > 1)
			{
				if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY1);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY2);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY3);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY4);
				}
				else if (logisticPartner.getName().equalsIgnoreCase(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY5);
				}
				else
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.ADD_COMMA);
				}
			} // if condition
			else
			{
				logisticPartner.setPrepaidPriority(TshipExcelConstants.ADD_COMMA);
			}
		} // if condition for surface mode
		else
		{
			if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1().length() > 1)
			{
				if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY1);

				} //if condition for checking priority equal or not with logisticpartner name
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY2);
				}
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY3);

				}
				else if (logisticPartner.getName()
						.equalsIgnoreCase(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4()))
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.CONST_PRIORITY4);
				}

				else
				{
					logisticPartner.setPrepaidPriority(TshipExcelConstants.ADD_COMMA);
				}
			} // if condition
			else
			{
				logisticPartner.setPrepaidPriority(TshipExcelConstants.ADD_COMMA);
			}
		} //else
	}


	/**
	 *
	 * In this method if priorities have logistic partner then corresponding logistic partner should have value, else it
	 * will raise validation in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validatePrepaidLimitOfPriorities(final String priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		if (logisticPartner.getName().equalsIgnoreCase(priorityMaster))
		{
			final String prepaidLimit = logisticPartner.getPrepaidLimit();
			if (!prepaidLimit.equals(","))
			{
				final String pLimit = prepaidLimit.substring(0, prepaidLimit.length() - 1);
				final boolean result = isNumeric(pLimit);
				// this condition is used to handle only numeric values.
				if (result)
				{
					final double prepaidLimitRes = Double.parseDouble(pLimit);
					// this condition is for not allowing negitive numbers
					if (prepaidLimitRes > 0)
					{
						logisticPartner.setPrepaidLimit(prepaidLimitRes + ",");
					} // if condition
					else
					{
						writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
								+ TshipExcelConstants.EMPTY_SPACE + logisticPartner.getName()
								+ TshipExcelConstants.PREPAID_LIMIT_MANDITORY + TshipExcelConstants.NEW_LINE);
					}
				} // if condition
				else
				{
					writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
							+ TshipExcelConstants.EMPTY_SPACE + logisticPartner.getName() + TshipExcelConstants.PREPAID_LIMIT_MANDITORY
							+ TshipExcelConstants.NEW_LINE);
				}
			} // if condition
			else
			{
				writer.write(
						TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
								+ logisticPartner.getName() + TshipExcelConstants.PREPAID_LIMIT_MANDITORY + TshipExcelConstants.NEW_LINE);
			}
		} //if condition for checking priority equal or not with logisticpartner name
	}



	/**
	 *
	 * In this method if priorities have logistic partner then corresponding logistic partner should have value yes, else
	 * it will raise validation in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateCodOfPriorities(final String priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		if (logisticPartner.getName().equalsIgnoreCase(priorityMaster))
		{
			final String cod = logisticPartner.getCod();
			if (cod.equalsIgnoreCase("YES,"))
			{
				logisticPartner.setCod("Y,");
			}
			else if (!cod.equalsIgnoreCase("YES,"))
			{
				writer.write(
						TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
								+ logisticPartner.getName() + TshipExcelConstants.COD_COLUMN_VALIDATION + TshipExcelConstants.NEW_LINE);
			}
		} //if condition for checking priority equal or not with logisticpartner name

	}

	/**
	 *
	 * In this method if priorities have logistic partner then corresponding logistic partner should have value, else it
	 * will raise validation in validation file.
	 *
	 * @param priorityMaster
	 * @param logisticPartner
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateCodLimitOfPriorities(final String priorityMaster, final LogisticPartner logisticPartner,
			final FileWriter writer, final int rowNumber) throws IOException
	{
		if (logisticPartner.getName().equalsIgnoreCase(priorityMaster))
		{
			final String codLimit = logisticPartner.getCodLimit();
			if (!codLimit.equals(","))
			{
				final String cLimit = codLimit.substring(0, codLimit.length() - 1);
				final boolean result = isNumeric(cLimit);
				// this condition is used to handle only numeric values.
				if (result)
				{
					final double codLimitRes = Double.parseDouble(cLimit);
					//this condition is for not allowing negitive numbers
					if (codLimitRes > 0)
					{
						logisticPartner.setCodLimit(codLimitRes + ",");
					} // if condition
					else
					{
						writer.write(TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber
								+ TshipExcelConstants.EMPTY_SPACE + logisticPartner.getName() + TshipExcelConstants.COD_LIMIT_MANDITORY
								+ TshipExcelConstants.NEW_LINE);
					}
				} // if condition
				else
				{
					writer.write(
							TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
									+ logisticPartner.getName() + TshipExcelConstants.COD_LIMIT_MANDITORY + TshipExcelConstants.NEW_LINE);
				}
			} // if condition
			else
			{
				writer.write(
						TshipExcelConstants.ROW_VALUE + TshipExcelConstants.EMPTY_SPACE + rowNumber + TshipExcelConstants.EMPTY_SPACE
								+ logisticPartner.getName() + TshipExcelConstants.COD_LIMIT_MANDITORY + TshipExcelConstants.NEW_LINE);
			}
		} //if condition for checking priority equal or not with logisticpartner name
	}

	/*	*//**
		   *
		   * This method compares result of the cod limit and assigns the lesser value.
		   *
		   * @param logisticPartner1
		   * @param logisticPartner2
		   * @return
		   */
	/*
	 * private String getMinValueForCOD(final LogisticPartner logisticPartner1,final LogisticPartner logisticPartner2){
	 * String result=""; //System.out.println("l"+logisticPartner1+" 2 "+logisticPartner2); if(logisticPartner1!=null &&
	 * logisticPartner2!=null){ final String codValue1= logisticPartner1.getCodLimit().substring(0,
	 * logisticPartner1.getCodLimit().length()-1); final String codValue2= logisticPartner2.getCodLimit().substring(0,
	 * logisticPartner2.getCodLimit().length()-1);
	 *
	 * if(!codValue1.isEmpty() && !codValue2.isEmpty()){ final double result1=Double.valueOf(codValue1);
	 *
	 * final double result2=Double.valueOf(codValue2); if(result1==result2){ result=result1+ExcelConstants.ADD_COMMA; }
	 * else if(result1>result2){ result=result2+ExcelConstants.ADD_COMMA; } else if(result1<result2){
	 * result=result1+ExcelConstants.ADD_COMMA; } else{ result=""+ExcelConstants.ADD_COMMA; } } } return result;
	 *
	 * }
	 *
	 *//**
	   * This method compares result of the prepaid limit and assigns the lesser value.
	   *
	   * @param logisticPartner1
	   * @param logisticPartner2
	   * @return
	   */
	/*
	 * private String getMinValueForPrepaid(final LogisticPartner logisticPartner1,final LogisticPartner
	 * logisticPartner2){ String result="";
	 *
	 * if(logisticPartner1!=null && logisticPartner2!=null){ final String codValue1=
	 * logisticPartner1.getPrepaidLimit().substring(0, logisticPartner1.getPrepaidLimit().length()-1); final String
	 * codValue2= logisticPartner2.getPrepaidLimit().substring(0, logisticPartner2.getPrepaidLimit().length()-1);;
	 * if(!codValue1.isEmpty() && !codValue2.isEmpty()){ final double result1=Double.valueOf(codValue1);
	 *
	 * final double result2=Double.valueOf(codValue2); if(result1==result2){ result=result1+ExcelConstants.ADD_COMMA; }
	 * else if(result1>result2){ result=result2+ExcelConstants.ADD_COMMA; } else if(result1<result2){
	 * result=result1+ExcelConstants.ADD_COMMA; } else{ result=""+ExcelConstants.ADD_COMMA; } } } return result;
	 *
	 * }
	 *
	 *//**
	   *
	   * @param logisticPartnerName
	   * @param logisticPartners
	   * @return
	   *//*
		  * private LogisticPartner getLogisticPartnerByName(final String logisticPartnerName,final List<LogisticPartner>
		  * logisticPartners){ LogisticPartner lp=null; for(final LogisticPartner partner : logisticPartners){
		  * //System.out.println("par"+partner); if(partner.getName().equalsIgnoreCase(logisticPartnerName)){ lp=partner;
		  * break; } } return lp; }
		  */

	/**
	 *
	 * @param value
	 * @return
	 */
	public boolean isNumeric(final String value)
	{
		return value.matches("[-+]?\\d*\\.?\\d+");
	}

	/**
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param logisticPartner
	 */
	private void transform(final String logisticPartner1, final String logisticPartner2, final String logisticPartner3,
			final String logisticPartner4, final String logisticPartner5, final LogisticPartner logisticPartner)
	{
		if (logisticPartner.getName().equalsIgnoreCase(logisticPartner1)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner2)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner3)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner4)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner5))
		{
			final String cod = logisticPartner.getCod();
			if (cod.equalsIgnoreCase("YES,"))
			{
				logisticPartner.setCod("Y,");
			}
			else if (cod.equalsIgnoreCase("NO,"))
			{
				logisticPartner.setCod("N,");
			}
			else
			{
				logisticPartner.setCod("N,");
			}
		}
	}

	/**
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param logisticPartner
	 */
	private void transform(final String logisticPartner1, final String logisticPartner2, final String logisticPartner3,
			final String logisticPartner4, final LogisticPartner logisticPartner)
	{
		if (logisticPartner.getName().equalsIgnoreCase(logisticPartner1)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner2)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner3)
				|| logisticPartner.getName().equalsIgnoreCase(logisticPartner4))
		{
			final String cod = logisticPartner.getCod();
			if (cod.equalsIgnoreCase("YES,"))
			{
				logisticPartner.setCod("Y,");
			}
			else if (cod.equalsIgnoreCase("NO,"))
			{
				logisticPartner.setCod("N,");
			}
			else
			{
				logisticPartner.setCod("N,");
			}
		}
	}

	//end

}


