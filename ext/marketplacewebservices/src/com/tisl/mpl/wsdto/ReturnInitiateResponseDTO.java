/**
 * 
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Dileep
 *
 */
@XmlRootElement(name = "ReturnInitiateResponse")
public class ReturnInitiateResponseDTO
{
	private List<OrderLineDTO> orderLines;

	/**
	 * @return the orderLines
	 */
	@XmlElement(name = "OrderLine")
	public List<OrderLineDTO> getOrderLines()
	{
		return orderLines;
	}

	/**
	 * @param orderLines the orderLines to set
	 */
	public void setOrderLines(List<OrderLineDTO> orderLines)
	{
		this.orderLines = orderLines;
	}
}
