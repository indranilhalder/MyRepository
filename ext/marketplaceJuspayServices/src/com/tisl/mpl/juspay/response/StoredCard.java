package com.tisl.mpl.juspay.response;

public class StoredCard
{

	private String cardToken;
	private String cardReference;
	private String cardNumber;
	private String cardIsin;
	private String cardExpYear;
	private String cardExpMonth;
	private String nameOnCard;
	private String nickname;
	private String cardBrand;
	private String cardType;
	private String cardIssuer;
	private String cardFingerprint;
	private String expired;

	public String getCardReference()
	{
		return cardReference;
	}

	public void setCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getCardIsin()
	{
		return cardIsin;
	}

	public void setCardIsin(final String cardIsin)
	{
		this.cardIsin = cardIsin;
	}

	public String getCardExpYear()
	{
		return cardExpYear;
	}

	public void setCardExpYear(final String cardExpYear)
	{
		this.cardExpYear = cardExpYear;
	}

	public String getCardExpMonth()
	{
		return cardExpMonth;
	}

	public void setCardExpMonth(final String cardExpMonth)
	{
		this.cardExpMonth = cardExpMonth;
	}

	public String getNameOnCard()
	{
		return nameOnCard;
	}

	public void setNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
	}

	public String getCardToken()
	{
		return cardToken;
	}

	public void setCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(final String nickname)
	{
		this.nickname = nickname;
	}

	public String getCardBrand()
	{
		return cardBrand;
	}

	public void setCardBrand(final String cardBrand)
	{
		this.cardBrand = cardBrand;
	}

	public String getCardType()
	{
		return cardType;
	}

	public void setCardType(final String cardType)
	{
		this.cardType = cardType;
	}

	public String getCardIssuer()
	{
		return cardIssuer;
	}

	public void setCardIssuer(final String cardIssuer)
	{
		this.cardIssuer = cardIssuer;
	}


	public String getCardFingerprint()
	{
		return cardFingerprint;
	}

	public void setCardFingerprint(final String cardFingerprint)
	{
		this.cardFingerprint = cardFingerprint;
	}

	public String getExpired()
	{
		return expired;
	}

	public void setExpired(final String expired)
	{
		this.expired = expired;
	}

	//Setters for chaining the method call.
	public StoredCard withCardExpMonth(final String cardExpMonth)
	{
		this.cardExpMonth = cardExpMonth;
		return this;
	}

	public StoredCard withCardExpYear(final String cardExpYear)
	{
		this.cardExpYear = cardExpYear;
		return this;
	}

	public StoredCard withCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
		return this;
	}

	public StoredCard withCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
		return this;
	}

	public StoredCard withCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
		return this;
	}

	public StoredCard withCardIsin(final String cardIsin)
	{
		this.cardIsin = cardIsin;
		return this;
	}

	public StoredCard withCardBrand(final String cardBrand)
	{
		this.cardBrand = cardBrand;
		return this;
	}

	public StoredCard withCardType(final String cardType)
	{
		this.cardType = cardType;
		return this;
	}

	public StoredCard withCardIssuer(final String cardIssuer)
	{
		this.cardIssuer = cardIssuer;
		return this;
	}

	public StoredCard withNickname(final String nickname)
	{
		this.nickname = nickname;
		return this;
	}

	public StoredCard withNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
		return this;
	}

	@Override
	public String toString()
	{
		return "StoredCard{" + "cardToken='" + cardToken + '\'' + ", cardNumber='" + cardNumber + '\'' + ", cardIsin='" + cardIsin
				+ '\'' + ", cardExpYear='" + cardExpYear + '\'' + ", cardExpMonth='" + cardExpMonth + '\'' + ", nameOnCard='"
				+ nameOnCard + '\'' + ", nickname='" + nickname + '\'' + ", cardBrand='" + cardBrand + '\'' + ", cardType='"
				+ cardType + '\'' + ",cardFingerprint='" + cardFingerprint + '\'' + ", expired='" + expired + '\'' + ",cardIssuer='"
				+ cardIssuer + '\'' + '}';
	}
}