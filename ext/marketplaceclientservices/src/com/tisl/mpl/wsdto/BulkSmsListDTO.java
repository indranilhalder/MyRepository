/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "bulkSms")
@XmlAccessorType(XmlAccessType.FIELD)
public class BulkSmsListDTO
{

	@XmlElement(name = "senderId")
	private String senderId;

	@XmlElement(name = "customerMsg")
	private List<BulkSmsDTO> bulkSmsDTO;

	/**
	 * @return the senderId
	 */
	public String getSenderId()
	{
		return senderId;
	}

	/**
	 * @param senderId
	 *           the senderId to set
	 */
	public void setSenderId(final String senderId)
	{
		this.senderId = senderId;
	}

	/**
	 * @return the bulkSmsDTO
	 */
	public List<BulkSmsDTO> getBulkSmsDTO()
	{
		return bulkSmsDTO;
	}

	/**
	 * @param bulkSmsDTO
	 *           the bulkSmsDTO to set
	 */
	public void setBulkSmsDTO(final List<BulkSmsDTO> bulkSmsDTO)
	{
		this.bulkSmsDTO = bulkSmsDTO;
	}

}
