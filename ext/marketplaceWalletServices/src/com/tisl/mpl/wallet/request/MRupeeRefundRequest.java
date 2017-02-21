/**
 *
 */
package com.tisl.mpl.wallet.request;

/**
 * @author TCS
 *
 */
public class MRupeeRefundRequest
{
	private String mCode;
	private String narration;
	private String txnType;
	private String purchaseRefNo;
	private Double amount;
	private String refNo;
	private String checkSum;
	private String uniqueRequestId;

	/**
	 * @return the uniqueRequestId
	 */
	public String getUniqueRequestId()
	{
		return uniqueRequestId;
	}

	/**
	 * @param uniqueRequestId
	 *           the uniqueRequestId to set
	 */
	public void setUniqueRequestId(final String uniqueRequestId)
	{
		this.uniqueRequestId = uniqueRequestId;
	}

	/**
	 * @return the mCode
	 */
	public String getmCode()
	{
		return mCode;
	}

	/**
	 * @param mCode
	 *           the mCode to set
	 */
	public void setmCode(final String mCode)
	{
		this.mCode = mCode;
	}

	/**
	 * @return the narration
	 */
	public String getNarration()
	{
		return narration;
	}

	/**
	 * @param narration
	 *           the narration to set
	 */
	public void setNarration(final String narration)
	{
		this.narration = narration;
	}

	/**
	 * @return the txnType
	 */
	public String getTxnType()
	{
		return txnType;
	}

	/**
	 * @param txnType
	 *           the txnType to set
	 */
	public void setTxnType(final String txnType)
	{
		this.txnType = txnType;
	}

	/**
	 * @return the purchaseRefNo
	 */
	public String getPurchaseRefNo()
	{
		return purchaseRefNo;
	}

	/**
	 * @param purchaseRefNo
	 *           the purchaseRefNo to set
	 */
	public void setPurchaseRefNo(final String purchaseRefNo)
	{
		this.purchaseRefNo = purchaseRefNo;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount()
	{
		return amount;
	}

	/**
	 * @param amount
	 *           the amount to set
	 */
	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	/**
	 * @return the refNo
	 */
	public String getRefNo()
	{
		return refNo;
	}

	/**
	 * @param refNo
	 *           the refNo to set
	 */
	public void setRefNo(final String refNo)
	{
		this.refNo = refNo;
	}

	/**
	 * @return the checkSum
	 */
	public String getCheckSum()
	{
		return checkSum;
	}

	/**
	 * @param checkSum
	 *           the checkSum to set
	 */
	public void setCheckSum(final String checkSum)
	{
		this.checkSum = checkSum;
	}

}
