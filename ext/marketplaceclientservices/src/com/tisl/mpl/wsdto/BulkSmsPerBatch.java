/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author 985717
 *
 */
public class BulkSmsPerBatch
{
	private String mobileNo;
	private String msg;
	private String txnId;
	private String senderId;

	/**
	 * @return the mobileNo
	 */
	public String getMobileNo()
	{
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *           the mobileNo to set
	 */
	public void setMobileNo(final String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @param msg
	 *           the msg to set
	 */
	public void setMsg(final String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the txnId
	 */
	public String getTxnId()
	{
		return txnId;
	}

	/**
	 * @param txnId
	 *           the txnId to set
	 */
	public void setTxnId(final String txnId)
	{
		this.txnId = txnId;
	}

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
}
