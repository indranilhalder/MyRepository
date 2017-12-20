//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.07.22 at 12:31:44 PM IST
//


package com.tisl.mpl.xml.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for CancelRequest element declaration.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;element name="CancelRequest">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="OrderLine" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string"/>  				--Order Reference Number
 *                     &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/> 		--Transaction ID
 *                     &lt;element name="returnCancelFlag" type="{http://www.w3.org/2001/XMLSchema}string"/> 	--if cancel C, if return and refund R
 *                     &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string"/> 			--Cancel or Return and Refund - Request ID
 *                     &lt;element name="ReasonCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="ReturnCancelRemarks" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "orderLine" })
@XmlRootElement(name = "CancelRequest")
public class MplCancelOrderRequest
{

	@XmlElement(name = "OrderLine", required = true)
	protected List<OrderLine> orderLine;

	/**
	 * Gets the value of the orderLine property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the orderLine property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getOrderLine().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link OrderLine }
	 *
	 *
	 */
	public List<OrderLine> getOrderLine()
	{
		if (orderLine == null)
		{
			orderLine = new ArrayList<OrderLine>();
		}
		return this.orderLine;
	}


	/**
	 * @param orderLine
	 *           the orderLine to set
	 */
	public void setOrderLine(final List<OrderLine> orderLine)
	{
		this.orderLine = orderLine;
	}


	/**
	 * <p>
	 * Java class for anonymous complex type.
	 *
	 * <p>
	 * The following schema fragment specifies the expected content contained within this class.
	 *
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="returnCancelFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="ReasonCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="ReturnCancelRemarks" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 *
	 *
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder =
	{ "orderId", "transactionId", "returnCancelFlag", "requestID", "reasonCode", "returnCancelRemarks", "pinCode",
			"returnFulfillmentMode", "subReasonCode", "comments", "uploadImg" })
	public static class OrderLine
	{

		@XmlElement(required = true)
		protected String orderId;
		@XmlElement(required = true)
		protected String transactionId;
		@XmlElement(required = true)
		protected String returnCancelFlag;
		@XmlElement(name = "RequestID", required = true)
		protected String requestID;
		@XmlElement(name = "ReasonCode", required = true)
		protected String reasonCode;
		@XmlElement(name = "ReturnCancelRemarks", required = true)
		protected String returnCancelRemarks;
		@XmlElement(name = "pinCode", required = true)
		protected String pinCode;
		@XmlElement(name = "ReturnFulfilmentType", required = true)
		protected String returnFulfillmentMode;

		//TPR-5954
		@XmlElement(name = "SubReasonCode")
		protected String subReasonCode;
		@XmlElement(name = "Comments")
		protected String comments;
		@XmlElement(name = "UploadImage")
		protected uploadImage uploadImg;

		/**
		 * @return the subReasonCode
		 */
		public String getSubReasonCode()
		{
			return subReasonCode;
		}

		/**
		 * @param subReasonCode
		 *           the subReasonCode to set
		 */
		public void setSubReasonCode(final String subReasonCode)
		{
			this.subReasonCode = subReasonCode;
		}

		/**
		 * @return the comments
		 */
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
		 * @return the uploadImg
		 */
		public uploadImage getUploadImg()
		{
			return uploadImg;
		}

		/**
		 * @param uploadImg
		 *           the uploadImg to set
		 */
		public void setUploadImg(final uploadImage uploadImg)
		{
			this.uploadImg = uploadImg;
		}

		/**
		 * @return the returnFulfillmentMode
		 */
		public String getReturnFulfillmentMode()
		{
			return returnFulfillmentMode;
		}

		/**
		 * @param returnFulfillmentMode
		 *           the returnFulfillmentMode to set
		 */
		public void setReturnFulfillmentMode(final String returnFulfillmentMode)
		{
			this.returnFulfillmentMode = returnFulfillmentMode;
		}

		/**
		 * Gets the value of the orderId property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getOrderId()
		{
			return orderId;
		}

		/**
		 * Sets the value of the orderId property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setOrderId(final String value)
		{
			this.orderId = value;
		}

		/**
		 * Gets the value of the transactionId property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getTransactionId()
		{
			return transactionId;
		}

		/**
		 * Sets the value of the transactionId property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setTransactionId(final String value)
		{
			this.transactionId = value;
		}

		/**
		 * Gets the value of the returnCancelFlag property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getReturnCancelFlag()
		{
			return returnCancelFlag;
		}

		/**
		 * Sets the value of the returnCancelFlag property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setReturnCancelFlag(final String value)
		{
			this.returnCancelFlag = value;
		}

		/**
		 * Gets the value of the requestID property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getRequestID()
		{
			return requestID;
		}

		/**
		 * Sets the value of the requestID property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setRequestID(final String value)
		{
			this.requestID = value;
		}

		/**
		 * Gets the value of the reasonCode property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getReasonCode()
		{
			return reasonCode;
		}

		/**
		 * Sets the value of the reasonCode property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setReasonCode(final String value)
		{
			this.reasonCode = value;
		}

		/**
		 * Gets the value of the returnCancelRemarks property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getReturnCancelRemarks()
		{
			return returnCancelRemarks;
		}

		/**
		 * Sets the value of the returnCancelRemarks property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setReturnCancelRemarks(final String value)
		{
			this.returnCancelRemarks = value;
		}

		/**
		 * @return the pinCode
		 */

		public String getPinCode()
		{
			return pinCode;
		}

		/**
		 * @param pinCode
		 *           the pinCode to set
		 */
		public void setPinCode(final String pinCode)
		{
			this.pinCode = pinCode;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder =
	{ "imgPath" })
	public static class uploadImage
	{
		@XmlElement(name = "ImagePath")
		private List<String> imgPath;

		/**
		 * @return the imgPath
		 */
		public List<String> getImgPath()
		{
			return imgPath;
		}

		/**
		 * @param imgPath
		 *           the imgPath to set
		 */
		public void setImgPath(final List<String> imgPath)
		{
			this.imgPath = imgPath;
		}


	}

}
