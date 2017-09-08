/**
 *
 */
package com.tisl.mpl.pojo.request;

/**
 * @author TUL
 *
 */
public class QCWalletCreationRequestData
{
	private String notes;

	private Customer customer;

	private String externalWalletId;

	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes
	 *           the notes to set
	 */
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/**
	 * @param customer
	 *           the customer to set
	 */
	public void setCustomer(final Customer customer)
	{
		this.customer = customer;
	}

	/**
	 * @return the externalWalletId
	 */
	public String getExternalWalletId()
	{
		return externalWalletId;
	}

	/**
	 * @param externalWalletId
	 *           the externalWalletId to set
	 */
	public void setExternalWalletId(final String externalWalletId)
	{
		this.externalWalletId = externalWalletId;
	}




}
