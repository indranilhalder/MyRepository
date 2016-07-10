/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FriendsInviteProcessModel;
import com.tisl.mpl.core.model.FriendsModel;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService;


/**
 * @author TCS
 *
 */
public class DefaultFriendsInviteFacade implements FriendsInviteFacade
{
	@Autowired
	private UserService userService;
	@Autowired
	private FriendsInviteService friendsInviteService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	private static final Logger LOG = Logger.getLogger(DefaultFriendsInviteFacade.class);


	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) getUserService().getCurrentUser();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected void validateDataBeforeUpdate(final FriendsInviteData friendsInviteData)
	{
		validateParameterNotNullStandardMessage("friendsInviteData", friendsInviteData);
	}


	/**
	 * @return the friendsInviteService
	 */
	public FriendsInviteService getFriendsInviteService()
	{
		return friendsInviteService;
	}


	/**
	 * @param friendsInviteService
	 *           the friendsInviteService to set
	 */
	public void setFriendsInviteService(final FriendsInviteService friendsInviteService)
	{
		this.friendsInviteService = friendsInviteService;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @description This method is used to check Uniqueness Of friends Email
	 * @return boolean
	 */
	@Override
	public boolean checkUniquenessOfEmail(final List<String> friendsEmailList)
	{
		try
		{
			boolean isExist = false;
			if (null != friendsEmailList)
			{
				for (final String friendsEmail : friendsEmailList)
				{
					final CustomerModel customer = friendsInviteService.checkUniquenessOfEmail(friendsEmail.toLowerCase());
					if (null != customer && !StringUtils.isEmpty(customer.getOriginalUid()))
					{
						isExist = true;
						break;
					}
				}
			}
			return isExist;
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	@Override
	public boolean isEmailEqualsToCustomer(final List<String> friendsEmailList)
	{
		try
		{
			final CustomerModel currentCustomer = getCurrentSessionCustomer();
			boolean isSameEmail = false;
			if (null != friendsEmailList)
			{
				for (final String friendsEmail : friendsEmailList)
				{
					if (null != currentCustomer && currentCustomer.getOriginalUid().equalsIgnoreCase(friendsEmail))
					{
						isSameEmail = true;
						break;
					}
				}
			}
			return isSameEmail;
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
	 * @description This method is used to invite friends
	 * @return FriendsInviteData
	 */
	@Override
	public boolean inviteFriends(final FriendsInviteData friendsInviteData)
	{
		try
		{
			validateParameterNotNullStandardMessage("friendsInviteData", friendsInviteData);
			final boolean flag = getFriendsInviteService().inviteNewFriends(friendsInviteData);
			return flag;
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
	 * @description This method is used to initiate mail with Inviting friends
	 */
	@Override
	public void sendInvite(final FriendsInviteData friendsInviteData, final String messageText)
	{
		try
		{
			if (null != friendsInviteData && null != friendsInviteData.getFriendsEmailList()
					&& friendsInviteData.getFriendsEmailList().size() > 0)
			{
				for (final String email : friendsInviteData.getFriendsEmailList())
				{
					final FriendsInviteProcessModel friendsInviteProcessModel = (FriendsInviteProcessModel) businessProcessService
							.createProcess("friendsInvite-" + friendsInviteData.getCustomerEmail() + "-" + System.currentTimeMillis(),
									"friendsInviteEmailProcess");

					friendsInviteProcessModel.setCustomer(getCurrentSessionCustomer());
					friendsInviteProcessModel.setSite(baseSiteService.getCurrentBaseSite());
					friendsInviteProcessModel.setCustomerEmail(friendsInviteData.getCustomerEmail());
					friendsInviteProcessModel.setFriendsEmail(email);
					friendsInviteProcessModel.setAffiliateId(friendsInviteData.getAffiliateId());
					friendsInviteProcessModel.setInviteBaseUrl(friendsInviteData.getInviteBaseUrl());
					friendsInviteProcessModel.setMessageText(messageText);
					final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
					if (currentBaseStore != null)
					{
						friendsInviteProcessModel.setStore(currentBaseStore);
					}
					friendsInviteProcessModel.setCurrency(commonI18NService.getCurrentCurrency());
					friendsInviteProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
					modelService.save(friendsInviteProcessModel);
					businessProcessService.startProcess(friendsInviteProcessModel);
				}
			}
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
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @description This method is used to update friends model in case of requested registration
	 */
	@Override
	public void updateFriendsModel(final FriendsInviteData friendsData)
	{
		try
		{
			if (null != friendsData)
			{
				FriendsModel friendsModel = null;
				friendsModel = getModelService().create(FriendsModel.class);
				friendsModel.setAffiliateId(friendsData.getAffiliateId());
				friendsModel.setFriendsEmail(friendsData.getFriendsEmail());
				getFriendsInviteService().updateFriendsModel(friendsModel);
			}
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
