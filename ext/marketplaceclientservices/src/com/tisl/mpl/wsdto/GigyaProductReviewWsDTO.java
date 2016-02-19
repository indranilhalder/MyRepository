/**
 *
 */
package com.tisl.mpl.wsdto;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Comparator;
import java.util.Date;


/**
 * @author TCS
 *
 */
public class GigyaProductReviewWsDTO implements Comparator<GigyaProductReviewWsDTO>
{
	private String commentId;
	private String commentTitle;
	private String commentText;
	private String overAllRating;
	private String qualityRating;
	private String fitRating;
	private String valueForMoneyRating;
	private Date commentDate;
	private ProductData productData;
	private String reviewDate;
	private String easeOfUse;
	private String rootCategory;
	private String mediaItems;
	private String mediaUrl;

	/**
	 * @return the rootCategory
	 */
	public String getRootCategory()
	{
		return rootCategory;
	}

	/**
	 * @param rootCategory
	 *           the rootCategory to set
	 */
	public void setRootCategory(final String rootCategory)
	{
		this.rootCategory = rootCategory;
	}

	/**
	 * @return the easeOfUse
	 */
	public String getEaseOfUse()
	{
		return easeOfUse;
	}

	/**
	 * @param easeOfUse
	 *           the easeOfUse to set
	 */
	public void setEaseOfUse(final String easeOfUse)
	{
		this.easeOfUse = easeOfUse;
	}

	/**
	 * @return the commentTitle
	 */
	public String getCommentTitle()
	{
		return commentTitle;
	}

	/**
	 * @param commentTitle
	 *           the commentTitle to set
	 */
	public void setCommentTitle(final String commentTitle)
	{
		this.commentTitle = commentTitle;
	}

	/**
	 * @return the commentText
	 */
	public String getCommentText()
	{
		return commentText;
	}

	/**
	 * @param commentText
	 *           the commentText to set
	 */
	public void setCommentText(final String commentText)
	{
		this.commentText = commentText;
	}

	/**
	 * @return the productData
	 */
	public ProductData getProductData()
	{
		return productData;
	}

	/**
	 * @param productData
	 *           the productData to set
	 */
	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}

	/**
	 * @return the overAllRating
	 */
	public String getOverAllRating()
	{
		return overAllRating;
	}

	/**
	 * @param overAllRating
	 *           the overAllRating to set
	 */
	public void setOverAllRating(final String overAllRating)
	{
		this.overAllRating = overAllRating;
	}

	/**
	 * @return the qualityRating
	 */
	public String getQualityRating()
	{
		return qualityRating;
	}

	/**
	 * @param qualityRating
	 *           the qualityRating to set
	 */
	public void setQualityRating(final String qualityRating)
	{
		this.qualityRating = qualityRating;
	}

	/**
	 * @return the fitRating
	 */
	public String getFitRating()
	{
		return fitRating;
	}

	/**
	 * @param fitRating
	 *           the fitRating to set
	 */
	public void setFitRating(final String fitRating)
	{
		this.fitRating = fitRating;
	}

	/**
	 * @return the valueForMoneyRating
	 */
	public String getValueForMoneyRating()
	{
		return valueForMoneyRating;
	}

	/**
	 * @param valueForMoneyRating
	 *           the valueForMoneyRating to set
	 */
	public void setValueForMoneyRating(final String valueForMoneyRating)
	{
		this.valueForMoneyRating = valueForMoneyRating;
	}

	/**
	 * @return the commentDate
	 */
	public Date getCommentDate()
	{
		return commentDate;
	}

	/**
	 * @param commentDate
	 *           the commentDate to set
	 */
	public void setCommentDate(final Date commentDate)
	{
		this.commentDate = commentDate;
	}

	/**
	 * @return the commentId
	 */
	public String getCommentId()
	{
		return commentId;
	}

	/**
	 * @param commentId
	 *           the commentId to set
	 */
	public void setCommentId(final String commentId)
	{
		this.commentId = commentId;
	}

	/**
	 * @return the reviewDate
	 */
	public String getReviewDate()
	{
		return reviewDate;
	}

	/**
	 * @param reviewDate
	 *           the reviewDate to set
	 */
	public void setReviewDate(final String reviewDate)
	{
		this.reviewDate = reviewDate;
	}

	/**
	 * @return the mediaItems
	 */
	public String getMediaItems()
	{
		return mediaItems;
	}

	/**
	 * @param mediaItems
	 *           the mediaItems to set
	 */
	public void setMediaItems(final String mediaItems)
	{
		this.mediaItems = mediaItems;
	}

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *           the mediaUrl to set
	 */
	public void setMediaUrl(final String mediaUrl)
	{
		this.mediaUrl = mediaUrl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final GigyaProductReviewWsDTO arg0, final GigyaProductReviewWsDTO arg1)
	{
		if (arg0.getProductData().getCode().equals(arg1.getProductData().getCode()))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
