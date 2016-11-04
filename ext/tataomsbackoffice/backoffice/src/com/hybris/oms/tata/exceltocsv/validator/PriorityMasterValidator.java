package com.hybris.oms.tata.exceltocsv.validator;

import java.io.FileWriter;
import java.io.IOException;

import com.hybris.oms.tata.exceltocsv.util.ExcelConstants;
import com.hybris.oms.tata.exceltocsv.util.LogisticPartnerUtil;
import com.hybris.oms.tata.pincodesupload.pojo.PriorityMaster;


/**
 *
 * This method validates the data of priorities, if this priorities fails to have validation check then it will raise
 * validation in validation file.
 *
 */
public class PriorityMasterValidator
{
	/**
	 * This method validates priority master data. it returns true if all the priorities matches validation rules, else
	 * it will throw exception with respective message.
	 *
	 * @param priorityMaster
	 * @param writer
	 * @param rowNumber
	 * @return
	 * @throws IOException
	 */
	public boolean validate(final PriorityMaster priorityMaster, final FileWriter writer, final int rowNumber) throws IOException
	{
		boolean result = false;
		validateAirAndSurfacePrepaidPriority1(priorityMaster, writer, rowNumber);
		validatePrioritiesAir(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5(), ExcelConstants.PREPAID_PRIORITY1_ISEMPTY_AIR, writer,
				rowNumber);

		validatePrioritiesAir(priorityMaster.getAirPriorityMaster().getAirCodPriority1(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority2(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority3(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority4(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority5(), ExcelConstants.COD_PRIORITY1_ISEMPTY_AIR, writer,
				rowNumber);

		validatePrioritiesSur(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4(),
				ExcelConstants.PREPAID_PRIORITY1_ISEMPTY_SURFACE, writer, rowNumber);

		validatePrioritiesSur(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4(), ExcelConstants.COD_PRIORITY1_ISEMPTY_SURFACE,
				writer, rowNumber);

		checkPrioritiesforEqualityAir(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4(),
				priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5(), ExcelConstants.PREPAID_PRIORITY1AND2_SAME_AIR, writer,
				rowNumber);
		checkPrioritiesforEqualityAir(priorityMaster.getAirPriorityMaster().getAirCodPriority1(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority2(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority3(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority4(),
				priorityMaster.getAirPriorityMaster().getAirCodPriority5(), ExcelConstants.COD_PRIORITY1AND2_SAME_AIR, writer,
				rowNumber);
		checkPrioritiesforEqualitySur(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3(),
				priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4(),
				ExcelConstants.PREPAID_PRIORITY1AND2_SAME_SURFACE, writer, rowNumber);
		checkPrioritiesforEqualitySur(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3(),
				priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4(), ExcelConstants.COD_PRIORITY1AND2_SAME_SURFACE,
				writer, rowNumber);
		if (validateAirAndSurfacePriorities(priorityMaster, writer, rowNumber))
		{
			result = true;
		}
		return result;
	}

	/**
	 * if any one of the logistic partner name is not matched with these GATI,BLUEDART,GOJAVAS,ROADRUNNR,FEDEX name, it
	 * throws validation exception with respective message.
	 *
	 * @param priorityMaster
	 * @return
	 * @throws IOException
	 */
	private boolean validateAirAndSurfacePriorities(final PriorityMaster priorityMaster, final FileWriter writer,
			final int rowNumber) throws IOException
	{
		boolean result = false;
		validateAirPrepaidPriorities(priorityMaster, writer, rowNumber);
		validateAirCodPriorities(priorityMaster, writer, rowNumber);
		validateSurPrepaidPriorities(priorityMaster, writer, rowNumber);
		result = validateSurCodPriorities(priorityMaster, writer, rowNumber, result);
		return result;
	}

	/**
	 * if priority1_prepaid_air is empty and priority1_prepaid_surface is empty,throws validation exception with
	 * respective message.
	 *
	 * @param priorityMaster
	 * @return
	 * @throws IOException
	 */
	private void validateAirAndSurfacePrepaidPriority1(final PriorityMaster priorityMaster, final FileWriter writer,
			final int rowNumber) throws IOException
	{
		if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1().length() == 1
				&& priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1().length() == 1)
		{
			writer.write((ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY1_AIR_OR_SURFACE_ISEMPTY + ExcelConstants.NEW_LINE));
		}

	}

	/**
	 *
	 * if logisticPartner1 is empty and logisticPartner2 to logisticPartner5 has one of the logisticPartner name then it
	 * will throw validation exception with respective message.
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param logisticPartner3
	 * @param logisticPartner4
	 * @param logisticPartner5
	 * @param validationMessage
	 * @param writer
	 * @param rowNumber
	 * @return
	 * @throws IOException
	 */
	private void validatePrioritiesAir(final String logisticPartner1, final String logisticPartner2, final String logisticPartner3,
			final String logisticPartner4, final String logisticPartner5, final String validationMessage, final FileWriter writer,
			final int rowNumber) throws IOException
	{
		if (logisticPartner1 != null && logisticPartner2 != null && logisticPartner3 != null && logisticPartner4 != null
				&& logisticPartner5 != null)
		{
			if (logisticPartner1.length() == 1 && (logisticPartner2.length() > 1 || logisticPartner3.length() > 1
					|| logisticPartner4.length() > 1 || logisticPartner5.length() > 1))
			{
				writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
						+ validationMessage + ExcelConstants.NEW_LINE);
			}
		}
	}

	/**
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param logisticPartner3
	 * @param logisticPartner4
	 * @param validationMessage
	 * @param writer
	 * @param rowNumber
	 * @return
	 * @throws IOException
	 */
	private void validatePrioritiesSur(final String logisticPartner1, final String logisticPartner2, final String logisticPartner3,
			final String logisticPartner4, final String validationMessage, final FileWriter writer, final int rowNumber)
			throws IOException
	{
		if (logisticPartner1 != null && logisticPartner2 != null && logisticPartner3 != null && logisticPartner4 != null)
		{
			if (logisticPartner1.length() == 1
					&& (logisticPartner2.length() > 1 || logisticPartner3.length() > 1 || logisticPartner4.length() > 1))
			{
				writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
						+ validationMessage + ExcelConstants.NEW_LINE);
			}
		}
	}


	/**
	 *
	 * if any of the prepaid/cod priorities in air have same logistic partner name then it will throw validation
	 * exception with respective message.
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param validationMessage
	 * @return
	 * @throws IOException
	 */
	private void checkPrioritiesforEqualityAir(final String logisticPartner1, final String logisticPartner2,
			final String logisticPartner3, final String logisticPartner4, final String logisticPartner5,
			final String validationMessage, final FileWriter writer, final int rowNumber) throws IOException
	{
		if (logisticPartner1 != null && logisticPartner2 != null && logisticPartner1 != null && logisticPartner2 != null
				&& logisticPartner5 != null)
		{
			if ((logisticPartner1.equalsIgnoreCase(logisticPartner2) && logisticPartner1.length() > 1)
					|| (logisticPartner1.equalsIgnoreCase(logisticPartner3) && logisticPartner1.length() > 1)
					|| (logisticPartner1.equalsIgnoreCase(logisticPartner4) && logisticPartner1.length() > 1)
					|| (logisticPartner1.equalsIgnoreCase(logisticPartner5) && logisticPartner1.length() > 1)
					|| (logisticPartner2.equalsIgnoreCase(logisticPartner3) && logisticPartner2.length() > 1)
					|| (logisticPartner2.equalsIgnoreCase(logisticPartner4) && logisticPartner2.length() > 1)
					|| (logisticPartner2.equalsIgnoreCase(logisticPartner5) && logisticPartner2.length() > 1)
					|| (logisticPartner3.equalsIgnoreCase(logisticPartner4) && logisticPartner3.length() > 1)
					|| (logisticPartner3.equalsIgnoreCase(logisticPartner5) && logisticPartner3.length() > 1)
					|| (logisticPartner4.equalsIgnoreCase(logisticPartner5) && logisticPartner4.length() > 1))
			{
				writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
						+ validationMessage + ExcelConstants.NEW_LINE);
			}
			/*
			 * List<String> logisticPartners = new ArrayList<String>(); logisticPartners.add(logisticPartner1);
			 * logisticPartners.add(logisticPartner2); logisticPartners.add(logisticPartner3);
			 * logisticPartners.add(logisticPartner4); logisticPartners.add(logisticPartner5); boolean
			 * res=findDuplicates(logisticPartners); if(res){
			 * writer.write(ExcelConstants.ROW_VALUE+ExcelConstants.EMPTY_SPACE+rowNumber+ExcelConstants.EMPTY_SPACE+
			 * validationMessage+ExcelConstants.NEW_LINE); }
			 */
		}
	}


	/**
	 * if any of the prepaid/cod priorities in sur have same logistic partner name then it will throw validation
	 * exception with respective message.
	 *
	 * @param logisticPartner1
	 * @param logisticPartner2
	 * @param logisticPartner3
	 * @param logisticPartner4
	 * @param validationMessage
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void checkPrioritiesforEqualitySur(final String logisticPartner1, final String logisticPartner2,
			final String logisticPartner3, final String logisticPartner4, final String validationMessage, final FileWriter writer,
			final int rowNumber) throws IOException
	{
		if (logisticPartner1 != null && logisticPartner2 != null)
		{
			if ((logisticPartner1.equalsIgnoreCase(logisticPartner2) && logisticPartner1.length() > 1)
					|| (logisticPartner1.equalsIgnoreCase(logisticPartner3) && logisticPartner1.length() > 1)
					|| (logisticPartner1.equalsIgnoreCase(logisticPartner4) && logisticPartner1.length() > 1)
					|| (logisticPartner2.equalsIgnoreCase(logisticPartner3) && logisticPartner2.length() > 1)
					|| (logisticPartner2.equalsIgnoreCase(logisticPartner4) && logisticPartner2.length() > 1)
					|| (logisticPartner3.equalsIgnoreCase(logisticPartner4) && logisticPartner3.length() > 1))
			{
				writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
						+ validationMessage + ExcelConstants.NEW_LINE);
			}
		}
	}


	/**
	 *
	 * @param priorityMaster
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateAirPrepaidPriorities(final PriorityMaster priorityMaster, final FileWriter writer, final int rowNumber)
			throws IOException
	{
		if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority1()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY1_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority2()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY2_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority3()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY3_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority4()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY4_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirPrepaidPriority5()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY5_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
	}

	/**
	 *
	 * @param priorityMaster
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateAirCodPriorities(final PriorityMaster priorityMaster, final FileWriter writer, final int rowNumber)
			throws IOException
	{
		if (priorityMaster.getAirPriorityMaster().getAirCodPriority1().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirCodPriority1()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY1_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirCodPriority2().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirCodPriority2()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY2_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirCodPriority3().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirCodPriority3()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY3_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirCodPriority4().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirCodPriority4()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY4_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getAirPriorityMaster().getAirCodPriority5().length() > 1
				&& !LogisticPartnerUtil.containsLogisticPartnerName(priorityMaster.getAirPriorityMaster().getAirCodPriority5()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY5_NAME_VALIDATION_AIR + ExcelConstants.NEW_LINE);
		}
	}

	/**
	 *
	 * @param priorityMaster
	 * @param writer
	 * @param rowNumber
	 * @throws IOException
	 */
	private void validateSurPrepaidPriorities(final PriorityMaster priorityMaster, final FileWriter writer, final int rowNumber)
			throws IOException
	{
		if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority1()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY1_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority2()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY2_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority3()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY3_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfacePrepaidPriority4()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.PREPAID_PRIORITY4_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
	}

	/**
	 *
	 * @param priorityMaster
	 * @param writer
	 * @param rowNumber
	 * @param result
	 * @return
	 * @throws IOException
	 */
	private boolean validateSurCodPriorities(final PriorityMaster priorityMaster, final FileWriter writer, final int rowNumber,
			boolean result) throws IOException
	{
		if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority1()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY1_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority2()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY2_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority3()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY3_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else if (priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4().length() > 1 && !LogisticPartnerUtil
				.containsLogisticPartnerName(priorityMaster.getSurfacePriorityMaster().getSurfaceCodPriority4()))
		{
			writer.write(ExcelConstants.ROW_VALUE + ExcelConstants.EMPTY_SPACE + rowNumber + ExcelConstants.EMPTY_SPACE
					+ ExcelConstants.COD_PRIORITY4_NAME_VALIDATION_SURFACE + ExcelConstants.NEW_LINE);
		}
		else
		{
			result = true;
		}
		return result;
	}



	//end
}