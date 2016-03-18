/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.CouponRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.service.MplCouponWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.wsdto.CommonCouponsDTO;
import com.tisl.mpl.wsdto.UnusedCouponsDTO;
import com.tisl.mpl.wsdto.UsedCouponsDTO;



/**
 * @author TCS
 *
 */
public class MplCouponWebServiceImpl implements MplCouponWebService
{

	@Autowired
	private ExtendedUserService extUserService;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	@Autowired
	private MplCouponService mplCouponService;

	@Autowired
	private VoucherModelService voucherModelService;

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "couponRestrictionService")
	private CouponRestrictionService couponRestrictionService;

	@Autowired
	private DiscountUtility discountUtility;

	/**
	 * @Description : For getting the details of all the Coupons available for the User
	 */
	@Override
	//	public CommonCouponsDTO getCoupons(final int currentPage, final int pageSize, final String emailId, final String usedCoupon,
	//			final String sortCode)
	public CommonCouponsDTO getCoupons(final int currentPage, final String emailId, final String usedCoupon, final String sortCode)
	{
		final CommonCouponsDTO couponDTO = new CommonCouponsDTO();
		try
		{
			final CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
			List<CouponHistoryData> couponHistoryDTOList = new ArrayList<CouponHistoryData>();
			//	final List<CouponHistoryData> couponHistoryDTOListModified = new ArrayList<CouponHistoryData>();
			final List<UsedCouponsDTO> usedCouponList = new ArrayList<UsedCouponsDTO>();
			final List<UnusedCouponsDTO> unusedCouponList = new ArrayList<UnusedCouponsDTO>();
			final CustomerModel customer = (CustomerModel) extUserService.getUserForUID(StringUtils.lowerCase(emailId));
			String savings = MarketplacecommerceservicesConstants.EMPTY;
			//*Pagination For used Coupons

			final int pageSizeVoucherHistory = Integer.parseInt(
					configurationService.getConfiguration().getString(MarketplacewebservicesConstants.PAZE_SIZE_COUPONS, "20").trim());

			final PageableData pageableData = createPageableData(currentPage, pageSizeVoucherHistory, sortCode);

			if (usedCoupon.equals(MarketplacecommerceservicesConstants.Y))
			{
				final Map<String, Double> countSavedSumMap = mplCouponFacade.getInvalidatedCouponCountSaved(customer);

				final SearchPageData<CouponHistoryData> searchPageDataVoucherHistoryFinal = mplCouponFacade
						.getVoucherHistoryTransactions(customer, pageableData);

				final String totalCouponCount = String
						.valueOf(searchPageDataVoucherHistoryFinal.getPagination().getTotalNumberOfResults());
				couponHistoryDTOList = searchPageDataVoucherHistoryFinal.getResults();
				final Collection<OrderModel> orders = customer.getOrders();
				final List<OrderModel> orderList = new ArrayList<OrderModel>();
				if (CollectionUtils.isNotEmpty(orders))
				{
					orderList.addAll(orders);

				}
				if (null != countSavedSumMap)
				{
					for (final Map.Entry<String, Double> iterator : countSavedSumMap.entrySet())
					{
						double value = 0.0d;
						if (null != iterator.getValue())
						{
							value = Math.round(iterator.getValue().doubleValue() * 100.0) / 100.0;
						}
						if (CollectionUtils.isNotEmpty(orderList))
						{
							savings = String
									.valueOf(discountUtility.createPrice(orderList.get(0), Double.valueOf(value)).getDoubleValue());
						}
						if (null != couponHistoryStoreDTO.getSavedSum())
						{
							couponDTO.setTotalSavings(savings);
						}
						if (CollectionUtils.isNotEmpty(couponHistoryDTOList))
						{
							couponDTO.setTotalCount(totalCouponCount);

							couponDTO.setTotalUniqueCouponsUsed(iterator.getKey());
						}
					}

				}
				if (CollectionUtils.isNotEmpty(couponHistoryDTOList))
				{
					UsedCouponsDTO usedCoupondto = null;
					for (final CouponHistoryData couponHistory : couponHistoryDTOList)
					{
						usedCoupondto = new UsedCouponsDTO();
						if (StringUtils.isNotEmpty(couponHistory.getCouponCode()))
						{
							usedCoupondto.setCouponCode(couponHistory.getCouponCode());
						}
						if (StringUtils.isNotEmpty(couponHistory.getCouponDescription()))
						{
							usedCoupondto.setDescription(couponHistory.getCouponDescription());
						}
						if (StringUtils.isNotEmpty(couponHistory.getRedeemedDate()))
						{
							usedCoupondto.setOrderDate(couponHistory.getRedeemedDate());
						}
						if (StringUtils.isNotEmpty(couponHistory.getOrderCode()))
						{
							usedCoupondto.setOrderNo(couponHistory.getOrderCode());
						}
						usedCouponList.add(usedCoupondto);
					}
				}
			}
			//*Pagination For unused Coupons

			else if (usedCoupon.equals(MarketplacecommerceservicesConstants.N))
			{
				final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.COUPONS_DATE_FORMAT);

				final int pageSize = Integer.parseInt(configurationService.getConfiguration()
						.getString(MarketplacewebservicesConstants.PAZE_SIZE_VOUCHER, "12").trim());

				final PageableData pageableData1 = createPageableData(currentPage, pageSize, sortCode);

				final SearchPageData<VoucherModel> searchVoucherModel = mplCouponService.getClosedVoucher(customer, pageableData1);
				final List<VoucherModel> voucherList = searchVoucherModel.getResults();

				final String totalCouponCount = String.valueOf(searchVoucherModel.getPagination().getTotalNumberOfResults());

				if (StringUtils.isNotEmpty(totalCouponCount))
				{
					couponDTO.setTotalCount(totalCouponCount);
				}
				for (final VoucherModel voucher : voucherList)
				{
					if (voucher instanceof PromotionVoucherModel)
					{
						final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) voucher;
						final UserRestrictionModel userRestrObj = couponRestrictionService.getUserRestriction(promoVoucher);
						final List<PrincipalModel> restrCustomerList = userRestrObj != null
								? couponRestrictionService.getRestrictionCustomerList(userRestrObj) : new ArrayList<PrincipalModel>();
						UnusedCouponsDTO unusedCouponsDTO = null;
						if (null != promoVoucher.getVoucherCode()
								&& voucherModelService.isReservable(promoVoucher, promoVoucher.getVoucherCode(), customer)
								&& restrCustomerList.contains(customer))
						{
							unusedCouponsDTO = new UnusedCouponsDTO();
							final DateRestrictionModel dateRestriction = couponRestrictionService.getDateRestriction(promoVoucher);
							final Date endDate = dateRestriction.getEndDate() != null ? dateRestriction.getEndDate() : new Date();
							final Date startDate = dateRestriction.getStartDate();
							if (StringUtils.isNotEmpty(promoVoucher.getVoucherCode()))
							{
								unusedCouponsDTO.setCouponCode(promoVoucher.getVoucherCode());
							}
							if (StringUtils.isNotEmpty(promoVoucher.getDescription()))
							{
								unusedCouponsDTO.setDescription(promoVoucher.getDescription());
							}
							if (null != String.valueOf(promoVoucher.getRedemptionQuantityLimitPerUser()))
							{
								if (promoVoucher.getRedemptionQuantityLimitPerUser().intValue() > 1)
								{
									unusedCouponsDTO.setCouponType("Multiple");
								}
								else
								{
									unusedCouponsDTO.setCouponType("Single");
								}
							}
							if (null != String.valueOf(promoVoucher.getRedemptionQuantityLimitPerUser()))
							{
								unusedCouponsDTO
										.setRedemptionQtyLimitPerUser(promoVoucher.getRedemptionQuantityLimitPerUser().toString());
							}
							if (StringUtils.isNotEmpty(String.valueOf(endDate)))
							{
								unusedCouponsDTO.setExpiryDate(sdf.format(endDate));
							}
							if (StringUtils.isNotEmpty(String.valueOf(startDate)))
							{
								unusedCouponsDTO.setCouponCreationDate(sdf.format(startDate));
							}
						}
						unusedCouponList.add(unusedCouponsDTO);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(unusedCouponList))
			{
				couponDTO.setUnusedCouponsList(unusedCouponList);
			}
			if (CollectionUtils.isNotEmpty(usedCouponList))
			{
				couponDTO.setUsedCouponsList(usedCouponList);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return couponDTO;
	}

	protected PageableData createPageableData(final int currentPage, final int pageSize, final String sort)
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		return pageable;
	}
}
