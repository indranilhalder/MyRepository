package com.tisl.mpl.interceptor;

/**
 *@author TCS
 */

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.util.localization.Localization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;




public class AddProductCategoryInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(AddProductCategoryInterceptor.class);

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @Javadoc
	 * @Description : The Method sets the Product Category Type based on the Supercatgeories
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("product.addproductcategoryIntercepter.message"));
		try
		{

			if (object instanceof ProductModel)
			{
				final ProductModel product = (ProductModel) object;
				final String input = configurationService.getConfiguration().getString("mpl.productCategory.keyvalue");


				if (input != null && !input.isEmpty())
				{
					final Map<String, String> categoryMap = new HashMap<String, String>();

					for (final String actualElement : input.split(MarketplacecommerceservicesConstants.COMMA))
					{
						categoryMap.put(actualElement.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer
								.parseInt(MarketplacecommerceservicesConstants.KEY)], actualElement
								.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer
								.parseInt(MarketplacecommerceservicesConstants.VALUE)]);
					}


					if (product.getSupercategories() != null)
					{
						product.setProductCategoryType(setCategory(product.getSupercategories(), categoryMap));
					}

					else
					{
						LOG.debug(Localization.getLocalizedString("product.addproductcategoryIntercepter.variablenotSet"));
					}

				}
				else
				{
					LOG.debug(Localization.getLocalizedString("product.addproductcategoryIntercepter.variablenotSet"));
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

	}

	public String setCategory(final Collection<CategoryModel> categoryList, final Map<String, String> categoryMap)
	{

		String mastercategory = "";
		for (final CategoryModel category : categoryList)
		{

			for (final Map.Entry<String, String> entry : categoryMap.entrySet())
			{

				if (category.getCode().startsWith(entry.getKey()))
				{
					mastercategory = entry.getValue();
				}

			}
		}

		return mastercategory;

	}

}
