/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;


/**
 * @author TCS
 *
 */
//@XmlRootElement(name = "ticketMaster")
public class OneTouchCancelReturnCrmRequestDTO
{

	private String OrderRefNum;
	private String subOrderNum;
	@XmlElement(name = "UploadImage")
	private String USSID;
	private String ticketType;
	private String TicketSubType;
	private String TransactionId;
	private String ReturnReasonCode;
	private String CancelReasonCode;
	private String refundType;
	private String Address1;
	private String Address2;
	private String Address3;
	private String Country;
	private String City;
	private String State;
	private String Pincode;
	private String Landmark;
	private String BankName;
	private String Branch;
	private String AccHolderName;
	private String IFSC;
	private String AccNum;
	private String Title;
	//Added for TPR-5954
	private String subReturnReasonCode;
	private String comments;
	private List<UploadImage> UploadImage;





	/**
	 * @return the subReturnReasonCode
	 */
	@XmlElement(name = "SubReturnReasonCode")
	public String getSubReturnReasonCode()
	{
		return subReturnReasonCode;
	}

	/**
	 * @param subReturnReasonCode
	 *           the subReturnReasonCode to set
	 */
	public void setSubReturnReasonCode(final String subReturnReasonCode)
	{
		this.subReturnReasonCode = subReturnReasonCode;
	}

	/**
	 * @return the comments
	 */
	@XmlElement(name = "Comments")
	public String getComments()
	{
		return comments;
	}

	/**
	 * @param comments
	 *           the comments to set
	 */
	public void setComments(final String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return the ticketType
	 */
	@XmlElement(name = "ticketType")
	public String getTicketType()
	{
		return ticketType;
	}

	/**
	 * @param ticketType
	 *           the ticketType to set
	 */
	public void setTicketType(final String ticketType)
	{
		this.ticketType = ticketType;
	}

	/**
	 * @return the ticketSubType
	 */
	@XmlElement(name = "TicketSubType")
	public String getTicketSubType()
	{
		return TicketSubType;
	}

	/**
	 * @param ticketSubType
	 *           the ticketSubType to set
	 */
	public void setTicketSubType(final String ticketSubType)
	{
		TicketSubType = ticketSubType;
	}

	/**
	 * @return the transactionId
	 */
	@XmlElement(name = "TransactionId")
	public String getTransactionId()
	{
		return TransactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		TransactionId = transactionId;
	}

	/**
	 * @return the returnReasonCode
	 */
	@XmlElement(name = "ReturnReasonCode")
	public String getReturnReasonCode()
	{
		return ReturnReasonCode;
	}

	/**
	 * @param returnReasonCode
	 *           the returnReasonCode to set
	 */
	public void setReturnReasonCode(final String returnReasonCode)
	{
		ReturnReasonCode = returnReasonCode;
	}

	/**
	 * @return the cancelReasonCode
	 */
	@XmlElement(name = "CancelReasonCode")
	public String getCancelReasonCode()
	{
		return CancelReasonCode;
	}

	/**
	 * @param cancelReasonCode
	 *           the cancelReasonCode to set
	 */
	public void setCancelReasonCode(final String cancelReasonCode)
	{
		CancelReasonCode = cancelReasonCode;
	}

	/**
	 * @return the refundType
	 */
	@XmlElement(name = "refundType")
	public String getRefundType()
	{
		return refundType;
	}

	/**
	 * @param refundType
	 *           the refundType to set
	 */
	public void setRefundType(final String refundType)
	{
		this.refundType = refundType;
	}

	/**
	 * @return the address1
	 */
	@XmlElement(name = "Address1")
	public String getAddress1()
	{
		return Address1;
	}

	/**
	 * @param address1
	 *           the address1 to set
	 */
	public void setAddress1(final String address1)
	{
		Address1 = address1;
	}

	/**
	 * @return the address2
	 */
	@XmlElement(name = "Address2")
	public String getAddress2()
	{
		return Address2;
	}

	/**
	 * @param address2
	 *           the address2 to set
	 */
	public void setAddress2(final String address2)
	{
		Address2 = address2;
	}

	/**
	 * @return the address3
	 */
	@XmlElement(name = "Address3")
	public String getAddress3()
	{
		return Address3;
	}

	/**
	 * @param address3
	 *           the address3 to set
	 */
	public void setAddress3(final String address3)
	{
		Address3 = address3;
	}

	/**
	 * @return the country
	 */
	@XmlElement(name = "Country")
	public String getCountry()
	{
		return Country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		Country = country;
	}

	/**
	 * @return the city
	 */
	@XmlElement(name = "City")
	public String getCity()
	{
		return City;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		City = city;
	}

	/**
	 * @return the state
	 */
	@XmlElement(name = "State")
	public String getState()
	{
		return State;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		State = state;
	}

	/**
	 * @return the pincode
	 */
	@XmlElement(name = "Pincode")
	public String getPincode()
	{
		return Pincode;
	}

	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		Pincode = pincode;
	}

