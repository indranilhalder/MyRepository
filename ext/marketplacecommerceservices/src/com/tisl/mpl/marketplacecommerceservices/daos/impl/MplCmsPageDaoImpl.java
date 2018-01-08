/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.impl.DefaultCMSPageDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.AmpMenifestModel;
import com.tisl.mpl.core.model.AmpServiceworkerModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.MplFooterLinkModel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao;
import com.tisl.mpl.model.SellerMasterModel;




/**
 * @author TCS
 *
 */
public class MplCmsPageDaoImpl extends DefaultCMSPageDao implements MplCmsPageDao
{
	//private final String MOBILE_UID = "MobileHomepage";	//SONAR Fix for unused private field
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private CatalogVersionService catalogVersionService;


	private static final String SELECT_CLASS = "Select {";

	private static final String FROM_CLASS = "} From {";

	private static final Object ONLINE_CATALOG_VERSION = "Online";

	private static final String UID = "uid";

	private static final String LEFT_JOIN = " as cp left join ";



	@Override
	public ContentPageModel getLandingPageForCategory(final CategoryModel category)
	{
		final StringBuilder queryString = getQuery();

		queryString.append(" And ({cm.code} = ?channel or {cp.channel} is null)");


		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("category", category);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, CMSChannel.DESKTOP.getCode());


		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;

	}

	@Override
	public ContentPageModel getCategoryLandingPageForMobile(final CategoryModel category, final CMSChannel cms)
	{
		final StringBuilder queryString = getQuery();

		queryString.append(" And {cm.code} = ?channel");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("category", category);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, cms.getCode());


		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	@Override
	public ContentPageModel getLandingPageForSeller(final SellerMasterModel sellerMaster)
	{
		//final StringBuilder queryString = new StringBuilder("SELECT {con." + ContentPageModel.PK + "} ");

		/*
		 * final StringBuilder queryString = new StringBuilder(57); queryString.append("SELECT {con." +
		 * ContentPageModel.PK + "} "); queryString.append("FROM {" + ContentPageModel._TYPECODE + " AS con} ");
		 * queryString.append("WHERE {con." + ContentPageModel.ASSOCIATEDSELLER + "} = ?sellerMaster");
		 */

		final StringBuilder queryString = getSellerQuery();

		queryString.append(" And ({cm.code} = ?channel or {cp.channel} is null)");


		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("sellerMaster", sellerMaster);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, CMSChannel.DESKTOP.getCode());

		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	public StringBuilder getQuery()
	{
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(LEFT_JOIN).append(CMSChannel._TYPECODE)
				.append(" as cm ON {cp.channel} = {cm.pk}}").append(" Where {cp.categoryAssociated} = ?category ");

		return queryString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao#getHomePageForMobile()
	 */
	@Override
	public ContentPageModel getHomePageForMobile(final CMSChannel cms, final String pageUid)
	{
		// YTODO Auto-generated method stub
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(" AS C}, {").append(CatalogVersionModel._TYPECODE)
				.append(" AS CV} Where {C:").append(ContentPageModel.UID).append("} = ?uid").append(" And {C:")
				.append(ContentPageModel.CHANNEL).append("} = ?channel").append(" And {C:").append(ContentPageModel.CATALOGVERSION)
				.append("} = {CV:").append(CatalogVersionModel.PK).append(" } AND {CV:").append(CatalogVersionModel.VERSION)
				.append("} = ?version");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter(UID, pageUid);
		query.addQueryParameter("version", ONLINE_CATALOG_VERSION);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, cms);

		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	@Override
	public MplShopByLookModel getShopByLookModelForCollection(final String collectionId)
	{
		final StringBuilder queryString = new StringBuilder("SELECT {pk}  FROM {MplShopByLook} WHERE {collectionId} = ?collection");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("collection", collectionId);


		final List<MplShopByLookModel> shopByLookList = flexibleSearchService.<MplShopByLookModel> search(query).getResult();
		//if (shopByLookList != null && shopByLookList.size() > 0)
		if (CollectionUtils.isNotEmpty(shopByLookList))
		{
			return shopByLookList.get(0);
		}

		return null;
	}

	@Override
	public ContentPageModel getPageForAppById(final String pageUid)
	{
		// YTODO Auto-generated method stub
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(" AS C}, {").append(CatalogVersionModel._TYPECODE)
				.append(" AS CV} Where {C:").append(ContentPageModel.UID).append("} = ?uid").append(" And {C:")
				.append(ContentPageModel.CATALOGVERSION).append("} = {CV:").append(CatalogVersionModel.PK).append(" } AND {CV:")
				.append(CatalogVersionModel.VERSION).append("} = ?version");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter(UID, pageUid);
		query.addQueryParameter("version", ONLINE_CATALOG_VERSION);

		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	@Override
	public ContentPageModel getCollectionLandingPageForMobile(final CMSChannel cms, final MplShopByLookModel shopByLook)
	{
		final StringBuilder queryString = getCollectionQuery();

		//final MplShopByLookModel shopByLook = getShopByLookModelForCollection(collectionId);


		queryString.append(" And {cm.code} = ?channel");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("collection", shopByLook);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, cms.getCode());


		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	public StringBuilder getCollectionQuery()
	{
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(LEFT_JOIN).append(CMSChannel._TYPECODE)
				.append(" as cm ON {cp.channel} = {cm.pk}}").append(" Where {cp.collectionAssociated} = ?collection ");

		return queryString;
	}

	@Override
	public ContentPageModel getSellerLandingPageForMobile(final SellerMasterModel sellerMasterModel, final CMSChannel cms)
	{
		final StringBuilder queryString = getSellerQuery();

		queryString.append(" And {cm.code} = ?channel");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("sellerMaster", sellerMasterModel);
		query.addQueryParameter("channel", cms.getCode());

		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	public StringBuilder getSellerQuery()
	{
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(LEFT_JOIN).append(CMSChannel._TYPECODE)
				.append(" as cm ON {cp.channel} = {cm.pk}}")
				.append(" Where {cp." + ContentPageModel.ASSOCIATEDSELLER + "} = ?sellerMaster ");
		return queryString;
	}


	@Override
	public ContentSlotModel getContentSlotByUidForPage(final String pageId, final String contentSlotId, final String catalogVersion)
	{
		final String queryString = "Select {cs." + ContentSlotModel.PK + "} from {" + ContentSlotForPageModel._TYPECODE
				+ " as csp join " + ContentSlotModel._TYPECODE + " as cs on {csp." + ContentSlotForPageModel.CONTENTSLOT + "}={cs."
				+ ContentSlotModel.PK + "} " + "join " + ContentPageModel._TYPECODE + " as cp on {csp."
				+ ContentSlotForPageModel.PAGE + "}={cp.pk} join " + CatalogVersionModel._TYPECODE + " as cv on {cp."
				+ ContentPageModel.CATALOGVERSION + "}={cv." + CatalogVersionModel.PK + "}} " + "where {cp." + ContentPageModel.UID
				+ "}=?uid and {cs." + ContentSlotModel.UID + "}=?contentUid and {cv." + CatalogVersionModel.VERSION + "}=?catVersion";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(UID, pageId);
		query.addQueryParameter("contentUid", contentSlotId);
		query.addQueryParameter("catVersion", catalogVersion);

		final List<ContentSlotModel> contentSlots = flexibleSearchService.<ContentSlotModel> search(query).getResult();
		//if (contentSlots != null && contentSlots.size() > 0)
		if (CollectionUtils.isNotEmpty(contentSlots))
		{
			return contentSlots.get(0);
		}

		return null;
	}

	@Resource
	private PagedFlexibleSearchService pagedFlexibleSearchService;



	/**
	 * @return the pagedFlexibleSearchService
	 */
	public PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}

	/**
	 * @param pagedFlexibleSearchService
	 *           the pagedFlexibleSearchService to set
	 */
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}

	/* SONAR FIX */
	/*
	 * @Autowired private CatalogVersionService catalogversionservice;
	 */

	@Autowired
	private ConfigurationService configurationService;

	/*
	 * @Override public SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(final String pageUid, final
	 * PageableData pageableData) {
	 *
	 * final CatalogVersionModel catalogmodel =
	 * catalogversionservice.getCatalogVersion(configurationService.getConfiguration()
	 * .getString("internal.campaign.catelog"),
	 * configurationService.getConfiguration().getString("internal.campaign.catalogVersionName"));
	 *
	 * final Map params = new HashMap(); params.put("uid", pageUid); params.put("version", catalogmodel);
	 *
	 * final String query =
	 * "Select {CSP.pk} From {ContentSlotForPage AS CSP JOIN ContentPage as CP ON {CSP.page}={CP.pk}} where {CP.uid} = ?uid and {CSP.catalogVersion}=?version"
	 * ;
	 *
	 * return getPagedFlexibleSearchService().search(query, params, pageableData);
	 *
	 *
	 * }
	 */

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

		final CatalogVersionModel catalogmodel = catalogVersionService.getCatalogVersion(configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.MPLCATELOG),
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.MPLCATALOGNNAME));

		//final String query = MarketplacecommerceservicesConstants.WCMSPAGINATIONQUERY;
		final String queryStr = "Select {CSP." + ContentSlotForPageModel.PK + "} From {" + ContentSlotForPageModel._TYPECODE
				+ " AS CSP JOIN " + ContentPageModel._TYPECODE + " AS CP ON {CSP." + ContentSlotForPageModel.PAGE + "}={CP."
				+ ContentPageModel.PK + "}} " + "where {CP." + ContentPageModel.UID + "} = ?uid and {CSP."
				+ ContentSlotForPageModel.CATALOGVERSION + "}=?version";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(UID, pageUid);
		params.put("version", catalogmodel);

		return pagedFlexibleSearchService.search(queryStr, params, pageableData);

	}

	/**
	 * @Description get the content page for product
	 * @param product
	 * @return ContentPageModel
	 */
	@Override
	public ContentPageModel getContentPageForProduct(final ProductModel product)
	{

		final StringBuilder queryString = getProductContentQuery();

		queryString.append(" where ({cm.code} = ?channel or {cp.channel} is null)").append(
				" and {cp.associatedProducts} like '%" + product.getPk().toString() + "%'");

		//	System.out.println("Query:::::::::::::" + queryString.toString());
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		//query.addQueryParameter("category", category);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CHANNEL, CMSChannel.DESKTOP.getCode());


		final List<ContentPageModel> contentPages = flexibleSearchService.<ContentPageModel> search(query).getResult();
		//if (contentPages != null && contentPages.size() > 0)
		if (CollectionUtils.isNotEmpty(contentPages))
		{
			return contentPages.get(0);
		}

		return null;
	}

	/**
	 * @Description get the product query
	 * @return StringBuilder
	 */
	public StringBuilder getProductContentQuery()
	{
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append(LEFT_JOIN).append(CMSChannel._TYPECODE)
				.append(" as cm ON {cp.channel} = {cm.pk}} ");

		return queryString;
	}

	/*
	 * TPR-1072 to fetch all the brands UID
	 */
	@Override
	public List<BrandComponentModel> getBrandsForShopByBrand()
	{
		final CatalogVersionModel catalogmodel = catalogVersionService.getCatalogVersion(configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.MPLCATELOG),
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.MPLCATALOGNNAME));

		final String queryString = "Select {" + BrandComponentModel.PK + "} from {" + BrandComponentModel._TYPECODE + "} where {"
				+ BrandComponentModel.LAYOUT + "} is not null AND {" + BrandComponentModel.CATALOGVERSION + "}=?catVersion";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("catVersion", catalogmodel);
		//		query.addQueryParameter("catVersion", catalogVersion);
		return flexibleSearchService.<BrandComponentModel> search(query).getResult();
	}

	/**
	 * @param siteUid
	 * @return CMSSite
	 * @CAR-285
	 */
	@Override
	public CMSSiteModel getSiteforId(final String siteUid) throws CMSItemNotFoundException
	{
		final CMSSiteModel cms = new CMSSiteModel();
		cms.setUid(siteUid);
		return flexibleSearchService.getModelByExample(cms);
	}

	/*
	 * Fetches all footerLink models
	 *
	 * @param void
	 *
	 * @return List<MplFooterLinkModel>
	 */

	@Override
	public List<MplFooterLinkModel> getAllFooterLinks()
	{
		final String queryStr = MarketplacecommerceservicesConstants.FOOTER_LINK_QUERY;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		final List<MplFooterLinkModel> footerLinks = flexibleSearchService.<MplFooterLinkModel> search(query).getResult();
		return footerLinks;
	}

	/*
	 * Fetches all AmpServiceworker models
	 *
	 * @param void
	 *
	 * @return List<AmpServiceworkerModel>
	 */
	@Override
	public List<AmpServiceworkerModel> getAllAmpServiceworkers()
	{
		final String queryStr = MarketplacecommerceservicesConstants.AMP_SERVICEWORKER_QUERY;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		final List<AmpServiceworkerModel> serviceworkerModels = flexibleSearchService.<AmpServiceworkerModel> search(query)
				.getResult();
		return serviceworkerModels;
	}

	/*
	 * Fetches all AmpServiceworker models
	 *
	 * @param void
	 *
	 * @return List<AmpServiceworkerModel>
	 */
	@Override
	public List<AmpMenifestModel> getAllAmpMenifestJsons()
	{
		final String queryStr = MarketplacecommerceservicesConstants.AMP_MENIFEST_JSON_QUERY;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		final List<AmpMenifestModel> menifestJsonModels = flexibleSearchService.<AmpMenifestModel> search(query).getResult();
		return menifestJsonModels;
	}
}
