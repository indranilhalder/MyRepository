package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

public class DeliveryFulfillmentModeColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	
	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(DeliveryFulfillmentModeColumn.class);

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		
		try {
			
		if (model instanceof ConsignmentEntryModel ){
			return getMplFindDeliveryFulfillModeStrategy().findDeliveryFulfillMode(((ConsignmentEntryModel) model).getOrderEntry().getSelectedUSSID());
		}
		}catch(Exception ex){
			LOG.error(ex);
		}
		return StringUtils.EMPTY;
	}
	

	
	protected MplFindDeliveryFulfillModeStrategy getMplFindDeliveryFulfillModeStrategy() {
		return ((MplFindDeliveryFulfillModeStrategy) SpringUtil
				.getBean("mplFindDeliveryFulfillModeStrategy"));
	}
	

}
