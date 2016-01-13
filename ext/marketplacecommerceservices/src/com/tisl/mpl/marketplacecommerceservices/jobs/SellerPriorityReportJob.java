/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.data.SellerPriorityReportData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityReportService;
import com.tisl.mpl.model.MplSellerPriorityJobModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SellerPriorityReportJob extends AbstractJobPerformable<MplSellerPriorityJobModel>
{

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final String FILE_HEADER = MarketplacecommerceservicesConstants.CSVFILEHEADER_SELLERPRIORITY;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplSellerPriorityReportService mplSellerPriorityReportService;


	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SellerPriorityReportJob.class.getName());



	/**
	 * @Descriptiion: CronJob to generate Audit log for Seller Priority
	 * @param: arg0
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final MplSellerPriorityJobModel cronModel)
	{
		try
		{
			if (null != cronModel.getStartDate() && null != cronModel.getEndDate())
			{
				fetchSpecificDetails(cronModel.getStartDate(), cronModel.getEndDate());
			}
			else
			{
				fetchAllDetails();
			}


		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	/**
	 * This Method fetches all modified details in SellerPriority Model within the prescribed date Range
	 *
	 * @param startDate
	 * @param endDate
	 */
	private void fetchSpecificDetails(final Date startDate, final Date endDate)
	{
		LOG.debug("fetchSpecificDetails : startDate: " + startDate + "   endDate: " + endDate);
		final List<SavedValuesModel> savedChangedValues = getMplSellerPriorityReportService().getSellerPriorityDetails(startDate,
				endDate);

		if (null != savedChangedValues)
		{
			//put customer data in the POJO class
			putDataForSellerPriority(savedChangedValues);
		}
	}


	/**
	 * This Method fetches all details in SellerPriority Model
	 *
	 */
	private void fetchAllDetails()
	{
		final List<SavedValuesModel> savedChangedValues = getMplSellerPriorityReportService().getAllSellerPriorityDetails();

		if (null != savedChangedValues)
		{
			//put customer data in the POJO class
			putDataForSellerPriority(savedChangedValues);
		}
	}


	/**
	 * This method takes the list of blacklisted customers and sets in the POJO class which is in turn set in the CSV
	 * file to be generated in a specified location
	 *
	 * @param customers
	 */
	void putDataForSellerPriority(final List<SavedValuesModel> savedValuesList)
	//public static List<SavedValuesModel> putDataForSellerPriority(final List<SavedValuesModel> savedValues)
	{
		final List<SellerPriorityReportData> savedValueDataList = new ArrayList<SellerPriorityReportData>();

		final List<SavedValuesModel> savedValues = new ArrayList<SavedValuesModel>(savedValuesList);

		//Sorting savedValues list for Timestamp
		Collections.sort(savedValues, new Comparator<SavedValuesModel>()
		{
			@Override
			public int compare(final SavedValuesModel val1, final SavedValuesModel val2)
			{
				if (val2.getTimestamp().compareTo(val1.getTimestamp()) > 0)
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
		});



		//final iterating through the final list and adding final the rows in final the list of final the pojo class
		LOG.debug("savedValues size: " + savedValues.size());

		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		for (final SavedValuesModel savedVal : savedValues)
		{
			final SellerPriorityReportData savedValueData = new SellerPriorityReportData();
			LOG.debug("savedValuesEntries size: " + savedVal.getSavedValuesEntries().size());
			// Modified / Created Time
			if (null != savedVal.getTimestamp().toString())
			{
				savedValueData.setModifiedTime(sdf.format(savedVal.getTimestamp()));
			}
			else
			{
				savedValueData.setModifiedTime(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			// Modified / Created Entry details
			final MplSellerPriorityModel sellerModData = (MplSellerPriorityModel) savedVal.getModifiedItem();

			LOG.debug("SellerID: " + sellerModData.getSellerId() + " Active/Deactive: " + sellerModData.getIsActive().booleanValue());

			// Modified / Created SellerID
			if (null != sellerModData.getSellerId())
			{
				final SellerMasterModel sellerDataVal = sellerModData.getSellerId();
				if (null != sellerDataVal.getId())
				{
					savedValueData.setSellerId(sellerDataVal.getId());
				}
				else
				{
					savedValueData.setSellerId(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				if (null != sellerDataVal.getFirstname())
				{
					savedValueData.setSellerName(sellerDataVal.getFirstname());
				}
				else
				{
					savedValueData.setSellerName(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
			}
			else
			{
				savedValueData.setSellerId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			// Modified / Created CategoryID
			if (null != sellerModData.getCategoryId())
			{
				final CategoryModel catData = sellerModData.getCategoryId();
				if (null != catData.getCode())
				{
					savedValueData.setCategoryId(catData.getCode());
				}
			}
			else
			{
				savedValueData.setCategoryId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			// Modified / Created Product ID
			if (null != sellerModData.getListingId())
			{
				final ProductModel prodData = sellerModData.getListingId();
				if (null != prodData.getCode())
				{
					savedValueData.setProductId(prodData.getCode());
				}
			}
			else
			{
				savedValueData.setProductId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}
			// Activation status of Priority
			if (sellerModData.getIsActive().booleanValue())
			{
				savedValueData.setIsActive("Activate");
			}
			else
			{
				savedValueData.setIsActive("Deactivate");
			}

			// Priority Start Date
			if (null != sellerModData.getPriorityStartDate())
			{
				savedValueData.setOldStartDate(sdf.format(sellerModData.getPriorityStartDate()));
			}
			else
			{
				savedValueData.setOldStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			// Priority End Date

			if (null != sellerModData.getPriorityEndDate())
			{
				savedValueData.setOldEndDate(sdf.format(sellerModData.getPriorityEndDate()));
			}
			else
			{
				savedValueData.setOldEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}


			// User
			if (null != savedVal.getUser())
			{
				savedValueData.setChangedBy(savedVal.getUser().getName());
			}
			else
			{
				savedValueData.setChangedBy(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}
			LOG.debug("ModificationType: " + savedVal.getModificationType().getCode());

			savedValueData.setModifiedActiveFlag(MarketplacecommerceservicesConstants.EMPTYSTRING);
			// Modified Data
			if (savedVal.getModificationType().getCode().equalsIgnoreCase("CHANGED"))
			{
				savedValueData.setNewlyCreated("N");

				for (final SavedValueEntryModel savedValueEntry : savedVal.getSavedValuesEntries())
				{
					LOG.debug("ModifiedAttribute: " + savedValueEntry.getModifiedAttribute());

					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityStartDate"))
					{
						if (null != savedValueEntry.getOldValue())
						{
							savedValueData.setOldStartDate(sdf.format(savedValueEntry.getOldValue()));
						}
						else
						{
							savedValueData.setOldStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
						if (null != savedValueEntry.getNewValue())
						{
							savedValueData.setModifiedStartDate(sdf.format(savedValueEntry.getNewValue()));
						}
						else
						{
							savedValueData.setModifiedStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}
					//					else
					//					{
					//						if (null != sellerModData.getPriorityStartDate())
					//						{
					//							savedValueData.setOldStartDate(sdf.format(sellerModData.getPriorityStartDate()));
					//						}
					//						else
					//						{
					//							savedValueData.setOldStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
					//						}
					//					}
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityEndDate"))
					{
						if (null != savedValueEntry.getOldValue())
						{
							savedValueData.setOldEndDate(sdf.format(savedValueEntry.getOldValue()));
						}
						else
						{
							savedValueData.setOldEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
						if (null != savedValueEntry.getNewValue())
						{
							savedValueData.setModifiedEndDate(sdf.format(savedValueEntry.getNewValue()));
						}
						else
						{
							savedValueData.setModifiedEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}
					//					else
					//					{
					//						if (null != sellerModData.getPriorityEndDate())
					//						{
					//							savedValueData.setOldEndDate(sdf.format(sellerModData.getPriorityEndDate()));
					//						}
					//						else
					//						{
					//							savedValueData.setOldEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
					//						}
					//					}
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("isactive"))
					{
						if (savedValueEntry.getNewValue().toString().equalsIgnoreCase("true"))
						{
							savedValueData.setModifiedActiveFlag("Activate");
						}
						else
						{
							savedValueData.setModifiedActiveFlag("Deactivated");
						}
					}
					if (null == savedValueData.getModifiedStartDate())
					{
						savedValueData.setModifiedStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
					}
					if (null == savedValueData.getModifiedEndDate())
					{
						savedValueData.setModifiedEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
					}
				}
			}
			else
			{
				// Created Data
				savedValueData.setNewlyCreated("Y");
				savedValueData.setModifiedStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
				savedValueData.setModifiedEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);


				// Modified / Created Priority Start Date
				for (final SavedValueEntryModel savedValueEntry : savedVal.getSavedValuesEntries())
				{
					LOG.debug("ModifiedAttribute: " + savedValueEntry.getModifiedAttribute());

					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityStartDate"))
					{
						if (null != savedValueEntry.getNewValue())
						{
							savedValueData.setOldStartDate(sdf.format(savedValueEntry.getNewValue()));
						}
						else
						{
							savedValueData.setOldStartDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityEndDate"))
					{
						if (null != savedValueEntry.getNewValue())
						{
							savedValueData.setOldEndDate(sdf.format(savedValueEntry.getNewValue()));
						}
						else
						{
							savedValueData.setOldEndDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}

					// Modified / Created SellerID
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("sellerid"))
					{
						final SellerMasterModel sellerDataVal = (SellerMasterModel) savedValueEntry.getNewValue();
						if (null != sellerDataVal.getId())
						{
							savedValueData.setSellerId(sellerDataVal.getId());
						}
						else
						{
							savedValueData.setSellerId(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
						if (null != sellerDataVal.getFirstname())
						{
							savedValueData.setSellerName(sellerDataVal.getFirstname());
						}
						else
						{
							savedValueData.setSellerName(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}
					// Modified / Created CategoryID
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("categoryid"))
					{
						final CategoryModel catData = (CategoryModel) savedValueEntry.getNewValue();
						if (null != catData.getCode())
						{
							savedValueData.setCategoryId(catData.getCode());
						}
						else
						{
							savedValueData.setCategoryId(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}

					// Modified / Created Product ID
					if (null != savedValueEntry.getModifiedAttribute()
							&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("listingid"))
					{
						final ProductModel prodData = (ProductModel) savedValueEntry.getNewValue();
						if (null != prodData.getCode())
						{
							savedValueData.setProductId(prodData.getCode());
						}
						else
						{
							savedValueData.setProductId(MarketplacecommerceservicesConstants.EMPTYSTRING);
						}
					}


					/*
					 * // Activation status of Priority if (null != savedValueEntry.getModifiedAttribute() &&
					 * savedValueEntry.getModifiedAttribute().equalsIgnoreCase("isactive")) { //final SellerMasterModel
					 * sellerDataVal = sellerModData.getSellerId(); if (null != savedValueEntry.getNewValue()) { if
					 * (savedValueEntry.getNewValue().toString().equalsIgnoreCase("true")) { savedValueData.setIsActive("Y");
					 * } else { savedValueData.setProductId("N"); } } else {
					 * savedValueData.setProductId(MarketplacecommerceservicesConstants.EMPTYSTRING); } } else {
					 * savedValueData.setProductId(MarketplacecommerceservicesConstants.EMPTYSTRING); }
					 */
				}

			}
			savedValueDataList.add(savedValueData);
		}


		LOG.debug(savedValueDataList);

		FileWriter fileWriter = null;
		final File rootFolder1 = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.FILE_LOCATION), MarketplacecommerceservicesConstants.SELLERPRIORITYREPORT);
		try
		{
			fileWriter = new FileWriter(rootFolder1);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER);

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (final SellerPriorityReportData report : savedValueDataList)
			{
				fileWriter.append(report.getModifiedTime());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getChangedBy());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getCategoryId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getProductId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getOldStartDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getOldEndDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getIsActive());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getNewlyCreated());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getModifiedStartDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getModifiedEndDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getModifiedActiveFlag());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in CsvFileWriter !!!", e);
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
				LOG.error("Error while flushing/closing fileWriter !!!");
			}
		}

	}





	final Comparator<List<SellerPriorityReportData>> comparator = new Comparator<List<SellerPriorityReportData>>()
	{
		public int compare(final List<SellerPriorityReportData> pList1, final List<SellerPriorityReportData> pList2)
		{
			return ((Comparable<SellerPriorityReportData>) pList1.get(0)).compareTo(pList2.get(0));
		}
	};

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
	 * @return the mplSellerPriorityReportService
	 */
	public MplSellerPriorityReportService getMplSellerPriorityReportService()
	{
		return mplSellerPriorityReportService;
	}


	/**
	 * @param mplSellerPriorityReportService
	 *           the mplSellerPriorityReportService to set
	 */
	public void setMplSellerPriorityReportService(final MplSellerPriorityReportService mplSellerPriorityReportService)
	{
		this.mplSellerPriorityReportService = mplSellerPriorityReportService;
	}
}
