package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

public interface QcRefundService
{

	public void processQcRefundEntryWise(AbstractOrderEntryModel orderEntry);
	public void createPaymentEntryForQCTransaction(final OrderModel subOrderModel,
			final WalletCardApportionDetailModel walletCardApportionDetailModel);
	public  void constructQuickCilverOrderEntryForSplit(
			final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList, final AbstractOrderEntryModel abstractOrderEntryModel
		);

}
