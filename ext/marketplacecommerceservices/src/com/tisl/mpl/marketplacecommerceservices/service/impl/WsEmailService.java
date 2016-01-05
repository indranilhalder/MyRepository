/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendEmailRequestData;
import com.tisl.mpl.email.MplSendEmailService;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * The Class WsEmailService. Extension of the hybris email service. Reason being hybris will not call SAP PI webservice
 *
 * @author Prium
 */
public class WsEmailService extends DefaultEmailService
{
	private static final Logger LOG = Logger.getLogger(WsEmailService.class);


	@Autowired
	private MplSendEmailService sendEmailService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private KeyGenerator emailUniqueCodeGenerator;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService#send(de.hybris.platform.acceleratorservices
	 * .model.email.EmailMessageModel)
	 */
	@Override
	public boolean send(final EmailMessageModel message)
	{
		final boolean flag = false;
		try
		{
			if (message == null)
			{
				throw new IllegalArgumentException("message must not be null");
			}

			final SendEmailRequestData emailRequestData = new SendEmailRequestData();
			emailRequestData.setSenderEmail(message.getFromAddress().getEmailAddress());
			emailRequestData.setSenderName(message.getFromAddress().getDisplayName());
			if (CollectionUtils.isNotEmpty(message.getToAddresses()))
			{
				emailRequestData.setRecipientEmail(message.getToAddresses().get(0).getEmailAddress());
			}
			else
			{
				throw new IllegalArgumentException("message has not sent to addresses");
			}

			if (CollectionUtils.isNotEmpty(message.getCcAddresses()))
			{
				emailRequestData.setCcEmail((message.getCcAddresses().get(0).getEmailAddress()));
			}
			if (CollectionUtils.isNotEmpty(message.getBccAddresses()))
			{
				emailRequestData.setBccEmail((message.getBccAddresses().get(0).getEmailAddress()));
			}
			emailRequestData.setReplyTo(message.getReplyToAddress());
			emailRequestData.setSubject(message.getSubject());
			emailRequestData.setContent(message.getBody());
			sendEmailService.sendEmail(emailRequestData);

			message.setSent(true);
			message.setSentMessageID(emailUniqueCodeGenerator.generate().toString());
			message.setSentDate(new Date());
			modelService.save(message);
			return flag;
		}
		catch (final ModelSavingException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailBusinessExceptions("ModelSavingException", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			LOG.info("Mail Not Sent" + ex.toString());
			throw new EtailNonBusinessExceptions(ex);
		}
	}

}
