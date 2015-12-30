/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.List;

import org.apache.log4j.Logger;



public class ImplDefaultEmailService extends DefaultEmailService
{

	private static final Logger LOG = Logger.getLogger(ImplDefaultEmailService.class);



	@Override
	public EmailMessageModel createEmailMessage(final List<EmailAddressModel> toAddresses,
			final List<EmailAddressModel> ccAddresses, final List<EmailAddressModel> bccAddresses,
			final EmailAddressModel fromAddress, final String replyToAddress, final String subject, final String body,
			final List<EmailAttachmentModel> attachments)
	{
		// Do all validation now before creating the message
		if (toAddresses == null || toAddresses.isEmpty())
		{
			throw new IllegalArgumentException("toAddresses must not be empty");
		}
		if (fromAddress == null)
		{
			throw new IllegalArgumentException("fromAddress must not be null");
		}
		if (subject == null || subject.isEmpty())
		{
			throw new IllegalArgumentException("subject must not be empty");
		}
		if (body == null || body.isEmpty())
		{

			throw new IllegalArgumentException("body must not be empty");
		}
		validateEmailAddress(replyToAddress, "replyToAddress");

		final EmailMessageModel emailMessageModel = getModelService().create(EmailMessageModel.class);
		emailMessageModel.setToAddresses(toAddresses);
		emailMessageModel.setCcAddresses(ccAddresses);
		emailMessageModel.setBccAddresses(bccAddresses);
		emailMessageModel.setFromAddress(fromAddress);
		emailMessageModel.setReplyToAddress((replyToAddress != null && !replyToAddress.isEmpty()) ? replyToAddress : fromAddress
				.getEmailAddress());
		emailMessageModel.setSubject(subject);
		emailMessageModel.setAttachments(attachments);
		emailMessageModel.setBody(body);

		LOG.info("Email body:::::" + emailMessageModel.getBody());

		getModelService().save(emailMessageModel);
		final MediaModel bodyMedia = createBodyMedia("bodyMedia-" + emailMessageModel.getPk(), body);
		emailMessageModel.setBodyMedia(bodyMedia);
		getModelService().save(emailMessageModel);

		return emailMessageModel;
	}

}
