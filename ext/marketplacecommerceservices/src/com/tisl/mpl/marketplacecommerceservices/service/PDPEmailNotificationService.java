/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;



/**
 * @author TCS
 *
 */
public interface PDPEmailNotificationService
{
	public void sendMail(final List<String> emailId, final ProductModel product, final String productUrl);

}
