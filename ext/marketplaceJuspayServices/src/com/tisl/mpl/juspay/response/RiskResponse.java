/**
 *
 */
package com.tisl.mpl.juspay.response;

/**
 * @author TCS
 *
 */
public class RiskResponse
{
	private String provider;
	private String status;
	private String message;
	private Boolean flagged;
	private String recommendedAction;
	private String ebsRiskLevel;
	private Long ebsRiskPercentage;
	private String ebsBinCountry;
	private String ebsPaymentStatus;

	/**
	 * @return the provider
	 */
	public String getProvider()
	{
		return provider;
	}

	/**
	 * @param provider
	 *           the provider to set
	 */
	public void setProvider(final String provider)
	{
		this.provider = provider;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}

	/**
	 * @return the flagged
	 */
	public Boolean getFlagged()
	{
		return flagged;
	}

	/**
	 * @param flagged
	 *           the flagged to set
	 */
	public void setFlagged(final Boolean flagged)
	{
		this.flagged = flagged;
	}



	/**
	 * @return the recommendedAction
	 */
	public String getRecommendedAction()
	{
		return recommendedAction;
	}

	/**
	 * @param recommendedAction
	 *           the recommendedAction to set
	 */
	public void setRecommendedAction(final String recommendedAction)
	{
		this.recommendedAction = recommendedAction;
	}


	/**
	 * @return the ebsRiskLevel
	 */
	public String getEbsRiskLevel()
	{
		return ebsRiskLevel;
	}

	/**
	 * @param ebsRiskLevel
	 *           the ebsRiskLevel to set
	 */
	public void setEbsRiskLevel(final String ebsRiskLevel)
	{
		this.ebsRiskLevel = ebsRiskLevel;
	}



	/**
	 * @return the ebsRiskPercentage
	 */
	public Long getEbsRiskPercentage()
	{
		return ebsRiskPercentage;
	}

	/**
	 * @param ebsRiskPercentage
	 *           the ebsRiskPercentage to set
	 */
	public void setEbsRiskPercentage(final Long ebsRiskPercentage)
	{
		this.ebsRiskPercentage = ebsRiskPercentage;
	}



	/**
	 * @return the ebsBinCountry
	 */
	public String getEbsBinCountry()
	{
		return ebsBinCountry;
	}

	/**
	 * @param ebsBinCountry
	 *           the ebsBinCountry to set
	 */
	public void setEbsBinCountry(final String ebsBinCountry)
	{
		this.ebsBinCountry = ebsBinCountry;
	}



	/**
	 * @return the ebsPaymentStatus
	 */
	public String getEbsPaymentStatus()
	{
		return ebsPaymentStatus;
	}

	/**
	 * @param ebsPaymentStatus
	 *           the ebsPaymentStatus to set
	 */
	public void setEbsPaymentStatus(final String ebsPaymentStatus)
	{
		this.ebsPaymentStatus = ebsPaymentStatus;
	}

	@Override
	public String toString()
	{
		return "RiskResponse{" + "provider='" + provider + '\'' + ", status='" + status + '\'' + ", message='" + message + '\''
				+ ", flagged='" + flagged + '\'' + ", recommended_action='" + recommendedAction + '\'' + ", ebs_risk_level='"
				+ ebsRiskLevel + '\'' + ", ebs_risk_percentage='" + ebsRiskPercentage + '\'' + ", ebs_payment_status='"
				+ ebsPaymentStatus + '\'' + ", ebs_bin_country='" + ebsBinCountry + '\'' + '}';
	}

}
