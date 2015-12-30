/**
 *
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "isCancellableResponse")
@XmlType(propOrder =
{ "isCancellable" })
public class CancellableResponse
{
	private String isCancellable;

	/**
	 * @return the isCancellable
	 */
	@XmlElement(name = "isCancellable")
	public String getIsCancellable()
	{
		return isCancellable;
	}

	/**
	 * @param isCancellable
	 *           the isCancellable to set
	 */
	public void setIsCancellable(final String isCancellable)
	{
		this.isCancellable = isCancellable;
	}


}
