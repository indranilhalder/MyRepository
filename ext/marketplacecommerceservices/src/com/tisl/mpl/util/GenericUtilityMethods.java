/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.QCWalletPaymentInfoModel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.ui.Model;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.jalo.EtailExcludeSellerSpecificRestriction;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.ManufacturesRestriction;
import com.tisl.mpl.jalo.SellerMaster;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.OrderConfirmationWsDTO;


/**
 * @author TCS
 *
 */
public class GenericUtilityMethods
{
	private static final Logger LOG = Logger.getLogger(GenericUtilityMethods.class);
	public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";
	private static final String MISSING_IMAGE_URL = "/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";
	private static final String REGEX = "[^\\w\\s]";


	/**
	 * @Description: Checks whether the requested Date lies within range provided
	 * @param start
	 * @param end
	 * @param comparableDate
	 * @return status
	 */
	public static boolean compareDate(final Date start, final Date end, final Date comparableDate)
	{
		boolean status = false;
		if (comparableDate.after(start) && comparableDate.before(end))
		{
			status = true;
		}
		else if (comparableDate.equals(start) || comparableDate.equals(end))
		{
			status = true;
		}
		else
		{
			status = false;
		}
		return status;
	}

	public static Object jsonToObject(final Class<?> classType, final String stringJson) throws JsonParseException,
			JsonMappingException, IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(stringJson, classType);
	}

	/**
	 * @Description: Sends the year from Date
	 * @param : date
	 * @return year
	 */
	public static String redirectYear(final Date date)
	{
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.YYYYMMDD);
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split(MarketplacecommerceservicesConstants.FRONTSLASH);

			final String year = bDayStringArry[0];

			return year;

		}
		catch (final Exception e)
		{
			return null;
		}

	}

	/**
	 * @Description: Modifies Date with the required Year
	 * @param : date,yeartoModify
	 * @return modifedDate
	 */
	public static Date modifiedBDate(final Date date, final String yeartoModify)
	{
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split(MarketplacecommerceservicesConstants.FRONTSLASH);

			final String month = bDayStringArry[1];
			final String day = bDayStringArry[0];


			final String modifiedBDay = day + MarketplacecommerceservicesConstants.FRONTSLASH + month
					+ MarketplacecommerceservicesConstants.FRONTSLASH + yeartoModify;

			final Date modifedDate = dateFormat.parse(modifiedBDay);

			return modifedDate;
		}
		catch (final Exception e)
		{
			return null;
		}

	}

	/**
	 * @Description: Modifies the System Date according to the format dd/MM/yyyy
	 * @param date
	 * @return modifedDate
	 */
	public static Date modifiedSysDate(final Date date)
	{
		Date modifedDate = null;
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split(MarketplacecommerceservicesConstants.FRONTSLASH);

			final String month = bDayStringArry[1];
			final String day = bDayStringArry[0];
			final String year = bDayStringArry[2];

			final String modifiedBDay = day + MarketplacecommerceservicesConstants.FRONTSLASH + month
					+ MarketplacecommerceservicesConstants.FRONTSLASH + year;

			modifedDate = dateFormat.parse(modifiedBDay);

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return modifedDate;

	}

	/**
	 * @Description: Calculates Number of Days Between Dates
	 * @param date1
	 * @param date2
	 * @return noOfDays
	 */
	public static int noOfDaysCalculatorBetweenDates(final Date date1, final Date date2)
	{
		int noOfDays = 0;
		if (null != date1 && null != date2)
		{
			Date dateBefore = null, dateAfter = null;
			if (date1.before(date2))
			{
				dateBefore = date1;
				dateAfter = date2;
			}
			else if (date1.after(date2))
			{
				dateBefore = date2;
				dateAfter = date1;
			}
			else if (date1.equals(date2))
			{
				dateBefore = date1;
				dateAfter = date1;
			}
			// Change regarding INC144316821
			double result;
			result = ((dateAfter.getTime() - dateBefore.getTime()) / (1000.0 * 60.0 * 60.0 * 24.0));
			noOfDays = (int) Math.ceil(result);
			//	noOfDays = (int) ((dateAfter.getTime() - dateBefore.getTime()) / (1000 * 60 * 60 * 24));
		}
		return noOfDays;
	}

	/**
	 * @Description: Compares with System Date
	 * @param : date
	 * @return flag
	 */
	public static boolean compareDateWithSysDate(final Date date)
	{
		boolean flag = false;
		try
		{
			final Date sysDate = modifiedSysDate(new Date());
			if (sysDate.equals(date))
			{
				flag = true;
			}
		}
		catch (final Exception e)
		{
			flag = false;
		}
		return flag;
	}

	/**
	 * @Description: @Promtion: Checks for Excluded Products
	 * @param product
	 * @param excludedProductList
	 * @return boolean
	 */
	public static boolean isProductExcluded(final Product product, final List<Product> excludedProductList)
	{
		if (CollectionUtils.isNotEmpty(excludedProductList) && excludedProductList.contains(product))
		{
			LOG.debug("Product code:" + product.getCode() + " is in the excluded list.");
			return true;
		}
		return false;
	}

	/**
	 * @Description: @Promtion: Checks Excluded Manufacturer Restriction
	 * @param : List<AbstractPromotionRestriction> restrictionLists
	 * @param restrictionList
	 * @return manufactureList
	 */
	//	public static List<String> getExcludeManufactureList(final List<AbstractPromotionRestriction> restrictionList)
	//	{
	//		final List<String> manufactureList = new ArrayList<String>();
	//		for (final AbstractPromotionRestriction restriction : restrictionList)
	//		{
	//			if (restriction instanceof ExcludeManufacturesRestriction)
	//			{
	//				final ExcludeManufacturesRestriction excludeManufacturesRestriction = (ExcludeManufacturesRestriction) restriction;
	//				final List<Category> excludeBrandList = (List<Category>) excludeManufacturesRestriction.getManufacturers();
	//				for (final Category excludeBrand : excludeBrandList)
	//				{
	//					manufactureList.add(excludeBrand.getName());
	//				}
	//			}
	//		}
	//
	//		return manufactureList;
	//	}

	/**
	 * @Description: @Promtion: Checks whether Product Exist in Category
	 * @param categoryList
	 * @param productCategoryList
	 * @return boolean
	 */
	public static boolean productExistsIncat(final List<Category> categoryList, final List<String> productCategoryList)
	{
		if (null != categoryList && !categoryList.isEmpty() && null != productCategoryList && !productCategoryList.isEmpty())
		{
			for (final Category category : categoryList)
			{
				if (productCategoryList.contains(category.getCode()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @Description: @Promtion: Checks whther product lies in Excluded Manufacture List
	 * @param product
	 * @param excludedManufactureList
	 * @return boolean
	 */
	public static boolean isProductExcludedForManufacture(final Product product, final List<String> excludedManufactureList)
	{
		boolean flag = false;
		if (null != excludedManufactureList)
		{
			flag = getDefaultPromotionsManager().excludeBrandDataCheck(excludedManufactureList, product);
		}

		return flag;
	}



	/**
	 *
	 * @param date
	 * @return int
	 */
	public static int daysBetweenPresentDateAndGivenDate(final Date date)
	{

		final SimpleDateFormat myFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATEFORMATMMDDYYYY);

		final Date presentDate = new Date();
		long diffDays = 0L;
		try
		{
			final String date1 = myFormat.format(date);


			final String date2 = myFormat.format(presentDate);

			final Date pwdChngDate = myFormat.parse(date1);
			final Date prsntDate = myFormat.parse(date2);
			final long diff = prsntDate.getTime() - pwdChngDate.getTime();
			diffDays = diff / (24 * 60 * 60 * 1000);
		}
		catch (final ParseException e)
		{
			LOG.debug(e.getMessage());
		}
		return (int) diffDays;
	}

	/**
	 * @Description: Convert System Date to String
	 * @param date
	 * @return modifiedDay
	 */
	public static String convertSysDateToString(final Date date)
	{
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
			final String dayString = dateFormat.format(date);
			final String dayStringArry[] = dayString.split(MarketplacecommerceservicesConstants.FRONTSLASH);

			final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			final String timeFormatString = timeFormat.format(date);
			final String timeFormatStringArry[] = timeFormatString.split(MarketplacecommerceservicesConstants.COLON);

			final String month = dayStringArry[1];
			final String day = dayStringArry[0];
			final String year = dayStringArry[2];

			final String hour = timeFormatStringArry[0];
			final String min = timeFormatStringArry[1];
			//final String sec = timeFormatStringArry[2];

			final String modifiedBDay = day + MarketplacecommerceservicesConstants.UNDER_SCORE + month
					+ MarketplacecommerceservicesConstants.UNDER_SCORE + year + MarketplacecommerceservicesConstants.LEFT_PARENTHESIS
					+ hour + MarketplacecommerceservicesConstants.UNDER_SCORE + min
					+ MarketplacecommerceservicesConstants.RIGHT_PARENTHESIS;
			return modifiedBDay;
		}
		catch (final Exception e)
		{
			LOG.debug(e.getMessage());
			return null;

		}
	}

	/**
	 *
	 * @param sellerDataList
	 * @return sellerArticleSKUs
	 */
	public static String getSellersUSSIDs(final List<SellerInformationData> sellerDataList)
	{
		String sellerArticleSKUs = null;
		if (null != sellerDataList && !sellerDataList.isEmpty())
		{
			final StringBuilder sellerArticleSKUsBuilder = new StringBuilder();
			for (final SellerInformationData sellerInformationData : sellerDataList)
			{
				sellerArticleSKUsBuilder.append('\'').append(sellerInformationData.getUssid()).append('\'').append(',');
			}

			sellerArticleSKUs = sellerArticleSKUsBuilder.toString();
		}

		return sellerArticleSKUs;
	}

	/**
	 *
	 * @param sellerDataList
	 * @return sellerArticleSKUs
	 */
	public static String getcommaSepUSSIDs(final List<String> ussidList)
	{
		String sellerArticleSKUs = null;
		if (null != ussidList && !ussidList.isEmpty())
		{
			final StringBuilder sellerArticleSKUsBuilder = new StringBuilder();
			for (final String ussid : ussidList)
			{
				sellerArticleSKUsBuilder.append('\'').append(ussid).append('\'').append(',');
			}

			sellerArticleSKUs = sellerArticleSKUsBuilder.toString();
		}

		return sellerArticleSKUs;
	}


	/**
	 * @Description: Checks whether the Product belong to the brand mentioned in Brand Level Restriction
	 * @param restrictionList
	 * @param product
	 * @return boolean
	 */
	public static boolean checkBrandData(final List<AbstractPromotionRestriction> restrictionList, final Product product)
	{
		boolean applyPromotion = false;
		try
		{
			if (CollectionUtils.isNotEmpty(restrictionList))
			{
				for (final AbstractPromotionRestriction retrManufacturer : restrictionList)
				{
					applyPromotion = false;
					if (retrManufacturer instanceof ManufacturesRestriction)
					{
						final List<String> promotionManufacturerList = new ArrayList<String>();
						final ManufacturesRestriction manufacturesRestriction = (ManufacturesRestriction) retrManufacturer;
						final List<Category> brandList = (List<Category>) manufacturesRestriction.getManufacturers();
						for (final Category restrBrand : brandList)
						{
							promotionManufacturerList.add(restrBrand.getName());
						}
						applyPromotion = getDefaultPromotionsManager().brandDataCheck(promotionManufacturerList, product);
						break;
					}
					else
					{
						applyPromotion = true;
					}
				}
			}
			else
			{
				applyPromotion = true;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return applyPromotion;

	}

	/**
	 * @Description: Verifies Seller Data corresponding to the cart added Product
	 * @param restrictionList
	 * @param productSellerData
	 * @return flag
	 */
	//	public static boolean checkSellerData(final List<AbstractPromotionRestriction> restrictionList,
	//			final List<SellerInformationModel> productSellerData)
	//	{
	//		boolean flag = false;
	//		boolean checkFlag = false;
	//		List<SellerMaster> sellerData = null;
	//		try
	//		{
	//			if (null == restrictionList || restrictionList.isEmpty())
	//			{
	//				flag = true;
	//			}
	//			else
	//			{
	//				if (null != productSellerData)
	//				{
	//					for (final AbstractPromotionRestriction restriction : restrictionList)
	//					{
	//						if (restriction instanceof EtailSellerSpecificRestriction)
	//						{
	//							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
	//							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
	//							for (final SellerMaster seller : sellerData)
	//							{
	//								for (final SellerInformationModel speficSeller : productSellerData)
	//								{
	//									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
	//									{
	//										checkFlag = true;
	//									}
	//								}
	//							}
	//							if (checkFlag)
	//							{
	//								flag = true;
	//								break;
	//							}
	//							else
	//							{
	//								flag = false;
	//								break;
	//							}
	//						}
	//						else
	//						{
	//							flag = true;
	//						}
	//					}
	//				}
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error(e.getMessage());
	//		}
	//		return flag;
	//	}

	/**
	 * @Description: Freebie will not be applied if no Seller Restriction is added
	 * @param restrictionList
	 * @return boolean
	 */
	public static boolean checkRestrictionData(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean flag = false;
		if (null != restrictionList && !restrictionList.isEmpty())
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestriction)
				{
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * @Description: Verifies Seller Data corresponding to the cart added Product
	 * @param restrictionList
	 * @param productSellerData
	 * @return boolean
	 */
	//	public static boolean checkExcludeSellerData(final List<AbstractPromotionRestriction> restrictionList,
	//			final List<SellerInformationModel> productSellerData)
	//	{
	//		boolean excludeSellerFlag = false;
	//		boolean checkFlag = false;
	//		List<SellerMaster> sellerData = null;
	//		try
	//		{
	//			if (null == restrictionList || restrictionList.isEmpty())
	//			{
	//				excludeSellerFlag = false;
	//			}
	//			else
	//			{
	//				if (CollectionUtils.isNotEmpty(productSellerData))
	//				{
	//					for (final AbstractPromotionRestriction restriction : restrictionList)
	//					{
	//						if (restriction instanceof EtailExcludeSellerSpecificRestriction)
	//						{
	//							final EtailExcludeSellerSpecificRestriction excludeSellerRestrict = (EtailExcludeSellerSpecificRestriction) restriction;
	//							sellerData = excludeSellerRestrict.getSellerMasterList();
	//
	//							for (final SellerMaster seller : sellerData)
	//							{
	//								for (final SellerInformationModel speficSeller : productSellerData)
	//								{
	//									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
	//									{
	//										checkFlag = true;
	//									}
	//								}
	//							}
	//
	//							if (checkFlag)
	//							{
	//								excludeSellerFlag = true;
	//								break;
	//							}
	//							else
	//							{
	//								excludeSellerFlag = false;
	//								break;
	//							}
	//
	//						}
	//						else
	//						{
	//							excludeSellerFlag = false;
	//						}
	//					}
	//				}
	//
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error(e.getMessage());
	//		}
	//		return excludeSellerFlag;
	//	}

	/**
	 * @Description : Populate the Excluded Product and Manufacture Data in separate Lists
	 * @param : SessionContext arg0,PromotionEvaluationContext arg1
	 */
	//	public static void populateExcludedProductManufacturerList(final SessionContext arg0, final PromotionEvaluationContext arg1,
	//			final List<Product> excludedProductList, final List<String> excludeManufactureList,
	//			final List<AbstractPromotionRestriction> restrictionList, final ProductPromotion productPromotion)
	//	{
	//		try
	//		{
	//			if (productPromotion.getProperty(arg0, MarketplacecommerceservicesConstants.EXCLUDEDPRODUCTS) != null
	//					&& excludedProductList != null)
	//			{
	//				excludedProductList.addAll((List<Product>) productPromotion.getProperty(arg0,
	//						MarketplacecommerceservicesConstants.EXCLUDEDPRODUCTS));
	//			}
	//			if (excludeManufactureList != null)
	//			{
	//				excludeManufactureList.addAll(getExcludeManufactureList(restrictionList));
	//			}
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
	//		}
	//	}

	/**
	 * @Description: It validates the Brand And Category Minimum Amt
	 * @param validProductUssidMap
	 * @param ctx
	 * @param productPromotion
	 * @param restrictionList
	 * @param promoEvalCtx
	 * @return boolean
	 */

	public static boolean checkBrandAndCategoryMinimumAmt(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final SessionContext ctx, final PromotionEvaluationContext promoEvalCtx, final ProductPromotion productPromotion,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		return (getDefaultPromotionsManager().checkMinimumCategoryValue(validProductUssidMap, ctx, productPromotion) && getDefaultPromotionsManager()
				.checkMinimumBrandAmount(validProductUssidMap, restrictionList));

	}

	/**
	 * @param qCount
	 * @param eligibleProducts
	 * @return noOfProducts
	 */
	public static int calculateNoOfProducts(final long qCount, final int eligibleProducts)
	{
		int noOfProducts = 0;
		int factor = 0;
		if (qCount > 0 && eligibleProducts > 0)
		{
			factor = (int) (eligibleProducts / qCount);
			if (factor > 0)
			{
				factor = factor + 1;
			}

			noOfProducts = ((int) (factor * qCount)) - eligibleProducts;
		}
		return noOfProducts;
	}


	protected static DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	/*
	 * @description Setting DeliveryAddress
	 * 
	 * @param orderDetail
	 * 
	 * @param type (1-Billing, 2-Shipping)
	 * 
	 * @return BillingAddressWsDTO
	 */
	public static BillingAddressWsDTO setAddress(final OrderData orderDetail, final int type)
	{
		final BillingAddressWsDTO billingAddress = new BillingAddressWsDTO();

		if (null != orderDetail.getDeliveryAddress() && StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getId()))
		{
			final String countrycode = MarketplacecommerceservicesConstants.COUNTRYCODE;//"91";
			final AddressData address = orderDetail.getDeliveryAddress();

			if (StringUtils.isNotEmpty(address.getFirstName()))
			{
				billingAddress.setFirstName(address.getFirstName());
			}
			if (StringUtils.isNotEmpty(address.getLastName()))
			{
				billingAddress.setLastName(address.getLastName());
			}
			if (address.getCountry() != null && StringUtils.isNotEmpty(address.getCountry().getName()))
			{
				billingAddress.setCountry(address.getCountry().getName());
			}
			if (StringUtils.isNotEmpty(address.getTown()))
			{
				billingAddress.setTown(address.getTown());
			}
			if (StringUtils.isNotEmpty(address.getPostalCode()))
			{
				billingAddress.setPostalcode(address.getPostalCode());
			}
			if (StringUtils.isNotEmpty(address.getState()))
			{
				billingAddress.setState(address.getState());
			}
			if (StringUtils.isNotEmpty(address.getLine1()))
			{
				billingAddress.setAddressLine1(address.getLine1());
			}
			if (StringUtils.isNotEmpty(address.getLine2()))
			{
				billingAddress.setAddressLine2(address.getLine2());
			}
			if (StringUtils.isNotEmpty(address.getLine3()))
			{
				billingAddress.setAddressLine3(address.getLine3());
			}
			if (StringUtils.isNotEmpty(address.getPhone()))
			{
				billingAddress.setPhone(countrycode + address.getPhone());
			}

			billingAddress.setShippingFlag(Boolean.valueOf(address.isShippingAddress()));
			if (StringUtils.isNotEmpty(address.getId()))
			{
				billingAddress.setId(address.getId());
			}
			if (type == 2)
			{
				billingAddress.setDefaultAddress(Boolean.valueOf(address.isDefaultAddress()));
				if (StringUtils.isNotEmpty(address.getAddressType()))
				{
					billingAddress.setAddressType(address.getAddressType());
				}
			}
			/* Added in R2.3 for TISRLUAT-904 start */
			if (StringUtils.isNotEmpty(address.getLandmark()))
			{
				billingAddress.setLandmark(address.getLandmark());
			}
			/* Added in R2.3 for TISRLUAT-904 end */
		}
		return billingAddress;

	}



	/**
	 * @Description : Generate Folder if not present
	 * @param file
	 */
	public void isFolderExist(final File file)
	{
		if (null != file)
		{
			if (!file.exists())
			{
				file.mkdir();
				LOG.debug("Generated Folder:" + file.getName());
			}
		}
	}

	/**
	 * @param fmtDate
	 * @return String
	 */
	public static String getFormattedDate(final Date fmtDate)
	{
		String newdate = MarketplacecommerceservicesConstants.EMPTY;
		if (fmtDate != null)
		{
			final SimpleDateFormat sd = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATEFORMAT_FULL);
			newdate = sd.format(fmtDate);
		}
		return newdate;
	}


	/**
	 *
	 * @param dateData
	 * @return Date
	 */
	public static Date returnDateData(final String dateData)
	{
		final String str_date = dateData;
		final DateFormat formatter;
		Date date = null;
		try
		{
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			date = formatter.parse(str_date);
		}
		catch (final ParseException exception)
		{
			LOG.error(exception.getMessage());
		}
		return date;
	}

	/**
	 * @Description: Verifies Seller Data corresponding to the cart added Product
	 * @param restrictionList
	 * @param productSellerData
	 * @return flag
	 */
	//	public static boolean checkBOGOData(final List<AbstractPromotionRestriction> restrictionList,
	//			final List<SellerInformationModel> productSellerData)
	//	{
	//		boolean flag = false;
	//		boolean checkFlag = false;
	//		List<SellerMaster> sellerData = null;
	//		try
	//		{
	//			if (null == restrictionList || restrictionList.isEmpty())
	//			{
	//				flag = false;
	//			}
	//			else
	//			{
	//				if (null != productSellerData)
	//				{
	//					for (final AbstractPromotionRestriction restriction : restrictionList)
	//					{
	//						if (restriction instanceof EtailSellerSpecificRestriction)
	//						{
	//							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
	//							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
	//							for (final SellerMaster seller : sellerData)
	//							{
	//								for (final SellerInformationModel speficSeller : productSellerData)
	//								{
	//									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
	//									{
	//										checkFlag = true;
	//									}
	//								}
	//							}
	//							if (checkFlag)
	//							{
	//								flag = true;
	//								break;
	//							}
	//							else
	//							{
	//								flag = false;
	//								break;
	//							}
	//						}
	//						else
	//						{
	//							flag = false;
	//						}
	//					}
	//				}
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error(e.getMessage());
	//		}
	//		return flag;
	//	}



	/**
	 * @param url
	 * @return String
	 */
	public static String changeUrl(String url)
	{
		url = url.replaceAll("[^\\w/-]", "");
		//TISSTRT-1297
		if (url.contains("--"))
		{
			url = url.replaceAll("--", "-");
		}
		return url;
	}

	public static String buildPathString(final List<CategoryModel> path)
	{
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); ++i)
		{
			if (i != 0)
			{
				result.append('-');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	public static String urlSafe(final String text)
	{
		if ((text == null) || (text.isEmpty()))
		{
			return "";
		}
		String encodedText;
		try
		{
			encodedText = URLEncoder.encode(text, "utf-8");
		}
		catch (final UnsupportedEncodingException encodingException)
		{
			encodedText = text;
			//	LOG.debug(encodingException.getMessage());
		}

		String cleanedText = encodedText;
		cleanedText = cleanedText.replaceAll("%2F", "/");
		cleanedText = cleanedText.replaceAll("[^%A-Za-z0-9\\-]+", "-");
		return cleanedText;
	}

	/**
	 * @param request
	 * @return boolean This method checks if the current session is active
	 */
	public static boolean checkSessionActive(final HttpServletRequest request)
	{
		boolean isSessionActive = true;
		if (null != request && null != request.getSession())
		{
			final String guid = (String) request.getSession().getAttribute(SECURE_GUID_SESSION_KEY);
			if (null == guid)
			{
				LOG.debug("::::::::Session is not active:::::::");
				isSessionActive = false;
			}

		}
		return isSessionActive;
	}


	/**
	 * @return String This method returns the missing image url
	 */
	public static String getMissingImageUrl()

	{
		final ConfigurationService configService = (ConfigurationService) Registry.getApplicationContext().getBean(
				"configurationService");
		String missingImageUrl = MISSING_IMAGE_URL;
		String staticHost = null;
		if (null != configService)
		{
			staticHost = configService.getConfiguration().getString("marketplace.static.resource.host");
		}
		if (StringUtils.isNotEmpty(staticHost))
		{
			missingImageUrl = "//" + staticHost + MISSING_IMAGE_URL;
		}
		return missingImageUrl;

	}

	public static void populateTealiumDataForCartCheckout(final Model model, final AbstractOrderData cartData)
	{
		String sku = null;
		String adobeSku = null;
		String name = null;
		String quantity = null;
		String basePrice = null;//base price for a cart entry
		String totalEntryPrice = null;
		String category = null;
		String brand = null;
		String adobeProductSku = null;
		String page_subCategory_name = null;
		String cartTotal = null;
		String page_subcategory_name_L3 = null;
		//For kidswear L4 needs to be populated
		String page_subcategory_name_L4 = null;
		final List<String> productBrandList = new ArrayList<String>();
		final List<String> productCategoryList = new ArrayList<String>();
		final List<String> productIdList = new ArrayList<String>();
		final List<String> productListPriceList = new ArrayList<String>();
		final List<String> productNameList = new ArrayList<String>();
		final List<String> productQuantityList = new ArrayList<String>();

		final List<String> productSkuList = new ArrayList<String>();
		final List<String> productUnitPriceList = new ArrayList<String>();
		final List<String> pageSubCategories = new ArrayList<String>();
		final List<String> pageSubcategoryNameL3List = new ArrayList<String>();
		//For kidswear L4 needs to be populated
		final List<String> pageSubcategoryNameL4List = new ArrayList<String>();
		final List<String> adobeProductSkuList = new ArrayList<String>();
		String productCatL1 = null;
		String productCatL2 = null;
		String productCatL3 = null;
		String cartLevelSellerID = null;
		//for tealium
		//For kidswear L4 needs to be populated
		String productCatL4 = null;

		String order_shipping_charge = "";
		final List<String> orderShippingCharges = new ArrayList<String>();
		try
		{

			if (null != cartData)
			{

				final String currencySymbol = cartData.getCurrencySymbol();
				if (null != cartData.getTotalPriceWithTax() && null != cartData.getTotalPriceWithTax().getValue())
				{
					cartTotal = cartData.getTotalPriceWithTax().getValue().toString();
				}

				if (CollectionUtils.isNotEmpty(cartData.getEntries()))
				{
					for (final OrderEntryData entry : cartData.getEntries())
					{
						if (null != entry)
						{
							if (null != entry.getProduct() && StringUtils.isNotEmpty(entry.getProduct().getCode()))
							{
								adobeSku = entry.getProduct().getCode();
								sku = appendQuote(adobeSku);

							}
							if (null != entry.getProduct() && StringUtils.isNotEmpty(entry.getProduct().getName()))
							{
								name = appendQuote(entry.getProduct().getName().replace("\"", "").replace("\'", ""));

							}
							if (null != entry.getQuantity())
							{
								quantity = appendQuote(String.valueOf(entry.getQuantity()));

							}

							if (null != entry.getBasePrice() && null != entry.getBasePrice().getValue())
							{
								basePrice = appendQuote(entry.getBasePrice().getValue().toString());//base price for a cart entry

							}

							if (null != entry.getTotalPrice() && null != entry.getTotalPrice().getValue())
							{
								totalEntryPrice = appendQuote(entry.getTotalPrice().getValue().toString());//total price for a cart entry

							}
							if (entry.getCurrDelCharge() != null && null != entry.getCurrDelCharge().getValue()
									&& null != currencySymbol)
							{

								order_shipping_charge = appendQuote(currencySymbol.concat(entry.getCurrDelCharge().getValue().toString()));
							}
						}

						if (null != entry.getBrandName())
						{

							//	brand = appendQuote(entry.getBrandName());
							//PRDI-560 fix
							brand = appendQuote(entry.getBrandName().replaceAll("[^\\w\\s]", "").replaceAll(" ", "_").replace("__", "_")
									.toLowerCase());
						}


						//TPR-430 starts
						final StringBuffer categoryName = new StringBuffer();


						for (final CategoryData catData : entry.getProduct().getCategories())
						{
							if (catData.getCode().contains(MarketplacecommerceservicesConstants.SELLER_NAME_PREFIX))
							{
								categoryName.append(catData.getName()).append(':');
								//getCategoryLevel(category, 1, categoryName);
								getCategoryLevel(catData, categoryName);
							}
						}


						if (StringUtils.isNotEmpty(categoryName.toString()))
						{
							final String[] categoryNames = categoryName.toString().split(":");
							if (categoryNames.length == 5)
							{
								//category = appendQuote(categoryNames[2].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_").toLowerCase());
								category = categoryNames[3].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								productCategoryList.add(category);

								//page_subCategory_name = appendQuote(categoryNames[1].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_")
								//	.toLowerCase());

								page_subCategory_name = categoryNames[2].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								pageSubCategories.add(page_subCategory_name);

								//page_subcategory_name_L3 = appendQuote(categoryNames[0].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_")
								//	.toLowerCase());

								page_subcategory_name_L3 = categoryNames[1].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								pageSubcategoryNameL3List.add(page_subcategory_name_L3);

								//For kidswear L4 needs to be populated
								page_subcategory_name_L4 = categoryNames[0].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								pageSubcategoryNameL4List.add(page_subcategory_name_L4);
							}
							else
							{
								//category = appendQuote(categoryNames[2].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_").toLowerCase());
								category = categoryNames[2].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								productCategoryList.add(category);

								//page_subCategory_name = appendQuote(categoryNames[1].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_")
								//	.toLowerCase());

								page_subCategory_name = categoryNames[1].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								pageSubCategories.add(page_subCategory_name);

								//page_subcategory_name_L3 = appendQuote(categoryNames[0].replaceAll("[^\\w\\s]", "").replaceAll(" ", "_")
								//	.toLowerCase());

								page_subcategory_name_L3 = categoryNames[0].replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase();
								pageSubcategoryNameL3List.add(page_subcategory_name_L3);

								//For kidswear L4 needs to be populated
								//Commented as per TISPRDT-1462
								//page_subcategory_name_L4 = "\" \"";
								pageSubcategoryNameL4List.add(page_subcategory_name_L4);
							}
						}

						//TPR-430 ends

						productBrandList.add(brand);
						productIdList.add(sku);
						productListPriceList.add(totalEntryPrice);
						productNameList.add(name);
						productQuantityList.add(quantity);

						productSkuList.add(sku);
						productUnitPriceList.add(basePrice);
						adobeProductSkuList.add(adobeSku);
						//for tealium

						orderShippingCharges.add(order_shipping_charge);

					}
				}

				int count = 1;
				for (final String productSku : adobeProductSkuList)
				{
					final String appendedSku = ";" + productSku;
					if (adobeProductSku == null)
					{
						adobeProductSku = appendedSku;
					}
					else
					{
						adobeProductSku += appendedSku;
					}

					if (count < productSkuList.size())
					{
						adobeProductSku += ",";

					}
					count++;
				}

				final List<OrderEntryData> sellerList = cartData.getEntries();
				for (final OrderEntryData seller : sellerList)
				{
					final String sellerID = seller.getSelectedSellerInformation().getSellerID();
					if (cartLevelSellerID != null)
					{
						cartLevelSellerID += "_" + sellerID;
					}
					else
					{
						cartLevelSellerID = sellerID;
					}
				}

				model.addAttribute("cartLevelSellerID", cartLevelSellerID);
				model.addAttribute("productBrandList", productBrandList);

				model.addAttribute("productIdList", productIdList);
				model.addAttribute("productListPriceList", productListPriceList);
				model.addAttribute("productNameList", productNameList);
				model.addAttribute("productQuantityList", productQuantityList);
				model.addAttribute("productSkuList", productSkuList);
				model.addAttribute("productUnitPriceList", productUnitPriceList);
				model.addAttribute("adobe_product", adobeProductSku);
				model.addAttribute("cart_total", cartTotal);
				//for tealium

				model.addAttribute("orderShippingCharges", orderShippingCharges);


				//TPR-430

				if (CollectionUtils.isNotEmpty(pageSubCategories))
				{
					productCatL1 = StringUtils.join(pageSubCategories, ',');

				}

				if (CollectionUtils.isNotEmpty(productCategoryList))
				{
					productCatL2 = StringUtils.join(productCategoryList, ',');

				}

				if (CollectionUtils.isNotEmpty(pageSubcategoryNameL3List))
				{
					productCatL3 = StringUtils.join(pageSubcategoryNameL3List, ',');

				}
				//For kidswear L4 needs to be populated
				productCatL4 = StringUtils.join(pageSubcategoryNameL4List, ',');

				model.addAttribute("pageSubCategories", productCatL1);
				model.addAttribute("productCategoryList", productCatL2);
				model.addAttribute("page_subcategory_name_L3", productCatL3);
				//For kidswear L4 needs to be populated
				model.addAttribute("page_subcategory_name_L4", productCatL4);
			}
		}
		catch (final Exception te)
		{
			LOG.error("Error while populating tealium data in cart page:::::" + te.getMessage());
		}
	}

	/**
	 * @param catData
	 * @param categoryName
	 */
	private static void getCategoryLevel(final CategoryData catData, final StringBuffer categoryName)
	{
		if (CollectionUtils.isNotEmpty(catData.getSuperCategories()))
		{
			for (final CategoryData data : catData.getSuperCategories())
			{
				categoryName.append(data.getName()).append(':');
			}
		}

	}

	/* Checking payment type and then setting payment info */
	public static void setPaymentInfo(final OrderData orderDetail, final OrderConfirmationWsDTO orderWsDTO)
	{
		MplPaymentInfoData paymentInfo = null;

		if (null != orderDetail.getMplPaymentInfo())
		{
			paymentInfo = orderDetail.getMplPaymentInfo();

			if (null != paymentInfo.getPaymentOption())
			{
				orderWsDTO.setPaymentMethod(paymentInfo.getPaymentOption());
			}
			if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}

			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getBank()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getBank());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}

				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.WALLET))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
			//Paytm related changes
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYTM))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
					orderWsDTO.setIsWalletPay(true);
				}
				orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
		}
		else
		{

			orderWsDTO.setPaymentCard(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setCardholdername(MarketplacecommerceservicesConstants.NA);
		}
	}

	public static String appendQuote(final String param)
	{
		final StringBuilder str = new StringBuilder(100);
		str.append('\"').append(param).append('\"');
		return str.toString();
	}


	public static void getCategoryLevel(final CategoryModel categoryId, int count, final StringBuffer categoryName)
	{
		final int finalCount = 3;
		try
		{
			if (!categoryId.getSupercategories().isEmpty())
			{
				for (final CategoryModel superCategory : categoryId.getSupercategories())
				{
					categoryName.append(superCategory.getName()).append(':');
					count++;
					if (count == finalCount)
					{
						break;
					}
					else
					{
						getCategoryLevel(superCategory, count, categoryName);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//return finalCount;
	}

	/**
	 * For TPR-429
	 *
	 * @doc populates the seller IDs of the product during checkout
	 * @param abstractOrderModel
	 * @return checkoutSellerID
	 */
	public static void populateCheckoutSellersOrderConfirmation(final Model model, final OrderModel orderModel,
			final OrderData orderData)
	{

		String sellerId = null;
		String sellerIds = null;
		String transactionIds = null;
		String orderCurrency = null;
		String order_shipping = "";

		final List<String> sellerIdList = new ArrayList<String>();
		final List<String> transactionIdList = new ArrayList<String>();
		final List<String> deliveryModes = new ArrayList<String>();
		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			//TPR-429
			if (entry.getSelectedUSSID() != null)
			{
				sellerId = entry.getSelectedUSSID().substring(0, 6);

			}
			sellerIdList.add(sellerId);
			if (entry.getMplDeliveryMode() != null)
			{
				//order_shipping = entry.getDeliveryMode().getName();
				//order_shipping = entry.getMplZoneDeliveryModeValue().getMplDeliveryMode().toString();
				if (entry.getMplDeliveryMode().getDeliveryMode() != null)
				{
					order_shipping = appendQuote(entry.getMplDeliveryMode().getDeliveryMode().getName());
				}
			}
			deliveryModes.add(order_shipping);
		}
		if (CollectionUtils.isNotEmpty(sellerIdList))
		{
			Collections.reverse(sellerIdList);
			sellerIds = StringUtils.join(sellerIdList, '_');
		}

		if (orderModel.getPaymentInfo() instanceof QCWalletPaymentInfoModel)
		{
			final QCWalletPaymentInfoModel modelQC = (QCWalletPaymentInfoModel) orderModel.getPaymentInfo();
			if (null != modelQC.getType() && modelQC.getType().equalsIgnoreCase("Cliq Cash"))
			{
				model.addAttribute("qc_paymentMode", modelQC.getType());
			}
		}

		if (orderData.getMplPaymentInfo() != null)
		{
			String paymentType = "";
			if (orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Credit Card")
					|| orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Debit Card"))
			{
				paymentType = orderData.getMplPaymentInfo().getPaymentOption();
				if (null != orderData.getMplPaymentInfo().getCardCardType())
				{
					paymentType += '|' + orderData.getMplPaymentInfo().getCardCardType();
				}


			}
			else if (orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Netbanking"))
			{
				paymentType = orderData.getMplPaymentInfo().getPaymentOption();
				if (null != orderData.getMplPaymentInfo().getBank())
				{
					paymentType += '|' + orderData.getMplPaymentInfo().getBank();
				}
			}
			else
			{
				paymentType = orderData.getMplPaymentInfo().getPaymentOption();
			}
			model.addAttribute("order_payment_type", paymentType);
		}
		if (CollectionUtils.isNotEmpty(orderModel.getChildOrders()))
		{
			for (final OrderModel childOrder : orderModel.getChildOrders())
			{
				for (final AbstractOrderEntryModel childOrderEntry : childOrder.getEntries())
				{
					if (StringUtils.isNotEmpty(childOrderEntry.getTransactionID()))
					{
						transactionIdList.add(childOrderEntry.getTransactionID());
					}
				}
			}

		}
		if (CollectionUtils.isNotEmpty(transactionIdList))
		{
			transactionIds = StringUtils.join(transactionIdList, ',');
		}




		orderCurrency = orderModel.getCurrency().getIsocode();
		model.addAttribute("order_currency", orderCurrency);
		model.addAttribute("transaction_id", transactionIds);
		model.addAttribute("checkout_seller_ids", sellerIds);
		model.addAttribute("order_shipping_modes", deliveryModes);
	}


	//TPR-1285
	/**
	 * Return the Prefix
	 *
	 * @param prefix
	 * @return prefix
	 */
	public static String changePrefix(String prefix)
	{
		prefix = prefix.replaceAll("[^\\w/-]", "-");
		//TISSTRT-1297
		if (prefix.contains("--"))
		{
			prefix = prefix.replaceAll("--", "-");
		}
		return prefix;
	}



	/**
	 * @Description : Return Channel Data
	 * @param channel
	 * @return salesApplication
	 */
	public static List<SalesApplication> returnChannelData(final String channel)
	{
		final List<SalesApplication> salesApplication = new ArrayList<SalesApplication>();
		if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEB))
		{
			salesApplication.add(SalesApplication.WEB);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEBMOBILE))
		{
			salesApplication.add(SalesApplication.WEBMOBILE);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_MOBILE))
		{
			salesApplication.add(SalesApplication.MOBILE);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER))
		{
			salesApplication.add(SalesApplication.CALLCENTER);
		}
		return salesApplication;
	}

	/**
	 * UF-260
	 *
	 * @param model
	 * @param cartModel
	 * @param priceData
	 */
	public static void getCartPriceDetails(final Model model, final AbstractOrderModel abstractOrderModel,
			final MplPromoPriceData responseData)
	{
		final PriceDataFactory priceDataFactory = Registry.getApplicationContext().getBean("priceDataFactory",
				PriceDataFactory.class);
		Long cartTotalMrp = Long.valueOf(0);
		double cartTotalNetSelPrice = 0.0D;
		//double couponDiscount = 0.0D;
		double cartEntryNetSellPrice = 0.0D;
		final DecimalFormat df = new DecimalFormat("#.##");
		double totalDeliveryCharge = 0;
		Long cartEntryMrp = Long.valueOf(0);
		double cartDiscount = 0.0D;

		double cartCouponDiscount = 0;

		if (CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
			{
				if (!entry.getGiveAway().booleanValue())
				{
					cartEntryMrp = Long.valueOf((null == entry.getMrp() ? 0l : entry.getMrp().longValue())
							* (null == entry.getQuantity() ? 0l : entry.getQuantity().longValue()));

					cartTotalMrp = Long.valueOf(cartTotalMrp.longValue() + cartEntryMrp.longValue());

					//					if (null != entry.getNetAmountAfterAllDisc() && entry.getNetAmountAfterAllDisc().doubleValue() > 0)
					//					{
					//						cartEntryNetSellPrice = Double.parseDouble(df.format(entry.getNetAmountAfterAllDisc().doubleValue()));
					//					}
					//					else
					//					{
					//						cartEntryNetSellPrice = Double
					//								.parseDouble(df.format((null == entry.getBasePrice() ? 0.0d : entry.getBasePrice().doubleValue())
					//										* (null == entry.getQuantity() ? 0.0d : entry.getQuantity().doubleValue())));
					//					}

					cartEntryNetSellPrice = entry.getTotalPrice().doubleValue();
					cartTotalNetSelPrice = cartTotalNetSelPrice + cartEntryNetSellPrice;

					//couponDiscount += (null == entry.getCouponValue() ? 0.0d : entry.getCouponValue().doubleValue());

					cartDiscount += (null == entry.getCartLevelDisc() ? 0.0d : entry.getCartLevelDisc().doubleValue());

					cartCouponDiscount += (null == entry.getCartCouponValue() ? 0.0d : entry.getCartCouponValue().doubleValue());

					totalDeliveryCharge += ((null == entry.getCurrDelCharge() ? 0.0d : entry.getCurrDelCharge().doubleValue()) + (null == entry
							.getScheduledDeliveryCharge() ? 0.0d : entry.getScheduledDeliveryCharge().doubleValue()));

				}
			}
		}
		final BigDecimal cartTotalMrpValue = new BigDecimal(cartTotalMrp.longValue());
		final PriceData cartTotalMrpVal = priceDataFactory.create(PriceDataType.BUY, cartTotalMrpValue,
				MarketplacecommerceservicesConstants.INR);

		//if (!(cartModel instanceof OrderModel) && (OrderStatus.PAYMENT_PENDING.equals(cartModel.getStatus())))
		//SDI-2156
		//if (abstractOrderModel instanceof CartModel)
		//{
		//couponDiscount = 0.0D;
		//}

		final BigDecimal totalDiscount = new BigDecimal((cartTotalMrp.longValue() - cartTotalNetSelPrice) + cartDiscount
				+ cartCouponDiscount);

		final PriceData totalDiscountVal = priceDataFactory.create(PriceDataType.BUY, totalDiscount,
				MarketplacecommerceservicesConstants.INR);
		if (null != model)
		{
			model.addAttribute("cartTotalMrp", cartTotalMrpVal);
			model.addAttribute("totalDiscount", totalDiscountVal);
		}
		if (null != responseData)
		{
			responseData.setTotalDiscntIncMrp(totalDiscountVal);

			////TISSTRT-1605
			if (totalDeliveryCharge > 0)
			{
				final PriceData totalDeliveryChargeVal = priceDataFactory.create(PriceDataType.BUY, new BigDecimal(
						totalDeliveryCharge), MarketplacecommerceservicesConstants.INR);
				responseData.setDeliveryCost(totalDeliveryChargeVal);
			}
		}
	}

	/**
	 * @Description: Verifies Seller Data corresponding to the cart added Product
	 * @param restrictionList
	 * @param productSellerData
	 * @return flag
	 */
	//	public static boolean checkSellerData(final List<AbstractPromotionRestriction> restrictionList,
	//			final List<SellerInformationModel> productSellerData)
	//	{
	//		boolean checkFlag = false;
	//		List<SellerMaster> sellerData = null;
	//		try
	//		{
	//			if (null == restrictionList || restrictionList.isEmpty())
	//			{
	//				checkFlag = true;
	//			}
	//			else
	//			{
	//				if (null != productSellerData)
	//				{
	//					boolean isSellerIncluded = false;
	//
	//					for (final AbstractPromotionRestriction restriction : restrictionList)
	//					{
	//						if (restriction instanceof EtailSellerSpecificRestriction)
	//						{
	//							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
	//							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
	//							isSellerIncluded = true;
	//							break;
	//						}
	//						else if (restriction instanceof EtailExcludeSellerSpecificRestriction)
	//						{
	//							final EtailExcludeSellerSpecificRestriction excludeSellerRestriction = (EtailExcludeSellerSpecificRestriction) restriction;
	//							sellerData = excludeSellerRestriction.getSellerMasterList();
	//							break;
	//						}
	//					}
	//
	//					if (sellerData == null)
	//					{
	//						checkFlag = true;
	//					}
	//					else
	//					{
	//						final SellerInformationModel speficSeller = productSellerData.get(0);
	//						final String sellerInformationId = speficSeller != null ? speficSeller.getSellerID() : "";
	//
	//						for (final SellerMaster seller : sellerData)
	//						{
	//							if ((seller.getId().equalsIgnoreCase(sellerInformationId) && isSellerIncluded)
	//									|| (!seller.getId().equalsIgnoreCase(sellerInformationId) && !isSellerIncluded))
	//							{
	//								checkFlag = true;
	//								break;
	//							}
	//						}
	//
	//					}
	//
	//				}
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error(e.getMessage());
	//		}
	//		return checkFlag;
	//	}

	/**
	 * For UF-93
	 *
	 * @doc This Generic method is written for retrieving cookie from request for a given Cookie Name
	 * @param HttpServletRequest
	 *           , String
	 * @return Cookie
	 */
	public static Cookie getCookieByName(final HttpServletRequest request, final String cookieName)
	{
		if (request != null && request.getCookies() != null && cookieName != null)
		{
			for (final Cookie cookie : request.getCookies())
			{
				if (cookieName.equals(cookie.getName()))
				{
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * @Description: Verifies Seller Data corresponding to the cart added Product
	 * @param restrictionList
	 * @param productSellerData
	 * @return flag
	 */
	public static boolean checkSellerData(final List<AbstractPromotionRestriction> restrictionList,
			final List<SellerInformationModel> productSellerData)
	{
		boolean checkFlag = false;
		List<SellerMaster> sellerData = null;
		try
		{
			if (null == restrictionList || restrictionList.isEmpty())
			{
				checkFlag = true;
			}
			else
			{
				if (null != productSellerData)
				{
					boolean isSellerIncluded = false;

					for (final AbstractPromotionRestriction restriction : restrictionList)
					{
						if (restriction instanceof EtailSellerSpecificRestriction)
						{
							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
							isSellerIncluded = true;
							break;
						}
						else if (restriction instanceof EtailExcludeSellerSpecificRestriction)
						{
							final EtailExcludeSellerSpecificRestriction excludeSellerRestriction = (EtailExcludeSellerSpecificRestriction) restriction;
							sellerData = excludeSellerRestriction.getSellerMasterList();
							break;
						}
					}

					if (sellerData == null)
					{
						checkFlag = true;
					}
					else
					{
						final SellerInformationModel speficSeller = productSellerData.get(0);
						final String sellerInformationId = speficSeller != null ? speficSeller.getSellerID() : "";
						final List<String> sellerIdList = new ArrayList<String>();

						for (final SellerMaster seller : sellerData)
						{
							if (null != seller.getId())
							{
								sellerIdList.add(seller.getId());
							}
						}
						if (CollectionUtils.isNotEmpty(sellerIdList)
								&& ((sellerIdList.contains(sellerInformationId) && isSellerIncluded) || (!sellerIdList
										.contains(sellerInformationId) && !isSellerIncluded)))
						{
							checkFlag = true;

						}

					}

				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return checkFlag;
	}

	/**
	 * SDI_2801
	 * 
	 * @param cartModel
	 * @param cartDataDetails
	 */
	public static void getCartPriceDetailsMobile(final AbstractOrderModel cartModel, final CartDataDetailsWsDTO cartDataDetails)
	{
		Long cartTotalMrp = Long.valueOf(0);
		double cartTotalNetSelPrice = 0.0D;
		double cartEntryNetSellPrice = 0.0D;
		Long cartEntryMrp = Long.valueOf(0);
		double cartDiscount = 0.0D;

		double cartCouponDiscount = 0;
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (!entry.getGiveAway().booleanValue())
				{
					cartEntryMrp = Long.valueOf((null == entry.getMrp() ? 0l : entry.getMrp().longValue())
							* (null == entry.getQuantity() ? 0l : entry.getQuantity().longValue()));

					cartTotalMrp = Long.valueOf(cartTotalMrp.longValue() + cartEntryMrp.longValue());

					cartEntryNetSellPrice = entry.getTotalPrice().doubleValue();
					cartTotalNetSelPrice = cartTotalNetSelPrice + cartEntryNetSellPrice;

					cartDiscount += (null == entry.getCartLevelDisc() ? 0.0d : entry.getCartLevelDisc().doubleValue());

					cartCouponDiscount += (null == entry.getCartCouponValue() ? 0.0d : entry.getCartCouponValue().doubleValue());

				}
			}
		}

		final BigDecimal totalDiscount = new BigDecimal((cartTotalMrp.longValue() - cartTotalNetSelPrice) + cartDiscount
				+ cartCouponDiscount);

		cartDataDetails.setSubtotalPrice(String.valueOf(cartTotalMrp));
		cartDataDetails.setDiscountPrice(String.valueOf(totalDiscount));
	}
}
