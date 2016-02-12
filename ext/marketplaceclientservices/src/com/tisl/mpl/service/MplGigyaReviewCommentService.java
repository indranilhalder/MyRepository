/**
 *
 */
package com.tisl.mpl.service;

import java.util.Date;
import java.util.List;

import com.gigya.socialize.GSObject;
import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public interface MplGigyaReviewCommentService
{
	public boolean getReviewsByCategoryProductId(String category, String productId, String customerUID);

	public List<GigyaProductReviewWsDTO> getReviewsByUID(String customerUID);

	public String editComment(final String categoryID, final String streamID, final String commentID, final String commentText,
			final String commentTitle, final String ratings, String UID);

	public String deleteComment(String categoryID, String streamID, String commentID);

	public String getDate(final Date commentDateObj);

	public boolean checkItemKey(GSObject ratings, String key);
}
