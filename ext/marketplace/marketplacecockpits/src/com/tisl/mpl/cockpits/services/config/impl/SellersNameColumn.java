package com.tisl.mpl.cockpits.services.config.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.zkoss.spring.SpringUtil;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class SellersNameColumn  extends AbstractSimpleCustomColumnConfiguration<String, ItemModel> {

	private MplSellerInformationService mplSellerInformationService;
	//fine Jewellery changes starts
	private MplJewelleryService mplJewelleryService;
	//fine Jewellery changes ends
	
	@Override
	protected String getItemValue(ItemModel itemModel, Locale locale)
			throws ValueHandlerException {
		Set<String> sellers = new HashSet<>();
		if(itemModel instanceof AbstractOrderModel){
			AbstractOrderModel order =   (AbstractOrderModel) itemModel;
			SellerInformationModel sellerInfo =null;
			for(AbstractOrderEntryModel entry  : order.getEntries()){
				//fine Jewellery changes starts
				if(entry.getProduct().getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
				{
					final List<JewelleryInformationModel> jewelleryInfo = getMplJewelleryService().getJewelleryInfoByUssid(entry.getSelectedUSSID());
					final String ussid = jewelleryInfo.get(0).getPCMUSSID();
					sellerInfo = getMplSellerInformationService().getSellerDetail(ussid);
					sellers.add(sellerInfo.getSellerName());
				}
				//fine Jewellery changes ends
				else{
				sellerInfo = getMplSellerInformationService().getSellerDetail(entry.getSelectedUSSID());
				sellers.add(sellerInfo.getSellerName());
				}
			}
			
		}
		
		return String.join(",", sellers);
	}

	protected MplSellerInformationService getMplSellerInformationService() {
		
		if(mplSellerInformationService ==null){
			mplSellerInformationService = ((MplSellerInformationService) SpringUtil
					.getBean("mplSellerInformationService"));
		}
		return mplSellerInformationService;
				
	}
	//fine Jewellery changes starts
	protected MplJewelleryService getMplJewelleryService() {
		
		if(mplJewelleryService ==null){
			mplJewelleryService = ((MplJewelleryService) SpringUtil
					.getBean("mplJewelleryService"));
		}
		return mplJewelleryService;
				
	}
	//fine Jewellery changes ends
}


