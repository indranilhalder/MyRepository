/**
 * 
 */
package com.tisl.mpl.storefront.web.forms;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tech
 *
 */
public class MplReturnInfoForm
{
	private String lpname;
	private MultipartFile dispatchProof;
	private String awbNumber;
	private String orderId;
	private String lpNameOther;
	private String transactionId;
	private String amount;

	/**
	 * @return the lpname
	 */
	public String getLpname()
	{
		return lpname;
	}
	/**
	 * @param lpname the lpname to set
	 */
	public void setLpname(String lpname)
	{
		this.lpname = lpname;
	}
	
	/**
	 * @return the dispatchProof
	 */
	public MultipartFile getDispatchProof()
	{
		return dispatchProof;
	}
	/**
	 * @param dispatchProof the dispatchProof to set
	 */
	public void setDispatchProof(MultipartFile dispatchProof)
	{
		this.dispatchProof = dispatchProof;
	}
	/**
	 * @return the awbNumber
	 */
	public String getAwbNumber()
	{
		return awbNumber;
	}
	/**
	 * @param awbNumber the awbNumber to set
	 */
	public void setAwbNumber(String awbNumber)
	{
		this.awbNumber = awbNumber;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	/**
	 * @return the lpNameOther
	 */
	public String getLpNameOther()
	{
		return lpNameOther;
	}
	/**
	 * @param lpNameOther the lpNameOther to set
	 */
	public void setLpNameOther(String lpNameOther)
	{
		this.lpNameOther = lpNameOther;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}
	/**
	 * @return the amount
	 */
	public String getAmount()
	{
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	@Override
	public String toString()
	{
		return "MplReturnInfoForm [lpname=" + lpname + ", dispatchProof=" + dispatchProof + ", awbNumber=" + awbNumber
				+ ", orderId=" + orderId + ", lpNameOther=" + lpNameOther + ", transactionId=" + transactionId + ", amount=" + amount
				+ "]";
	}
	
}
