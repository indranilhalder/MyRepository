/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * @author TCS
 *
 */
//@XmlRootElement(name = "Seller")
@XStreamAlias("Seller")
/*
 * @XmlType(propOrder = { "id", "name", "type" })
 */
public class SellerMasterWsDTO implements java.io.Serializable
{
	private String isupdate;

	private String id;
	private String firstname;
	private String midname;
	private String lastname;
	private String type;
	private String legalName;
	private String CompanyCode;
	private String AccountGroup;
	private String Search1;
	private String Search2;
	private String Title;
	private String Language;
	private String Customer;
	@XStreamImplicit(itemFieldName = "corporateAddress")
	private List<CorporateAddressWsDTO> corporateAddress;
	private RegisteredAddressWsDTO registerAddress;
	private String TAN;
	private String LST;
	private String CST;
	private String ServiceTAXNo;
	private String PAN;
	private Date PANValidThru;
	private String SettlementPeriod;
	private String collectionDays;
	private String daysToExtend;
	private String extensionTimes;
	private String PayoutPeriod;
	@XStreamImplicit(itemFieldName = "WthhldTAX")
	private List<WthhldTAXWsDTO> WthhldTAX;
	@XStreamImplicit(itemFieldName = "PaymentInfo")
	private List<PaymentInfoWsDTO> PaymentInfo;

	/**
	 * @return the isupdate
	 */
	public String getIsupdate()
	{
		return isupdate;
	}

	/**
	 * @param isupdate
	 *           the isupdate to set
	 */
	public void setIsupdate(final String isupdate)
	{
		this.isupdate = isupdate;
	}



	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname()
	{
		return firstname;
	}

	/**
	 * @param firstname
	 *           the firstname to set
	 */
	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * @return the midname
	 */
	public String getMidname()
	{
		return midname;
	}

