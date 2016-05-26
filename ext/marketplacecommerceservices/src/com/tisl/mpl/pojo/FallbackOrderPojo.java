/**
 *
 */
package com.tisl.mpl.pojo;

/**
 * @author TCS
 *
 */
public class FallbackOrderPojo
{
	private String orderId;
	private String user;
	private String orderDate;
	private String transactionId;
	private String orderStatus;

	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *           the user to set
	 */
	public void setUser(final String user)
	{
		this.user = user;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate()
	{
		return orderDate;
	}

	/**
	 * @param orderDate
	 *           the orderDate to set
	 */
	public void setOrderDate(final String orderDate)
	{
		this.orderDate = orderDate;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *           the orderStatus to set
	 */
	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}
}
