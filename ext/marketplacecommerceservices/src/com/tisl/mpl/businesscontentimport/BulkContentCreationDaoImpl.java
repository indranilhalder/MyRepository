/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.VideoComponentModel;


/**
 * @author TCS
 *
 */
public class BulkContentCreationDaoImpl implements BulkContentCreationDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Override
	/**
	 * Fetches the Page template for the UID
	 * @param templateName
	 * @return PageTemplateModel
	 */
	public PageTemplateModel fetchPageTemplate(final String templateName)
	{
		final PageTemplateModel pm = new PageTemplateModel();

		pm.setUid(templateName);
		pm.setCatalogVersion(getContentCatalogVersion());

		return flexibleSearchService.getModelByExample(pm);

	}

	@Override
	/**
	 * Fetches the Product for the given code
	 * @param productCode
	 * @return ProductModel
	 */
	public ProductModel fetchProductforCode(final String productCode)
	{
		final ProductModel product = new ProductModel();

		product.setCode(productCode);
		product.setCatalogVersion(getProductCatalogVersion());

		return flexibleSearchService.getModelByExample(product);
	}

	@Override
	public ContentPageModel fetchContentPageifPresent(final String contentPageUid)
	{
		ContentPageModel cm = null;
		try
		{
			cm = new ContentPageModel();

			cm.setUid(contentPageUid);
			cm.setCatalogVersion(getContentCatalogVersion());

			cm = flexibleSearchService.getModelByExample(cm);
		}
		catch (final Exception e)
		{
			e.getMessage();
		}
		return cm;
	}


	@Override
	public SimpleBannerComponentModel getSimpleBannerComponentforUid(final String uid)
	{
		final SimpleBannerComponentModel sm = new SimpleBannerComponentModel();

		sm.setUid(uid);
		sm.setCatalogVersion(getContentCatalogVersion());

		return flexibleSearchService.getModelByExample(sm);
	}

	@Override
	public VideoComponentModel getVideoComponentforUid(final String uid)
	{
		final VideoComponentModel vm = new VideoComponentModel();

		vm.setUid(uid);
		vm.setCatalogVersion(getContentCatalogVersion());

		return flexibleSearchService.getModelByExample(vm);
	}

	@Override
	public CMSParagraphComponentModel getCMSParagraphComponentforUid(final String uid)
	{
		final CMSParagraphComponentModel cmPara = new CMSParagraphComponentModel();

		cmPara.setUid(uid);
		cmPara.setCatalogVersion(getContentCatalogVersion());

		return flexibleSearchService.getModelByExample(cmPara);
	}

	/**
	 * Fetches the Staged Content Catalog
	 *
	 * @return catalogVersionModel
	 */
	private CatalogVersionModel getContentCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CONTENT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CONTENT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	/**
	 * Fetches the Staged Product Catalog
	 *
	 * @return catalogVersionModel
	 */
	private CatalogVersionModel getProductCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID, MarketplacecommerceservicesConstants.STAGED);
		return catalogVersionModel;
	}

}
