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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplShopByLookProductCarouselComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
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

	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;


	private static final String EMPTY_STRING = "";

	private static final String EXCEPTION_MESSAGE_PRICE = "Exception to fetch price for product code";


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
		final Map<String, String> productPriceList = new HashMap<String, String>();
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
						productPriceList.put(productModel.getCode(),
								getProductPrice(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS)));
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
		model.addAttribute(ModelAttributetConstants.PRODUCT_PRICE_LIST, productPriceList);
	}

	//product-price

	/**
	 * @param buyBoxData
	 * @param product
	 * @return productPrice
	 */
	private String getProductPrice(final ProductData product)
	{
		String productPrice = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final BuyBoxData buyBoxData = buyBoxFacade.buyboxPrice(product.getCode());

			if (buyBoxData != null)
			{
				if (buyBoxData.getSpecialPrice() != null)
				{
					productPrice = buyBoxData.getSpecialPrice().getFormattedValueNoDecimal();
				}
				else if (buyBoxData.getPrice() != null)
				{
					productPrice = buyBoxData.getPrice().getFormattedValueNoDecimal();
				}
				else
				{
					productPrice = buyBoxData.getMrp().getFormattedValueNoDecimal();
				}
			}
			LOG.info("ProductPrice>>>>>>>" + productPrice);
		}
		catch (final EtailBusinessExceptions e)
		{
			productPrice = EMPTY_STRING;
			LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			productPrice = EMPTY_STRING;
			LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
		}
		catch (final Exception e)
		{
			productPrice = EMPTY_STRING;
			LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
		}

		return productPrice;
	}
}
