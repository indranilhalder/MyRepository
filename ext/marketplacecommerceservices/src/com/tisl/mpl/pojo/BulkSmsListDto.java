/**
 *
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author 985717
 *
 */
@XmlRootElement(name = "bulkSms")
@XmlAccessorType(XmlAccessType.FIELD)
public class BulkSmsListDto
{
	public String senderId;
	@XmlElement(name = "customerMsg")
	public BulkSmsDto bulkSmsDto;

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
	 * @return the bulkSmsDto
	 */
	public BulkSmsDto getBulkSmsDto()
	{
		return bulkSmsDto;
	}

	/**
	 * @param bulkSmsDto
	 *           the bulkSmsDto to set
	 */
	public void setBulkSmsDto(final BulkSmsDto bulkSmsDto)
	{
		this.bulkSmsDto = bulkSmsDto;
	}
}
