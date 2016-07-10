/**
 *
 */
package com.tisl.mpl.juspay.response;


public class CardResponse
{
	private String cardNumber;
	private String cardISIN;
	private String expiryMonth;
	private String expiryYear;
	private String nameOnCard;
	private String cardType;
	private String cardIssuer;
	private String cardBrand;
	private String cardReference;
	private String cardFingerprint;
	private Boolean usingSavedCard;
	private Boolean savedToLocker;

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber()
	{
		return cardNumber;
	}

	/**
	 * @param cardNumber
	 *           the cardNumber to set
	 */
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the cardISIN
	 */
	public String getCardISIN()
	{
		return cardISIN;
	}

	/**
	 * @param cardISIN
	 *           the cardISIN to set
	 */
	public void setCardISIN(final String cardISIN)
	{
		this.cardISIN = cardISIN;
	}

	/**
	 * @return the expiryMonth
	 */
	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	/**
	 * @param expiryMonth
	 *           the expiryMonth to set
	 */
	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	/**
	 * @return the expiryYear
	 */
	public String getExpiryYear()
	{
		return expiryYear;
	}

	/**
	 * @param expiryYear
	 *           the expiryYear to set
	 */
	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	/**
	 * @return the nameOnCard
	 */
	public String getNameOnCard()
	{
		return nameOnCard;
	}

	/**
	 * @param nameOnCard
	 *           the nameOnCard to set
	 */
	public void setNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType()
	{
		return cardType;
	}

	/**
	 * @param cardType
	 *           the cardType to set
	 */
	public void setCardType(final String cardType)
	{
		this.cardType = cardType;
	}

	/**
	 * @return the cardIssuer
	 */
	public String getCardIssuer()
	{
		return cardIssuer;
	}

	/**
	 * @param cardIssuer
	 *           the cardIssuer to set
	 */
	public void setCardIssuer(final String cardIssuer)
	{
		this.cardIssuer = cardIssuer;
	}

	/**
	 * @return the cardBrand
	 */
	public String getCardBrand()
	{
		return cardBrand;
	}

	/**
	 * @param cardBrand
	 *           the cardBrand to set
	 */
	public void setCardBrand(final String cardBrand)
	{
		this.cardBrand = cardBrand;
	}

	/**
	 * @return the cardReference
	 */
	public String getCardReference()
	{
		return cardReference;
	}

	/**
	 * @param cardReference
	 *           the cardReference to set
	 */
	public void setCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
	}

	/**
	 * @return the cardFingerprint
	 */
	public String getCardFingerprint()
	{
		return cardFingerprint;
	}

	/**
	 * @param cardFingerprint
	 *           the cardFingerprint to set
	 */
	public void setCardFingerprint(final String cardFingerprint)
	{
		this.cardFingerprint = cardFingerprint;
	}

	/**
	 * @return the usingSavedCard
	 */
	public Boolean getUsingSavedCard()
	{
		return usingSavedCard;
	}

	/**
	 * @param usingSavedCard
	 *           the usingSavedCard to set
	 */
	public void setUsingSavedCard(final Boolean usingSavedCard)
	{
		this.usingSavedCard = usingSavedCard;
	}

	/**
	 * @return the savedToLocker
	 */
	public Boolean getSavedToLocker()
	{
		return savedToLocker;
	}

	/**
	 * @param savedToLocker
	 *           the savedToLocker to set
	 */
	public void setSavedToLocker(final Boolean savedToLocker)
	{
		this.savedToLocker = savedToLocker;
	}

	@Override
	public String toString()
	{
		return "CardResponse{" + "cardNumber='" + cardNumber + '\'' + ", cardISIN='" + cardISIN + '\'' + ", expiryMonth='"
				+ expiryMonth + '\'' + ", expiryYear='" + expiryYear + '\'' + ", nameOnCard='" + nameOnCard + '\'' + ", cardType='"
				+ cardType + '\'' + ", cardIssuer='" + cardIssuer + '\'' + ", cardBrand='" + cardBrand + '\'' + ", cardReference='"
				+ cardReference + '\'' + ", cardFingerprint='" + cardFingerprint + '\'' + ", usingSavedCard='" + usingSavedCard
				+ '\'' + ", savedToLocker='" + savedToLocker + '\'' + '}';
	}

}
