/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.data.SellerPriorityReportData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityReportDAO;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityReportService;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 * @description CronJob to generate Audit log for Seller Priority
 *
 */
public class MplSellerPriorityReportServiceImpl implements MplSellerPriorityReportService
{
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static Date lastStartDate = null;
	private static Date lastEndDate = null;

	//CSV file header
	private static final String FILE_HEADER = MarketplacecommerceservicesConstants.CSVFILEHEADER_SELLERPRIORITY;

	@Autowired
	private MplSellerPriorityReportDAO mplSellerPriorityReportDAO;

	@Autowired
	private ConfigurationService configurationService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplSellerPriorityReportServiceImpl.class.getName());

	/**
	 * It gets the list of Seller Priority Changed Details with in date range
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getSellerPriorityDetails(final Date startDate, final Date endDate)
	{
		//Get the list of Seller Priority Changed Details with in date renge
		return getMplSellerPriorityReportDAO().getSellerPriorityDetails(startDate, endDate);
	}

	/**
	 * It gets the list of Seller Priority Changed Details with in date renge
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getAllSellerPriorityDetails()
	{
		//Get the list of Seller Priority Changed Details with no date range
		return getMplSellerPriorityReportDAO().getAllSellerPriorityDetails();
	}

	/**
	 * This Method fetches all modified details in SellerPriority Model within the prescribed date Range
	 *
	 * @param startDate
	 * @param endDate
	 */
	@Override
	public void fetchSpecificDetails(final Date startDate, final Date endDate)
	{
		LOG.debug("fetchSpecificDetails : startDate: " + startDate + "   endDate: " + endDate);
		try
		{
			final List<SavedValuesModel> savedChangedValues = getSellerPriorityDetails(startDate, endDate);

			if (CollectionUtils.isNotEmpty(savedChangedValues))
			{
				//put SellerPriority Changed and Modified data in the POJO class
				putDataForSellerPriority(savedChangedValues);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}

	}

	/**
	 * This Method fetches all details in SellerPriority Model
	 *
	 */
	@Override
	public void fetchAllDetails()
	{
		final List<SavedValuesModel> savedChangedValues = getAllSellerPriorityDetails();
		try
		{
			if (CollectionUtils.isNotEmpty(savedChangedValues))
			{
				//put SellerPriority Changed and Modified data in the POJO class
				putDataForSellerPriority(savedChangedValues);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}

	}

	/**
	 * This method takes the list of SellerPriority Created and Modified data in the POJO class which is in turn set in
	 * the CSV file to be generated in a specified location
	 *
	 * @param savedValuesList
	 */
	void putDataForSellerPriority(final List<SavedValuesModel> savedValuesList)
	{
		final List<SellerPriorityReportData> savedValueDataList = new ArrayList<SellerPriorityReportData>();

		final List<SavedValuesModel> savedValues = new ArrayList<SavedValuesModel>(savedValuesList);

		final ArrayList<String> modItemsList = new ArrayList<String>();
		try
		{
			for (final SavedValuesModel savedValmodItems : savedValues)
			{
				if (null != savedValmodItems.getModifiedItem()
						&& (modItemsList.isEmpty() || !modItemsList.contains(savedValmodItems.getModifiedItem().toString())))
				{
					// Addinf all the modified date in a arraylist
					modItemsList.add(savedValmodItems.getModifiedItem().toString());

				}
			}

			if (CollectionUtils.isNotEmpty(modItemsList))
			{
				LOG.debug("fetchSpecificDetails :" + modItemsList);
				for (final String modItem : modItemsList)
				{
					// Global variable
					setLastStartDate(null);
					setLastEndDate(null);
					// method to get ListOfSellerPrioritydata
					getListOfSellerPriorityReport(savedValuesList, modItem, savedValueDataList);
				}
			}

			//Write the Seller Priority Report data in csv file
			fileWriteReport(savedValueDataList);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
	}

	/**
	 * private method to get ListOfSellerPrioritydata
	 *
	 * @param savedValues
	 * @param modItem
	 * @return List<SellerPriorityReportData>
	 */
	private List<SellerPriorityReportData> getListOfSellerPriorityReport(final List<SavedValuesModel> savedValues,
			final String modItem, final List<SellerPriorityReportData> savedValueDataList)
	{
		try
		{
			// iterating Sellerpriority data
			for (final SavedValuesModel savedVal : savedValues)
			{
				//SavedData from SavedValue and Saved Value Entries
				getSavedDataFromSubMethod(modItem, savedVal, savedValueDataList);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		return savedValueDataList;
	}

	/**
	 * SavedData from SavedValue and Saved Value Entries
	 *
	 * @param modItem
	 * @param savedVal
	 * @return SellerPriorityReportData
	 */
	private List<SellerPriorityReportData> getSavedDataFromSubMethod(final String modItem, final SavedValuesModel savedVal,
			final List<SellerPriorityReportData> savedValueDataList)
	{
		try
		{
			if (null != savedVal.getModifiedItem() && modItem.equalsIgnoreCase(savedVal.getModifiedItem().toString()))
			{
				final SellerPriorityReportData savedValueData = new SellerPriorityReportData();
				LOG.debug("savedValuesEntries size: " + savedVal.getSavedValuesEntries().size());
				// Modified / Created Time
				if (null != savedVal.getTimestamp())
				{
					savedValueData.setModifiedTime(savedVal.getTimestamp());
				}

				// Modified / Created Entry details
				final MplSellerPriorityModel sellerModData = (MplSellerPriorityModel) savedVal.getModifiedItem();

				// Modified / Created SellerID

				if (null != sellerModData.getSellerId() && null != sellerModData.getSellerId().getId())
				{
					LOG.debug("SellerID: " + sellerModData.getSellerId());
					savedValueData.setSellerId(sellerModData.getSellerId().getId());
					if (null != sellerModData.getSellerId().getFirstname())
					{
						savedValueData.setSellerName(sellerModData.getSellerId().getFirstname());
					}
				}

				// Modified / Created CategoryID
				if (null != sellerModData.getCategoryId() && null != sellerModData.getCategoryId().getCode())
				{
					savedValueData.setCategoryId(sellerModData.getCategoryId().getCode());
				}

				// Modified / Created Product ID

				if (null != sellerModData.getListingId() && null != sellerModData.getListingId().getCode())
				{
					savedValueData.setProductId(sellerModData.getListingId().getCode());
				}

				// Activation status of Priority
				savedValueData.setIsActive(sellerModData.getIsActive().booleanValue() ? "Y" : "N");


				// Priority Start Date
				if (null != sellerModData.getPriorityStartDate())
				{
					savedValueData.setOldStartDate(sellerModData.getPriorityStartDate());
				}

				// Priority End Date

				if (null != sellerModData.getPriorityEndDate())
				{
					savedValueData.setOldEndDate(sellerModData.getPriorityEndDate());
				}

				// User
				if (null != savedVal.getUser() && null != savedVal.getUser().getName())
				{
					savedValueData.setChangedBy(savedVal.getUser().getName());
				}
				//set value according to the Modified Type in SavedVale Model (Created / Changed)
				checkModifiedType(savedVal, savedValueData);

				savedValueDataList.add(savedValueData);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		return savedValueDataList;
	}

	/**
	 * private method to set value according to the Modified Type in SavedVale Model (Created / Changed)
	 *
	 * @param savedVal
	 * @param savedValueData
	 * @return SellerPriorityReportData
	 */
	private final SellerPriorityReportData checkModifiedType(final SavedValuesModel savedVal,
			final SellerPriorityReportData savedValueData)
	{
		// Modified Data
		try
		{
			if (null != savedVal.getModificationType() && null != savedVal.getModificationType().getCode()
					&& null != savedVal.getSavedValuesEntries())
			{
				LOG.debug("ModificationType: " + savedVal.getModificationType().getCode());
				if (savedVal.getModificationType().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANGED))
				{
					//ModifienType of Saved Value is Changed
					modifiedTypeChanged(savedValueData, savedVal);
				}
				else
				{
					// ModifienType of Saved Value is Created
					modifiedTypeCreated(savedValueData, savedVal);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		return savedValueData;
	}


	/**
	 * ModifienType of Saved Value is Changed
	 *
	 * @param savedValueData
	 * @param savedVal
	 * @return SellerPriorityReportData
	 */
	private SellerPriorityReportData modifiedTypeChanged(final SellerPriorityReportData savedValueData,
			final SavedValuesModel savedVal)
	{
		try
		{
			//Modified
			savedValueData.setNewlyCreated("N");
			for (final SavedValueEntryModel savedValueEntry : savedVal.getSavedValuesEntries())
			{
				LOG.debug("ModifiedAttribute: " + null != savedValueEntry.getModifiedAttribute() ? savedValueEntry
						.getModifiedAttribute() : "");
				// Modified Attribute Priority Start Date
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(
								MarketplacecommerceservicesConstants.PRIORITYSTARTDATE))
				{
					if (null != savedValueEntry.getOldValue())
					{
						savedValueData.setOldStartDate((Date) savedValueEntry.getOldValue());
						// Store Last Changed Start Date
						setLastStartDate((Date) savedValueEntry.getOldValue());
					}
					if (null != savedValueEntry.getNewValue())
					{
						savedValueData.setModifiedStartDate((Date) savedValueEntry.getNewValue());
					}

				}
				// Modified Attribute Priority End Date
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.PRIORITYENDDATE))
				{
					if (null != savedValueEntry.getOldValue())
					{
						savedValueData.setOldEndDate((Date) savedValueEntry.getOldValue());
						// Store Last Changed End Date
						setLastEndDate((Date) savedValueEntry.getOldValue());
					}
					if (null != savedValueEntry.getNewValue())
					{
						savedValueData.setModifiedEndDate((Date) savedValueEntry.getNewValue());
					}
				}
				// Modified Attribute Active Flag
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(MarketplacecommerceservicesConstants.ISACTIVE))
				{
					if (savedValueEntry.getNewValue().toString().equalsIgnoreCase("true"))
					{
						savedValueData.setModifiedActiveFlag(MarketplacecommerceservicesConstants.ACTIVATE);
					}
					else
					{
						savedValueData.setModifiedActiveFlag(MarketplacecommerceservicesConstants.DEACTIVATED);
					}
				}
				// to display the proper start date and end date at the time of attribute modification
				if (null != getLastStartDate())
				{
					savedValueData.setOldStartDate(getLastStartDate());
				}
				if (null != getLastEndDate())
				{
					savedValueData.setOldEndDate(getLastEndDate());
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		return savedValueData;
	}


	/**
	 * ModifienType of Saved Value is Created
	 *
	 * @param savedValueData
	 * @param savedVal
	 * @return SellerPriorityReportData
	 */
	private SellerPriorityReportData modifiedTypeCreated(final SellerPriorityReportData savedValueData,
			final SavedValuesModel savedVal)
	{
		try
		{
			// Created Data
			savedValueData.setNewlyCreated("Y");
			for (final SavedValueEntryModel savedValueEntry : savedVal.getSavedValuesEntries())
			{
				LOG.debug("ModifiedAttribute: " + savedValueEntry.getModifiedAttribute());
				// Priority Creation Start date
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(
								MarketplacecommerceservicesConstants.PRIORITYSTARTDATE) && null != savedValueEntry.getNewValue())
				{
					savedValueData.setOldStartDate((Date) savedValueEntry.getNewValue());
				}
				// Priority Creation End date
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.PRIORITYENDDATE)
						&& null != savedValueEntry.getNewValue())
				{
					savedValueData.setOldEndDate((Date) savedValueEntry.getNewValue());
				}

				// Modified / Created SellerID
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(MarketplacecommerceservicesConstants.SELLERID))
				{
					final SellerMasterModel sellerDataVal = (SellerMasterModel) savedValueEntry.getNewValue();
					if (null != sellerDataVal.getId())
					{
						savedValueData.setSellerId(sellerDataVal.getId());
					}
					if (null != sellerDataVal.getFirstname())
					{
						savedValueData.setSellerName(sellerDataVal.getFirstname());
					}
				}
				// Modified / Created CategoryID
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(MarketplacecommerceservicesConstants.CATEGORYID))
				{
					final CategoryModel catData = (CategoryModel) savedValueEntry.getNewValue();
					if (null != catData.getCode())
					{
						savedValueData.setCategoryId(catData.getCode());
					}
				}

				// Modified / Created Product ID
				if (null != savedValueEntry.getModifiedAttribute()
						&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase(MarketplacecommerceservicesConstants.LISTINGID))
				{
					final ProductModel prodData = (ProductModel) savedValueEntry.getNewValue();
					if (null != prodData.getCode())
					{
						savedValueData.setProductId(prodData.getCode());
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		return savedValueData;
	}

	/**
	 * This method takes the list of SellerPriority Created and Modified data in the POJO class which is in turn set in
	 * the CSV file to be generated in a specified location
	 */
	void fileWriteReport(final List<SellerPriorityReportData> savedValueDataList)
	{


		final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.SIMPLEDATEFORMAT);

		FileWriter fileWriter = null;
		// Get the file location
		final File sellerPriorityReportLoc = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.FILE_LOCATION));
		// Get the Seller Priority file name
		final File sellerPriorityReportFile = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.FILE_LOCATION), MarketplacecommerceservicesConstants.SELLERPRIORITYREPORT);

		try
		{
			//Sorting savedValues list for Timestamp Current to old (Descending order)
			if (CollectionUtils.isNotEmpty(savedValueDataList))
			{
				Collections.sort(savedValueDataList, new Comparator<SellerPriorityReportData>()
				{
					@Override
					public int compare(final SellerPriorityReportData val1, final SellerPriorityReportData val2)
					{
						if (val2.getModifiedTime().compareTo(val1.getModifiedTime()) > 0)
						{
							return 1;
						}
						else
						{
							return -1;
						}
					}
				});
				LOG.debug(savedValueDataList);
			}
			if (sellerPriorityReportLoc.exists())
			{
				fileWriter = new FileWriter(sellerPriorityReportFile);

				//Write the CSV file header
				fileWriter.append(FILE_HEADER);

				//Add a new line separator after the header
				fileWriter.append(NEW_LINE_SEPARATOR);

				//Write a new student object list to the CSV file
				for (final SellerPriorityReportData report : savedValueDataList)
				{ // If value dn put vale else put empty
					fileWriter.append(null != report.getModifiedTime() ? sdf.format(report.getModifiedTime())
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getChangedBy() ? report.getChangedBy()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getSellerId() ? report.getSellerId()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getSellerName() ? report.getSellerName()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getCategoryId() ? report.getCategoryId()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getProductId() ? report.getProductId()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getOldStartDate() ? sdf.format(report.getOldStartDate())
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getOldEndDate() ? sdf.format(report.getOldEndDate())
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getIsActive() ? report.getIsActive()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getNewlyCreated() ? report.getNewlyCreated()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getModifiedStartDate() ? sdf.format(report.getModifiedStartDate())
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(null != report.getModifiedEndDate() ? sdf.format(report.getModifiedEndDate())
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(COMMA_DELIMITER);

					fileWriter.append(null != report.getModifiedActiveFlag() ? report.getModifiedActiveFlag()
							: MarketplacecommerceservicesConstants.EMPTYSTRING);
					fileWriter.append(NEW_LINE_SEPARATOR);
				}
			}
			else
			{
				throw new Exception(MarketplacecommerceservicesConstants.FILEPATHNOTAVAILABLE);
			}
		}
		catch (final FileNotFoundException e)
		{
			LOG.error("Cannot find file for batch update. " + e.getLocalizedMessage());
		}
		catch (final IOException e)
		{
			LOG.error("Exception closing file handle. " + e.getLocalizedMessage());
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException e)
			{
				LOG.error("Error while flushing/closing fileWriter !!!", e);
			}
			catch (final Exception e)
			{
				LOG.error(MarketplacecommerceservicesConstants.ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES, e);
			}
		}
	}

	/**
	 * @return the mplSellerPriorityReportDAO
	 */
	public MplSellerPriorityReportDAO getMplSellerPriorityReportDAO()
	{
		return mplSellerPriorityReportDAO;
	}

	/**
	 * @param mplSellerPriorityReportDAO
	 *           the mplSellerPriorityReportDAO to set
	 */
	public void setMplSellerPriorityReportDAO(final MplSellerPriorityReportDAO mplSellerPriorityReportDAO)
	{
		this.mplSellerPriorityReportDAO = mplSellerPriorityReportDAO;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the lastStartDate
	 */
	public static Date getLastStartDate()
	{
		return lastStartDate;
	}

	/**
	 * @param lastStartDate
	 *           the lastStartDate to set
	 */
	public static void setLastStartDate(final Date lastStartDate)
	{
		MplSellerPriorityReportServiceImpl.lastStartDate = lastStartDate;
	}

	/**
	 * @return the lastEndDate
	 */
	public static Date getLastEndDate()
	{
		return lastEndDate;
	}

	/**
	 * @param lastEndDate
	 *           the lastEndDate to set
	 */
	public static void setLastEndDate(final Date lastEndDate)
	{
		MplSellerPriorityReportServiceImpl.lastEndDate = lastEndDate;
	}

}
