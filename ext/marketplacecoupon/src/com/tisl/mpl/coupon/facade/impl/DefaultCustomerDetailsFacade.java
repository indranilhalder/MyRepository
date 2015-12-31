/**
 *
 */
package com.tisl.mpl.coupon.facade.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.tisl.mpl.coupon.facade.CustomerDetailsFacade;
import com.tisl.mpl.coupon.service.CronJobDataService;
import com.tisl.mpl.coupon.service.CustomerDetailsService;




/**
 * @author TCS
 *
 */
public class DefaultCustomerDetailsFacade implements CustomerDetailsFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultCustomerDetailsFacade.class);

	@Resource(name = "customerDetailsService")
	private CustomerDetailsService customerDetailsService;

	@Resource(name = "cronJobDataService")
	private CronJobDataService cronJobDataService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;

	@Resource(name = "voucherService")
	DefaultVoucherService voucherService;

	private static String voucherCode;


	@Override
	public Map<String, List<PrincipalModel>> anniversaryVoucherDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();

		//Read values from properties file
		readValuesFromPropFile(eventType);

		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.anniversaryVoucherDetails(oCusModel, eventStartDate, eventEndDate))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> birthdayVoucherDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.birthdayVoucherDetails(oCusModel, eventStartDate, eventEndDate))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> purchaseBasedVoucherDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate, final Double purAmtOrderValue)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.purchaseBasedVoucherDetails(oCusModel, eventStartDate, eventEndDate, purAmtOrderValue))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> firstTimeRegVoucherDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate, final int firstTimeRegNoOfDays)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.firstTimeRegVoucherDetails(oCusModel, eventStartDate, eventEndDate, firstTimeRegNoOfDays))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> cartNotShoppedVoucherDetails(final String eventType, final int cartNotShoppedNoOfDays)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.cartNotShoppedVoucherDetails(oCusModel, cartNotShoppedNoOfDays))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> cartAbanAtPmtPageDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate, final Boolean cartAbanIsGreater, final Double cartAbanCartValue)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.cartAbandonmentVoucherDetails(oCusModel, eventStartDate, eventEndDate, true,
						cartAbanIsGreater.booleanValue(), cartAbanCartValue.intValue()))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}

	@Override
	public Map<String, List<PrincipalModel>> cartAbanAtCartPageDetails(final String eventType, final Date eventStartDate,
			final Date eventEndDate, final Boolean cartAbanIsGreater, final Double cartAbanCartValue)
	{
		//Mapping with voucher code and customer list
		final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();
		//Read values from properties file
		readValuesFromPropFile(eventType);
		//Fetches All Customers
		final List<CustomerModel> customerList = customerDetailsService.getCustomer();

		if (null != customerList && !customerList.isEmpty())
		{
			for (final CustomerModel oCusModel : customerList)
			{
				final PrincipalModel customer = oCusModel;

				if (cronJobDataService.cartAbandonmentVoucherDetails(oCusModel, eventStartDate, eventEndDate, false,
						cartAbanIsGreater.booleanValue(), cartAbanCartValue.intValue()))
				{
					populateEventCustomerMap(voucherCode, customer, eventCustomerMap);
				}

			}
		}
		return eventCustomerMap;
	}



	private void populateEventCustomerMap(final String voucherCode, final PrincipalModel customer,
			final Map<String, List<PrincipalModel>> eventCustomerMap)
	{
		final List<PrincipalModel> validCustomerList = (eventCustomerMap.get(voucherCode) == null) ? new ArrayList<PrincipalModel>()
				: eventCustomerMap.get(voucherCode);

		validCustomerList.add(customer);

		eventCustomerMap.put(voucherCode, validCustomerList);
	}

	@Override
	public void saveVoucherVals(final Map<String, List<PrincipalModel>> eventCustomerMap, final CurrencyModel currency,
			final Double discountVal, final String voucherCode, final Date couponStartDate, final Date couponEndDate,
			final int redemptionLmtPerUser, final int redemptionQtyLimit)
	{
		saveVouchers(eventCustomerMap, currency, discountVal, voucherCode, couponStartDate, couponEndDate, redemptionLmtPerUser,
				redemptionQtyLimit);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.jobs.facade.CustomerDetailsFacade#getCartCustomer()
	 */
	@Override
	public void getCartCustomer(final String eventType, final CurrencyModel currency, final Double discountVal,
			final Boolean isFreeShipping, final Integer noOfDays)
	{
		//Mapping with voucher code and customer list
		//final Map<String, List<PrincipalModel>> eventCustomerMap = new HashMap<String, List<PrincipalModel>>();

		//Read values from properties file
		//readValuesFromPropFile();

		//Fetches All Customers
		final List<CartModel> cartCustomerList = customerDetailsService.getCartDetails();

		if (null != cartCustomerList && !cartCustomerList.isEmpty())
		{
			LOG.debug("To write code");
		}
	}


	public void readValuesFromPropFile(final String eventType)
	{

		//--------------------------------------------//
		//          coupon.event.birthday.code
		//			coupon.event.anniversary.code
		//			coupon.event.purchaseAmount.code
		//		    coupon.event.firstTimeReg.code
		//	        coupon.event.cartNotShopped.code
		//	        coupon.event.cartAbandonment.code
		//--------------------------------------------//

		//final DateFormat df = new SimpleDateFormat("ddMMyyyy");
		final Configuration configuration = configurationService.getConfiguration();
		final String propFileKey = "coupon.event." + eventType + ".code";
		voucherCode = configuration.getString(propFileKey);
	}


	private void saveVouchers(final Map<String, List<PrincipalModel>> eventCustomerMap, final CurrencyModel currency,
			final Double discountVal, final String voucherCode, final Date couponStartDate, final Date couponEndDate,
			final int redemptionLmtPerUser, final int redemptionQtyLimit)
	{
		final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		final Date sysDate = Calendar.getInstance().getTime();
		final String reportDate = df.format(sysDate);

		for (final Map.Entry<String, List<PrincipalModel>> mapEntry : eventCustomerMap.entrySet())
		{
			final String identifier = mapEntry.getKey().concat(reportDate);
			final List<PrincipalModel> validCustomerList = mapEntry.getValue();

			final PromotionVoucherModel voucher = modelService.create(PromotionVoucherModel.class);
			voucher.setCode(identifier);

			if (voucherCode != null && !voucherCode.isEmpty() && voucherService.getVoucher(voucherCode) == null)
			{
				voucher.setVoucherCode(voucherCode);
			}
			else
			{
				voucher.setVoucherCode(identifier);
			}

			voucher.setValue(discountVal);
			if (currency != null)
			{
				voucher.setCurrency(currency);
			}
			voucher.setRedemptionQuantityLimitPerUser(Integer.valueOf(redemptionLmtPerUser));
			voucher.setRedemptionQuantityLimit(Integer.valueOf(redemptionQtyLimit));
			voucher.setName(identifier);

			modelService.save(voucher);

			//ATTACHING TEMPORAL RESTRICTION
			final DateRestrictionModel temporalRestriction = modelService.create(DateRestrictionModel.class);
			temporalRestriction.setStartDate(couponStartDate);
			temporalRestriction.setEndDate(couponEndDate);
			temporalRestriction.setVoucher(voucher);
			modelService.save(temporalRestriction);

			//ATTACHING USER RESTRICTION
			final UserRestrictionModel userRestriction = modelService.create(UserRestrictionModel.class);
			userRestriction.setUsers(validCustomerList);
			userRestriction.setVoucher(voucher);
			modelService.save(userRestriction);

			modelService.refresh(voucher);

		}
	}

}