/**
 * 
 */
package com.tisl.mpl.wsdto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author TECH
 *
 */
@XStreamAlias("SlaveInfo")
public class SlaveInfoDTO implements java.io.Serializable
{
	private String sellerid;
	private String slaveid;
	private String name;
	private String type;
	private String latitude;
	private String longitude;
	private String OpeningTime;
	private String ClosingTime;
	private String WorkingDays;
	private String ClicknCollect;
	private String Remark;
	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return Remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark)
	{
		Remark = remark;
	}
	private String isReturnable;
	private int orderAcceptanceTAT;
	private int orderProcessingTAT;
	private String orderCutoffTimeHD;
	private String orderCutoffTimeED;
	private String ParkingAvailable;
	private String Location;
	private String StoreSize;
	/**
	 * @param storeSize the storeSize to set
	 */
	public void setStoreSize(String storeSize)
	{
		StoreSize = storeSize;
	}
	private String FootFall;
	private String NormalRetailSalesOfStore;
	private String Ownership;
	private String ManagerName;
	private String ManagerPhone;
	private String StoreImage;
	private String StoreContactNo;
	/**
	 * @return the sellerid
	 */
	public String getSellerid()
	{
		return sellerid;
	}
	/**
	 * @param sellerid the sellerid to set
	 */
	public void setSellerid(String sellerid)
	{
		this.sellerid = sellerid;
	}
	/**
	 * @return the slaveid
	 */
	public String getSlaveid()
	{
		return slaveid;
	}
	/**
	 * @param slaveid the slaveid to set
	 */
	public void setSlaveid(String slaveid)
	{
		this.slaveid = slaveid;
	}
	/**
	 * @return the name
	 */
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
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * @return the openingTime
	 */
	public String getOpeningTime()
	{
		return OpeningTime;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude()
	{
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude()
	{
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	/**
	 * @param openingTime the openingTime to set
	 */
	public void setOpeningTime(String openingTime)
	{
		OpeningTime = openingTime;
	}
	/**
	 * @return the closingTime
	 */
	public String getClosingTime()
	{
		return ClosingTime;
	}
	/**
	 * @param closingTime the closingTime to set
	 */
	public void setClosingTime(String closingTime)
	{
		ClosingTime = closingTime;
	}
	/**
	 * @return the workingDays
	 */
	public String getWorkingDays()
	{
		return WorkingDays;
	}
	/**
	 * @param workingDays the workingDays to set
	 */
	public void setWorkingDays(String workingDays)
	{
		WorkingDays = workingDays;
	}
	/**
	 * @return the clicknCollect
	 */
	public String getClicknCollect()
	{
		return ClicknCollect;
	}
	/**
	 * @param clicknCollect the clicknCollect to set
	 */
	public void setClicknCollect(String clicknCollect)
	{
		ClicknCollect = clicknCollect;
	}
	/**
	 * @return the isReturnable
	 */
	public String getIsReturnable()
	{
		return isReturnable;
	}
	/**
	 * @param isReturnable the isReturnable to set
	 */
	public void setIsReturnable(String isReturnable)
	{
		this.isReturnable = isReturnable;
	}
	/**
	 * @return the orderAcceptanceTAT
	 */
	public int getOrderAcceptanceTAT()
	{
		return orderAcceptanceTAT;
	}
	/**
	 * @param orderAcceptanceTAT the orderAcceptanceTAT to set
	 */
	public void setOrderAcceptanceTAT(int orderAcceptanceTAT)
	{
		this.orderAcceptanceTAT = orderAcceptanceTAT;
	}
	/**
	 * @return the orderProcessingTAT
	 */
	public int getOrderProcessingTAT()
	{
		return orderProcessingTAT;
	}
	/**
	 * @param orderProcessingTAT the orderProcessingTAT to set
	 */
	public void setOrderProcessingTAT(int orderProcessingTAT)
	{
		this.orderProcessingTAT = orderProcessingTAT;
	}
	/**
	 * @return the orderCutoffTimeHD
	 */
	public String getOrderCutoffTimeHD()
	{
		return orderCutoffTimeHD;
	}
	/**
	 * @param orderCutoffTimeHD the orderCutoffTimeHD to set
	 */
	public void setOrderCutoffTimeHD(String orderCutoffTimeHD)
	{
		this.orderCutoffTimeHD = orderCutoffTimeHD;
	}
	/**
	 * @return the orderCutoffTimeED
	 */
	public String getOrderCutoffTimeED()
	{
		return orderCutoffTimeED;
	}
	/**
	 * @param orderCutoffTimeED the orderCutoffTimeED to set
	 */
	public void setOrderCutoffTimeED(String orderCutoffTimeED)
	{
		this.orderCutoffTimeED = orderCutoffTimeED;
	}
	/**
	 * @return the parkingAvailable
	 */
	public String getParkingAvailable()
	{
		return ParkingAvailable;
	}
	/**
	 * @param parkingAvailable the parkingAvailable to set
	 */
	public void setParkingAvailable(String parkingAvailable)
	{
		ParkingAvailable = parkingAvailable;
	}
	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return Location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		Location = location;
	}
	
	/**
	 * @return the storeSize
	 */
	public String getStoreSize()
	{
		return StoreSize;
	}
	/**
	 * @return the footFall
	 */
	public String getFootFall()
	{
		return FootFall;
	}
	/**
	 * @param footFall the footFall to set
	 */
	public void setFootFall(String footFall)
	{
		FootFall = footFall;
	}
	/**
	 * @return the normalRetailSalesOfStore
	 */
	public String getNormalRetailSalesOfStore()
	{
		return NormalRetailSalesOfStore;
	}
	/**
	 * @param normalRetailSalesOfStore the normalRetailSalesOfStore to set
	 */
	public void setNormalRetailSalesOfStore(String normalRetailSalesOfStore)
	{
		NormalRetailSalesOfStore = normalRetailSalesOfStore;
	}
	/**
	 * @return the ownership
	 */
	public String getOwnership()
	{
		return Ownership;
	}
	/**
	 * @param ownership the ownership to set
	 */
	public void setOwnership(String ownership)
	{
		Ownership = ownership;
	}
	/**
	 * @return the managerName
	 */
	public String getManagerName()
	{
		return ManagerName;
	}
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName)
	{
		ManagerName = managerName;
	}
	/**
	 * @return the managerPhone
	 */
	public String getManagerPhone()
	{
		return ManagerPhone;
	}
	/**
	 * @param managerPhone the managerPhone to set
	 */
	public void setManagerPhone(String managerPhone)
	{
		ManagerPhone = managerPhone;
	}
	/**
	 * @return the storeImage
	 */
	public String getStoreImage()
	{
		return StoreImage;
	}
	/**
	 * @param storeImage the storeImage to set
	 */
	public void setStoreImage(String storeImage)
	{
		StoreImage = storeImage;
	}
	/**
	 * @return the storeContactNo
	 */
	public String getStoreContactNo()
	{
		return StoreContactNo;
	}
	/**
	 * @param storeContactNo the storeContactNo to set
	 */
	public void setStoreContactNo(String storeContactNo)
	{
		StoreContactNo = storeContactNo;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1()
	{
		return Address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1)
	{
		Address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2()
	{
		return Address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2)
	{
		Address2 = address2;
	}
	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return Country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country)
	{
		Country = country;
	}
	/**
	 * @return the state
	 */
	public String getState()
	{
		return State;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		State = state;
	}
	/**
	 * @return the city
	 */
	public String getCity()
	{
		return City;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		City = city;
	}
	/**
	 * @return the pin
	 */
	public String getPin()
	{
		return Pin;
	}
	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin)
	{
		Pin = pin;
	}
	/**
	 * @return the returnstoreID
	 */
	public String getReturnstoreID()
	{
		return ReturnstoreID;
	}
	/**
	 * @param returnstoreID the returnstoreID to set
	 */
	public void setReturnstoreID(String returnstoreID)
	{
		ReturnstoreID = returnstoreID;
	}
	/**
	 * @return the returnAddress1
	 */
	public String getReturnAddress1()
	{
		return ReturnAddress1;
	}
	/**
	 * @param returnAddress1 the returnAddress1 to set
	 */
	public void setReturnAddress1(String returnAddress1)
	{
		ReturnAddress1 = returnAddress1;
	}
	/**
	 * @return the returnAddress2
	 */
	public String getReturnAddress2()
	{
		return ReturnAddress2;
	}
	/**
	 * @param returnAddress2 the returnAddress2 to set
	 */
	public void setReturnAddress2(String returnAddress2)
	{
		ReturnAddress2 = returnAddress2;
	}
	/**
	 * @return the returnCountry
	 */
	public String getReturnCountry()
	{
		return ReturnCountry;
	}
	/**
	 * @param returnCountry the returnCountry to set
	 */
	public void setReturnCountry(String returnCountry)
	{
		ReturnCountry = returnCountry;
	}
	/**
	 * @return the returnState
	 */
	public String getReturnState()
	{
		return ReturnState;
	}
	/**
	 * @param returnState the returnState to set
	 */
	public void setReturnState(String returnState)
	{
		ReturnState = returnState;
	}
	/**
	 * @return the returnCity
	 */
	public String getReturnCity()
	{
		return ReturnCity;
	}
	/**
	 * @param returnCity the returnCity to set
	 */
	public void setReturnCity(String returnCity)
	{
		ReturnCity = returnCity;
	}
	/**
	 * @return the returnPin
	 */
	public String getReturnPin()
	{
		return ReturnPin;
	}
	/**
	 * @param returnPin the returnPin to set
	 */
	public void setReturnPin(String returnPin)
	{
		ReturnPin = returnPin;
	}
	/**
	 * @return the active
	 */
	public String getActive()
	{
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active)
	{
		this.active = active;
	}
	/**
	 * @return the email0
	 */
	public String getEmail0()
	{
		return Email0;
	}
	/**
	 * @param email0 the email0 to set
	 */
	public void setEmail0(String email0)
	{
		Email0 = email0;
	}
	/**
	 * @return the phoneNo0
	 */
	public String getPhoneNo0()
	{
		return PhoneNo0;
	}
	/**
	 * @param phoneNo0 the phoneNo0 to set
	 */
	public void setPhoneNo0(String phoneNo0)
	{
		PhoneNo0 = phoneNo0;
	}
	/**
	 * @return the phoneNo1
	 */
	public String getPhoneNo1()
	{
		return PhoneNo1;
	}
	/**
	 * @param phoneNo1 the phoneNo1 to set
	 */
	public void setPhoneNo1(String phoneNo1)
	{
		PhoneNo1 = phoneNo1;
	}
	/**
	 * @return the phoneNo2
	 */
	public String getPhoneNo2()
	{
		return PhoneNo2;
	}
	/**
	 * @param phoneNo2 the phoneNo2 to set
	 */
	public void setPhoneNo2(String phoneNo2)
	{
		PhoneNo2 = phoneNo2;
	}
	/**
	 * @return the phoneNo3
	 */
	public String getPhoneNo3()
	{
		return PhoneNo3;
	}
	/**
	 * @param phoneNo3 the phoneNo3 to set
	 */
	public void setPhoneNo3(String phoneNo3)
	{
		PhoneNo3 = phoneNo3;
	}
	/**
	 * @return the phoneNo4
	 */
	public String getPhoneNo4()
	{
		return PhoneNo4;
	}
	/**
	 * @param phoneNo4 the phoneNo4 to set
	 */
	public void setPhoneNo4(String phoneNo4)
	{
		PhoneNo4 = phoneNo4;
	}
	/**
	 * @return the phoneNo5
	 */
	public String getPhoneNo5()
	{
		return PhoneNo5;
	}
	/**
	 * @param phoneNo5 the phoneNo5 to set
	 */
	public void setPhoneNo5(String phoneNo5)
	{
		PhoneNo5 = phoneNo5;
	}
	/**
	 * @return the phoneNo6
	 */
	public String getPhoneNo6()
	{
		return PhoneNo6;
	}
	/**
	 * @param phoneNo6 the phoneNo6 to set
	 */
	public void setPhoneNo6(String phoneNo6)
	{
		PhoneNo6 = phoneNo6;
	}
	/**
	 * @return the phoneNo7
	 */
	public String getPhoneNo7()
	{
		return PhoneNo7;
	}
	/**
	 * @param phoneNo7 the phoneNo7 to set
	 */
	public void setPhoneNo7(String phoneNo7)
	{
		PhoneNo7 = phoneNo7;
	}
	/**
	 * @return the phoneNo8
	 */
	public String getPhoneNo8()
	{
		return PhoneNo8;
	}
	/**
	 * @param phoneNo8 the phoneNo8 to set
	 */
	public void setPhoneNo8(String phoneNo8)
	{
		PhoneNo8 = phoneNo8;
	}
	/**
	 * @return the phoneNo9
	 */
	public String getPhoneNo9()
	{
		return PhoneNo9;
	}
	/**
	 * @param phoneNo9 the phoneNo9 to set
	 */
	public void setPhoneNo9(String phoneNo9)
	{
		PhoneNo9 = phoneNo9;
	}
	/**
	 * @return the email1
	 */
	public String getEmail1()
	{
		return Email1;
	}
	/**
	 * @param email1 the email1 to set
	 */
	public void setEmail1(String email1)
	{
		Email1 = email1;
	}
	/**
	 * @return the email2
	 */
	public String getEmail2()
	{
		return Email2;
	}
	/**
	 * @param email2 the email2 to set
	 */
	public void setEmail2(String email2)
	{
		Email2 = email2;
	}
	/**
	 * @return the email3
	 */
	public String getEmail3()
	{
		return Email3;
	}
	/**
	 * @param email3 the email3 to set
	 */
	public void setEmail3(String email3)
	{
		Email3 = email3;
	}
	/**
	 * @return the email4
	 */
	public String getEmail4()
	{
		return Email4;
	}
	/**
	 * @param email4 the email4 to set
	 */
	public void setEmail4(String email4)
	{
		Email4 = email4;
	}
	/**
	 * @return the email5
	 */
	public String getEmail5()
	{
		return Email5;
	}
	/**
	 * @param email5 the email5 to set
	 */
	public void setEmail5(String email5)
	{
		Email5 = email5;
	}
	/**
	 * @return the email6
	 */
	public String getEmail6()
	{
		return Email6;
	}
	/**
	 * @param email6 the email6 to set
	 */
	public void setEmail6(String email6)
	{
		Email6 = email6;
	}
	/**
	 * @return the email7
	 */
	public String getEmail7()
	{
		return Email7;
	}
	/**
	 * @param email7 the email7 to set
	 */
	public void setEmail7(String email7)
	{
		Email7 = email7;
	}
	/**
	 * @return the email8
	 */
	public String getEmail8()
	{
		return Email8;
	}
	/**
	 * @param email8 the email8 to set
	 */
	public void setEmail8(String email8)
	{
		Email8 = email8;
	}
	/**
	 * @return the email9
	 */
	public String getEmail9()
	{
		return Email9;
	}
	/**
	 * @param email9 the email9 to set
	 */
	public void setEmail9(String email9)
	{
		Email9 = email9;
	}
	private String Address1;
	private String Address2;
	private String Country;
	private String State;
	private String City;
	private String Pin;
	private String ReturnstoreID;
	private String ReturnAddress1;
	private String ReturnAddress2;
	private String ReturnCountry;
	private String ReturnState;
	private String ReturnCity;
	private String ReturnPin;
	private String active;
	private String Email0;
	private String PhoneNo0;
	private String PhoneNo1;
	private String PhoneNo2;
	private String PhoneNo3;
	private String PhoneNo4;
	private String PhoneNo5;
	private String PhoneNo6;
	private String PhoneNo7;
	private String PhoneNo8;
	private String PhoneNo9;
	private String Email1;
	private String Email2;
	private String Email3;
	private String Email4;
	private String Email5;
	private String Email6;
	private String Email7;
	private String Email8;
	private String Email9;
}
