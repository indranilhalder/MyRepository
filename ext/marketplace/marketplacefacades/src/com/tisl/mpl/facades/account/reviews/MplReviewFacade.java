/**
 *
 */
package com.tisl.mpl.facades.account.reviews;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public interface MplReviewFacade
{
	public List<GigyaProductReviewWsDTO> getReviewedProductPrice(final List<GigyaProductReviewWsDTO> reviewDTO);

	public List<GigyaProductReviewWsDTO> getProductPrice(final List<GigyaProductReviewWsDTO> commentsWithProductData,
			final List<OrderModel> orderModels);

}
