/**
 *
 */
package com.tisl.mpl.marketplacecommerceservice.url;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.DefaultCategoryModelUrlResolver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class ExtDefaultCategoryModelUrlResolver extends DefaultCategoryModelUrlResolver
{

	Logger LOG = Logger.getLogger(this.getClass().getName());

	@Override
	protected String resolveInternal(final CategoryModel source)
	{
		String url = getPattern();
		String urlSpecialRemoved = null;

		if (url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", getBaseSiteUid());
		}
		if (url.contains("{category-path}"))
		{
			final String categoryPath = buildPathString(getCategoryPath(source));

			url = url.replace("{category-path}", categoryPath);
		}
		if (url.contains("{category-code}"))
		{
			final String categoryCode = urlEncode(source.getCode()).replaceAll("\\+", "%20");
			url = url.replace("{category-code}", categoryCode); //categoryCode code changed to direct source.getCode()
		}
		if (url.contains("{catalog-id}"))
		{
			url = url.replace("{catalog-id}", source.getCatalogVersion().getCatalog().getId());
		}
		if (url.contains("{catalogVersion}"))
		{
			url = url.replace("{catalogVersion}", source.getCatalogVersion().getVersion());
		}
		url = url.toLowerCase();

		try
		{
			url = URLDecoder.decode(url, "UTF-8");
			urlSpecialRemoved = url.replaceAll("[+.^:,'!@#]", "");
			urlSpecialRemoved = url.replaceAll("-&-", "-");
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error(e.getMessage());
		}
		return urlSpecialRemoved;
	}

	@Override
	protected CharSequence getBaseSiteUid()
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		if (currentBaseSite == null)
		{
			return "{baseSiteUid}";
		}

		return currentBaseSite.getUid();
	}

	@Override
	protected String buildPathString(final List<CategoryModel> path)
	{
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); ++i)
		{
			if (i != 0)
			{
				result.append('-');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	@Override
	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> path = getCommerceCategoryService().getPathsForCategory(category);
		if (null != path)
		{
			for (final List<CategoryModel> path1 : path)
			{
				if (path1.size() > 1)
				{
					path1.remove(0);
				}
			}
		}

		return (path.iterator().next());
	}




}
