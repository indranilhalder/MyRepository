package com.hybris.oms.tata.tship.exceltocsv.pojo;

/**
 *
 * This class is used to store the fields of pincode,priorities,city and state.
 *
 */
public class PriorityMaster
{

	private AirPriorityMaster airPriorityMaster;
	private SurfacePriorityMaster surfacePriorityMaster;
	private String city;
	private String state;
	private String pincode;




	public String getPincode()
	{
		return pincode;
	}

	public void setPincode(final String pincode)
	{
		this.pincode = pincode;
	}

	public AirPriorityMaster getAirPriorityMaster()
	{
		return airPriorityMaster;
	}

	public void setAirPriorityMaster(final AirPriorityMaster airPriorityMaster)
	{
		this.airPriorityMaster = airPriorityMaster;
	}

	public SurfacePriorityMaster getSurfacePriorityMaster()
	{
		return surfacePriorityMaster;
	}

	public void setSurfacePriorityMaster(final SurfacePriorityMaster surfacePriorityMaster)
	{
		this.surfacePriorityMaster = surfacePriorityMaster;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(final String state)
	{
		this.state = state;
	}

	@Override
	public String toString()
	{
		return "PriorityMaster [pincode=" + pincode + ", airPriorityMaster=" + airPriorityMaster + ", surfacePriorityMaster="
				+ surfacePriorityMaster + ", city=" + city + ", state=" + state + "]";
	}





}
