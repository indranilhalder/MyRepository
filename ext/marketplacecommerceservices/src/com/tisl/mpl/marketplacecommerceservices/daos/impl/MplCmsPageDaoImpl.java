/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.daos.impl.DefaultCMSPageDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public class MplCmsPageDaoImpl extends DefaultCMSPageDao implements MplCmsPageDao
{

	private final String MOBILE_UID = "MobileHomepage";

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_CLASS = "Select {";

	private static final String FROM_CLASS = "} From {";




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
				.append(ContentPageModel._TYPECODE).append(" as cp left join ").append(CMSChannel._TYPECODE)
				.append(" as cm ON {cp.channel} = {cm.pk}}").append(" Where {cp.categoryAssociated} = ?category ");

		return queryString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplCmsPageDao#getHomePageForMobile()
	 */
	@Override
	public ContentPageModel getHomePageForMobile(final CMSChannel cms)
	{
		// YTODO Auto-generated method stub
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(ContentPageModel.PK).append(FROM_CLASS)
				.append(ContentPageModel._TYPECODE).append("} Where {").append(ContentPageModel.UID).append("} = ?uid")
				.append(" And {").append(ContentPageModel.CHANNEL).append("} = ?channel");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("uid", MOBILE_UID);
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
				.append(ContentPageModel._TYPECODE).append(" as cp left join ").append(CMSChannel._TYPECODE)
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
				.append(ContentPageModel._TYPECODE).append(" as cp left join ").append(CMSChannel._TYPECODE)
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
		query.addQueryParameter("uid", pageId);
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
}
