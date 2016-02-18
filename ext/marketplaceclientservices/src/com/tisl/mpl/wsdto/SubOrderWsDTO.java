/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 884206
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(/* name = "suborderelement", */propOrder =
{ "orderNo", "transaction" })
public class SubOrderWsDTO
{
	@XmlElement(name = "orderNo")
	private String orderNo;
	//@XmlElementWrapper(name = "transaction")
	//	@XmlElement(name = "transactionelement")
	private List<TransactionWsDTO> transaction;

	/**
	 * @return the orderNo
	 */
	public String getOrderNo()
	{
		return orderNo;
	}

	/**
	 * @param orderNo
	 *           the orderNo to set
	 */
	public void setOrderNo(final String orderNo)
	{
		this.orderNo = orderNo;
	}

	/**
	 * @return the transaction
	 */
	public List<TransactionWsDTO> getTransaction()
	{
		return transaction;
	}

	/**
	 * @param transaction
	 *           the transaction to set
	 */
	public void setTransaction(final List<TransactionWsDTO> transaction)
	{
		this.transaction = transaction;
	}


}
