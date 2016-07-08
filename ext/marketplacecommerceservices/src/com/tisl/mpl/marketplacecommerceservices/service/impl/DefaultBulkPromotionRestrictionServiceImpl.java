/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.marketplacecommerceservices.service.BulkPromotionRestrictionService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.DeliveryModePromotionRestrictionModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;
import com.tisl.mpl.model.PaymentModeSpecificPromotionRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.promotion.processor.BulkPromotionErrorHandling;


/**
 * @author TCS
 *
 */
public class DefaultBulkPromotionRestrictionServiceImpl implements BulkPromotionRestrictionService
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultBulkPromotionRestrictionServiceImpl.class);

	private BulkPromotionCreationDao bulkPromotionCreationDao;

	private ModelService modelService;

	private BulkPromotionErrorHandling bulkPromotionErrorHandling;

	//@Description: Variable Declaration for Promotion Restrictions
	private static final int RESTRICTIONCODE = 0;
	private static final int PROMOTION = 1;
	private static final int SELLERID = 2;
	private static final int PAYMENTMODES = 3;
	private static final int BANK = 4;
	private static final int DELIVERYMODEDETAILSLIST = 5;
	private static final int BRAND = 6;
	private static final int BRANDDISCOUNTLIMIT = 7;


	/**
	 * @Description: For Creating Promotion Restriction in bulk
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */
	@Override
	public void processUpdateForPromoRestriction(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded)
	{
		LOG.debug("Generationg Promotion Restrictions");
		while (reader.readNextLine())
		{
			final Map<Integer, String> line = reader.getLine();
			final StringBuilder invalidColumns = new StringBuilder();

			final String restrictioncode = line.get(Integer.valueOf(RESTRICTIONCODE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "RESTRICTIONCODE", restrictioncode);

			final String promotion = line.get(Integer.valueOf(PROMOTION));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PROMOTION", promotion);

			final String sellerid = line.get(Integer.valueOf(SELLERID));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "SELLERID", sellerid);

			final String paymentmodes = line.get(Integer.valueOf(PAYMENTMODES));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PAYMENTMODES", paymentmodes);

			final String bank = line.get(Integer.valueOf(BANK));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "BANK", bank);

			final String deliverymodedetailslist = line.get(Integer.valueOf(DELIVERYMODEDETAILSLIST));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "DELIVERYMODEDETAILSLIST", deliverymodedetailslist);

			final String brand = line.get(Integer.valueOf(BRAND));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "BRAND", brand);

			final String branddiscountlimit = line.get(Integer.valueOf(BRANDDISCOUNTLIMIT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "BRANDDISCOUNTLIMIT", branddiscountlimit);


			if (!StringUtils.isEmpty(invalidColumns.toString()))
			{
				processData(line, writer);
				continue;
			}
		}
	}


	/**
	 * @Description: To process data for Promotions
	 * @param line
	 * @param writer
	 */
	private void processData(final Map<Integer, String> line, final CSVWriter writer)
	{
		LOG.debug("Processing Promotion Data");
		try
		{
			if (line.get(Integer.valueOf(RESTRICTIONCODE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.ETAILSELLERSPECIFICRESTRICTION))
			{
				createSellerRestriction(line, writer);
			}
			else if (line.get(Integer.valueOf(RESTRICTIONCODE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.DELIVERYMODEPROMOTIONRESTRICTION))
			{
				createDeliveryRestriction(line, writer);
			}
			else if (line.get(Integer.valueOf(RESTRICTIONCODE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.PAYMENTMODESPECIFICPROMOTIONRESTRICTION))
			{
				createPaymentModeRestriction(line, writer);
			}
			else if (line.get(Integer.valueOf(RESTRICTIONCODE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.BRANDRESTRICTION))
			{
				createBrandRestriction(line, writer);
			}
			else if (line.get(Integer.valueOf(RESTRICTIONCODE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.EXCLUDEBRANDRESTRICTION))
			{
				createExcludeBrandRestriction(line, writer);
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(false);
			LOG.error(exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
		}
	}

	/**
	 * @Description : Create Exclude Brand Level Restriction
	 * @param line
	 * @param writer
	 */
	private void createExcludeBrandRestriction(final Map<Integer, String> line, final CSVWriter writer)
	{

		final List<Integer> errorColumnList = errorListData(false);
		final ExcludeManufacturersRestrictionModel oModel = modelService.create(ExcludeManufacturersRestrictionModel.class);

		try
		{
			if (CollectionUtils.isEmpty(errorColumnList))
			{
				if (null != line.get(Integer.valueOf(BRAND)) && line.get(Integer.valueOf(BRAND)).trim().length() > 0)
				{
					List<CategoryModel> categoryList = new ArrayList<>();
					categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
					if (null != categoryList && !categoryList.isEmpty())
					{
						oModel.setManufacturers(categoryList);
					}
				}


				AbstractPromotionModel oPromotionModel = null;
				List<AbstractPromotionRestrictionModel> promoRestrictionList = null;
				if (null != line.get(Integer.valueOf(PROMOTION)) && line.get(Integer.valueOf(PROMOTION)).trim().length() > 0)
				{
					oPromotionModel = bulkPromotionCreationDao.fetchExistingPromotionData(line.get(Integer.valueOf(PROMOTION)));
					if (null != oPromotionModel && null != oPromotionModel.getRestrictions()
							&& !oPromotionModel.getRestrictions().isEmpty())
					{
						promoRestrictionList = new ArrayList<>(oPromotionModel.getRestrictions());
					}
					else
					{
						promoRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
					}

					if (null != oModel.getManufacturers() && !oModel.getManufacturers().isEmpty())
					{
						promoRestrictionList.add(oModel);
						oPromotionModel.setRestrictions(promoRestrictionList);
						modelService.save(oModel); //Creating the Restriction
						modelService.save(oPromotionModel); //Adding the restriction
					}
					else
					{
						populateErrorEntry(line, writer, errorColumnList);
					}

				}
				else
				{
					populateErrorEntry(line, writer, errorColumnList);
				}
			}
			else
			{
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			populateErrorEntry(line, writer, errorColumnList);
			LOG.error(exception.getMessage());
		}
	}


	/**
	 * @Description : Create Brand Level Restriction
	 * @param line
	 * @param writer
	 */
	private void createBrandRestriction(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final ManufacturersRestrictionModel oModel = modelService.create(ManufacturersRestrictionModel.class);

		try
		{
			if (CollectionUtils.isEmpty(errorColumnList))
			{
				if (null != line.get(Integer.valueOf(BRAND)) && line.get(Integer.valueOf(BRAND)).trim().length() > 0)
				{
					List<CategoryModel> categoryList = new ArrayList<>();
					categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
					if (null != categoryList && !categoryList.isEmpty())
					{
						oModel.setManufacturers(categoryList);
					}

					if (null != line.get(Integer.valueOf(BRANDDISCOUNTLIMIT))
							&& line.get(Integer.valueOf(BRANDDISCOUNTLIMIT)).trim().length() > 0)
					{
						oModel.setMinimumManufactureAmount(Double.valueOf(line.get(Integer.valueOf(BRANDDISCOUNTLIMIT))));
					}
				}


				AbstractPromotionModel oPromotionModel = null;
				List<AbstractPromotionRestrictionModel> promoRestrictionList = null;
				if (null != line.get(Integer.valueOf(PROMOTION)) && line.get(Integer.valueOf(PROMOTION)).trim().length() > 0)
				{
					oPromotionModel = bulkPromotionCreationDao.fetchExistingPromotionData(line.get(Integer.valueOf(PROMOTION)));
					if (null != oPromotionModel && null != oPromotionModel.getRestrictions()
							&& !oPromotionModel.getRestrictions().isEmpty())
					{
						promoRestrictionList = new ArrayList<>(oPromotionModel.getRestrictions());
					}
					else
					{
						promoRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
					}

					if (null != oModel.getManufacturers() && !oModel.getManufacturers().isEmpty())
					{
						promoRestrictionList.add(oModel);
						oPromotionModel.setRestrictions(promoRestrictionList);
						modelService.save(oModel); //Creating the Restriction
						modelService.save(oPromotionModel); //Adding the restriction
					}
					else
					{
						populateErrorEntry(line, writer, errorColumnList);
					}

				}
				else
				{
					populateErrorEntry(line, writer, errorColumnList);
				}


			}
			else
			{
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			populateErrorEntry(line, writer, errorColumnList);
			LOG.error(exception.getMessage());
		}
	}


	/**
	 * @Description : Create Payment Mode Specific Restriction for Promotion
	 * @param line
	 * @param writer
	 */
	private void createPaymentModeRestriction(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final PaymentModeSpecificPromotionRestrictionModel oModel = modelService
				.create(PaymentModeSpecificPromotionRestrictionModel.class);
		try
		{
			if (CollectionUtils.isEmpty(errorColumnList))
			{
				if (null != line.get(Integer.valueOf(PAYMENTMODES)) && line.get(Integer.valueOf(PAYMENTMODES)).trim().length() > 0)
				{
					List<PaymentTypeModel> paymentModeList = new ArrayList<>();
					paymentModeList = fetchPaymentDetails(line);
					if (null != paymentModeList && !paymentModeList.isEmpty())
					{
						oModel.setPaymentModes(paymentModeList);
					}

					if (null != line.get(Integer.valueOf(BANK)) && line.get(Integer.valueOf(BANK)).trim().length() > 0)
					{
						List<BankModel> bankList = new ArrayList<>();
						bankList = fetchBankDetails(line);
						if (null != bankList && !bankList.isEmpty())
						{
							oModel.setBanks(bankList);
						}
					}
				}


				AbstractPromotionModel oPromotionModel = null;
				List<AbstractPromotionRestrictionModel> promoRestrictionList = null;
				if (null != line.get(Integer.valueOf(PROMOTION)) && line.get(Integer.valueOf(PROMOTION)).trim().length() > 0)
				{
					oPromotionModel = bulkPromotionCreationDao.fetchExistingPromotionData(line.get(Integer.valueOf(PROMOTION)));
					if (null != oPromotionModel && null != oPromotionModel.getRestrictions()
							&& !oPromotionModel.getRestrictions().isEmpty())
					{
						promoRestrictionList = new ArrayList<>(oPromotionModel.getRestrictions());
					}
					else
					{
						promoRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
					}

					if (null != oModel.getPaymentModes() && !oModel.getPaymentModes().isEmpty())
					{
						promoRestrictionList.add(oModel);
						oPromotionModel.setRestrictions(promoRestrictionList);
						modelService.save(oModel); //Creating the Restriction
						modelService.save(oPromotionModel); //Adding the restriction
					}
					else
					{
						populateErrorEntry(line, writer, errorColumnList);
					}

				}
				else
				{
					populateErrorEntry(line, writer, errorColumnList);
				}


			}
			else
			{
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			populateErrorEntry(line, writer, errorColumnList);
			LOG.error(exception.getMessage());
		}

	}


	/**
	 * @Description : Create Delivery Mode Specific Restriction for Promotion
	 * @param line
	 * @param writer
	 */
	private void createDeliveryRestriction(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final DeliveryModePromotionRestrictionModel oModel = modelService.create(DeliveryModePromotionRestrictionModel.class);
		try
		{
			if (CollectionUtils.isEmpty(errorColumnList))
			{
				if (null != line.get(Integer.valueOf(DELIVERYMODEDETAILSLIST))
						&& line.get(Integer.valueOf(DELIVERYMODEDETAILSLIST)).trim().length() > 0)
				{
					List<DeliveryModeModel> deliveryList = new ArrayList<>();
					deliveryList = fetchDeliveryInformationData(line);
					if (null != deliveryList && !deliveryList.isEmpty())
					{
						oModel.setDeliveryModeDetailsList(deliveryList);
					}
				}

				AbstractPromotionModel oPromotionModel = null;
				List<AbstractPromotionRestrictionModel> promoRestrictionList = null;
				if (null != line.get(Integer.valueOf(PROMOTION)) && line.get(Integer.valueOf(PROMOTION)).trim().length() > 0)
				{
					oPromotionModel = bulkPromotionCreationDao.fetchExistingPromotionData(line.get(Integer.valueOf(PROMOTION)));
					if (null != oPromotionModel && null != oPromotionModel.getRestrictions()
							&& !oPromotionModel.getRestrictions().isEmpty())
					{
						promoRestrictionList = new ArrayList<>(oPromotionModel.getRestrictions());
					}
					else
					{
						promoRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
					}

					if (null != oModel.getDeliveryModeDetailsList() && !oModel.getDeliveryModeDetailsList().isEmpty())
					{
						promoRestrictionList.add(oModel);
						oPromotionModel.setRestrictions(promoRestrictionList);
						modelService.save(oModel); //Creating the Restriction
						modelService.save(oPromotionModel); //Adding the restriction
					}
					else
					{
						populateErrorEntry(line, writer, errorColumnList);
					}

				}
				else
				{
					populateErrorEntry(line, writer, errorColumnList);
				}

			}
			else
			{
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			populateErrorEntry(line, writer, errorColumnList);
			LOG.error(exception.getMessage());
		}

	}

	/**
	 * @Description : Create EtailSellerSpecificRestriction for Promotion
	 * @param line
	 * @param writer
	 */
	private void createSellerRestriction(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final EtailSellerSpecificRestrictionModel oModel = modelService.create(EtailSellerSpecificRestrictionModel.class);
		try
		{
			if (CollectionUtils.isEmpty(errorColumnList))
			{
				if (null != line.get(Integer.valueOf(SELLERID)) && line.get(Integer.valueOf(SELLERID)).trim().length() > 0)
				{
					List<SellerMasterModel> sellerList = new ArrayList<>();
					sellerList = fetchSellerInformationData(line);
					if (null != sellerList && !sellerList.isEmpty())
					{
						oModel.setSellerMasterList(sellerList);
					}
				}
				AbstractPromotionModel oPromotionModel = null;
				List<AbstractPromotionRestrictionModel> promoRestrictionList = null;
				if (null != line.get(Integer.valueOf(PROMOTION)) && line.get(Integer.valueOf(PROMOTION)).trim().length() > 0)
				{
					oPromotionModel = bulkPromotionCreationDao.fetchExistingPromotionData(line.get(Integer.valueOf(PROMOTION)));
					if (null != oPromotionModel && null != oPromotionModel.getRestrictions()
							&& !oPromotionModel.getRestrictions().isEmpty())
					{
						promoRestrictionList = new ArrayList<>(oPromotionModel.getRestrictions());
					}
					else
					{
						promoRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
					}

					if (null != oModel.getSellerMasterList() && !oModel.getSellerMasterList().isEmpty())
					{
						promoRestrictionList.add(oModel);
						oPromotionModel.setRestrictions(promoRestrictionList);
						modelService.save(oModel); //Creating the Restriction
						modelService.save(oPromotionModel); //Adding the restriction
					}
					else
					{
						populateErrorEntry(line, writer, errorColumnList);
					}
				}
				else
				{
					populateErrorEntry(line, writer, errorColumnList);
				}
			}
			else
			{
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			populateErrorEntry(line, writer, errorColumnList);
			LOG.error(exception.getMessage());
		}
	}



	private List<DeliveryModeModel> fetchDeliveryInformationData(final Map<Integer, String> line)
	{
		List<DeliveryModeModel> deliveryList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(DELIVERYMODEDETAILSLIST)).contains(
					MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String deliveryString = line.get(Integer.valueOf(DELIVERYMODEDETAILSLIST));
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				final String[] deliveryArray = deliveryString.split(Pattern.quote(delimiter));

				for (int i = 0; i < deliveryArray.length; i++)
				{
					final DeliveryModeModel deliveryModeModel = bulkPromotionCreationDao.fetchDeliveryModeDetails(deliveryArray[i]);
					if (null != deliveryModeModel)
					{
						deliveryList.add(deliveryModeModel);
					}
				}
			}
			else
			{
				final DeliveryModeModel deliveryModeModel = bulkPromotionCreationDao.fetchDeliveryModeDetails(line.get(Integer
						.valueOf(DELIVERYMODEDETAILSLIST)));
				if (null != deliveryModeModel)
				{
					deliveryList.add(deliveryModeModel);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			deliveryList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return deliveryList;
	}




	/**
	 * @Description : Fetch Seller Information Data
	 * @param catalogVersionModel
	 * @param line
	 * @return sellerList
	 */
	private List<SellerMasterModel> fetchSellerInformationData(final Map<Integer, String> line)
	{
		List<SellerMasterModel> sellerList = new ArrayList<>();
		List<SellerMasterModel> sellerDataList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(SELLERID)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String productString = line.get(Integer.valueOf(SELLERID));
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				final String[] sellerArray = productString.split(Pattern.quote(delimiter));

				for (int i = 0; i < sellerArray.length; i++)
				{
					sellerDataList = bulkPromotionCreationDao.fetchSellerMasterInfo(sellerArray[i]);
					if (CollectionUtils.isNotEmpty(sellerDataList))
					{
						sellerList.add(sellerDataList.get(0));
					}
				}
			}
			else
			{
				sellerDataList = bulkPromotionCreationDao.fetchSellerMasterInfo(line.get(Integer.valueOf(SELLERID)));
				if (CollectionUtils.isNotEmpty(sellerDataList))
				{
					sellerList.add(sellerDataList.get(0));
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			sellerList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return sellerList;
	}

	/**
	 * @Description : Fetch Bank Details
	 * @param line
	 * @return paymentModeList
	 */
	private List<PaymentTypeModel> fetchPaymentDetails(final Map<Integer, String> line)
	{
		List<PaymentTypeModel> paymentModeList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(PAYMENTMODES)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String paymentModeString = line.get(Integer.valueOf(PAYMENTMODES));
				final String[] paymentModeArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				paymentModeArray = paymentModeString.split(Pattern.quote(delimiter));

				for (int i = 0; i < paymentModeArray.length; i++)
				{
					final PaymentTypeModel paymentTypeModel = bulkPromotionCreationDao.fetchPaymentModeDetails(paymentModeArray[i]);
					if (null != paymentTypeModel)
					{
						paymentModeList.add(paymentTypeModel);
					}
				}
			}
			else
			{
				final PaymentTypeModel paymentTypeModel = bulkPromotionCreationDao.fetchPaymentModeDetails(line.get(Integer
						.valueOf(PAYMENTMODES)));
				if (null != paymentTypeModel)
				{
					paymentModeList.add(paymentTypeModel);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			paymentModeList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return paymentModeList;
	}



	private List<BankModel> fetchBankDetails(final Map<Integer, String> line)
	{

		List<BankModel> bankList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(BANK)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String bankString = line.get(Integer.valueOf(BANK));
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				final String[] bankArray = bankString.split(Pattern.quote(delimiter));

				for (int i = 0; i < bankArray.length; i++)
				{
					final BankModel oModel = bulkPromotionCreationDao.fetchBankDetails(bankArray[i]);
					if (null != oModel)
					{
						bankList.add(oModel);
					}
				}
			}
			else
			{
				final BankModel oModel = bulkPromotionCreationDao.fetchBankDetails(line.get(Integer.valueOf(BANK)));
				if (null != oModel)
				{
					bankList.add(oModel);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			bankList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return bankList;

	}


	/**
	 * @Description : Populate error entry to file
	 * @param line
	 * @param writer
	 * @param errorColumnList
	 */
	private void populateErrorEntry(final Map<Integer, String> line, final CSVWriter writer, final List<Integer> errorColumnList)
	{
		try
		{
			bulkPromotionErrorHandling.writeErrorData(writer, line, errorColumnList, line,
					MarketplacecommerceservicesConstants.ERRORMESSAGE);
		}
		catch (final IOException e)
		{
			LOG.debug(e.getMessage());
		}
	}


	/**
	 * @Description: Validates Data Uploaded
	 * @param line
	 * @param isIncorrectCode
	 * @return data
	 */
	private List<Integer> errorListData(final boolean isIncorrectCode)
	{
		final List<Integer> data = new ArrayList<>();

		if (isIncorrectCode)
		{
			data.add(Integer.valueOf(RESTRICTIONCODE));
		}
		return data;
	}

	/**
	 * @return the bulkPromotionCreationDao
	 */
	public BulkPromotionCreationDao getBulkPromotionCreationDao()
	{
		return bulkPromotionCreationDao;
	}

	/**
	 * @param bulkPromotionCreationDao
	 *           the bulkPromotionCreationDao to set
	 */
	public void setBulkPromotionCreationDao(final BulkPromotionCreationDao bulkPromotionCreationDao)
	{
		this.bulkPromotionCreationDao = bulkPromotionCreationDao;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @Description : Returns List of Promotion Categories and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return categoryList
	 */
	private List<CategoryModel> fetchCategoryData(final CatalogVersionModel catalogVersionModel, final Map<Integer, String> line)
	{
		List<CategoryModel> categoryList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(BRAND)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String categoryString = line.get(Integer.valueOf(BRAND));
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				final String[] categoryArray = categoryString.split(Pattern.quote(delimiter));

				for (int i = 0; i < categoryArray.length; i++)
				{
					final CategoryModel categoryData = bulkPromotionCreationDao.fetchCategoryData(catalogVersionModel,
							categoryArray[i]);
					if (null != categoryData)
					{
						categoryList.add(categoryData);
					}
				}
			}
			else
			{
				final CategoryModel categoryData = bulkPromotionCreationDao.fetchCategoryData(catalogVersionModel,
						line.get(Integer.valueOf(BRAND)));
				if (null != categoryData)
				{
					categoryList.add(categoryData);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			categoryList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}

		return categoryList;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}


	/**
	 * @return the bulkPromotionErrorHandling
	 */
	public BulkPromotionErrorHandling getBulkPromotionErrorHandling()
	{
		return bulkPromotionErrorHandling;
	}


	/**
	 * @param bulkPromotionErrorHandling
	 *           the bulkPromotionErrorHandling to set
	 */
	public void setBulkPromotionErrorHandling(final BulkPromotionErrorHandling bulkPromotionErrorHandling)
	{
		this.bulkPromotionErrorHandling = bulkPromotionErrorHandling;
	}
}
