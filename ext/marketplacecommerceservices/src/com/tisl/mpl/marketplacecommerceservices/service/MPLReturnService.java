package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;


public interface MPLReturnService extends ReturnService
{
	public abstract ReplacementEntryModel createReplacement(ReturnRequestModel returnrequestmodel,
			AbstractOrderEntryModel abstractorderentrymodel, String s, Long long1, ReturnAction returnaction,
			ReplacementReason replacementreason);

	public abstract RefundEntryModel createRefund(ReturnRequestModel returnrequestmodel,
			AbstractOrderEntryModel abstractorderentrymodel, String s, Long long1, ReturnAction returnaction,
			RefundReason refundreason);

	public abstract void addReplacementOrderEntries(ReplacementOrderModel order, List<ReplacementEntryModel> replacements);
	
	/**
	 * 
	 * @param customerId
	 * @return MplCustomerBankAccountDetailsModel
	 */
	public MplCustomerBankAccountDetailsModel getCustomerBakDetailsById(String customerId);


	public List<ReturnRequestModel> getListOfReturnRequest(String orlderId);

	/**
	 * getting order using the orderId
	 * 
	 * @param orderId
	 * @return OrderModel
	 */
	public OrderModel getOrder(final String orderId);

	public boolean checkProductEligibilityForRTS(List<AbstractOrderEntryModel> entries);
	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByDates(Date fromDate, Date toDate);

	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByParams(String orderID, String customerId, String pincode);
}
