package com.tisl.mpl.coupon.job;
///**
// *
// */
//package com.tisl.mpl.coupon.facade.job;
//
//import de.hybris.platform.core.model.c2l.CurrencyModel;
//import de.hybris.platform.core.model.security.PrincipalModel;
//import de.hybris.platform.cronjob.enums.CronJobResult;
//import de.hybris.platform.cronjob.enums.CronJobStatus;
//import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
//import de.hybris.platform.servicelayer.cronjob.PerformResult;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
//import com.tisl.mpl.coupon.facade.CustomerDetailsFacade;
//import com.tisl.mpl.model.EventCouponCreationJobModel;
//
//
//
///**
// * @author TCS
// *
// */
//public class CouponEventJob extends AbstractJobPerformable<EventCouponCreationJobModel>
//{
//	private static final Logger LOG = Logger.getLogger(CouponEventJob.class);
//
//	private CustomerDetailsFacade defaultCustomerDetailsFacade;
//
//
//
//	/**
//	 * @return the defaultCustomerDetailsFacade
//	 */
//	public CustomerDetailsFacade getDefaultCustomerDetailsFacade()
//	{
//		return defaultCustomerDetailsFacade;
//	}
//
//
//
//	/**
//	 * @param defaultCustomerDetailsFacade
//	 *           the defaultCustomerDetailsFacade to set
//	 */
//	public void setDefaultCustomerDetailsFacade(final CustomerDetailsFacade defaultCustomerDetailsFacade)
//	{
//		this.defaultCustomerDetailsFacade = defaultCustomerDetailsFacade;
//	}
//
//
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
//	 * CronJobModel )
//	 */
//	@Override
//	public PerformResult perform(final EventCouponCreationJobModel arg0)
//	{
//		final String checkData = MarketplacecouponConstants.CRONJOB_TITLE;
//		try
//		{
//			final String eventType = arg0.getEventType().getCode();
//
//			//Voucher Attributes
//			final CurrencyModel currency = arg0.getCurrency();
//			final Double discountVal = arg0.getDiscountValue();
//			final String voucherCode = arg0.getVoucherCode();
//			final Date couponStartDate = arg0.getCouponStartDate();
//			final Date couponEndDate = arg0.getCouponEndDate();
//
//			// Event Specific Attributes
//			final Date eventStartDate = arg0.getEventStartDate();
//			final Date eventEndDate = arg0.getEventEndDate();
//			final Boolean cartAbanIsGreater = arg0.getCartAbanIsGreater();
//			final Double orderThresholdVal = arg0.getOrderThresholdVal();
//			final int noOfDays = arg0.getNoOfDays().intValue();
//			final int redemptionLmtPerUser = arg0.getRedemptionLmtPerUser().intValue();
//			final int redemptionQtyLimit = arg0.getRedemptionQtyLimit().intValue();
//
//			Map<String, List<PrincipalModel>> eventCustomerMap = null;
//
//			switch (eventType)
//			{
//				case "anniversary":
//					//For Anniversary Coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.anniversaryVoucherDetails(eventType, eventStartDate, eventEndDate);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//
//					break;
//
//				case "birthday":
//					//For Birthday Coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.birthdayVoucherDetails(eventType, eventStartDate, eventEndDate);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//					break;
//
//				case "purchaseAmount":
//					//For Purchase Amount Specific Coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.purchaseBasedVoucherDetails(eventType, eventStartDate,
//							eventEndDate, orderThresholdVal);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//					break;
//
//				case "firstTimeReg":
//					//For First Time Registration Specific Coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.firstTimeRegVoucherDetails(eventType, eventStartDate, eventEndDate,
//							noOfDays);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//					break;
//
//				case "cartNotShopped":
//					//For Cart Not Shopped Specific Coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.cartNotShoppedVoucherDetails(eventType, noOfDays);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//
//					}
//					break;
//
//				case "cartAbanPmtPage":
//					//For Cart Abandonment at Payment Page coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.cartAbanAtPmtPageDetails(eventType, eventStartDate, eventEndDate,
//							cartAbanIsGreater, orderThresholdVal);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//					break;
//
//				case "cartAbanCartPage":
//					//For Cart Abandonment at Cart Page coupons
//					eventCustomerMap = defaultCustomerDetailsFacade.cartAbanAtCartPageDetails(eventType, eventStartDate, eventEndDate,
//							cartAbanIsGreater, orderThresholdVal);
//					if (eventCustomerMap != null && !eventCustomerMap.isEmpty())
//					{
//						defaultCustomerDetailsFacade.saveVoucherVals(eventCustomerMap, currency, discountVal, voucherCode,
//								couponStartDate, couponEndDate, redemptionLmtPerUser, redemptionQtyLimit);
//					}
//					break;
//
//			}
//
//
//			LOG.debug("*****************" + checkData + " is triggered*******************************");
//
//			//defaultCustomerDetailsFacade.getCustomer(eventType, currency, discountVal, isFreeShipping, noOfDays);
//			//defaultCustomerDetailsFacade.getCartCustomer(eventType, currency, discountVal, isFreeShipping, noOfDays);
//
//			LOG.debug("*****************" + checkData + " Ends*******************************");
//
//			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
//
//
//		}
//		catch (final Exception e)
//		{
//			LOG.error("**********Error*************" + e.getMessage());
//			//e.printStackTrace();
//			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
//		}
//	}
//}