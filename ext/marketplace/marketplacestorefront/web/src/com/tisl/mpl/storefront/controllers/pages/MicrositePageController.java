/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.category.MplCategoryFacade;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

@Controller
@RequestMapping(value = "/**/m")
public class MicrositePageController extends AbstractSearchPageController
{

	protected static final Logger LOG = Logger.getLogger(MicrositePageController.class);

	protected static final String SELLER_NAME_PATH_VARIABLE_PATTERN = "/{mSellerName:.*}";

	private static final String ERROR_CMS_PAGE = "notFound";
	@Autowired
	private MplCategoryFacade mplCategoryFacade;
	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	@RequestMapping(value = SELLER_NAME_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String get(@PathVariable("mSellerName") final String mSellerName, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final String mSellerID = mplCategoryFacade.getSellerInformationBySellerName(mSellerName);
		model.addAttribute("mSellerID", mSellerID);
		model.addAttribute("mSellerName", mSellerName);
		// Check for CMS Page where label or id is like /page
		final ContentPageModel pageForRequest = getContentPageForRequest(request);
		if (pageForRequest != null)
		{
			storeCmsPageInModel(model, pageForRequest);
			setUpMetaDataForContentPage(model, pageForRequest);
			//	model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(pageForRequest));
			return getViewForPage(pageForRequest);
		}

		// No page found - display the notFound page with error from controller
		storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));

		//model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, "system.error.page.not.found");

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
	}

	/**
	 * Lookup the CMS Content Page for this request.
	 *
	 * @param request
	 *           The request
	 * @return the CMS content page
	 */

	protected ContentPageModel getContentPageForRequest(final HttpServletRequest request)
	{
		// Get the path for this request.
		// Note that the path begins with a '/'
		final String lookupPathForRequest = urlPathHelper.getLookupPathForRequest(request);
		System.out.println("********  lookupPathForRequest  " + lookupPathForRequest);
		try
		{
			System.out
					.println("********  lookupPathForRequest returns " + getCmsPageService().getPageForLabel(lookupPathForRequest));
			// Lookup the CMS Content Page by label. Note that the label value must begin with a '/'.
			return getCmsPageService().getPageForLabel(lookupPathForRequest);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			// Ignore exception
			LOG.error(ignore);
		}
		return null;
	}


	/*
	 * This method will get category data from MplCategoryFacade for the given category name.
	 *
	 * @param sellerName category name comes from ajax url
	 */
	@RequestMapping(value = "/fetchSellerSalesHierarchyCategories/{sellerName}", method = RequestMethod.GET)
	@ResponseBody
	public CategoryData getShopBrandCategories(@PathVariable final String sellerName)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		CategoryData catData = null;
		try
		{
			catData = mplCategoryFacade.getShopBrandCategories(sellerName);
		}
		catch (final EtailBusinessExceptions businessException)
		{
			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
		}
		catch (final EtailNonBusinessExceptions nonBusinessException)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
		}

		return catData;
	}

}