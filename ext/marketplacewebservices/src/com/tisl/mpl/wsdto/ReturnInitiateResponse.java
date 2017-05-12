/**
 * 
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.tisl.mpl.jaxb.bind.annotation.UseDeclaredXmlRootElement; 

/**
 * @author Dileep
 *
 */
@XmlRootElement(name = "ReturnInitiateResponse")
@UseDeclaredXmlRootElement(enabled=true)
public class ReturnInitiateResponse implements java.io.Serializable 
{
	private List<OrderLineDTO> orderLines;
	private String errorCode;

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

	/**
	 * @return the errorCode
	 */
	@XmlElement(name = "errorCode")
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
}
