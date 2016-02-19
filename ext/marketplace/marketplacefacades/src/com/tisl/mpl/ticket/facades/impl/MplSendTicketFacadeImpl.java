/**
 *
 */
package com.tisl.mpl.ticket.facades.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.impl.WsTicketService;
import com.tisl.mpl.ticket.facades.MplSendTicketFacade;


/**
 * @author TCS
 *
 */
public class MplSendTicketFacadeImpl implements MplSendTicketFacade
{
	@Autowired
	private WsTicketService wsTicketService;


	/**
	 * @return the wsTicketService
	 */
	public WsTicketService getWsTicketService()
	{
		return wsTicketService;
	}


	/**
	 * @param wsTicketService
	 *           the wsTicketService to set
	 */
	public void setWsTicketService(final WsTicketService wsTicketService)
	{
		this.wsTicketService = wsTicketService;
	}


	/**
	 * @description method is used for creating tickets
	 * @return boolean
	 */
	@Override
	public boolean setTicket(final SendTicketRequestData sendTicketRequestData)
	{
		try
		{
			return wsTicketService.sendOrderAction(sendTicketRequestData);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

}
