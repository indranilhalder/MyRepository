/**
 *
 */
package com.tisl.mpl.facade.wishlist.impl;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.data.EditWishlistNameData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplWishlistService;


/**
 * @author TCS
 *
 */
public class DefaultWishlistFacade implements WishlistFacade
{
	protected static final Logger LOG = Logger.getLogger(DefaultWishlistFacade.class);

	@Autowired
	private Wishlist2Service wishlistService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplWishlistService mplWishlistService;
	@Autowired
	private UserService userService;
	@Autowired
	private BuyBoxService buyBoxService;
	@Autowired
	private PriceDataFactory priceDataFactory;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderFacade orderFacade;



	/**
	 * @description get size of entries in default wishlist
	 * @return integer
	 */
	@Override
	public int getSize()
	{
		try
		{
			return wishlistService.getDefaultWishlist().getEntries().size();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is called to remove Product From Wishlist
	 * @return wishlist2Model
	 */
	@Override
	public Wishlist2Model removeProductFromWl(final String productCode, final String wishlistName, final String ussid)
	{
		Wishlist2Model wishlist2Model = null;
		try
		{
			wishlist2Model = getWishlistForName(wishlistName);
			for (final Wishlist2EntryModel entryModel : wishlist2Model.getEntries())
			{
				if (null != entryModel.getProduct() && entryModel.getProduct().getCode().equals(productCode)
						&& entryModel.getUssid().equals(ussid))
				{
					//TPR-5787 starts here
					LOG.debug("To remove the prod from wishlist ----- 1");
					final Date date = new Date();
					entryModel.setIsDeleted(Boolean.TRUE);
					entryModel.setDeletedDate(new Timestamp(date.getTime()));
					entryModel.setDesired(Integer.valueOf(0));//TISSPTEN-2
					modelService.save(entryModel);
					break;
					//TPR-5787 ends here
				}
			}
			//wishlistService.removeWishlistEntry(wishlist2Model, wishlist2EntryModel);//commented for TPR-5787
		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			LOG.error("removeProductFromWl:" + e.getMessage());
		}
		return wishlist2Model;

	}

	/* Changes for INC144313867 */

	/**
	 * @description this method is called to remove Product From Wishlist
	 * @return wishlist2Model
	 */
	@Override
	public Wishlist2Model removeProductFromWl(final String productCode, final String wishlistName)
	{
		Wishlist2Model wishlist2Model = null;
		try
		{
			wishlist2Model = getWishlistForName(wishlistName);
			for (final Wishlist2EntryModel entryModel : wishlist2Model.getEntries())
			{
				if (null != entryModel.getProduct() && entryModel.getProduct().getCode().equals(productCode))
				{
					//TPR-5787 starts here
					LOG.debug("To remove the prod from wishlist ----- 2");
					final Date date = new Date();
					entryModel.setIsDeleted(Boolean.TRUE);
					entryModel.setDeletedDate(new Timestamp(date.getTime()));
					entryModel.setDesired(Integer.valueOf(0));//TISSPTEN-2
					modelService.save(entryModel);
					break;
					//TPR-5787 ends here
				}
			}
			//wishlistService.removeWishlistEntry(wishlist2Model, wishlist2EntryModel);//commented for TPR-5787

		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			LOG.error("removeProductFromWl:" + e.getMessage());
		}
		return wishlist2Model;
	}

	/**
	 * @description this method is called to remove Product From Wishlist with USSID
	 * @return wishlist2Model
	 */
	@Override
	public boolean removeWishlistEntry(final Wishlist2EntryModel wishentryModel)
	{
		boolean saved = false;
		try
		{
			if (null != wishentryModel.getProduct()
					&& (wishentryModel.getIsDeleted() == null || (wishentryModel.getIsDeleted() != null && !wishentryModel
							.getIsDeleted().booleanValue())))//TPR-5787 check added here
			{
				//TPR-5787 starts here
				LOG.debug("To remove the prod from wishlist entry-----");
				final Date date = new Date();
				wishentryModel.setIsDeleted(Boolean.TRUE);
				wishentryModel.setDeletedDate(new Timestamp(date.getTime()));
				wishentryModel.setDesired(Integer.valueOf(0));//TISSPTEN-2
				modelService.save(wishentryModel);
				saved = true;
				//TPR-5787 ends here
			}

		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			LOG.error("removeProductFromWl:" + e.getMessage());
		}
		return saved;
	}

	/**
	 * @description to get all wishlist
	 * @return List<Wishlist2Model>
	 */
	@Override
	public List<Wishlist2Model> getAllWishlists()
	{
		try
		{
			final UserModel user = userService.getCurrentUser();
			return mplWishlistService.getWishlists(user);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description to get all wishlist
	 * @return List<Wishlist2Model>
	 */
	@Override
	public List<Wishlist2EntryModel> getAllWishlistByUssid(final String ussid)
	{
		try
		{
			final UserModel user = userService.getCurrentUser();
			return mplWishlistService.getWishlistByUserAndUssid(user, ussid);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description to create new wishlist
	 * @return wishlist2Model
	 */
	@Override
	public Wishlist2Model createNewWishlist(final UserModel user, final String name, final String description)
	{
		Wishlist2Model createdWl = null;

		try
		{
			if (wishlistService.hasDefaultWishlist(user))
			{
				createdWl = wishlistService.createWishlist(name, description);
			}
			else
			{
				createdWl = wishlistService.createDefaultWishlist(user, name, description);
			}
			return createdWl;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description to add product to Wishlist
	 * @return boolean
	 */
	@Override
	public boolean addProductToWishlist(final Wishlist2Model wishlist, final String productCode, final String ussid,
			final boolean selectedSize)
	{
		//TPR-5787 starts here
		boolean add = true;
		try
		{
			LOG.debug("addProductToWishlist in web : *****productCode: " + productCode + " **** ussid: " + ussid
					+ " *** selectedSize: " + selectedSize);
			List<Wishlist2EntryModel> wishlist2Entry = null;

			wishlist2Entry = mplWishlistService.findWishlistEntryByProductAndUssid(ussid);

			ProductModel product = null;
			if (CollectionUtils.isEmpty(wishlist2Entry))
			{
				LOG.debug("wishlistentry is empty");
				product = productService.getProductForCode(productCode);
				final String comment = MplConstants.MPL_WISHLIST_COMMENT;
				if (null != ussid && !ussid.isEmpty())
				{
					LOG.debug("wishlistentry is empty and ussid is not null");
					mplWishlistService.addWishlistEntry(wishlist, product, Integer.valueOf(1), Wishlist2EntryPriority.HIGH, comment,
							ussid, selectedSize);
				}
			}
			else
			{
				LOG.debug("wishlist entry is not empty");
				final Wishlist2EntryModel wishlist2UpdateEntry = wishlist2Entry.get(0);
				if (wishlist2UpdateEntry.getIsDeleted().booleanValue())
				{
					LOG.debug("wishlist entry added");
					final Date date = new Date();//TISSPTEN-2
					wishlist2UpdateEntry.setAddedDate(new Timestamp(date.getTime()));//TISSPTEN-2
					wishlist2UpdateEntry.setIsDeleted(Boolean.FALSE);
					wishlist2UpdateEntry.setDeletedDate(null);
					wishlist2UpdateEntry.setSizeSelected(Boolean.valueOf(selectedSize));//TISSPTEN-2
					wishlist2UpdateEntry.setDesired(Integer.valueOf(1));//TISSPTEN-2
					modelService.save(wishlist2UpdateEntry);

				}
				else
				{
					LOG.debug("wishlist entry not added");
					add = false;
				}
			}
		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			e.getMessage();
		}
		return add;
		//TPR-5787 ends here
	}

	//CAR Project performance issue fixed

	/**
	 * @description to add product to Wishlist for mobile
	 * @return boolean
	 */
	@Override
	public boolean addProductToWishlistMobile(final Wishlist2Model wishlist, final String productCode, final String ussid,
			final boolean selectedSize)
	{
		boolean add = true;
		try
		{
			LOG.debug("addProductToWishlist in Mobile : *****productCode: " + productCode + " **** ussid: " + ussid
					+ " *** selectedSize: " + selectedSize);
			List<Wishlist2EntryModel> wishlist2Entry = null;

			wishlist2Entry = mplWishlistService.findWishlistEntryByProductAndUssid(ussid);

			ProductModel product = null;
			if (CollectionUtils.isEmpty(wishlist2Entry))
			{
				LOG.debug("wishlistentry is empty");
				product = productService.getProductForCode(productCode);
				final String comment = MplConstants.MPL_WISHLIST_COMMENT;
				if (null != ussid && !ussid.isEmpty())
				{
					LOG.debug("wishlistentry is empty and ussid is not null");
					mplWishlistService.addWishlistEntry(wishlist, product, Integer.valueOf(1), Wishlist2EntryPriority.HIGH, comment,
							ussid, selectedSize);
				}
			}
			else
			{
				//TPR-5787 starts here
				LOG.debug("wishlist entry is not empty");
				final Wishlist2EntryModel wishlist2UpdateEntry = wishlist2Entry.get(0);
				if (wishlist2UpdateEntry.getIsDeleted().booleanValue())
				{
					LOG.debug("wishlist entry added");
					final Date date = new Date();//TISSPTEN-2
					wishlist2UpdateEntry.setAddedDate(new Timestamp(date.getTime()));//TISSPTEN-2
					wishlist2UpdateEntry.setIsDeleted(Boolean.FALSE);
					wishlist2UpdateEntry.setDeletedDate(null);
					wishlist2UpdateEntry.setSizeSelected(Boolean.valueOf(selectedSize));//TISSPTEN-2
					wishlist2UpdateEntry.setDesired(Integer.valueOf(1));//TISSPTEN-2
					modelService.save(wishlist2UpdateEntry);
				}
				else
				{
					LOG.debug("wishlist entry not added");
					add = false;
				}
				//TPR-5787 ends here
			}
		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			e.getMessage();
		}
		return add;
	}


	/**
	 * @description to fetch a particular Wishlist by name
	 * @return wishlist2Model
	 */
	@Override
	public Wishlist2Model getWishlistForName(final String wishlistName)
	{
		try
		{
			LOG.debug("getWishlistForName: ***** wishlistName: " + wishlistName);
			final List<Wishlist2Model> allWishlists = getAllWishlists();
			Wishlist2Model requiredWl = null;
			for (final Wishlist2Model wl : allWishlists)
			{
				if (wishlistName.equals(wl.getName()))
				{
					requiredWl = wl;
					break;
				}
			}
			LOG.debug("getWishlistForName: ***** Return requiredWl: " + requiredWl);
			return requiredWl;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description to edit Name of a Wishlist
	 * @return wishlist2Model
	 */
	@Override
	public Wishlist2Model editWishlistName(final EditWishlistNameData editWishlistNameData)
	{
		try
		{
			Wishlist2Model wishlist2Model = null;
			if (editWishlistNameData != null)
			{
				if (editWishlistNameData.getParticularWishlistName() != null
						&& !editWishlistNameData.getParticularWishlistName().isEmpty())
				{
					wishlist2Model = getWishlistForName(editWishlistNameData.getParticularWishlistName());
				}
				if (null != wishlist2Model && null != editWishlistNameData.getNewWishlistName()
						&& !editWishlistNameData.getNewWishlistName().isEmpty())
				{
					wishlist2Model.setName(editWishlistNameData.getNewWishlistName());
					modelService.save(wishlist2Model);
					return wishlist2Model;
				}

			}
			return null;
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.wishlist.WishlistFacade#getBuyBoxPrice(java.lang.String,
	 * de.hybris.platform.commercefacades.product.data.ProductData)
	 */
	@Override
	public ProductData getBuyBoxPrice(final String ussid, final ProductData productData1)
	{
		try
		{
			final BuyBoxModel buyBox = buyBoxService.getpriceForUssid(ussid);
			final CartModel serviceCart = cartService.getSessionCart();
			if (null != serviceCart.getEntries() && !serviceCart.getEntries().isEmpty())
			{
				if (null != buyBox.getSpecialPrice() && buyBox.getSpecialPrice().doubleValue() > 0.00)
				{
					productData1.setProductMOP(createPrice(serviceCart.getEntries().get(0), buyBox.getSpecialPrice()));
				}
				else
				{
					productData1.setProductMOP(createPrice(serviceCart.getEntries().get(0), buyBox.getPrice()));
				}
			}
			return productData1;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	protected PriceData createPrice(final AbstractOrderEntryModel orderEntry, final Double val)
	{
		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(val.doubleValue()), orderEntry.getOrder()
				.getCurrency());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.wishlist.WishlistFacade#removeProductFromWL(java.lang.String)
	 */
	@Override
	public void removeProductFromWL(final String orderCode)
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			final List<OrderEntryData> orderEntryDatas = orderDetails.getEntries();
			final List<Wishlist2Model> allWishlists = getAllWishlists();

			if (null != orderEntryDatas && !orderEntryDatas.isEmpty())
			{

				for (final OrderEntryData orderEntryData : orderEntryDatas)
				{
					if (null != orderEntryData.getProduct() && !orderEntryData.getSelectedUssid().isEmpty())
					{
						final String productCode = orderEntryData.getProduct().getCode();
						final String ussid = orderEntryData.getSelectedUssid();
						for (final Wishlist2Model wishlist2Model : allWishlists)
						{
							for (final Wishlist2EntryModel entry : wishlist2Model.getEntries())
							{
								if (null != entry.getAddToCartFromWl() && entry.getAddToCartFromWl().equals(Boolean.TRUE))
								{
									removeProductFromWl(productCode, wishlist2Model.getName(), ussid);
								}
							}
						}
					}
				}
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This method removes products from wishlist when the customer is redirected to order confirmation page after
	 * placing order
	 *
	 * @param orderDetails
	 *
	 */
	//TISPT-175 : changing to reduce populator call multiple times -- also multiple for loop removed by this method
	@Override
	public void remProdFromWLForConf(final OrderData orderDetails, final UserModel userModel)
	{
		try
		{
			List<Wishlist2EntryModel> wishlist2EntryModels = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(orderDetails.getEntries()))
			{
				for (final OrderEntryData orderEntryData : orderDetails.getEntries())
				{
					if (StringUtils.isNotEmpty(orderEntryData.getSelectedUssid()))
					{
						wishlist2EntryModels = mplWishlistService.getWishlistByUserAndUssid(userModel,
								orderEntryData.getSelectedUssid());

						for (final Wishlist2EntryModel wishlist2EntryModel : wishlist2EntryModels)
						{
							if (null != wishlist2EntryModel.getAddToCartFromWl()
									&& wishlist2EntryModel.getAddToCartFromWl().equals(Boolean.TRUE)
									&& (wishlist2EntryModel.getIsDeleted() == null || (wishlist2EntryModel.getIsDeleted() != null && !wishlist2EntryModel
											.getIsDeleted().booleanValue())))//TPR-5787 check added here
							{
								final Date date = new Date();
								wishlist2EntryModel.setIsDeleted(Boolean.TRUE);
								wishlist2EntryModel.setDeletedDate(new Timestamp(date.getTime()));
								wishlist2EntryModel.setDesired(Integer.valueOf(0));//TISSPTEN-2
								modelService.save(wishlist2EntryModel);
								break;
								//wishlistService.removeWishlistEntry(wishlist2Model, wishlist2EntryModel);
							}
						}
					}
				}
			}

		}
		/*
		 * catch (final Exception ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final ModelSavingException e)
		{
			e.getMessage();
		}
	}

	/**
	 * Desc It will fetch all wishlists for a customer/user TISPT-179 Point 1
	 *
	 * @param userModel
	 * @return List<Wishlist2Model>
	 */
	@Override
	public List<Wishlist2Model> getAllWishlistsForCustomer(final UserModel userModel)
	{
		try
		{
			return mplWishlistService.getWishlists(userModel);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.wishlist.WishlistFacade#getSingleWishlist(de.hybris.platform.core.model.user.UserModel)
	 */
	@Override
	public Wishlist2Model getSingleWishlist(final UserModel user)
	{
		try
		{
			final List<Wishlist2Model> allWishlists = mplWishlistService.getWishListAgainstUser(user);
			final Wishlist2Model requiredWl = (CollectionUtils.isEmpty(allWishlists)) ? null : allWishlists.get(0);
			LOG.debug("getWishlistForName: ***** Return requiredWl: " + requiredWl);
			return requiredWl;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}


	}

	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */
	@Override
	public Wishlist2Model findMobileWishlistswithName(final UserModel user, final String name)
	{
		return mplWishlistService.findMobileWishlistswithName(user, name);
	}

	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */
	@Override
	public int findMobileWishlistswithNameCount(final UserModel user, final String name)
	{
		return mplWishlistService.findMobileWishlistswithNameCount(user, name);
	}

}
