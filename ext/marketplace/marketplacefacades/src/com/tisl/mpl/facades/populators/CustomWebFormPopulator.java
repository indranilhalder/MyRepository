/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.cms.data.WebFormData;


/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomWebFormPopulator<SOURCE extends WebFormData, TARGET extends MplWebCrmTicketModel> implements
		Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE webFormData, final TARGET webCrmTicketModel) throws ConversionException,
			EtailNonBusinessExceptions
	{
		webCrmTicketModel.setCommerceTicketId(webFormData.getCommerceTicketId());
		webCrmTicketModel.setCustomerId(webFormData.getCustomerId());
		webCrmTicketModel.setL0code(webFormData.getL0code());
		webCrmTicketModel.setL1code(webFormData.getL1code());
		webCrmTicketModel.setL2code(webFormData.getL2code());
		webCrmTicketModel.setL3code(webFormData.getL3code());
		webCrmTicketModel.setL4code(webFormData.getL4code());
	}

}
