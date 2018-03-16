/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;

import com.tisl.mpl.core.model.VideoComponentModel;



/**
 * @author TCS
 *
 */
public interface BulkContentCreationDao
{
	/**
	 * Fetches the Page template for the UID
	 *
	 * @param templateName
	 * @return PageTemplateModel
	 */
	public PageTemplateModel fetchPageTemplate(String templateName, String site);

	/**
	 * Fetches the Product for the given code
	 *
	 * @param productCode
	 * @return ProductModel
	 */
	public ProductModel fetchProductforCode(String productCode, String site);


	/**
	 * Fetches the Product for the given code
	 *
	 * @param contentPageUid
	 * @return ContentPageModel
	 */
	public ContentPageModel fetchContentPageifPresent(String contentPageUid, String site);

	public SimpleBannerComponentModel getSimpleBannerComponentforUid(String uid, String site);

	public VideoComponentModel getVideoComponentforUid(String uid, String site);

	public CMSParagraphComponentModel getCMSParagraphComponentforUid(String uid, String site);

}
