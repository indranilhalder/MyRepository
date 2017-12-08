/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Override
	public void populate(final SOURCE webFormData, final TARGET webCrmTicketModel) throws ConversionException,
			EtailNonBusinessExceptions
	{
		webCrmTicketModel.setCommerceTicketId(webFormData.getCommerceTicketId());

		webCrmTicketModel.setL0code(webFormData.getL0code());
		webCrmTicketModel.setL1code(webFormData.getL1code());
		webCrmTicketModel.setL2code(webFormData.getL2code());
		webCrmTicketModel.setL3code(webFormData.getL3code());
		webCrmTicketModel.setL4code(webFormData.getL4code());
		webCrmTicketModel.setAttachments(String.join(",", webFormData.getAttachments()));
		webCrmTicketModel.setComment(webFormData.getComment());
		webCrmTicketModel.setOrderCode(webFormData.getOrderCode());
		webCrmTicketModel.setSubOrderCode(webFormData.getSubOrderCode());
		webCrmTicketModel.setTransactionId(webFormData.getTransactionId());
		webCrmTicketModel.setTicketSubType(webFormData.getTicketSubType());
		webCrmTicketModel.setTicketType(webFormData.getTicketType());
		webCrmTicketModel.setStatus(MarketplacecommerceservicesConstants.SUBMITTED);

		final CustomerData currentUser = customerFacade.getCurrentCustomer();
		if (currentUser != null)
		{
			webCrmTicketModel.setCustomerId(currentUser.getUid());
		}
		webCrmTicketModel.setCustomerName(currentUser.getName());
		webCrmTicketModel.setCustomerId(webFormData.getCustomerId());

	}

}