	/**
	 * @param midname
	 *           the midname to set
	 */
	public void setMidname(final String midname)
	{
		this.midname = midname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname()
	{
		return lastname;
	}

	/**
	 * @param lastname
	 *           the lastname to set
	 */
	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * @return the legalName
	 */
	public String getLegalName()
	{
		return legalName;
	}

	/**
	 * @param legalName
	 *           the legalName to set
	 */
	public void setLegalName(final String legalName)
	{
		this.legalName = legalName;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode()
	{
		return CompanyCode;
	}

	/**
	 * @param companyCode
	 *           the companyCode to set
	 */
	public void setCompanyCode(final String companyCode)
	{
		CompanyCode = companyCode;
	}

	/**
	 * @return the accountGroup
	 */
	public String getAccountGroup()
	{
		return AccountGroup;
	}

	/**
	 * @param accountGroup
	 *           the accountGroup to set
	 */
	public void setAccountGroup(final String accountGroup)
	{
		AccountGroup = accountGroup;
	}

	/**
	 * @return the search1
	 */
	public String getSearch1()
	{
		return Search1;
	}

	/**
	 * @param search1
	 *           the search1 to set
	 */
	public void setSearch1(final String search1)
	{
		Search1 = search1;
	}

	/**
	 * @return the search2
	 */
	public String getSearch2()
	{
		return Search2;
	}

	/**
	 * @param search2
	 *           the search2 to set
	 */
	public void setSearch2(final String search2)
	{
		Search2 = search2;
	}

	/**
	 * @return the title
	 */
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
	 * @return the language
	 */
	public String getLanguage()
	{
		return Language;
	}

	/**
	 * @param language
	 *           the language to set
	 */
	public void setLanguage(final String language)
	{
		Language = language;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer()
	{
		return Customer;
	}

	/**
	 * @param customer
	 *           the customer to set
	 */
	public void setCustomer(final String customer)
	{
		Customer = customer;
	}

	/**
	 * @return the corporateAddress
	 */
	@XmlElement(name = "corporateAddress")
	public List<CorporateAddressWsDTO> getCorporateAddress()
	{
		return corporateAddress;
	}

	/**
	 * @param corporateAddress
	 *           the corporateAddress to set
	 */
	public void setCorporateAddress(final List<CorporateAddressWsDTO> corporateAddress)
	{
		this.corporateAddress = corporateAddress;
	}

	/**
	 * @return the registerAddress
	 */
	@XmlElement(name = "registerAddress")
	public RegisteredAddressWsDTO getRegisterAddress()
	{
		return registerAddress;
	}

	/**
	 * @param registerAddress
	 *           the registerAddress to set
	 */
	public void setRegisterAddress(final RegisteredAddressWsDTO registerAddress)
	{
		this.registerAddress = registerAddress;
	}

	/**
	 * @return the tAN
	 */
	public String getTAN()
	{
		return TAN;
	}

	/**
	 * @param tAN
	 *           the tAN to set
	 */
	public void setTAN(final String tAN)
	{
		TAN = tAN;
	}

	/**
	 * @return the lST
	 */
	public String getLST()
	{
		return LST;
	}

	/**
	 * @param lST
	 *           the lST to set
	 */
	public void setLST(final String lST)
	{
		LST = lST;
	}

	/**
	 * @return the cST
	 */
	public String getCST()
	{
		return CST;
	}

	/**
	 * @param cST
	 *           the cST to set
	 */
	public void setCST(final String cST)
	{
		CST = cST;
	}

	/**
	 * @return the serviceTAXNo
	 */
	public String getServiceTAXNo()
	{
		return ServiceTAXNo;
	}

	/**
	 * @param serviceTAXNo
	 *           the serviceTAXNo to set
	 */
	public void setServiceTAXNo(final String serviceTAXNo)
	{
		ServiceTAXNo = serviceTAXNo;
	}

	/**
	 * @return the pAN
	 */
	public String getPAN()
	{
		return PAN;
	}

	/**
	 * @param pAN
	 *           the pAN to set
	 */
	public void setPAN(final String pAN)
	{
		PAN = pAN;
	}

	/**
	 * @return the pANValidThru
	 */
	public Date getPANValidThru()
	{
		return PANValidThru;
	}

	/**
	 * @param pANValidThru
	 *           the pANValidThru to set
	 */
	public void setPANValidThru(final Date pANValidThru)
	{
		PANValidThru = pANValidThru;
	}

	/**
	 * @return the settlementPeriod
	 */
	public String getSettlementPeriod()
	{
		return SettlementPeriod;
	}

	/**
	 * @param settlementPeriod
	 *           the settlementPeriod to set
	 */
	public void setSettlementPeriod(final String settlementPeriod)
	{
		SettlementPeriod = settlementPeriod;
	}

	/**
	 * @return the payoutPeriod
	 */
	public String getPayoutPeriod()
	{
		return PayoutPeriod;
	}

	/**
	 * @param payoutPeriod
	 *           the payoutPeriod to set
	 */
	public void setPayoutPeriod(final String payoutPeriod)
	{
		PayoutPeriod = payoutPeriod;
	}

	/**
	 * @return the wthhldTAX
	 */
	@XmlElement(name = "WthhldTAX")
	public List<WthhldTAXWsDTO> getWthhldTAX()
	{
		return WthhldTAX;
	}

	/**
	 * @param wthhldTAX
	 *           the wthhldTAX to set
	 */
	public void setWthhldTAX(final List<WthhldTAXWsDTO> wthhldTAX)
	{
		WthhldTAX = wthhldTAX;
	}

	/**
	 * @return the paymentInfo
	 */
	@XmlElement(name = "PaymentInfo")
	public List<PaymentInfoWsDTO> getPaymentInfo()
	{
		return PaymentInfo;
	}

	/**
	 * @param paymentInfo
	 *           the paymentInfo to set
	 */
	public void setPaymentInfo(final List<PaymentInfoWsDTO> paymentInfo)
	{
		PaymentInfo = paymentInfo;
	}

	/**
	 * @return the collectionDays
	 */
	public String getCollectionDays()
	{
		return collectionDays;
	}

	/**
	 * @param collectionDays
	 *           the collectionDays to set
	 */
	public void setCollectionDays(final String collectionDays)
	{
		this.collectionDays = collectionDays;
	}

	/**
	 * @return the daysToExtend
	 */
	public String getDaysToExtend()
	{
		return daysToExtend;
	}

	/**
	 * @param daysToExtend
	 *           the daysToExtend to set
	 */
	public void setDaysToExtend(final String daysToExtend)
	{
		this.daysToExtend = daysToExtend;
	}

	/**
	 * @return the extensionTimes
	 */
	public String getExtensionTimes()
	{
		return extensionTimes;
	}

	/**
	 * @param extensionTimes
	 *           the extensionTimes to set
	 */
	public void setExtensionTimes(final String extensionTimes)
	{
		this.extensionTimes = extensionTimes;
	}


}
