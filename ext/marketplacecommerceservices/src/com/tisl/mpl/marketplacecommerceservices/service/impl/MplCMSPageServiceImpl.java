/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPageService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author 765463
 *
 */
public class MplCMSPageServiceImpl extends DefaultCMSPageService implements MplCmsPageService
{

	private MplCmsPageDao mplCmsPageDao;

	/**
	 * @return the mplCmsPageDao
	 */
	public MplCmsPageDao getMplCmsPageDao()
	{
		return mplCmsPageDao;
	}

	/**
	 * @param mplCmsPageDao
	 *           the mplCmsPageDao to set
	 */
	@Required
	public void setMplCmsPageDao(final MplCmsPageDao mplCmsPageDao)
	{
		this.mplCmsPageDao = mplCmsPageDao;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService#getLandingPageForCategory(de.hybris.platform
	 * .category.model.CategoryModel)
	 */
	@Override
	public ContentPageModel getLandingPageForCategory(final CategoryModel category) throws CMSItemNotFoundException
	{
		ContentPageModel landingPage;
		try
		{
			landingPage = mplCmsPageDao.getLandingPageForCategory(category);
		}
		catch (final NullPointerException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new CMSItemNotFoundException("Could not find a landing page for the given category " + category.getName());
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new CMSItemNotFoundException("Could not find a landing page for the given category " + category.getName());
		}

		return landingPage;

	}

	/**
	 * @param category
	 * @param mobile
	 * @return
	 */

	@Override
	public ContentPageModel getCategoryLandingPageForMobile(final CategoryModel category, final CMSChannel cms)
			throws CMSItemNotFoundException, NullPointerException
	{
		final ContentPageModel landingPage = mplCmsPageDao.getCategoryLandingPageForMobile(category, cms);

		return landingPage;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService#getHomePageForMobile()
	 */
	@Override
	public ContentPageModel getHomePageForMobile()
	{
		// YTODO Auto-generated method stub
		return mplCmsPageDao.getHomePageForMobile(CMSChannel.MOBILE);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService#getLandingPageForCategory(de.hybris.platform
	 * .category.model.CategoryModel)
	 */
	@Override
	public ContentPageModel getLandingPageForSeller(final SellerMasterModel sellerMaster) throws CMSItemNotFoundException
	{
		ContentPageModel landingPage;
		try
		{
			landingPage = mplCmsPageDao.getLandingPageForSeller(sellerMaster);
		}
		catch (final NullPointerException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new CMSItemNotFoundException("Could not find a landing page for the given seller " + sellerMaster.getLegalName());
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new CMSItemNotFoundException("Could not find a landing page for the given seller " + sellerMaster.getLegalName());
		}

		return landingPage;

	}

	@Override
	public ContentPageModel getCollectionLandingPageForMobile(final String collectionId, final CMSChannel cms)
	{
		final MplShopByLookModel shopByLook = mplCmsPageDao.getShopByLookModelForCollection(collectionId);

		if (null != shopByLook)
		{
			final Date today = new Date();
			if ((null != shopByLook.getStartDate() && null != shopByLook.getEndDate() && shopByLook.getStartDate().before(today)
					&& shopByLook.getEndDate().after(today))
					|| (null == shopByLook.getStartDate() && null != shopByLook.getEndDate() && shopByLook.getEndDate().after(today))
					|| (null != shopByLook.getStartDate() && shopByLook.getStartDate().before(today)
							&& null == shopByLook.getEndDate())
					|| null == shopByLook.getStartDate() && null == shopByLook.getEndDate())
			{
				final ContentPageModel landingPage = mplCmsPageDao.getCollectionLandingPageForMobile(cms, shopByLook);
				return landingPage;
			}
		}
		return null;
	}

	/**
	 * @param sellerMasterModel
	 * @param cms
	 * @return
	 */

	@Override
	public ContentPageModel getSellerLandingPageForMobile(final SellerMasterModel sellerMasterModel, final CMSChannel cms)
			throws CMSItemNotFoundException, NullPointerException
	{
		final ContentPageModel landingPage = mplCmsPageDao.getSellerLandingPageForMobile(sellerMasterModel, cms);

		return landingPage;

	}
}
