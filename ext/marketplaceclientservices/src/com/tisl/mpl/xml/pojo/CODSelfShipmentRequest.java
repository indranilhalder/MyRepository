/**
 * 
 */
package com.tisl.mpl.xml.pojo;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Techouts
 *
 */

@XmlRootElement(name = "CODSelfShip")
@XmlType(propOrder =
{ "transactionType", "orderDate", "transactionDate", "orderNo", "orderRefNo", "transactionID", "orderTag", "amount",
		"customerNumber", "title", "name", "street", "poBox", "city", "country", "bankKey", "bankName", "region", "bankStreet", 
		"bankCity", "bankBranch", "bankAccount", "bankCounrty", "paymentMode"})
public class CODSelfShipmentRequest
{
	
	private String transactionType;
	private String orderDate;
	private String transactionDate;
	private String orderNo;
	private String orderRefNo;
	private String transactionID;
	private String orderTag;
	private String amount;
	private String customerNumber; // Customer ID
	private String title;
	private String name; // Customer Name
	private String street;
	private String poBox; // Postal Code
	private String city;
	private String country;
	private String bankKey; // IFSC Code
	private String bankName;
	private String region; // State Codes GCM
	private String bankStreet;
	private String bankCity;
	private String bankBranch;
	private String bankAccount;
	private String bankCounrty;
	private String paymentMode;
	
	/**
	 * @return the transactionType
	 */
	
	@XmlElement(name = "TransactionType")
	public String getTransactionType()
	{
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType)
	{
		this.transactionType = transactionType;
	}
	/**
	 * @return the orderDate
	 */
	
	@XmlElement(name = "OrderDate")
	public String getOrderDate()
	{
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate)
	{
		this.orderDate = orderDate;
	}
	/**
	 * @return the transactionDate
	 */
	
	@XmlElement(name = "TransactionDate")
	public String getTransactionDate()
	{
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate)
	{
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the orderNo
	 */
	
	@XmlElement(name = "OrderNo")
	public String getOrderNo()
	{
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	/**
	 * @return the orderRefNo
	 */
	
	@XmlElement(name = "OrderRefNo")
	public String getOrderRefNo()
	{
		return orderRefNo;
	}
	/**
	 * @param orderRefNo the orderRefNo to set
	 */
	public void setOrderRefNo(String orderRefNo)
	{
		this.orderRefNo = orderRefNo;
	}
	/**
	 * @return the transactionID
	 */
	
	@XmlElement(name = "TransactionID")
	public String getTransactionID()
	{
		return transactionID;
	}
	/**
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(String transactionID)
	{
		this.transactionID = transactionID;
	}
	/**
	 * @return the orderTag
	 */
	
	@XmlElement(name = "OrderTag")
	public String getOrderTag()
	{
		return orderTag;
	}
	/**
	 * @param orderTag the orderTag to set
	 */
	public void setOrderTag(String orderTag)
	{
		this.orderTag = orderTag;
	}
	/**
	 * @return the amount
	 */
	
	@XmlElement(name = "Amount")
	public String getAmount()
	{
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String  amount)
	{
		this.amount = amount;
	}
	/**
	 * @return the customerNumber
	 */
	
	@XmlElement(name = "CustomerNumber")
	public String getCustomerNumber()
	{
		return customerNumber;
	}
	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber)
	{
		this.customerNumber = customerNumber;
	}
	/**
	 * @return the title
	 */
	
	@XmlElement(name = "Title")
	public String getTitle()
	{
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	/**
	 * @return the name
	 */
	
	@XmlElement(name = "Name")
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the street
	 */
	
	@XmlElement(name = "Street")
	public String getStreet()
	{
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}
	/**
	 * @return the poBox
	 */
	
	@XmlElement(name = "POBox")
	public String getPoBox()
	{
		return poBox;
	}
	/**
	 * @param poBox the poBox to set
	 */
	public void setPoBox(String poBox)
	{
		this.poBox = poBox;
	}
	/**
	 * @return the city
	 */
	
	@XmlElement(name = "City")
	public String getCity()
	{
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * @return the country
	 */
	
	@XmlElement(name = "Country")
	public String getCountry()
	{
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}
	/**
	 * @return the bankKey
	 */
	
	@XmlElement(name = "BankKey")
	public String getBankKey()
	{
		return bankKey;
	}
	/**
	 * @param bankKey the bankKey to set
	 */
	public void setBankKey(String bankKey)
	{
		this.bankKey = bankKey;
	}
	/**
	 * @return the bankName
	 */
	
	@XmlElement(name = "BankName")
	public String getBankName()
	{
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}
	/**
	 * @return the region
	 */
	
	@XmlElement(name = "Region")
	public String getRegion()
	{
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region)
	{
		this.region = region;
	}
	/**
	 * @return the bankStreet
	 */
	
	@XmlElement(name = "BankStreet")
	public String getBankStreet()
	{
		return bankStreet;
	}
	/**
	 * @param bankStreet the bankStreet to set
	 */
	public void setBankStreet(String bankStreet)
	{
		this.bankStreet = bankStreet;
	}
	/**
	 * @return the bankCity
	 */
	
	@XmlElement(name = "BankCity")
	public String getBankCity()
	{
		return bankCity;
	}
	/**
	 * @param bankCity the bankCity to set
	 */
	public void setBankCity(String bankCity)
	{
		this.bankCity = bankCity;
	}
	/**
	 * @return the bankBranch
	 */
	
	@XmlElement(name = "BankBranch")
	public String getBankBranch()
	{
		return bankBranch;
	}
	/**
	 * @param bankBranch the bankBranch to set
	 */
	public void setBankBranch(String bankBranch)
	{
		this.bankBranch = bankBranch;
	}
	/**
	 * @return the bankAccount
	 */
	
	@XmlElement(name = "BankAccount")
	public String getBankAccount()
	{
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount)
	{
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the bankCounrty
	 */
	
	@XmlElement(name = "BankCounrty")
	public String getBankCounrty()
	{
		return bankCounrty;
	}
	/**
	 * @param bankCounrty the bankCounrty to set
	 */
	public void setBankCounrty(String bankCounrty)
	{
		this.bankCounrty = bankCounrty;
	}
	/**
	 * @return the paymentMode
	 */
	
	@XmlElement(name = "PaymentMode")
	public String getPaymentMode()
	{
		return paymentMode;
	}
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

}
