package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PDPEmailProcessModel;


/**
 * @author TCS
 *
 */
public class PDPEmailContext extends CustomerEmailContext
{
	/**
	 *
	 */
	private static final String USERNAME = "username";
	/**
	 *
	 */
	private static final String PRODUCT_URL = "productUrl";
	private UrlResolver<ProductModel> productModelUrlResolver;
	private static final String CUSTOMER_EMAIL = "customerEmail";
	private static final String PRODUCT_NAME = "productName";
	//	@Autowired
	//	private UserService userService;

	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	@Required
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		{
			super.init(storeFrontCustomerProcessModel, emailPageModel);

			if (storeFrontCustomerProcessModel instanceof PDPEmailProcessModel)
			{

				put(DISPLAY_NAME, ((PDPEmailProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				/*
				 * final CustomerModel user = (CustomerModel) userService.getCurrentUser(); final String email =
				 * user.getContactEmail();
				 */
				final CustomerModel user = ((PDPEmailProcessModel) storeFrontCustomerProcessModel).getCustomer();
				put(CUSTOMER_EMAIL, user.getOriginalUid());
				/* put(CUSTOMER_EMAIL, email); */


				put(EMAIL, ((PDPEmailProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				final String userName = (user.getFirstName() != null ? user.getFirstName() : "") + " "
						+ (user.getLastName() != null ? user.getLastName() : "");
				put(USERNAME, userName);
				final ProductModel product = ((PDPEmailProcessModel) storeFrontCustomerProcessModel).getProduct();
				if (null != product)
				{
					put(PRODUCT_URL, new StringBuilder(((PDPEmailProcessModel) storeFrontCustomerProcessModel).getProductUrl())
							.append(productModelUrlResolver.resolve(product)).toString());
					put(PRODUCT_NAME, product.getName());
				}



			}
		}
	}
}
