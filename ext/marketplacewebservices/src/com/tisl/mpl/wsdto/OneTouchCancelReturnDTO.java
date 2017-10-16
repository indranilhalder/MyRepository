package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OneTouchCancelReturnDTO
{
	@XmlElement(name = "OrderRefNum")
	private String orderRefNum;
	@XmlElement(name = "TransactionId")
	private String transactionId;
	@XmlElement(name = "Serviceability")
	private String serviceability;
	@XmlElement(name = "Valid_Flag")
	private String validFlag;
	@XmlElement(name = "Remarks")
	private String remarks;

	/**
	 * @return the orderRefNum
	 */
	public String getOrderRefNum()
	{
		return orderRefNum;
	}

	/**
	 * @param orderRefNum
	 *           the orderRefNum to set
	 */
	public void setOrderRefNum(final String orderRefNum)
	{
		this.orderRefNum = orderRefNum;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the serviceability
	 */
	public String getServiceability()
	{
		return serviceability;
	}

	/**
	 * @param serviceability
	 *           the serviceability to set
	 */
	public void setServiceability(final String serviceability)
	{
		this.serviceability = serviceability;
	}

	/**
	 * @return the valid_Flag
	 */
	public String getValidFlag()
	{
		return validFlag;
	}

	/**
	 * @param validFlag
	 *           the valid_Flag to set
	 */
	public void setValidFlag(final String validFlag)
	{
		this.validFlag = validFlag;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks()
	{
		return remarks;
	}

	/**
	 * @param remarks
	 *           the remarks to set
	 */
	public void setRemarks(final String remarks)
	{
		this.remarks = remarks;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final OneTouchCancelReturnDTO other = (OneTouchCancelReturnDTO) obj;
		if (transactionId == null)
		{
			if (other.transactionId != null)
			{
				return false;
			}
		}
		else if (!transactionId.equals(other.transactionId))
		{
			return false;
		}
		return true;
	}



}