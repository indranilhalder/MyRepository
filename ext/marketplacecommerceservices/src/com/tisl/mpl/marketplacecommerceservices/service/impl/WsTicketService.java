/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.ticket.SendTicketService;


/**
 * @author TCS
 *
 */
public class WsTicketService
{
	@Resource
	private SendTicketService sendTicketService;


	/**
	 * @return the sendTicketService
	 */
	public SendTicketService getSendTicketService()
	{
		return sendTicketService;
	}


	/**
	 * @param sendTicketService
	 *           the sendTicketService to set
	 */
	public void setSendTicketService(final SendTicketService sendTicketService)
	{
		this.sendTicketService = sendTicketService;
	}


	public boolean sendOrderAction(final SendTicketRequestData sendTicketRequestData)
	{
		try
		{
			if (null == sendTicketRequestData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E0000);
			}
			else if (!validateNullRequestData(sendTicketRequestData))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0007);
			}
			try
			{
				if (sendTicketService.sendTicketAction(sendTicketRequestData))
				{
					return true;
				}
			}
			catch (final Exception ex)
			{
				throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
			}
			return false;
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


	/**
	 * @param requestData
	 * @return boolean
	 */
	private boolean validateNullRequestData(final SendTicketRequestData requestData)
	{
		try
		{
			boolean isValid = false;
			if (StringUtils.isEmpty(requestData.getEmailId()))
			{
				isValid = false;
			}
			else if (StringUtils.isEmpty(requestData.getOrderId()))
			{
				isValid = false;
			}
			else if (StringUtils.isEmpty(requestData.getActionType()))
			{
				isValid = false;
			}
			else
			{
				isValid = true;
			}
			return isValid;
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
