/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.marketplacecommerceservices.service.BulkPromotionCreationService;
import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.BuyAandBGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAandBPrecentageDiscountModel;
import com.tisl.mpl.model.BuyAandBgetCModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountCashbackModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountPromotionModel;
import com.tisl.mpl.model.CustomOrderThresholdFreeGiftPromotionModel;
import com.tisl.mpl.model.CustomProductBOGOFPromotionModel;
import com.tisl.mpl.promotion.processor.BulkPromotionErrorHandling;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class DefaultBulkPromotionCreationServiceImpl implements BulkPromotionCreationService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultBulkPromotionCreationServiceImpl.class);
	//Content
	private BulkPromotionCreationDao bulkPromotionCreationDao;

	private ModelService modelService;

	@Autowired
	private BulkPromotionErrorHandling bulkPromotionErrorHandling;


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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


	//@Description: Variable Declaration for Promotions
	private static final int PROMOTYPE = 0;
	private static final int PROMOTIONCODE = 1;
	private static final int PROMOTIONGROUP = 2;
	private static final int DESCRIPTION = 3;
	private static final int ENABLED = 4;
	private static final int PRIORITY = 5;
	private static final int CHANNEL = 6;
	private static final int PRODUCTS = 7;
	private static final int CATEGORIES = 8;
	private static final int SECONDPRODUCTS = 9;
	private static final int SECONDCATEGORIES = 10;
	private static final int EXCLUDEDPRODUCTS = 11;
	private static final int GIFTPRODUCT = 12;
	private static final int QUALIFYINGCOUNT = 13;
	private static final int FREECOUNT = 14;
	private static final int MINIMUMAMOUNT = 15;
	private static final int TITLE = 16;
	private static final int PERCENTAGEORAMOUNT = 17;
	private static final int PERCENTAGEDISCOUNT = 18;
	private static final int DELIVERYCOST = 19;
	private static final int DISCOUNTPRICECURRENCY = 20;
	private static final int DISCOUNTPRICEAMOUNT = 21;
	private static final int THRESHOLDCURRENCY = 22;
	private static final int THRESHOLDAMOUNT = 23;
	private static final int MAXDISCOUNT = 24;
	private static final int STARTDATE = 25;
	private static final int ENDDATE = 26;

	/**
	 * @Description: For Creating Promotion in bulk
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */
	@Override
	public void processUpdateForPromotions(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded)
	{
		LOG.debug("Generationg Promotions");
		while (reader.readNextLine())
		{
			final Map<Integer, String> line = reader.getLine();
			final StringBuilder invalidColumns = new StringBuilder();

			final String code = line.get(Integer.valueOf(PROMOTYPE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PROMOTYPE", code);

			final String promotioncode = line.get(Integer.valueOf(PROMOTIONCODE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PROMOTIONCODE", promotioncode);

			final String promotiongroup = line.get(Integer.valueOf(PROMOTIONGROUP));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PROMOTIONGROUP", promotiongroup);

			final String description = line.get(Integer.valueOf(DESCRIPTION));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "DESCRIPTION", description);

			final String enabled = line.get(Integer.valueOf(ENABLED));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "ENABLED", enabled);

			final String priority = line.get(Integer.valueOf(PRIORITY));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PRIORITY", priority);

			final String channel = line.get(Integer.valueOf(CHANNEL));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "CHANNEL", channel);

			final String product = line.get(Integer.valueOf(PRODUCTS));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PRODUCTS", product);

			final String categories = line.get(Integer.valueOf(CATEGORIES));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "CATEGORIES", categories);

			final String secondProduct = line.get(Integer.valueOf(SECONDPRODUCTS));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "SECONDPRODUCTS", secondProduct);

			final String secondCategories = line.get(Integer.valueOf(SECONDCATEGORIES));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "SECONDCATEGORIES", secondCategories);

			final String excludedProducts = line.get(Integer.valueOf(EXCLUDEDPRODUCTS));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "EXCLUDEDPRODUCTS", excludedProducts);

			final String giftProduct = line.get(Integer.valueOf(GIFTPRODUCT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "GIFTPRODUCT", giftProduct);

			final String qualifyingCount = line.get(Integer.valueOf(QUALIFYINGCOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "QUALIFYINGCOUNT", qualifyingCount);

			final String freeCount = line.get(Integer.valueOf(FREECOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "CODE", freeCount);

			final String minimumAmount = line.get(Integer.valueOf(MINIMUMAMOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "MINIMUMAMOUNT", minimumAmount);

			final String quantity = line.get(Integer.valueOf(TITLE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "TITLE", quantity);

			final String percentageOrAmount = line.get(Integer.valueOf(PERCENTAGEORAMOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PERCENTAGEORAMOUNT", percentageOrAmount);

			final String percentageDiscount = line.get(Integer.valueOf(PERCENTAGEDISCOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "PERCENTAGEDISCOUNT", percentageDiscount);

			final String deliveryCost = line.get(Integer.valueOf(DELIVERYCOST));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "DELIVERYCOST", deliveryCost);

			final String disCurrency = line.get(Integer.valueOf(DISCOUNTPRICECURRENCY));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "DISCOUNTPRICECURRENCY", disCurrency);

			final String disAmount = line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "DISCOUNTPRICEAMOUNT", disAmount);

			final String thresholdCurrency = line.get(Integer.valueOf(THRESHOLDCURRENCY));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "THRESHOLDCURRENCY", thresholdCurrency);

			final String thresholdAmount = line.get(Integer.valueOf(THRESHOLDAMOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "THRESHOLDAMOUNT", thresholdAmount);

			final String maxDis = line.get(Integer.valueOf(MAXDISCOUNT));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "MAXDISCOUNT", maxDis);

			final String startDate = line.get(Integer.valueOf(STARTDATE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "STARTDATE", startDate);

			final String endDate = line.get(Integer.valueOf(ENDDATE));
			bulkPromotionErrorHandling.addInvalidColumnName(invalidColumns, "ENDDATE", endDate);

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
		boolean isIncorrectCode = false;
		try
		{
			if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.BUYAPERCENTAGEDISCOUNT))
			{
				createBuyAPercentageDiscountPromotion(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.BUYAANDBPERCENTAGEDISCOUNT))
			{
				createBuyAandBPercentageDiscountPromotion(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.BOGO))
			{
				createBOGOPromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.BUYXOFAGETBFREEANDDISCOUNT))
			{
				createBuyXofAGetBFree(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.BUYAANDBGETC))
			{
				createBuyAandBGetC(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.BUYABFREEPERCENTAGEDISCOUNT))
			{
				createBuyABFreeAndDiscount(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CASHBACKPROMO))
			{
				createBuyACashBackPromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.ABCASHBACKPROMO))
			{
				createBuyABCashBackPromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CARTDISCOUNTPROMO))
			{
				createCartDiscountPromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CARTFREEBIEPROMO))
			{
				createCartFreebiePromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CARTCASHBACKPROMO))
			{
				createCartCashBackPromo(line, writer);
			}
			else if (line.get(Integer.valueOf(PROMOTYPE)).equalsIgnoreCase(
					MarketplacecommerceservicesConstants.BUYAABOVEXGETPERCENTAGEORAMOUNTOFF))
			{
				createProductThreshPromo(line, writer);
			}
			else
			{
				isIncorrectCode = true;
				final List<Integer> errorColumnList = errorListData(isIncorrectCode);
				try
				{
					bulkPromotionErrorHandling.writeErrorData(writer, line, errorColumnList, line,
							MarketplacecommerceservicesConstants.ERRORMESSAGE);
				}
				catch (final IOException exception)
				{
					LOG.error(exception.getMessage());
				}
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(isIncorrectCode);
			LOG.error(exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
		}
	}


	/**
	 * @Description : To create BuyAAboveXGetPercentageOrAmountOff Promotion
	 * @param line
	 * @param writer
	 */
	private void createProductThreshPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAAboveXGetPercentageOrAmountOffModel oModel = modelService.create(BuyAAboveXGetPercentageOrAmountOffModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}


			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}


			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(THRESHOLDCURRENCY)) && line.get(Integer.valueOf(THRESHOLDCURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(THRESHOLDAMOUNT)) && line.get(Integer.valueOf(THRESHOLDAMOUNT)).trim()
							.length() > 0))
			{
				oModel.setThresholdTotals(returnThresholdData(line));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}

		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}


	}

	/**
	 * @Description : To create Order Level Promotion : Cart Cash Back Promotion
	 * @param line
	 * @param writer
	 */
	private void createCartCashBackPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final CartOrderThresholdDiscountCashbackModel oModel = modelService.create(CartOrderThresholdDiscountCashbackModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(THRESHOLDCURRENCY)) && line.get(Integer.valueOf(THRESHOLDCURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(THRESHOLDAMOUNT)) && line.get(Integer.valueOf(THRESHOLDAMOUNT)).trim()
							.length() > 0))
			{
				oModel.setThresholdTotals(returnThresholdData(line));
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create Order Level Promotion : CustomOrderThresholdFreeGiftPromotion
	 * @param line
	 * @param writer
	 */
	private void createCartFreebiePromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final CustomOrderThresholdFreeGiftPromotionModel oModel = modelService
				.create(CustomOrderThresholdFreeGiftPromotionModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(THRESHOLDCURRENCY)) && line.get(Integer.valueOf(THRESHOLDCURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(THRESHOLDAMOUNT)) && line.get(Integer.valueOf(THRESHOLDAMOUNT)).trim()
							.length() > 0))
			{
				oModel.setThresholdTotals(returnThresholdData(line));
			}

			if (null != line.get(Integer.valueOf(GIFTPRODUCT)) && line.get(Integer.valueOf(GIFTPRODUCT)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchGiftProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setGiftProducts(productList);
				}
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}

		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}
	}

	/**
	 * @Description : To create Order Level Promotion : CartOrderThresholdDiscountPromotion
	 * @param line
	 * @param writer
	 */
	private void createCartDiscountPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final CartOrderThresholdDiscountPromotionModel oModel = modelService.create(CartOrderThresholdDiscountPromotionModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}


			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(THRESHOLDCURRENCY)) && line.get(Integer.valueOf(THRESHOLDCURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(THRESHOLDAMOUNT)) && line.get(Integer.valueOf(THRESHOLDAMOUNT)).trim()
							.length() > 0))
			{
				oModel.setThresholdTotals(returnThresholdData(line));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create Buy A and B get Cash Back Promotion
	 * @param line
	 * @param writer
	 */
	private void createBuyABCashBackPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAandBGetPrecentageDiscountCashbackModel oModel = modelService
				.create(BuyAandBGetPrecentageDiscountCashbackModel.class);
		if (errorColumnList.isEmpty())
		{

			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(SECONDPRODUCTS)) && line.get(Integer.valueOf(SECONDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchSecProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setSecondProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(SECONDCATEGORIES))
					&& line.get(Integer.valueOf(SECONDCATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchSecCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setSecondCategories(categoryList);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Max Discount Value
			if (null != line.get(Integer.valueOf(MAXDISCOUNT)) && line.get(Integer.valueOf(MAXDISCOUNT)).trim().length() > 0)
			{
				oModel.setMaxDiscount(Double.valueOf(line.get(Integer.valueOf(MAXDISCOUNT))));
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}

		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}
	}

	/**
	 * @Description : To create Buy A get Cash Back Promotion
	 * @param line
	 * @param writer
	 */
	private void createBuyACashBackPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAGetPrecentageDiscountCashbackModel oModel = modelService.create(BuyAGetPrecentageDiscountCashbackModel.class);
		if (errorColumnList.isEmpty())
		{

			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}


			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Qualifying Count Data
			if (null != line.get(Integer.valueOf(QUALIFYINGCOUNT)) && line.get(Integer.valueOf(QUALIFYINGCOUNT)).trim().length() > 0)
			{
				oModel.setQuantity(Long.valueOf(line.get(Integer.valueOf(QUALIFYINGCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Max Discount Value
			if (null != line.get(Integer.valueOf(MAXDISCOUNT)) && line.get(Integer.valueOf(MAXDISCOUNT)).trim().length() > 0)
			{
				oModel.setMaxDiscount(Double.valueOf(line.get(Integer.valueOf(MAXDISCOUNT))));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create Buy A get B Free and Percentage /Amount Discount
	 * @param line
	 * @param writer
	 */
	private void createBuyABFreeAndDiscount(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyABFreePrecentageDiscountModel oModel = modelService.create(BuyABFreePrecentageDiscountModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}


			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			if (null != line.get(Integer.valueOf(GIFTPRODUCT)) && line.get(Integer.valueOf(GIFTPRODUCT)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchGiftProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setGiftProducts(productList);
				}
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Qualifying Count Data
			if (null != line.get(Integer.valueOf(QUALIFYINGCOUNT)) && line.get(Integer.valueOf(QUALIFYINGCOUNT)).trim().length() > 0)
			{
				oModel.setQuantity(Long.valueOf(line.get(Integer.valueOf(QUALIFYINGCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Max Discount Value
			if (null != line.get(Integer.valueOf(MAXDISCOUNT)) && line.get(Integer.valueOf(MAXDISCOUNT)).trim().length() > 0)
			{
				oModel.setMaxDiscountVal(Double.valueOf(line.get(Integer.valueOf(MAXDISCOUNT))));
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}
	}

	/**
	 * @Description : To create Buy A and B Get C Promotion
	 * @param line
	 * @param writer
	 */
	private void createBuyAandBGetC(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAandBgetCModel oModel = modelService.create(BuyAandBgetCModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}


			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			if (null != line.get(Integer.valueOf(GIFTPRODUCT)) && line.get(Integer.valueOf(GIFTPRODUCT)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchGiftProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setGiftProducts(productList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(SECONDPRODUCTS)) && line.get(Integer.valueOf(SECONDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchSecProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setSecondProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(SECONDCATEGORIES))
					&& line.get(Integer.valueOf(SECONDCATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchSecCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setSecondCategories(categoryList);
				}
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}


			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}

		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create Buy X of A get B Free
	 * @param line
	 * @param writer
	 */
	private void createBuyXofAGetBFree(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyXItemsofproductAgetproductBforfreeModel oModel = modelService
				.create(BuyXItemsofproductAgetproductBforfreeModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}


			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			if (null != line.get(Integer.valueOf(GIFTPRODUCT)) && line.get(Integer.valueOf(GIFTPRODUCT)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchGiftProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setGiftProducts(productList);
				}
			}

			//Adding Qualifying Count Data
			if (null != line.get(Integer.valueOf(QUALIFYINGCOUNT)) && line.get(Integer.valueOf(QUALIFYINGCOUNT)).trim().length() > 0)
			{
				oModel.setQualifyingCount(Integer.valueOf(line.get(Integer.valueOf(QUALIFYINGCOUNT))));
			}


			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create CustomProductBOGOFPromotion
	 * @param line
	 * @param writer
	 */
	private void createBOGOPromo(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final CustomProductBOGOFPromotionModel oModel = modelService.create(CustomProductBOGOFPromotionModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}


			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}


			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Qualifying Count Data
			if (null != line.get(Integer.valueOf(QUALIFYINGCOUNT)) && line.get(Integer.valueOf(QUALIFYINGCOUNT)).trim().length() > 0)
			{
				oModel.setQualifyingCount(Integer.valueOf(line.get(Integer.valueOf(QUALIFYINGCOUNT))));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}
	}

	/**
	 * @Description : To create Buy A and B Percentage Discount
	 * @param line
	 * @param writer
	 */
	private void createBuyAandBPercentageDiscountPromotion(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAandBPrecentageDiscountModel oModel = modelService.create(BuyAandBPrecentageDiscountModel.class);
		if (errorColumnList.isEmpty())
		{

			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(SECONDPRODUCTS)) && line.get(Integer.valueOf(SECONDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchSecProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setSecondProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(SECONDCATEGORIES))
					&& line.get(Integer.valueOf(SECONDCATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchSecCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setSecondCategories(categoryList);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Max Discount Value
			if (null != line.get(Integer.valueOf(MAXDISCOUNT)) && line.get(Integer.valueOf(MAXDISCOUNT)).trim().length() > 0)
			{
				oModel.setMaxDiscount(Double.valueOf(line.get(Integer.valueOf(MAXDISCOUNT))));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}
		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @Description : To create BuyAPrecentageDiscountPromotion
	 * @param line
	 * @param writer
	 */
	private void createBuyAPercentageDiscountPromotion(final Map<Integer, String> line, final CSVWriter writer)
	{
		final List<Integer> errorColumnList = errorListData(false);
		final BuyAPercentageDiscountModel oModel = modelService.create(BuyAPercentageDiscountModel.class);
		if (errorColumnList.isEmpty())
		{
			//Adding Promotion Code
			if (null != line.get(Integer.valueOf(PROMOTIONCODE)) && line.get(Integer.valueOf(PROMOTIONCODE)).trim().length() > 0)
			{
				oModel.setCode(line.get(Integer.valueOf(PROMOTIONCODE)));
			}

			//Adding Promotion Group
			if (null != line.get(Integer.valueOf(PROMOTIONGROUP)) && line.get(Integer.valueOf(PROMOTIONGROUP)).trim().length() > 0)
			{
				final PromotionGroupModel oGroupModel = bulkPromotionCreationDao.fetchPromotionGroup(line.get(Integer
						.valueOf(PROMOTIONGROUP)));
				if (null != oGroupModel)
				{
					oModel.setPromotionGroup(oGroupModel);
				}
			}

			//Adding Description
			if (null != line.get(Integer.valueOf(DESCRIPTION)) && line.get(Integer.valueOf(DESCRIPTION)).trim().length() > 0)
			{
				oModel.setDescription(line.get(Integer.valueOf(DESCRIPTION)));
			}

			//Adding Title
			if (null != line.get(Integer.valueOf(TITLE)) && line.get(Integer.valueOf(TITLE)).trim().length() > 0)
			{
				oModel.setTitle(line.get(Integer.valueOf(TITLE)));
			}

			//Adding Enable Data
			if (null != line.get(Integer.valueOf(ENABLED)) && line.get(Integer.valueOf(ENABLED)).trim().length() > 0)
			{
				oModel.setEnabled(Boolean.valueOf(returnEnabledData(line)));
			}

			//Adding Priority Data
			if (null != line.get(Integer.valueOf(PRIORITY)) && line.get(Integer.valueOf(PRIORITY)).trim().length() > 0)
			{
				oModel.setPriority(Integer.valueOf(line.get(Integer.valueOf(PRIORITY))));
			}

			//Adding Channel Data
			if (null != line.get(Integer.valueOf(CHANNEL)) && line.get(Integer.valueOf(CHANNEL)).trim().length() > 0)
			{
				oModel.setChannel(returnChannelData(line));
			}


			//Adding Product Data
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setProducts(productList);
				}
			}

			//Adding Category Data
			if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					oModel.setCategories(categoryList);
				}
			}

			//Adding Product Data
			if (null != line.get(Integer.valueOf(EXCLUDEDPRODUCTS))
					&& line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchExcludedProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					oModel.setExcludedProducts(productList);
				}
			}

			//Adding Category Minimum Amount Data
			if (null != line.get(Integer.valueOf(MINIMUMAMOUNT)) && line.get(Integer.valueOf(MINIMUMAMOUNT)).trim().length() > 0)
			{
				oModel.setMinimumAmount(Double.valueOf(line.get(Integer.valueOf(MINIMUMAMOUNT))));
			}

			//Adding Percentage or Amount
			if (null != line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() > 0)
			{
				oModel.setPercentageOrAmount(Boolean.valueOf(returnIsPercentageOrAmnt(line)));
			}

			//Adding Percentage or Amount Value
			if (null != line.get(Integer.valueOf(PERCENTAGEDISCOUNT))
					&& line.get(Integer.valueOf(PERCENTAGEDISCOUNT)).trim().length() > 0)
			{
				oModel.setPercentageDiscount(Double.valueOf(line.get(Integer.valueOf(PERCENTAGEDISCOUNT))));
			}

			//Adding Qualifying Count Data
			if (null != line.get(Integer.valueOf(QUALIFYINGCOUNT)) && line.get(Integer.valueOf(QUALIFYINGCOUNT)).trim().length() > 0)
			{
				oModel.setQuantity(Long.valueOf(line.get(Integer.valueOf(QUALIFYINGCOUNT))));
			}

			//Adding Discount Data TODO: Presently considering only one price row is added
			if ((null != line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)) && line.get(Integer.valueOf(DISCOUNTPRICECURRENCY)).trim()
					.length() > 0)
					&& (null != line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT)) && line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))
							.trim().length() > 0))
			{
				oModel.setDiscountPrices(returnDiscountData(line));
			}

			//Adding Max Discount Value
			if (null != line.get(Integer.valueOf(MAXDISCOUNT)) && line.get(Integer.valueOf(MAXDISCOUNT)).trim().length() > 0)
			{
				oModel.setMaxDiscountVal(Double.valueOf(line.get(Integer.valueOf(MAXDISCOUNT))));
			}

			//Adding Promotion Start Date
			if (null != line.get(Integer.valueOf(STARTDATE)) && line.get(Integer.valueOf(STARTDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(STARTDATE)));
				if (null != date)
				{
					oModel.setStartDate(date);
				}
			}

			//Adding Promotion End Date
			if (null != line.get(Integer.valueOf(ENDDATE)) && line.get(Integer.valueOf(ENDDATE)).trim().length() > 0)
			{
				final Date date = GenericUtilityMethods.returnDateData(line.get(Integer.valueOf(ENDDATE)));
				if (null != date)
				{
					oModel.setEndDate(date);
				}
			}

			try
			{
				modelService.save(oModel);
				populateProductRelData(oModel, line);
			}
			catch (final ModelSavingException | ModelNotFoundException exception)
			{
				LOG.error(exception.getMessage());
				populateErrorEntry(line, writer, errorColumnList);
			}

		}
		else
		{
			populateErrorEntry(line, writer, errorColumnList);
		}

	}

	/**
	 * @param oModel
	 * @param line
	 */
	private void populateProductRelData(final ProductPromotionModel oModel, final Map<Integer, String> line)
	{
		try
		{
			List<ProductPromotionModel> promotionList = null;
			if (null != line.get(Integer.valueOf(PRODUCTS)) && line.get(Integer.valueOf(PRODUCTS)).trim().length() > 0)
			{
				List<ProductModel> productList = new ArrayList<>();
				productList = fetchPrimaryProductData(getDefaultPromotionsManager().catalogData(), line);
				if (!productList.isEmpty())
				{
					for (final ProductModel productModel : productList)
					{
						if (null != productModel && null != productModel.getPromotions() && !productModel.getPromotions().isEmpty())
						{
							promotionList = new ArrayList<>(productModel.getPromotions());
							promotionList.add(oModel);
							productModel.setPromotions(promotionList);
							modelService.save(productModel);
						}
					}
				}
			}
			else if (null != line.get(Integer.valueOf(CATEGORIES)) && line.get(Integer.valueOf(CATEGORIES)).trim().length() > 0)
			{
				List<CategoryModel> categoryList = new ArrayList<>();
				categoryList = fetchCategoryData(getDefaultPromotionsManager().catalogData(), line);
				if (!categoryList.isEmpty())
				{
					for (final CategoryModel categoryModel : categoryList)
					{
						if (null != categoryModel && null != categoryModel.getPromotions() && !categoryModel.getPromotions().isEmpty())
						{
							promotionList = new ArrayList<>(categoryModel.getPromotions());
							promotionList.add(oModel);
							categoryModel.setPromotions(promotionList);
							modelService.save(categoryModel); //Adding the Product Data
						}
					}
				}
			}
		}
		catch (ModelNotFoundException | ModelSavingException exception)
		{
			LOG.error(exception.getMessage());
		}
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
	 * @Description : Returns Threshold Data
	 * @param line
	 * @return priceData
	 */
	private List<PromotionPriceRowModel> returnThresholdData(final Map<Integer, String> line)
	{
		final PromotionPriceRowModel priceRowModel = new PromotionPriceRowModel();
		final List<PromotionPriceRowModel> priceData = new ArrayList<>();
		final CurrencyModel currencyModel = bulkPromotionCreationDao.fetchCurrencyData(line.get(Integer.valueOf(THRESHOLDCURRENCY))
				.toUpperCase());
		if (null != currencyModel)
		{
			priceRowModel.setCurrency(currencyModel);
			priceRowModel.setPrice(Double.valueOf(line.get(Integer.valueOf(THRESHOLDAMOUNT))));
			priceData.add(priceRowModel);
		}
		return priceData;
	}


	/**
	 * @Description : Returns Discount Data
	 * @param line
	 * @return priceData
	 */
	private List<PromotionPriceRowModel> returnDiscountData(final Map<Integer, String> line)
	{
		final PromotionPriceRowModel priceRowModel = new PromotionPriceRowModel();
		final List<PromotionPriceRowModel> priceData = new ArrayList<>();
		final CurrencyModel currencyModel = bulkPromotionCreationDao.fetchCurrencyData(line.get(
				Integer.valueOf(DISCOUNTPRICECURRENCY)).toUpperCase());
		if (null != currencyModel)
		{
			priceRowModel.setCurrency(currencyModel);
			priceRowModel.setPrice(Double.valueOf(line.get(Integer.valueOf(DISCOUNTPRICEAMOUNT))));
			priceData.add(priceRowModel);
		}
		return priceData;
	}


	/**
	 * @Description : Returns Whether Percentage or amount
	 * @param line
	 * @return flag
	 */
	private boolean returnIsPercentageOrAmnt(final Map<Integer, String> line)
	{
		boolean flag = false;
		if (null == line.get(Integer.valueOf(PERCENTAGEORAMOUNT))
				|| line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).trim().length() == 0)
		{
			flag = false;
		}
		else
		{
			if (line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				flag = true;
			}
			else if (line.get(Integer.valueOf(PERCENTAGEORAMOUNT)).equalsIgnoreCase(MarketplacecommerceservicesConstants.FALSE))
			{
				flag = false;
			}
			else
			{
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * @Description : Return Priority Data
	 * @param line
	 * @return isEnabled
	 */
	private boolean returnEnabledData(final Map<Integer, String> line)
	{
		boolean isEnabled = false;
		if (null == line.get(Integer.valueOf(ENABLED)) || line.get(Integer.valueOf(ENABLED)).trim().length() == 0)
		{
			isEnabled = false;
		}
		else
		{
			if (line.get(Integer.valueOf(ENABLED)).equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				isEnabled = true;
			}
			else if (line.get(Integer.valueOf(ENABLED)).equalsIgnoreCase(MarketplacecommerceservicesConstants.FALSE))
			{
				isEnabled = false;
			}
		}
		return isEnabled;
	}

	/**
	 * @Description : Return Channel Data
	 * @param line
	 * @return salesApplication
	 */
	private List<SalesApplication> returnChannelData(final Map<Integer, String> line)
	{
		final List<SalesApplication> salesApplication = new ArrayList<SalesApplication>();
		if (line.get(Integer.valueOf(CHANNEL)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEB))
		{
			salesApplication.add(SalesApplication.WEB);
		}
		else if (line.get(Integer.valueOf(CHANNEL)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEBMOBILE))
		{
			salesApplication.add(SalesApplication.WEBMOBILE);
		}
		else if (line.get(Integer.valueOf(CHANNEL)).equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER))
		{
			salesApplication.add(SalesApplication.CALLCENTER);
		}
		return salesApplication;
	}



	/**
	 * @Description: Validates Data Uploaded
	 * @param isIncorrectCode
	 * @return data
	 */
	private List<Integer> errorListData(final boolean isIncorrectCode)
	{
		final List<Integer> data = new ArrayList<>();

		if (isIncorrectCode)
		{
			data.add(Integer.valueOf(PROMOTYPE));
		}
		return data;
	}




	/**
	 * @Description : Returns List of Promotion Product and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return productList
	 */
	private List<ProductModel> fetchPrimaryProductData(final CatalogVersionModel catalogVersionModel,
			final Map<Integer, String> line)
	{
		List<ProductModel> productList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(PRODUCTS)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String productString = line.get(Integer.valueOf(PRODUCTS));
				String[] productArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				productArray = productString.split(Pattern.quote(delimiter));

				for (int i = 0; i < productArray.length; i++)
				{
					final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel, productArray[i]);
					if (null != productData)
					{
						productList.add(productData);
					}
				}
			}
			else
			{
				final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel,
						line.get(Integer.valueOf(PRODUCTS)));
				if (null != productData)
				{
					productList.add(productData);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			productList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return productList;
	}

	/**
	 * @Description : Returns List of Promotion Product and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return productList
	 */
	private List<ProductModel> fetchSecProductData(final CatalogVersionModel catalogVersionModel, final Map<Integer, String> line)
	{
		List<ProductModel> productList = new ArrayList<>();

		try
		{
			if (line.get(Integer.valueOf(SECONDPRODUCTS)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String productString = line.get(Integer.valueOf(SECONDPRODUCTS));
				String[] productArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				productArray = productString.split(Pattern.quote(delimiter));

				for (int i = 0; i < productArray.length; i++)
				{
					final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel, productArray[i]);
					if (null != productData)
					{
						productList.add(productData);
					}
				}
			}
			else
			{
				final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel,
						line.get(Integer.valueOf(SECONDPRODUCTS)));
				if (null != productData)
				{
					productList.add(productData);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			productList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}

		return productList;
	}


	/**
	 * @Description : Returns List of Promotion Product and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return productList
	 */
	private List<ProductModel> fetchExcludedProductData(final CatalogVersionModel catalogVersionModel,
			final Map<Integer, String> line)
	{
		List<ProductModel> productList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(EXCLUDEDPRODUCTS)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String productString = line.get(Integer.valueOf(EXCLUDEDPRODUCTS));
				String[] productArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				productArray = productString.split(Pattern.quote(delimiter));

				for (int i = 0; i < productArray.length; i++)
				{
					final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel, productArray[i]);
					if (null != productData)
					{
						productList.add(productData);
					}
				}
			}
			else
			{
				final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel,
						line.get(Integer.valueOf(EXCLUDEDPRODUCTS)));
				if (null != productData)
				{
					productList.add(productData);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			productList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return productList;
	}


	/**
	 * @Description : Returns List of Promotion Gift Product and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return productList
	 */
	private List<ProductModel> fetchGiftProductData(final CatalogVersionModel catalogVersionModel, final Map<Integer, String> line)
	{
		List<ProductModel> productList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(GIFTPRODUCT)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String productString = line.get(Integer.valueOf(GIFTPRODUCT));
				String[] productArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				productArray = productString.split(Pattern.quote(delimiter));

				for (int i = 0; i < productArray.length; i++)
				{
					final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel, productArray[i]);
					if (null != productData)
					{
						productList.add(productData);
					}
				}
			}
			else
			{
				final ProductModel productData = bulkPromotionCreationDao.fetchProductData(catalogVersionModel,
						line.get(Integer.valueOf(GIFTPRODUCT)));
				if (null != productData)
				{
					productList.add(productData);
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			productList = new ArrayList<>();
			LOG.debug(exception.getMessage());
		}
		return productList;
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
			if (line.get(Integer.valueOf(CATEGORIES)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String categoryString = line.get(Integer.valueOf(CATEGORIES));
				String[] categoryArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				categoryArray = categoryString.split(Pattern.quote(delimiter));

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
						line.get(Integer.valueOf(CATEGORIES)));
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
	 * @Description : Returns List of Promotion Categories and corresponding Model
	 * @param catalogVersionModel
	 * @param line
	 * @return categoryList
	 */
	private List<CategoryModel> fetchSecCategoryData(final CatalogVersionModel catalogVersionModel, final Map<Integer, String> line)
	{
		List<CategoryModel> categoryList = new ArrayList<>();
		try
		{
			if (line.get(Integer.valueOf(SECONDCATEGORIES)).contains(MarketplacecommerceservicesConstants.promotionProductDelimiter))
			{
				final String categoryString = line.get(Integer.valueOf(SECONDCATEGORIES));
				String[] categoryArray;
				final String delimiter = MarketplacecommerceservicesConstants.promotionProductDelimiter;
				categoryArray = categoryString.split(Pattern.quote(delimiter));

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
						line.get(Integer.valueOf(SECONDCATEGORIES)));
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

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}

