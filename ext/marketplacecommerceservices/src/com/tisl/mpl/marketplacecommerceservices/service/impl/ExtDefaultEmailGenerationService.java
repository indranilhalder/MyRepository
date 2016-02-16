package com.tisl.mpl.marketplacecommerceservices.service.impl;


import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.SendInvoiceProcessModel;
import com.tisl.mpl.core.model.SubmitRequestProcessModel;


/**
 * The Class DefaultTelcoEmailGenerationService.
 *
 * @author TCS
 *
 *
 */

public class ExtDefaultEmailGenerationService extends DefaultEmailGenerationService
{
	private static final Logger LOG = Logger.getLogger(ExtDefaultEmailGenerationService.class);

	@Resource(name = "mediaService")
	private MediaService mediaService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService#generate(de.hybris.platform.
	 * processengine.model.BusinessProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
	 */
	@Override
	public EmailMessageModel generate(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
			throws RuntimeException
	{
		final EmailMessageModel emailMessageModel = super.generate(businessProcessModel, emailPageModel);


		/*
		 * emailMessageModel.setBrand("MarletPlace"); emailMessageModel.setChannel(businessProcessModel.getChannel());
		 * emailMessageModel.setUser(businessProcessModel.getUser());
		 */
		if (businessProcessModel instanceof SubmitRequestProcessModel)
		{
			final SubmitRequestProcessModel processModel = (SubmitRequestProcessModel) businessProcessModel;
			// Deeply nested if..then statements are hard to read
			//			if (processModel.getAttachedFileName() != null)
			//			{
			//
			//				final EmailAttachmentModel emailAttachment = (EmailAttachmentModel) mediaService
			//						.getMedia(processModel.getAttachedFileName());
			//				if (emailAttachment != null)
			//				{
			//					final List<EmailAttachmentModel> emailAttachments = new ArrayList();
			//					emailAttachments.add(emailAttachment);
			//					emailMessageModel.setAttachments(emailAttachments);
			//				}
			//			}

			if (processModel.getAttachedFileName() != null
					&& (EmailAttachmentModel) mediaService.getMedia(processModel.getAttachedFileName()) != null)
			{
				final EmailAttachmentModel emailAttachment = (EmailAttachmentModel) mediaService
						.getMedia(processModel.getAttachedFileName());
				final List<EmailAttachmentModel> emailAttachments = new ArrayList();
				emailAttachments.add(emailAttachment);
				emailMessageModel.setAttachments(emailAttachments);
			}
		}

		if (businessProcessModel instanceof SendInvoiceProcessModel)
		{
			final SendInvoiceProcessModel processModel = (SendInvoiceProcessModel) businessProcessModel;

			//			if (processModel.getInvoiceUrl() != null)
			//			{
			//
			//				final EmailAttachmentModel emailAttachment = (EmailAttachmentModel) mediaService
			//						.getMedia(processModel.getInvoiceUrl());
			//				if (emailAttachment != null)
			//				{
			//					final List<EmailAttachmentModel> emailAttachments = new ArrayList();
			//					emailAttachments.add(emailAttachment);
			//					emailMessageModel.setAttachments(emailAttachments);
			//				}
			//			}
			if (processModel.getInvoiceUrl() != null
					&& (EmailAttachmentModel) mediaService.getMedia(processModel.getInvoiceUrl()) != null)
			{

				final EmailAttachmentModel emailAttachment = (EmailAttachmentModel) mediaService
						.getMedia(processModel.getInvoiceUrl());
				final List<EmailAttachmentModel> emailAttachments = new ArrayList();
				emailAttachments.add(emailAttachment);
				emailMessageModel.setAttachments(emailAttachments);
			}


		}
		getModelService().save(emailMessageModel);
		LOG.info("Email Subject: " + emailMessageModel.getSubject());
		LOG.info("Email body: " + emailMessageModel.getBody());
		return emailMessageModel;
	}

}
