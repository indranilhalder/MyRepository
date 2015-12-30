/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "CustomerBulk")
@XmlType(propOrder =
{ "bulkcustomerDetails" })
public class MplBulkCustomerXMlData
{

	private List<MplCustomerXMLData> bulkcustomerDetails;

	/**
	 * @return the bulkcustomerDetails
	 */
	@XmlElement(name = "CustomerCreateUpdate")
	public List<MplCustomerXMLData> getBulkcustomerDetails()
	{
		return bulkcustomerDetails;
	}

	/**
	 * @param bulkcustomerDetails
	 *           the bulkcustomerDetails to set
	 */
	public void setBulkcustomerDetails(final List<MplCustomerXMLData> bulkcustomerDetails)
	{
		this.bulkcustomerDetails = bulkcustomerDetails;
	}
}
