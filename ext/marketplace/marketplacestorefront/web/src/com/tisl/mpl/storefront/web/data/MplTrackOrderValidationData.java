/**
 *
 */
package com.tisl.mpl.storefront.web.data;

/**
 * @author TCS
 *
 */
public class MplTrackOrderValidationData
{
	private String trackKey;
	private boolean validationResult;
	private String errorMessage;

	/**
	 * @return the trackKey
	 */
	public String getTrackKey()
	{
		return trackKey;
	}

	/**
	 * @param trackKey
	 *           the trackKey to set
	 */
	public void setTrackKey(final String trackKey)
	{
		this.trackKey = trackKey;
	}

	/**
	 * @return the validationResult
	 */
	public boolean isValidationResult()
	{
		return validationResult;
	}

	/**
	 * @param validationResult
	 *           the validationResult to set
	 */
	public void setValidationResult(final boolean validationResult)
	{
		this.validationResult = validationResult;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *           the errorMessage to set
	 */
	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}


}