	/**
	 * @return the landmark
	 */
	@XmlElement(name = "Landmark")
	public String getLandmark()
	{
		return Landmark;
	}

	/**
	 * @param landmark
	 *           the landmark to set
	 */
	public void setLandmark(final String landmark)
	{
		Landmark = landmark;
	}

	/**
	 * @return the bankName
	 */
	@XmlElement(name = "BankName")
	public String getBankName()
	{
		return BankName;
	}

	/**
	 * @param bankName
	 *           the bankName to set
	 */
	public void setBankName(final String bankName)
	{
		BankName = bankName;
	}

	/**
	 * @return the branch
	 */
	@XmlElement(name = "Branch")
	public String getBranch()
	{
		return Branch;
	}

	/**
	 * @param branch
	 *           the branch to set
	 */
	public void setBranch(final String branch)
	{
		Branch = branch;
	}

	/**
	 * @return the accHolderName
	 */

	@XmlElement(name = "AccHolderName")
	public String getAccHolderName()
	{
		return AccHolderName;
	}

	/**
	 * @param accHolderName
	 *           the accHolderName to set
	 */
	public void setAccHolderName(final String accHolderName)
	{
		AccHolderName = accHolderName;
	}

	/**
	 * @return the iFSC
	 */
	@XmlElement(name = "IFSC")
	public String getIFSC()
	{
		return IFSC;
	}

	/**
	 * @param iFSC
	 *           the iFSC to set
	 */
	public void setIFSC(final String iFSC)
	{
		IFSC = iFSC;
	}

	/**
	 * @return the accNum
	 */
	@XmlElement(name = "AccNum")
	public String getAccNum()
	{
		return AccNum;
	}

	/**
	 * @param accNum
	 *           the accNum to set
	 */
	public void setAccNum(final String accNum)
	{
		AccNum = accNum;
	}

	/**
	 * @return the orderRefNum
	 */
	@XmlElement(name = "OrderRefNum")
	public String getOrderRefNum()
	{
		return OrderRefNum;
	}

	/**
	 * @param orderRefNum
	 *           the orderRefNum to set
	 */
	public void setOrderRefNum(final String orderRefNum)
	{
		OrderRefNum = orderRefNum;
	}

	/**
	 * @return the subOrderNum
	 */
	@XmlElement(name = "subOrderNum")
	public String getSubOrderNum()
	{
		return subOrderNum;
	}

	/**
	 * @param subOrderNum
	 *           the subOrderNum to set
	 */
	public void setSubOrderNum(final String subOrderNum)
	{
		this.subOrderNum = subOrderNum;
	}

	/**
	 * @return the uSSID
	 */
	@XmlElement(name = "USSID")
	public String getUSSID()
	{
		return USSID;
	}

	/**
	 * @param uSSID
	 *           the uSSID to set
	 */
	public void setUSSID(final String uSSID)
	{
		USSID = uSSID;
	}

	/**
	 * @return the title
	 */
	@XmlElement(name = "Title")
	public String getTitle()
	{
		return Title;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTitle(final String title)
	{
		Title = title;
	}

	/**
	 * @return the uploadImage
	 */
	@XmlElement(name = "UploadImage")
	public List<UploadImage> getUploadImage()
	{
		return UploadImage;
	}

	/**
	 * @param uploadImage
	 *           the uploadImage to set
	 */
	public void setUploadImage(final List<UploadImage> uploadImage)
	{
		UploadImage = uploadImage;
	}



}
