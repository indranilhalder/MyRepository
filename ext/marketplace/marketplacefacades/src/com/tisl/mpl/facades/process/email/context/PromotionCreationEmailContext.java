/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tisl.mpl.model.PromotionCreationMailProcessModel;


/**
 * @author TCS
 *
 */
public class PromotionCreationEmailContext extends AbstractEmailContext<PromotionCreationMailProcessModel>
{
	private static final String USER = "user";
	private static final String PROMOCODE = "promoCode";
	private static final String PROMOTIONTYPE = "promotionType";
	private static final String STARTDATE = "startDate";
	private static final String ENDDATE = "endDate";
	private static final String CURRENTDATE = "currentDate";
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void init(final PromotionCreationMailProcessModel PromotionCreationMailProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(PromotionCreationMailProcessModel, emailPageModel);

		put(USER, PromotionCreationMailProcessModel.getUser());
		put(PROMOCODE, PromotionCreationMailProcessModel.getPromoCode());
		put(PROMOTIONTYPE, PromotionCreationMailProcessModel.getPromotionType());
		put(STARTDATE, dateFormat.format(PromotionCreationMailProcessModel.getStartDate()));
		put(ENDDATE, dateFormat.format(PromotionCreationMailProcessModel.getEndDate()));
		put(EMAIL, PromotionCreationMailProcessModel.getRecipientEmail());
		put(DISPLAY_NAME, PromotionCreationMailProcessModel.getRecipientEmail());

		final Date date = new Date();
		final String currentDate = dateFormat.format(date);
		put(CURRENTDATE, currentDate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final PromotionCreationMailProcessModel businessProcessModel)
	{
		// YTODO Auto-generated method stub
		return businessProcessModel.getSite();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final PromotionCreationMailProcessModel businessProcessModel)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected LanguageModel getEmailLanguage(final PromotionCreationMailProcessModel businessProcessModel)
	{
		// YTODO Auto-generated method stub
		return businessProcessModel.getSite().getDefaultLanguage();
	}



}
