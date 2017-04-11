/**
 * 
 */
package com.tisl.mpl.wsdto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tech
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EDDRequest")
@XmlType(propOrder =
{ "CartId" })
public class EDDRequestWsDTO
{
	@XmlElement(name = "CartId")
	private String CartId;
	

	/**
	 * @return the cartId
	 */
	public String getCartId()
	{
		return CartId;
	}

	/**
	 * @param cartId the cartId to set
	 */
	public void setCartId(String cartId)
	{
		CartId = cartId;
	}
	
}
