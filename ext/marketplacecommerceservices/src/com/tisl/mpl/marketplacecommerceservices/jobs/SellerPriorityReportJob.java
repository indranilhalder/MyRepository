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

		//final iterating through the final list and adding final the rows in final the list of final the pojo class
		LOG.debug("savedValues size: " + savedValues.size());


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


		final ArrayList<String> modItemsList = new ArrayList<String>();
		for (final SavedValuesModel savedValmodItems : savedValues)
		{
			if (modItemsList.isEmpty() || !modItemsList.contains(savedValmodItems.getModifiedItem().toString()))
			{
				modItemsList.add(savedValmodItems.getModifiedItem().toString());
			}
		}

		Date lastStartDate = null;
		Date lastEndDate = null;

		for (final String modItem : modItemsList)
		{
			for (final SavedValuesModel savedVal : savedValues)
			{

				if (modItem.equalsIgnoreCase(savedVal.getModifiedItem().toString()))
				{
					final SellerPriorityReportData savedValueData = new SellerPriorityReportData();
					LOG.debug("savedValuesEntries size: " + savedVal.getSavedValuesEntries().size());
					// Modified / Created Time
					if (null != savedVal.getTimestamp().toString())
					{
						savedValueData.setModifiedTime(savedVal.getTimestamp());
					}

					// Modified / Created Entry details
					final MplSellerPriorityModel sellerModData = (MplSellerPriorityModel) savedVal.getModifiedItem();

					LOG.debug("SellerID: " + sellerModData.getSellerId() + " Active/Deactive: "
							+ sellerModData.getIsActive().booleanValue());

					// Modified / Created SellerID
					if (null != sellerModData.getSellerId())
					{
						final SellerMasterModel sellerDataVal = sellerModData.getSellerId();
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
					if (null != sellerModData.getCategoryId())
					{
						final CategoryModel catData = sellerModData.getCategoryId();
						if (null != catData.getCode())
						{
							savedValueData.setCategoryId(catData.getCode());
						}
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
					// Activation status of Priority
					if (sellerModData.getIsActive().booleanValue())
					{
						savedValueData.setIsActive("Y");
					}
					else
					{
						savedValueData.setIsActive("N");
					}

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
					if (null != savedVal.getUser())
					{
						savedValueData.setChangedBy(savedVal.getUser().getName());
					}
					LOG.debug("ModificationType: " + savedVal.getModificationType().getCode());

					//					savedValueData.setModifiedActiveFlag(MarketplacecommerceservicesConstants.EMPTYSTRING);
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
									savedValueData.setOldStartDate((Date) savedValueEntry.getOldValue());
									lastStartDate = (Date) savedValueEntry.getOldValue();
								}
								if (null != savedValueEntry.getNewValue())
								{
									savedValueData.setModifiedStartDate((Date) savedValueEntry.getNewValue());
								}

							}
							if (null != savedValueEntry.getModifiedAttribute()
									&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityEndDate"))
							{
								if (null != savedValueEntry.getOldValue())
								{
									savedValueData.setOldEndDate((Date) savedValueEntry.getOldValue());
									lastEndDate = (Date) savedValueEntry.getOldValue();
								}
								if (null != savedValueEntry.getNewValue())
								{
									savedValueData.setModifiedEndDate((Date) savedValueEntry.getNewValue());
								}
							}
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
							if (null != lastStartDate)
							{
								savedValueData.setOldStartDate(lastStartDate);
							}
							if (null != lastEndDate)
							{
								savedValueData.setOldEndDate(lastEndDate);
							}
						}
					}
					else
					{
						// Created Data
						savedValueData.setNewlyCreated("Y");


						// Modified / Created Priority Start Date
						for (final SavedValueEntryModel savedValueEntry : savedVal.getSavedValuesEntries())
						{
							LOG.debug("ModifiedAttribute: " + savedValueEntry.getModifiedAttribute());

							if (null != savedValueEntry.getModifiedAttribute()
									&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityStartDate"))
							{
								if (null != savedValueEntry.getNewValue())
								{
									savedValueData.setOldStartDate((Date) savedValueEntry.getNewValue());
								}
							}
							if (null != savedValueEntry.getModifiedAttribute()
									&& savedValueEntry.getModifiedAttribute().equalsIgnoreCase("priorityEndDate"))
							{
								if (null != savedValueEntry.getNewValue())
								{
									savedValueData.setOldEndDate((Date) savedValueEntry.getNewValue());
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
								if (null != sellerDataVal.getFirstname())
								{
									savedValueData.setSellerName(sellerDataVal.getFirstname());
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
							}
						}

					}
					savedValueDataList.add(savedValueData);
				}

			}
			lastStartDate = null;
			lastEndDate = null;
		}

		//Sorting savedValues list for Timestamp
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
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
				if (null != report.getModifiedTime())
				{
					fileWriter.append(sdf.format(report.getModifiedTime()));
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getChangedBy())
				{
					fileWriter.append(report.getChangedBy());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getSellerId())
				{
					fileWriter.append(report.getSellerId());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getSellerName())
				{
					fileWriter.append(report.getSellerName());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getCategoryId())
				{
					fileWriter.append(report.getCategoryId());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getProductId())
				{
					fileWriter.append(report.getProductId());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getOldStartDate())
				{
					fileWriter.append(sdf.format(report.getOldStartDate()));
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getOldEndDate())
				{
					fileWriter.append(sdf.format(report.getOldEndDate()));
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getIsActive())
				{
					fileWriter.append(report.getIsActive());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getNewlyCreated())
				{
					fileWriter.append(report.getNewlyCreated());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getModifiedStartDate())
				{
					fileWriter.append(sdf.format(report.getModifiedStartDate()));
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);
				if (null != report.getModifiedEndDate())
				{
					fileWriter.append(sdf.format(report.getModifiedEndDate()));
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(COMMA_DELIMITER);

				if (null != report.getModifiedActiveFlag())
				{
					fileWriter.append(report.getModifiedActiveFlag());
				}
				else
				{
					fileWriter.append(MarketplacecommerceservicesConstants.EMPTYSTRING);
				}
				fileWriter.append(NEW_LINE_SEPARATOR);

				//				fileWriter.append(COMMA_DELIMITER);
				//				fileWriter.append(report.getModifiedActiveFlag());
				//				fileWriter.append(NEW_LINE_SEPARATOR);
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
