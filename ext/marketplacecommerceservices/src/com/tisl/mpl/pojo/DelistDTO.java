/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * @author TCS
 *
 */
@XmlRootElement(name = "Delist")
public class DelistDTO
{
	private List<String> ussid;

	/**
	 * @return the ussid
	 */
	@XmlElement(name = "USSID")
	public List<String> getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final List<String> ussid)
	{
		this.ussid = ussid;
	}
}