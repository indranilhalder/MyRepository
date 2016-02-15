/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.MplShopByLookProductCarouselComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("MplShopByLookProductCarouselComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplShopByLookProductCarouselComponent)
public class MplShopByLookProductCarouselComponentController extends
		AbstractCMSComponentController<MplShopByLookProductCarouselComponentModel>
{
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	/**
	 * @Desc This method fills the JSP with products fetched from the WCMS component for shop the look product carousel
	 * @param request
	 * @param model
	 * @param component
	 * @return void
	 */
	@SuppressWarnings("boxing")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MplShopByLookProductCarouselComponentModel component)
	{
		final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
				ProductOption.VARIANT_FULL);
		final List<ProductData> productDatas = new ArrayList<ProductData>();

		final List<ProductModel> products = component.getProducts();

		if (null != products)
		{
			for (final ProductModel productModel : products)
			{
				/*
				 * if (productModel instanceof PcmProductVariantModel) {
				 */
				try
				{
					if (productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS) != null)
					{
						productDatas.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
					}
				}
				catch (final Exception exception)
				{
					LOG.error("MplShopByLookProductCarouselComponentController exception: " + exception);
					throw new EtailNonBusinessExceptions(exception);
				}

				/* } */
			}
		}
		model.addAttribute(ModelAttributetConstants.PRODUCT_DATA, productDatas);
	}
}
