/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author TCS
 *
 */
@XStreamAlias("WthhldTAX")
public class WthhldTAXWsDTO
{
	private String WthhldTAXCntry;
	private String WthhldTAXTyp;
	private String WthhldTAXCode;
	private String liable;
	private String ReceiptType;
	private String ExemptionNo;
	private String ExemptionRate;
	private String ExemptionReason;
	private Date ExemptionFromDate;
	private Date ExemptionToDate;

	/**
	 * @return the wthhldTAXCntry
	 */
	public String getWthhldTAXCntry()
	{
		return WthhldTAXCntry;
	}

	/**
	 * @param wthhldTAXCntry
	 *           the wthhldTAXCntry to set
	 */
	public void setWthhldTAXCntry(final String wthhldTAXCntry)
	{
		WthhldTAXCntry = wthhldTAXCntry;
	}

	/**
	 * @return the wthhldTAXTyp
	 */
	public String getWthhldTAXTyp()
	{
		return WthhldTAXTyp;
	}

	/**
	 * @param wthhldTAXTyp
	 *           the wthhldTAXTyp to set
	 */
	public void setWthhldTAXTyp(final String wthhldTAXTyp)
	{
		WthhldTAXTyp = wthhldTAXTyp;
	}

	/**
	 * @return the wthhldTAXCode
	 */
	public String getWthhldTAXCode()
	{
		return WthhldTAXCode;
	}

	/**
	 * @param wthhldTAXCode
	 *           the wthhldTAXCode to set
	 */
	public void setWthhldTAXCode(final String wthhldTAXCode)
	{
		WthhldTAXCode = wthhldTAXCode;
	}

	/**
	 * @return the liable
	 */
	public String getLiable()
	{
		return liable;
	}

	/**
	 * @param liable
	 *           the liable to set
	 */
	public void setLiable(final String liable)
	{
		this.liable = liable;
	}

	/**
	 * @return the receiptType
	 */
	public String getReceiptType()
	{
		return ReceiptType;
	}

	/**
	 * @param receiptType
	 *           the receiptType to set
	 */
	public void setReceiptType(final String receiptType)
	{
		ReceiptType = receiptType;
	}

	/**
	 * @return the exemptionNo
	 */
	public String getExemptionNo()
	{
		return ExemptionNo;
	}

	/**
	 * @param exemptionNo
	 *           the exemptionNo to set
	 */
	public void setExemptionNo(final String exemptionNo)
	{
		ExemptionNo = exemptionNo;
	}

	/**
	 * @return the exemptionRate
	 */
	public String getExemptionRate()
	{
		return ExemptionRate;
	}

	/**
	 * @param exemptionRate
	 *           the exemptionRate to set
	 */
	public void setExemptionRate(final String exemptionRate)
	{
		ExemptionRate = exemptionRate;
	}

	/**
	 * @return the exemptionReason
	 */
	public String getExemptionReason()
	{
		return ExemptionReason;
	}

	/**
	 * @param exemptionReason
	 *           the exemptionReason to set
	 */
	public void setExemptionReason(final String exemptionReason)
	{
		ExemptionReason = exemptionReason;
	}

	/**
	 * @return the exemptionFromDate
	 */
	public Date getExemptionFromDate()
	{
		return ExemptionFromDate;
	}

	/**
	 * @param exemptionFromDate
	 *           the exemptionFromDate to set
	 */
	public void setExemptionFromDate(final Date exemptionFromDate)
	{
		ExemptionFromDate = exemptionFromDate;
	}

	/**
	 * @return the exemptionToDate
	 */
	public Date getExemptionToDate()
	{
		return ExemptionToDate;
	}

	/**
	 * @param exemptionToDate
	 *           the exemptionToDate to set
	 */
	public void setExemptionToDate(final Date exemptionToDate)
	{
		ExemptionToDate = exemptionToDate;
	}

}
