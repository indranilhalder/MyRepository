/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.jalo.EtailExcludeSellerSpecificRestriction;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.ExcludeManufacturesRestriction;
import com.tisl.mpl.jalo.ManufacturesRestriction;
import com.tisl.mpl.jalo.SellerMaster;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;


/**
 * @author TCS
 *
 */
public class GenericUtilityMethods
{
	private static final Logger LOG = Logger.getLogger(GenericUtilityMethods.class);
	public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";

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

	/**
	 * @Description: Sends the year from Date
	 * @param : date
	 * @return year
	 */
	public static String redirectYear(final Date date)
	{
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split("/");

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
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split("/");

			final String month = bDayStringArry[1];
			final String day = bDayStringArry[0];


			final String modifiedBDay = day + "/" + month + "/" + yeartoModify;

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
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			final String bDayString = dateFormat.format(date);
			final String bDayStringArry[] = bDayString.split("/");

			final String month = bDayStringArry[1];
			final String day = bDayStringArry[0];
			final String year = bDayStringArry[2];

			final String modifiedBDay = day + "/" + month + "/" + year;

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
			noOfDays = (int) ((dateAfter.getTime() - dateBefore.getTime()) / (1000 * 60 * 60 * 24));
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
	public static List<String> getExcludeManufactureList(final List<AbstractPromotionRestriction> restrictionList)
	{
		final List<String> manufactureList = new ArrayList<String>();
		for (final AbstractPromotionRestriction restriction : restrictionList)
		{
			if (restriction instanceof ExcludeManufacturesRestriction)
			{
				final ExcludeManufacturesRestriction excludeManufacturesRestriction = (ExcludeManufacturesRestriction) restriction;
				final List<Category> excludeBrandList = (List<Category>) excludeManufacturesRestriction.getManufacturers();
				for (final Category excludeBrand : excludeBrandList)
				{
					manufactureList.add(excludeBrand.getName());
				}
			}
		}

		return manufactureList;
	}

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

		final SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");

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
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			final String dayString = dateFormat.format(date);
			final String dayStringArry[] = dayString.split("/");

			final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			final String timeFormatString = timeFormat.format(date);
			final String timeFormatStringArry[] = timeFormatString.split(":");

			final String month = dayStringArry[1];
			final String day = dayStringArry[0];
			final String year = dayStringArry[2];

			final String hour = timeFormatStringArry[0];
			final String min = timeFormatStringArry[1];
			//final String sec = timeFormatStringArry[2];

			final String modifiedBDay = day + "_" + month + "_" + year + "(" + hour + "_" + min + ")";
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
	public static boolean checkSellerData(final List<AbstractPromotionRestriction> restrictionList,
			final List<SellerInformationModel> productSellerData)
	{
		boolean flag = false;
		boolean checkFlag = false;
		List<SellerMaster> sellerData = null;
		try
		{
			if (null == restrictionList || restrictionList.isEmpty())
			{
				flag = true;
			}
			else
			{
				if (null != productSellerData)
				{
					for (final AbstractPromotionRestriction restriction : restrictionList)
					{
						if (restriction instanceof EtailSellerSpecificRestriction)
						{
							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
							for (final SellerMaster seller : sellerData)
							{
								for (final SellerInformationModel speficSeller : productSellerData)
								{
									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
									{
										checkFlag = true;
									}
								}
							}
							if (checkFlag)
							{
								flag = true;
								break;
							}
							else
							{
								flag = false;
								break;
							}
						}
						else
						{
							flag = true;
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return flag;
	}

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
	public static boolean checkExcludeSellerData(final List<AbstractPromotionRestriction> restrictionList,
			final List<SellerInformationModel> productSellerData)
	{
		boolean excludeSellerFlag = false;
		boolean checkFlag = false;
		List<SellerMaster> sellerData = null;
		try
		{
			if (null == restrictionList || restrictionList.isEmpty())
			{
				excludeSellerFlag = false;
			}
			else
			{
				if (CollectionUtils.isNotEmpty(productSellerData))
				{
					for (final AbstractPromotionRestriction restriction : restrictionList)
					{
						if (restriction instanceof EtailExcludeSellerSpecificRestriction)
						{
							final EtailExcludeSellerSpecificRestriction excludeSellerRestrict = (EtailExcludeSellerSpecificRestriction) restriction;
							sellerData = excludeSellerRestrict.getSellerMasterList();

							for (final SellerMaster seller : sellerData)
							{
								for (final SellerInformationModel speficSeller : productSellerData)
								{
									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
									{
										checkFlag = true;
									}
								}
							}

							if (checkFlag)
							{
								excludeSellerFlag = true;
								break;
							}
							else
							{
								excludeSellerFlag = false;
								break;
							}

						}
						else
						{
							excludeSellerFlag = false;
						}
					}
				}

			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return excludeSellerFlag;
	}

	/**
	 * @Description : Populate the Excluded Product and Manufacture Data in separate Lists
	 * @param : SessionContext arg0,PromotionEvaluationContext arg1
	 */
	public static void populateExcludedProductManufacturerList(final SessionContext arg0, final PromotionEvaluationContext arg1,
			final List<Product> excludedProductList, final List<String> excludeManufactureList,
			final List<AbstractPromotionRestriction> restrictionList, final ProductPromotion productPromotion)
	{
		try
		{
			if (productPromotion.getProperty(arg0, MarketplacecommerceservicesConstants.EXCLUDEDPRODUCTS) != null
					&& excludedProductList != null)
			{
				excludedProductList.addAll((List<Product>) productPromotion.getProperty(arg0,
						MarketplacecommerceservicesConstants.EXCLUDEDPRODUCTS));
			}
			if (excludeManufactureList != null)
			{
				excludeManufactureList.addAll(getExcludeManufactureList(restrictionList));
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
	}

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
				.checkMinimumBrandAmount(ctx, promoEvalCtx, validProductUssidMap, restrictionList));

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
			final String countrycode = "91";
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
	public static boolean checkBOGOData(final List<AbstractPromotionRestriction> restrictionList,
			final List<SellerInformationModel> productSellerData)
	{
		boolean flag = false;
		boolean checkFlag = false;
		List<SellerMaster> sellerData = null;
		try
		{
			if (null == restrictionList || restrictionList.isEmpty())
			{
				flag = false;
			}
			else
			{
				if (null != productSellerData)
				{
					for (final AbstractPromotionRestriction restriction : restrictionList)
					{
						if (restriction instanceof EtailSellerSpecificRestriction)
						{
							final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
							sellerData = etailSellerSpecificRestriction.getSellerMasterList();
							for (final SellerMaster seller : sellerData)
							{
								for (final SellerInformationModel speficSeller : productSellerData)
								{
									if (seller.getId().equalsIgnoreCase(speficSeller.getSellerID()))
									{
										checkFlag = true;
									}
								}
							}
							if (checkFlag)
							{
								flag = true;
								break;
							}
							else
							{
								flag = false;
								break;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return flag;
	}



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

}
