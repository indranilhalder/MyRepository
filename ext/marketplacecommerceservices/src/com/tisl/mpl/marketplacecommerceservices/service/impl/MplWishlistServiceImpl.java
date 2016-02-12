package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWishlistDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplWishlistService;


/**
 * @author TCS
 *
 */
public class MplWishlistServiceImpl implements MplWishlistService
{
	protected static final Logger LOG = Logger.getLogger(MplWishlistServiceImpl.class);
	@Autowired
	private ModelService modelService;
	@Deprecated
	private boolean saveAnonymousWishlists;
	@Autowired
	private MplWishlistDao mplWishlistDao;

	/**
	 * @description This method is used to accumulate all detail of product to add to wishlist
	 */
	@Override
	public void addWishlistEntry(final Wishlist2Model wishlist, final ProductModel product, final Integer desired,
			final Wishlist2EntryPriority priority, final String comment, final String ussid, final boolean selectedSize)
	{
		try
		{

			final Wishlist2EntryModel entry = new Wishlist2EntryModel();
			entry.setProduct(product);
			entry.setDesired(desired);
			entry.setPriority(priority);
			entry.setComment(comment);
			entry.setAddedDate(new Date());
			entry.setUssid(ussid);
			entry.setSizeSelected(Boolean.valueOf(selectedSize));
			addWishlistEntry(wishlist, entry);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this is called for adding entries to wishlist
	 * @param wishlist
	 * @param entry
	 */
	public void addWishlistEntry(final Wishlist2Model wishlist, final Wishlist2EntryModel entry)
	{
		try
		{
			if (saveWishlist(wishlist))
			{
				getModelService().save(entry);
				LOG.debug("MyWishListServiceImpl : addWishlistEntry: saved true");
			}
			final List entries = new ArrayList(wishlist.getEntries());
			entries.add(entry);
			wishlist.setEntries(entries);
			if (!(saveWishlist(wishlist)))
			{
				LOG.debug("MyWishListServiceImpl : addWishlistEntry: saved false");
				return;
			}
			getModelService().save(wishlist);
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
	 * @description internal method to save wishlist data
	 * @param wishlist
	 * @return boolean
	 */
	private boolean saveWishlist(final Wishlist2Model wishlist)
	{
		LOG.debug("MyWishListServiceImpl : saveWishlist: " + wishlist);
		final UserModel user = wishlist.getUser();
		return saveWishlist(user);
	}

	/**
	 * @description internal method to save wishlist data
	 * @param user
	 * @return boolean
	 */
	private boolean saveWishlist(final UserModel user)
	{
		if (user == null)
		{
			return false;
		}
		final boolean anonymous = MplConstants.USER.ANONYMOUS_CUSTOMER.equals(user.getUid());
		return ((!(anonymous)) || ((anonymous) && (this.saveAnonymousWishlists)));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWishlistService#getWishlists()
	 */
	@Override
	public List<Wishlist2Model> getWishlists(final UserModel user)
	{
		// YTODO Auto-generated method stub
		//return mplWishlistDao.findAllWishlists(user);
		return getMplWishlistDao().findAllWishlists(user);
	}


	/**
	 * @return the saveAnonymousWishlists
	 */
	public boolean isSaveAnonymousWishlists()
	{
		return saveAnonymousWishlists;
	}

	/**
	 * @param saveAnonymousWishlists
	 *           the saveAnonymousWishlists to set
	 */
	public void setSaveAnonymousWishlists(final boolean saveAnonymousWishlists)
	{
		this.saveAnonymousWishlists = saveAnonymousWishlists;
	}

	/**
	 * @return the mplWishlistDao
	 */
	public MplWishlistDao getMplWishlistDao()
	{
		return mplWishlistDao;
	}

	/**
	 * @param mplWishlistDao
	 *           the mplWishlistDao to set
	 */
	public void setMplWishlistDao(final MplWishlistDao mplWishlistDao)
	{
		this.mplWishlistDao = mplWishlistDao;
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
}
