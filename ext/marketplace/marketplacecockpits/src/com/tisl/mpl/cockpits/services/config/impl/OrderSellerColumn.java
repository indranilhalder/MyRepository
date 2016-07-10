/**
 * 
 */
package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.dispatcher.impl.MarketPlaceBasketAppender;
import com.tisl.mpl.model.SellerInformationModel;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * @author 1006687
 *
 */
public class OrderSellerColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	private ModelService modelService = (ModelService) Registry
			.getApplicationContext().getBean("modelService");

	private FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry
			.getApplicationContext().getBean("flexibleSearchService");

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(OrderSellerColumn.class);

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		if (model instanceof AbstractOrderEntryModel) {
			String selectedUSSID = ((AbstractOrderEntryModel) model)
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
		return StringUtils.EMPTY;
	}

}
