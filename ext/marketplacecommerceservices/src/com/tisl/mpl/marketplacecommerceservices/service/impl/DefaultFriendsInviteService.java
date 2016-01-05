/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.FriendsModel;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.FriendsInviteDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService;


/**
 * @author TCS
 *
 */
public class DefaultFriendsInviteService implements FriendsInviteService
{
	private ModelService modelService;
	private FriendsInviteDao friendsInviteDao;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Autowired
	private UserService userService;



	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the friendsInviteDao
	 */
	public FriendsInviteDao getFriendsInviteDao()
	{
		return friendsInviteDao;
	}

	/**
	 * @param friendsInviteDao
	 *           the friendsInviteDao to set
	 */
	public void setFriendsInviteDao(final FriendsInviteDao friendsInviteDao)
	{
		this.friendsInviteDao = friendsInviteDao;
	}

	protected CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) userService.getCurrentUser();
	}

	@Override
	public boolean inviteNewFriends(final FriendsInviteData friendsInviteData)
	{
		boolean isInvited = false;
		try
		{
			boolean isAlreadyInvited = false;
			validateParameterNotNullStandardMessage("friendsInviteData", friendsInviteData);
			final CustomerModel customer = getCurrentSessionCustomer();
			FriendsModel friendsModel = new FriendsModel();
			final List<FriendsModel> friendModelListToSave = new ArrayList<FriendsModel>();
			final List<FriendsModel> invitedFriendsList = customer.getFriendsEmailList();
			final List<String> friendListToInvite = friendsInviteData.getFriendsEmailList();
			FriendsModel itrFriendsModel = new FriendsModel();
			if (null != invitedFriendsList && invitedFriendsList.size() > 0)
			{
				for (final String email : friendListToInvite)
				{
					for (final FriendsModel friends : invitedFriendsList)
					{
						if (friends.getFriendsEmail().equalsIgnoreCase(email))
						{
							isAlreadyInvited = true;
							itrFriendsModel = friends;
							break;
						}
						else
						{
							isAlreadyInvited = false;
						}
					}
					if (isAlreadyInvited)
					{
						if (!itrFriendsModel.getRegistered().booleanValue())
						{
							friendsModel = itrFriendsModel;
							friendsModel.setCustomerEmail(customer.getOriginalUid());
							friendsModel.setInviteSent(Boolean.TRUE);
							friendsModel.setRegistered(Boolean.FALSE);
							friendsModel.setAffiliateId(customer.getUid());

							friendModelListToSave.add(friendsModel);
							modelService.save(friendsModel);
							isInvited = true;
						}
						else
						{
							isInvited = false;
							return isInvited;
						}
					}
					else
					{
						friendsModel = getModelService().create(FriendsModel.class);
						friendsModel.setFriendsEmail(email);
						friendsModel.setCustomerEmail(customer.getOriginalUid());
						friendsModel.setInviteSent(Boolean.FALSE);
						friendsModel.setRegistered(Boolean.FALSE);
						friendsModel.setAffiliateId(customer.getUid());

						friendModelListToSave.add(friendsModel);
						modelService.save(friendsModel);
						isInvited = true;
					}
				}
				customer.setFriendsEmailList(friendModelListToSave);
				modelService.save(customer);
			}
			else
			{
				for (final String email : friendListToInvite)
				{
					friendsModel = getModelService().create(FriendsModel.class);
					friendsModel.setFriendsEmail(email);
					friendsModel.setCustomerEmail(customer.getOriginalUid());
					friendsModel.setInviteSent(Boolean.FALSE);
					friendsModel.setRegistered(Boolean.FALSE);
					friendsModel.setAffiliateId(customer.getUid());

					friendModelListToSave.add(friendsModel);
					modelService.save(friendsModel);
					isInvited = true;
				}
				customer.setFriendsEmailList(friendModelListToSave);
				modelService.save(customer);
			}
		}
		catch (final ModelSavingException ex)
		{
			isInvited = false;
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			isInvited = false;
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return isInvited;
	}

	/**
	 * @description this is called to get invite Friends
	 * @return FriendsModel
	 */
	@Override
	public FriendsModel inviteFriends(final CustomerModel customer)
	{
		try
		{
			validateParameterNotNull(customer, MplConstants.CUSTOMER_MUST_NOT_BE_NULL);

			final List<FriendsModel> friendsInvited = this.getFriendsInvited(customer.getOriginalUid());
			FriendsModel friendsModelToBeSaved = null;
			boolean inviteSent = false;
			boolean isRegistered = false;

			friendsModelToBeSaved = getModelService().create(FriendsModel.class);
			for (final FriendsModel friendsModel : friendsInvited)
			{
				if (friendsModel.getFriendsEmail().equals(customer.getFriendsEmailList().get(0).getFriendsEmail()))
				{
					inviteSent = true;
					friendsModel.setInviteSent(Boolean.valueOf(inviteSent));
					modelService.save(friendsModel);
					return friendsModel;
				}
				else
				{
					inviteSent = false;
				}
			}
			if (null != friendsModelToBeSaved)
			{
				friendsModelToBeSaved = customer.getFriendsEmailList().get(0);
				if (null != friendsModelToBeSaved.getRegistered() && friendsModelToBeSaved.getRegistered().booleanValue())
				{
					isRegistered = true;
					return friendsModelToBeSaved;
				}
			}
			if (!isRegistered)
			{
				friendsModelToBeSaved.setInviteSent(Boolean.valueOf(inviteSent));
				friendsModelToBeSaved.setRegistered(Boolean.FALSE);
				friendsModelToBeSaved.setAffiliateId(customer.getUid().toString());
				getModelService().save(friendsModelToBeSaved);
				getModelService().save(customer);
				return friendsModelToBeSaved;
			}
			return null;
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this is called to get Friends already Invited
	 * @return List<FriendsModel>
	 */
	@Override
	public List<FriendsModel> getFriendsInvited(final String customerEmail)
	{
		try
		{
			validateParameterNotNull(customerEmail, MplConstants.CUSTOMER_MUST_NOT_BE_NULL);
			List<FriendsModel> friends = new ArrayList<FriendsModel>();
			final JaloSession session = JaloSession.getCurrentSession();
			session.createLocalSessionContext();
			try
			{
				friends = getFriendsInviteDao().findFriendsForACustomer(customerEmail);
			}
			finally
			{
				session.removeLocalSessionContext();
			}
			return friends;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this is called to get Friend Invited For An Affiliated Id
	 * @return List<FriendsModel>
	 */
	@Override
	public List<FriendsModel> getFriendsInvitedForAnAffiliate(final String affiliateId)
	{
		try
		{
			validateParameterNotNull(affiliateId, MplConstants.AFFILIATE_ID_MUST_NOT_BE_NULL);
			List<FriendsModel> friends = new ArrayList<FriendsModel>();
			final JaloSession session = JaloSession.getCurrentSession();
			session.createLocalSessionContext();
			try
			{
				friends = getFriendsInviteDao().findFriendsInvitedForAnAffiliate(affiliateId);
			}
			finally
			{
				session.removeLocalSessionContext();
			}
			return friends;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this is called to get Friend Invited For An Affiliated Id
	 * @return List<FriendsModel>
	 */
	@Override
	public List<FriendsModel> getFriendInvitedForAnAffiliate(final String affiliateId, final String friendsEmail)
	{
		try
		{
			validateParameterNotNull(friendsEmail, MplConstants.FRIENDS_EMAIL_MUST_NOT_BE_NULL);
			validateParameterNotNull(affiliateId, MplConstants.AFFILIATE_ID_MUST_NOT_BE_NULL);
			List<FriendsModel> friends = new ArrayList<FriendsModel>();
			final JaloSession session = JaloSession.getCurrentSession();
			session.createLocalSessionContext();
			try
			{
				friends = getFriendsInviteDao().findFriendInvitedForAnAffiliate(affiliateId, friendsEmail);
			}
			finally
			{
				session.removeLocalSessionContext();
			}
			return friends;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
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
	 * @description this is called to update the registered email flag
	 */
	@SuppressWarnings("static-access")
	@Override
	public void updateFriendsModel(final FriendsModel friendsModel)
	{
		try
		{
			validateParameterNotNull(friendsModel.getFriendsEmail(), MplConstants.FRIENDS_EMAIL_MUST_NOT_BE_NULL);
			validateParameterNotNull(friendsModel.getAffiliateId(), MplConstants.AFFILIATE_ID_MUST_NOT_BE_NULL);
			final List<FriendsModel> friendsInvitedByAffId = this.getAlreadyInvitedFriendsForAffiliatedId(friendsModel);

			FriendsModel friendsModelNew = new FriendsModel();
			if (null != friendsInvitedByAffId && friendsInvitedByAffId.size() > 0)
			{
				friendsModelNew = friendsInvitedByAffId.get(0);
				friendsModelNew.setRegistered(Boolean.TRUE);
				getModelService().save(friendsModelNew);
			}
			else
			{
				final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
				final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) friendsModelNew.getItemModelContext();
				itemModelContext.setLocaleProvider(localeProvider);
				final List<CustomerModel> customer = getFriendsInviteDao().findCustomerForUid(friendsModel.getAffiliateId());
				final String email = customer.get(0).getOriginalUid();
				friendsModelNew.setAffiliateId(friendsModel.getAffiliateId());
				friendsModelNew.setCustomerEmail(email);
				friendsModelNew.setInviteSent(Boolean.TRUE);
				friendsModelNew.setRegistered(Boolean.TRUE);
				friendsModelNew.setFriendsEmail(friendsModel.getFriendsEmail());
				getModelService().save(friendsModelNew);
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param friendsModel
	 * @return List<FriendsModel>
	 */
	private List<FriendsModel> getAlreadyInvitedFriendsForAffiliatedId(final FriendsModel friendsModel)
	{
		try
		{
			List<FriendsModel> friends = new ArrayList<FriendsModel>();
			final JaloSession session = JaloSession.getCurrentSession();
			session.createLocalSessionContext();
			try
			{
				friends = getFriendsInviteDao().findFriendsInvitedByAffIdAndFriendsEmail(friendsModel);
			}
			finally
			{
				session.removeLocalSessionContext();
			}
			return friends;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService#checkUniquenessOfEmail(java.lang.String)
	 */
	@Override
	public CustomerModel checkUniquenessOfEmail(final String emailId)
	{
		final CustomerModel customer = getCurrentCustomerByEmail(emailId);
		return customer;
	}

	public CustomerModel getCurrentCustomerByEmail(final String emailId)
	{
		try
		{
			CustomerModel customer = new CustomerModel();
			customer = extendedUserService.getUserForUid(emailId);
			return customer;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
