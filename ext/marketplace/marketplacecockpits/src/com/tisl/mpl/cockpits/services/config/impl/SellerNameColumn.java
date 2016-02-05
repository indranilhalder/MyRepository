package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.model.SellerInformationModel;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class SellerNameColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	private ModelService modelService = (ModelService) Registry
			.getApplicationContext().getBean("modelService");

	private FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry
			.getApplicationContext().getBean("flexibleSearchService");

	private static final Logger LOG = Logger.getLogger(OrderSellerColumn.class);

	@Override
	protected Object getItemValue(ItemModel item, Locale paramLocale)
			throws ValueHandlerException {
		if (item instanceof RefundEntryModel) {
			String selectedUSSID = ((RefundEntryModel) item).getOrderEntry()
					.getSelectedUSSID();
			if (StringUtils.isNotBlank(selectedUSSID)) {
				SellerInformationModel sellerInfoModel = modelService
						.create(SellerInformationModel.class);
				sellerInfoModel.setSellerID(selectedUSSID);
				try {
					SellerInformationModel seller = flexibleSearchService
							.getModelByExample(sellerInfoModel);
					return seller.getSellerName();
				} catch (ModelNotFoundException | AmbiguousIdentifierException e) {
					LOG.error(e);
					return StringUtils.EMPTY;
				}
			}
		}
		return null;
	}
}