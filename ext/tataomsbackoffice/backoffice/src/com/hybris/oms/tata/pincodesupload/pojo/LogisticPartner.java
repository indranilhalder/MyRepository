package com.hybris.oms.tata.pincodesupload.pojo;

/**
 *
 * This class is used to store the fields of Logisticpartner data.
 *
 */
public class LogisticPartner
{


	private String cod;
	private String prepaidLimit;
	private String codLimit;
	private String carea;
	private String cscrcd;
	private String cloctype;
	private String newzone;
	private String formRequired;
	private String name;
	private String pickUp;
	private String codPriority;
	private String prepaidPriority;
	private String adjCodLimit;
	private String adjPrepaidLimit;

	private int index;// in order for comparing air or surface


	public String getCod()
	{
		return cod;
	}

	public void setCod(final String cod)
	{
		this.cod = cod;
	}

	public String getPrepaidLimit()
	{
		return prepaidLimit;
	}

	public void setPrepaidLimit(final String prepaidLimit)
	{
		this.prepaidLimit = prepaidLimit;
	}

	public String getCodLimit()
	{
		return codLimit;
	}

	public void setCodLimit(final String codLimit)
	{
		this.codLimit = codLimit;
	}

	public String getCarea()
	{
		return carea;
	}

	public void setCarea(final String carea)
	{
		this.carea = carea;
	}

	public String getCscrcd()
	{
		return cscrcd;
	}

	public void setCscrcd(final String cscrcd)
	{
		this.cscrcd = cscrcd;
	}

	public String getCloctype()
	{
		return cloctype;
	}

	public void setCloctype(final String cloctype)
	{
		this.cloctype = cloctype;
	}

	public String getNewzone()
	{
		return newzone;
	}

	public void setNewzone(final String newzone)
	{
		this.newzone = newzone;
	}

	public String getFormRequired()
	{
		return formRequired;
	}

	public void setFormRequired(final String formRequired)
	{
		this.formRequired = formRequired;
	}

	public String getPickUp()
	{
		return pickUp;
	}

	public void setPickUp(final String pickUp)
	{
		this.pickUp = pickUp;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}



	public String getCodPriority()
	{
		return codPriority;
	}

	public void setCodPriority(final String codPriority)
	{
		this.codPriority = codPriority;
	}

	public String getPrepaidPriority()
	{
		return prepaidPriority;
	}

	public void setPrepaidPriority(final String prepaidPriority)
	{
		this.prepaidPriority = prepaidPriority;
	}



	public String getAdjCodLimit()
	{
		return adjCodLimit;
	}

	public void setAdjCodLimit(final String adjCodLimit)
	{
		this.adjCodLimit = adjCodLimit;
	}

	public String getAdjPrepaidLimit()
	{
		return adjPrepaidLimit;
	}

	public void setAdjPrepaidLimit(final String adjPrepaidLimit)
	{
		this.adjPrepaidLimit = adjPrepaidLimit;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(final int index)
	{
		this.index = index;
	}

	@Override
	public String toString()
	{
		return "LogisticPartner [cod=" + cod + ", prepaidLimit=" + prepaidLimit + ", codLimit=" + codLimit + ", carea=" + carea
				+ ", cscrcd=" + cscrcd + ", cloctype=" + cloctype + ", newzone=" + newzone + ", transitTatPrepaid="
				+ ", transitTatCod=" + ", formRequired=" + formRequired + ", name=" + name + ", isReturnPincode=" + ", pickUp="
				+ pickUp + ", codPriority=" + codPriority + ", prepaidPriority=" + prepaidPriority + ", adjCodLimit=" + adjCodLimit
				+ ", adjPrepaidLimit=" + adjPrepaidLimit + "]";
	}




}
