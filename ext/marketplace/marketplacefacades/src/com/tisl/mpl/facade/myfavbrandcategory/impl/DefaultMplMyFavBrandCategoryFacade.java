/**
 *
 */
package com.tisl.mpl.facade.myfavbrandcategory.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.myfavbrandcategory.MplMyFavBrandCategoryFacade;
import com.tisl.mpl.facades.data.MplFavBrandCategoryData;
import com.tisl.mpl.marketplacecommerceservices.service.MplMyFavBrandCategoryService;


/**
 * @author TCS
 *
 */
public class DefaultMplMyFavBrandCategoryFacade implements MplMyFavBrandCategoryFacade
{
	@Autowired
	private MplMyFavBrandCategoryService mplMyFavBrandCategoryService;
	private static final Logger LOG = Logger.getLogger(DefaultMplMyFavBrandCategoryFacade.class);


	@Override
	public List<CategoryModel> fetchFavCategories(final String emailId)
	{
		try
		{
			final List<CategoryModel> categories = mplMyFavBrandCategoryService.fetchFavCategories(emailId);
			return categories;
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
	public Map<String, MplFavBrandCategoryData> fetchAllCategories()
	{
		try
		{
			Map<String, MplFavBrandCategoryData> brandCategoryData = new HashMap<String, MplFavBrandCategoryData>();
			brandCategoryData = mplMyFavBrandCategoryService.fetchAllCategories();
			return brandCategoryData;
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
	public List<CategoryModel> fetchFavBrands(final String emailId)
	{
		try
		{
			final List<CategoryModel> brands = mplMyFavBrandCategoryService.fetchFavBrands(emailId);
			return brands;
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
	public Map<String, MplFavBrandCategoryData> fetchAllBrands()
	{
		try
		{
			Map<String, MplFavBrandCategoryData> brandCategoryData = new HashMap<String, MplFavBrandCategoryData>();
			brandCategoryData = mplMyFavBrandCategoryService.fetchAllBrands();
			return brandCategoryData;
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
	public boolean addFavCategories(final String emailId, final List<String> codeList)
	{
		try
		{
			final boolean result = mplMyFavBrandCategoryService.addFavCategories(emailId, codeList);
			return result;
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
	public boolean addFavBrands(final String emailId, final List<String> codeList)
	{
		try
		{
			final boolean result = mplMyFavBrandCategoryService.addFavBrands(emailId, codeList);
			return result;
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
	public boolean deleteFavCategories(final String emailId, final String code)
	{
		try
		{
			final boolean result = mplMyFavBrandCategoryService.deleteFavCategories(emailId, code);
			return result;
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
	public boolean deleteFavBrands(final String emailId, final String code)
	{
		try
		{
			final boolean result = mplMyFavBrandCategoryService.deleteFavBrands(emailId, code);
			return result;
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
}
