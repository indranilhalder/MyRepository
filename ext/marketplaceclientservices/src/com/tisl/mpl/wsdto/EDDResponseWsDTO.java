/**
 * 
 */
package com.tisl.mpl.wsdto;

import java.util.List;

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
@XmlRootElement(name = "EDDResponse")
@XmlType(propOrder =
{ "CartId", "ItemEDDInfo"  })
public class EDDResponseWsDTO
{
	
	@XmlElement(name = "CartId")
	private String CartId;
	
	@XmlElement(name = "ItemEDDInfo")
	private List<EDDInfoWsDTO> ItemEDDInfo;

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

	/**
	 * @return the itemEDDInfo
	 */
	public List<EDDInfoWsDTO> getItemEDDInfo()
	{
		return ItemEDDInfo;
	}

	/**
	 * @param itemEDDInfo the itemEDDInfo to set
	 */
	public void setItemEDDInfo(List<EDDInfoWsDTO> itemEDDInfo)
	{
		ItemEDDInfo = itemEDDInfo;
	}

	
}
