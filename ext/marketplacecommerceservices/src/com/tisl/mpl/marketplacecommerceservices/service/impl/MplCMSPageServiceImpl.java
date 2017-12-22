/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPageService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.MplFooterLinkModel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.lux.model.LuxuryHomePagePreferenceModel;

/**
 * @author 765463
 *
 */
public class MplCMSPageServiceImpl extends DefaultCMSPageService implements MplCmsPageService
{

	private MplCmsPageDao mplCmsPageDao;

	//	@Autowired
	//	private ConfigurationService configurationService;

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
	public ContentPageModel getHomePageForMobile(final String pageUid)
	{
		// YTODO Auto-generated method stub
		return mplCmsPageDao.getHomePageForMobile(CMSChannel.MOBILE, pageUid);

	}

	@Override
	public ContentPageModel getPageForAppById(final String pageUid)
	{
		// YTODO Auto-generated method stub
		return mplCmsPageDao.getPageForAppById(pageUid);

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
			if ((null != shopByLook.getStartDate() && null != shopByLook.getEndDate() && shopByLook.getStartDate().before(today) && shopByLook
					.getEndDate().after(today))
					|| (null == shopByLook.getStartDate() && null != shopByLook.getEndDate() && shopByLook.getEndDate().after(today))
					|| (null != shopByLook.getStartDate() && shopByLook.getStartDate().before(today) && null == shopByLook
							.getEndDate()) || null == shopByLook.getStartDate() && null == shopByLook.getEndDate())
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

	//public Collection<ContentPageModel> getAllContentPages(final CatalogVersionModel catalogmodel)
	@Override
	public Collection<ContentPageModel> getAllContentPages(final Collection<CatalogVersionModel> catalogmodel)
	{
		return mplCmsPageDao.findAllContentPagesByCatalogVersions(catalogmodel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService#getContentSlotByUidForPage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ContentSlotModel getContentSlotByUidForPage(final String pageId, final String contentSlotId, final String catalogVersion)
	{
		final ContentSlotModel contentSlot = mplCmsPageDao.getContentSlotByUidForPage(pageId, contentSlotId, catalogVersion);
		return contentSlot;
	}

	/**
	 * Method added for TPR-798
	 *
	 * @param pageUid
	 * @param pageableData
	 * @return SearchPageData<ContentSlotForPageModel>
	 */
	@Override
	public SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(final String pageUid, final PageableData pageableData)
	{
		// YTODO Auto-generated method stub
		return mplCmsPageDao.getContentSlotsForAppById(pageUid, pageableData);

	}

	//TPR-978
	@Override
	public ContentPageModel getContentPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		final ContentPageModel contentPage = mplCmsPageDao.getContentPageForProduct(product);
		return contentPage;
	}

	@Override
	public AbstractPageModel getPageForIdandCatalogVersion(final String id, final CatalogVersionModel cv)
			throws CMSItemNotFoundException
	{
		final List pages = this.cmsPageDao.findPagesById(id, Collections.singletonList(cv));
		if (pages.isEmpty())
		{
			throw new CMSItemNotFoundException("No page with id [" + id + "] found.");
		}
		return ((AbstractPageModel) pages.iterator().next());
	}

	//TPR-1072
	@Override
	public List<BrandComponentModel> getBrandsForShopByBrand()
	{
		return mplCmsPageDao.getBrandsForShopByBrand();

	}

	/**
	 * @param siteUid
	 * @return CMSSite
	 * @CAR-285
	 */
	@Override
	public CMSSiteModel getSiteforId(final String siteUid) throws CMSItemNotFoundException
	{
		return mplCmsPageDao.getSiteforId(siteUid);
	}

	/**
	 * TPR-5733 | Fetches list of MplFooterLinkModel
	 *
	 * @return List<FooterLinkData>
	 */
	public List<MplFooterLinkModel> getAllMplFooterLinks()
	{
		return mplCmsPageDao.getAllFooterLinks();
	}
	
	@Override
	public LuxuryHomePagePreferenceModel getHomePagePreference(final String gender, final String category)
	{
		// YTODO Auto-generated method stub
		return mplCmsPageDao.getHomePagePreference(gender, category);
	}

}
