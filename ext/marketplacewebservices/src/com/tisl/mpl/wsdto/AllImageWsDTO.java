/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class AllImageWsDTO implements java.io.Serializable
{
	private String imageUrl;
	private String thumbImageUrl;

	//	public AllImageWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl()
	{
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *           the imageUrl to set
	 */
	public void setImageUrl(final String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the thumbImageUrl
	 */
	public String getThumbImageUrl()
	{
		return thumbImageUrl;
	}

	/**
	 * @param thumbImageUrl
	 *           the thumbImageUrl to set
	 */
	public void setThumbImageUrl(final String thumbImageUrl)
	{
		this.thumbImageUrl = thumbImageUrl;
	}

}
