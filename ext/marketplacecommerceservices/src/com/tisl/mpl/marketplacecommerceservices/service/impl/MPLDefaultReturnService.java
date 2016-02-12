package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.impl.DefaultReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.math.BigDecimal;
import java.util.List;

import com.tisl.mpl.marketplacecommerceservices.service.MPLReturnService;


public class MPLDefaultReturnService extends DefaultReturnService implements MPLReturnService
{

	@Override
	public ReplacementEntryModel createReplacement(final ReturnRequestModel request, final AbstractOrderEntryModel entry,
			final String notes, final Long expectedQuantity, final ReturnAction action, final ReplacementReason reason)
	{
		final ReplacementEntryModel returnsEntry = (ReplacementEntryModel) getModelService().create(ReplacementEntryModel.class);
		returnsEntry.setOrderEntry(entry);
		returnsEntry.setAction(action);
		returnsEntry.setNotes(notes);
		returnsEntry.setReason(reason);
		returnsEntry.setReturnRequest(request);
		returnsEntry.setStatus(ReturnStatus.RETURN_INITIATED);
		returnsEntry.setExpectedQuantity(expectedQuantity);
		//getModelService().save(returnsEntry);
		return returnsEntry;
	}

	@Override
	public RefundEntryModel createRefund(final ReturnRequestModel request, final AbstractOrderEntryModel entry,
			final String notes, final Long expectedQuantity, final ReturnAction action, final RefundReason reason)
	{
		final RefundEntryModel returnsEntry = (RefundEntryModel) getModelService().create(RefundEntryModel.class);
		returnsEntry.setOrderEntry(entry);
		returnsEntry.setAction(action);
		returnsEntry.setNotes(notes);
		returnsEntry.setReason(reason);
		returnsEntry.setReturnRequest(request);
		returnsEntry.setExpectedQuantity(expectedQuantity);
		returnsEntry.setStatus(ReturnStatus.RETURN_INITIATED);
		returnsEntry.setAmount(new BigDecimal(entry.getNetAmountAfterAllDisc().doubleValue()));
		getModelService().save(returnsEntry);
		return returnsEntry;
	}

	@Override
	public void addReplacementOrderEntries(final ReplacementOrderModel order, final List<ReplacementEntryModel> replacements)
	{
		Double totPrice = 0.00;
		Double convCharges = 0.00d;

		for (final ReplacementEntryModel replacement : replacements)
		{
			if (replacement.getAction().equals(ReturnAction.HOLD))
			{
				break;
			}

			final ReplacementOrderEntryModel entry = (ReplacementOrderEntryModel) getModelService().create(
					ReplacementOrderEntryModel.class);

			entry.setProduct(replacement.getOrderEntry().getProduct());
			entry.setOrder(order);
			entry.setEntryNumber(replacement.getOrderEntry().getEntryNumber());
			entry.setQuantity(replacement.getExpectedQuantity());
			entry.setUnit(replacement.getOrderEntry().getUnit());

			entry.setBasePrice(replacement.getOrderEntry().getBasePrice());
			entry.setCalculated(replacement.getOrderEntry().getCalculated());
			entry.setCartLevelDisc(replacement.getOrderEntry().getCartLevelDisc());
			entry.setConvenienceChargeApportion(replacement.getOrderEntry().getConvenienceChargeApportion());
			//entry.setCostCenter(replacement.getOrderEntry().getCostCenter());
			entry.setDiscountValues(replacement.getOrderEntry().getDiscountValues());
			entry.setDiscountValuesInternal(replacement.getOrderEntry().getDiscountValuesInternal());
			entry.setNetAmountAfterAllDisc(replacement.getOrderEntry().getNetAmountAfterAllDisc());
			entry.setNetSellingPrice(replacement.getOrderEntry().getNetSellingPrice());
			entry.setTaxValues(replacement.getOrderEntry().getTaxValues());
			entry.setTaxValuesInternal(replacement.getOrderEntry().getTaxValuesInternal());
			entry.setTotalPrice(replacement.getOrderEntry().getTotalPrice());
			entry.setTotalProductLevelDisc(replacement.getOrderEntry().getTotalProductLevelDisc());
			entry.setTotalSalePrice(replacement.getOrderEntry().getTotalSalePrice());
			entry.setSelectedUSSID(replacement.getOrderEntry().getSelectedUSSID());
			entry.setDeliveryMode(replacement.getOrderEntry().getDeliveryMode());
			totPrice = totPrice + replacement.getOrderEntry().getTotalPrice();
			convCharges = convCharges + replacement.getOrderEntry().getConvenienceChargeApportion();
			entry.getOrder().setPaymentAddress(order.getPaymentAddress());
			getModelService().save(entry);
		}

		order.setTotalPrice(totPrice);
		order.setConvenienceCharges(convCharges);
		order.setTotalPriceWithConv(totPrice + convCharges);
		getModelService().save(order);

		getModelService().refresh(order);
	}


	//need to save to generate return id 
	/*
	 * @Override public ReturnRequestModel createReturnRequest(final OrderModel order) { final ReturnRequestModel request
	 * = (ReturnRequestModel) getModelService().create(ReturnRequestModel.class); request.setOrder(order); return
	 * request; }
	 */


}
