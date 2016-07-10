/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class CreateCartWsDto implements java.io.Serializable
{
	private String status;
	private String cartId;

	//	public CreateCartWsDto()
	//	{
	//
	//	}

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
	 * @return the cartId
	 */
	public String getCartId()
	{
		return cartId;
	}

	/**
	 * @param cartId
	 *           the cartId to set
	 */
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

}
