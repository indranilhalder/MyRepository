/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.FriendsInviteProcessModel;


/**
 * @author TCS
 *
 */
public class FriendsInviteEmailContext extends CustomerEmailContext
{

	private static final String CUSTOMER_EMAIL = "customerEmail";
	private static final String FRIENDS_EMAIL = "friendsEmail";
	private static final String INVITE_URL = "inviteUrl";
	private static final String MESSAGE_TEXT = "messageText";
	private static final String MESSAGE_TEXT_HEADER = "messageTextHeader";
	private static final String MESSAGE_TEXT_BODY = "messageTextBody";
	private static final String IS_MESSAGE_TEXT_HEADER = "isHeader";
	private static final String IS_MESSAGE_TEXT_BODY = "isBody";
	private static final String IS_MESSAGE_TEXT_EDITED = "isBodyEdited";
	private String affiliateId;
	private static final String UTF_8 = "UTF-8";
	private static final String LOGIN = "/login";



	/**
	 * @return the affiliateId
	 */
	public String getAffiliateId()
	{
		return affiliateId;
	}


	/**
	 * @param affiliateId
	 *           the affiliateId to set
	 */
	public void setAffiliateId(final String affiliateId)
	{
		this.affiliateId = affiliateId;
	}


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		{
			String header = null;
			String body = null;
			super.init(storeFrontCustomerProcessModel, emailPageModel);

			if (storeFrontCustomerProcessModel instanceof FriendsInviteProcessModel)
			{
				put(EMAIL, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getFriendsEmail());
				put(DISPLAY_NAME, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getFriendsEmail());
				put(CUSTOMER_EMAIL, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				put(FRIENDS_EMAIL, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getFriendsEmail());
				if (!StringUtils.isEmpty(((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getMessageText()))
				{
					final String text = ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getMessageText();
					if (text.contains("|"))
					{
						final String[] message = text.split("\\|");
						header = message[0];
						body = message[1];
						put(IS_MESSAGE_TEXT_HEADER, Boolean.TRUE);
						put(IS_MESSAGE_TEXT_BODY, Boolean.TRUE);
						put(MESSAGE_TEXT_HEADER, header);
						put(MESSAGE_TEXT_BODY, body);
					}
					else
					{
						put(IS_MESSAGE_TEXT_EDITED, Boolean.TRUE);
						put(IS_MESSAGE_TEXT_HEADER, Boolean.FALSE);
						put(IS_MESSAGE_TEXT_BODY, Boolean.FALSE);
						put(MESSAGE_TEXT, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getMessageText());
					}
				}

				put(getAffiliateId(), ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getAffiliateId());
				setAffiliateId(((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getAffiliateId());
				put(INVITE_URL, ((FriendsInviteProcessModel) storeFrontCustomerProcessModel).getInviteBaseUrl());
			}
		}
	}


	public String getURLEncodedAffiliateId() throws UnsupportedEncodingException
	{
		return URLEncoder.encode(affiliateId, UTF_8);
	}

	public String getSecureFriendsInviteUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true, LOGIN,
				"affiliateId=" + getURLEncodedAffiliateId());
	}

	public String getDisplaySecureFriendsInviteUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false, LOGIN);
	}

}
