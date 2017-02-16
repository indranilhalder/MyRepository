/**
 *
 */
package com.hybris.dtocache.testmodel;

import de.hybris.platform.core.model.ItemModel;

import java.util.Date;


/**
 * @author i309277
 *
 */
public class TestModel extends ItemModel
{
	private de.hybris.platform.core.PK pk;
	private String p1;
	private Date p2;
	private Long p3;
	private Float p4;
	private Double p5;


	/**
	 * @return the p1
	 */
	public String getP1()
	{
		return p1;
	}

	/**
	 * @param p1
	 *           the p1 to set
	 */
	public void setP1(final String p1)
	{
		this.p1 = p1;
	}

	/**
	 * @return the p2
	 */
	public Date getP2()
	{
		return p2;
	}

	/**
	 * @param p2
	 *           the p2 to set
	 */
	public void setP2(final Date p2)
	{
		this.p2 = p2;
	}

	/**
	 * @return the p3
	 */
	public Long getP3()
	{
		return p3;
	}

	/**
	 * @param p3
	 *           the p3 to set
	 */
	public void setP3(final Long p3)
	{
		this.p3 = p3;
	}

	/**
	 * @return the p4
	 */
	public Float getP4()
	{
		return p4;
	}

	/**
	 * @param p4
	 *           the p4 to set
	 */
	public void setP4(final Float p4)
	{
		this.p4 = p4;
	}

	/**
	 * @return the p5
	 */
	public Double getP5()
	{
		return p5;
	}

	/**
	 * @param p5
	 *           the p5 to set
	 */
	public void setP5(final Double p5)
	{
		this.p5 = p5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.model.AbstractItemModel#getPk()
	 */
	@Override
	public de.hybris.platform.core.PK getPk()
	{
		return this.pk;
	}

	/**
	 * @param pk
	 *           the pk to set
	 */
	public void setPk(final de.hybris.platform.core.PK pk)
	{
		this.pk = pk;
	}


}
