/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplSellerPriorityLevelModel;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityDao;


/**
 * fetch seller priorities ,categories and subcategories
 *
 * @author TCS
 *
 */
public class MplSellerPriorityDaoImpl implements MplSellerPriorityDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final String SELECT_CLASS = "SELECT {bb.PK} FROM {";
	private static final String AS_CLASS = " AS bb}";
	@Autowired
	private CatalogVersionService catalogVersionService;
	private static final String MPL_CONTENT_CATALOG = "mplProductCatalog";

	/**
	 * This method returns list of seller priorities
	 *
	 * @description
	 * @returns prioritySellerModels
	 */
	@Override
	public List<MplSellerPriorityModel> getAllSellerPriorities()
	{
		List<MplSellerPriorityModel> prioritySellerModels = null;
		try
		{
			final String queryString = SELECT_CLASS + MplSellerPriorityModel._TYPECODE + AS_CLASS;
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			prioritySellerModels = flexibleSearchService.<MplSellerPriorityModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return prioritySellerModels;
	}

	@Override
	public List<ProductModel> getProductListForCategory(final CategoryModel categoryModel)
	{
		try
		{

			final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {p.PK} "
					+ "FROM {Product as p JOIN CategoryProductRelation as rel " + "ON {p.PK} = {rel.target} } "
					+ "WHERE {rel.source} IN ( ?cat, ?cat.allSubCategories ) and {p.catalogVersion}=?catVersion ");
			query.addQueryParameter("cat", categoryModel);
			query.addQueryParameter("catVersion", getCatalogVersion());
			final SearchResult<ProductModel> result = flexibleSearchService.search(query);
			return result.getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(MPL_CONTENT_CATALOG,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	@Override
	public List<MplSellerPriorityLevelModel> loadExistingUssids()
	{
		List<MplSellerPriorityLevelModel> mplSellerPriority = null;
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {p:" + MplSellerPriorityLevelModel.PK + "}" //
					+ "FROM {" + MplSellerPriorityLevelModel._TYPECODE + " AS p} ");
			final SearchResult<MplSellerPriorityLevelModel> result = flexibleSearchService.search(query);

			mplSellerPriority = result.getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return mplSellerPriority;
	}

	/**
	 * check whether a seller priority already exits for a given ussid or not
	 *
	 * @param ussid
	 * @return mplSellerPriority
	 */
	@Override
	public List<MplSellerPriorityLevelModel> loadExistingUssid(final String ussid)
	{
		List<MplSellerPriorityLevelModel> mplSellerPriority = null;
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					"SELECT {pk} FROM {MplSellerPriorityLevel} WHERE {ussid}=?ussid");
			query.addQueryParameter("ussid", ussid);
			final SearchResult<MplSellerPriorityLevelModel> result = flexibleSearchService.search(query);
			mplSellerPriority = result.getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return mplSellerPriority;
	}
}
