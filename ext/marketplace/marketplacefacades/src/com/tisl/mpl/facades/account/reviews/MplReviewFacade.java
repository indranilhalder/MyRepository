/**
 *
 */
package com.tisl.mpl.facades.account.reviews;

import java.util.List;

import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public interface MplReviewFacade
{
	public List<GigyaProductReviewWsDTO> getReviewedProductPrice(final List<GigyaProductReviewWsDTO> reviewDTO);

}
