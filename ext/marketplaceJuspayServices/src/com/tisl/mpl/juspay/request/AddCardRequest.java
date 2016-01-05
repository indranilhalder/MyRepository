package com.tisl.mpl.juspay.request;


public class AddCardRequest
{

	private String customerId;
	private String customerEmail;
	private String cardNumber;
	private String cardExpYear;
	private String cardExpMonth;
	private String nameOnCard;
	private String nickname;




	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail()
	{
		return customerEmail;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber()
	{
		return cardNumber;
	}

	/**
	 * @return the cardExpYear
	 */
	public String getCardExpYear()
	{
		return cardExpYear;
	}

	/**
	 * @return the cardExpMonth
	 */
	public String getCardExpMonth()
	{
		return cardExpMonth;
	}

	/**
	 * @return the nameOnCard
	 */
	public String getNameOnCard()
	{
		return nameOnCard;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname()
	{
		return nickname;
	}

	//Normal Setters
	public void setNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
	}

	public void setCardExpMonth(final String cardExpMonth)
	{
		this.cardExpMonth = cardExpMonth;
	}

	public void setCardExpYear(final String cardExpYear)
	{
		this.cardExpYear = cardExpYear;
	}

	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public void setCustomerEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	public void setNickname(final String nickname)
	{
		this.nickname = nickname;
	}

	//Setters for method chaining calls
	public AddCardRequest withNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
		return this;
	}

	public AddCardRequest withCardExpMonth(final String cardExpMonth)
	{
		this.cardExpMonth = cardExpMonth;
		return this;
	}

	public AddCardRequest withCardExpYear(final String cardExpYear)
	{
		this.cardExpYear = cardExpYear;
		return this;
	}

	public AddCardRequest withCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
		return this;
	}

	public AddCardRequest withCustomerId(final String customerId)
	{
		this.customerId = customerId;
		return this;
	}

	public AddCardRequest withCustomerEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
		return this;
	}

	public AddCardRequest withNickname(final String nickname)
	{
		this.nickname = nickname;
		return this;
	}
}
