/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Feb 26, 2016 10:59:48 PM                    ---
 * ----------------------------------------------------------------
 */
package com.tisl.mpl.coupon.jalo;

import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.jalo.CouponMailJob;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.voucher.jalo.ProductRestriction;
import de.hybris.platform.voucher.jalo.Restriction;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.VoucherInvalidation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>MarketplacecouponManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMarketplacecouponManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("savedAmount", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.voucher.jalo.VoucherInvalidation", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("maxDiscountValue", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.voucher.jalo.Voucher", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("productCodeList", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.voucher.jalo.ProductRestriction", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public CouponMailJob createCouponMailJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MarketplacecouponConstants.TC.COUPONMAILJOB );
			return (CouponMailJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CouponMailJob : "+e.getMessage(), 0 );
		}
	}
	
	public CouponMailJob createCouponMailJob(final Map attributeValues)
	{
		return createCouponMailJob( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return MarketplacecouponConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.maxDiscountValue</code> attribute.
	 * @return the maxDiscountValue - the code used redeeming the voucher.
	 */
	public Double getMaxDiscountValue(final SessionContext ctx, final Voucher item)
	{
		return (Double)item.getProperty( ctx, MarketplacecouponConstants.Attributes.Voucher.MAXDISCOUNTVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.maxDiscountValue</code> attribute.
	 * @return the maxDiscountValue - the code used redeeming the voucher.
	 */
	public Double getMaxDiscountValue(final Voucher item)
	{
		return getMaxDiscountValue( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @return the maxDiscountValue - the code used redeeming the voucher.
	 */
	public double getMaxDiscountValueAsPrimitive(final SessionContext ctx, final Voucher item)
	{
		Double value = getMaxDiscountValue( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @return the maxDiscountValue - the code used redeeming the voucher.
	 */
	public double getMaxDiscountValueAsPrimitive(final Voucher item)
	{
		return getMaxDiscountValueAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @param value the maxDiscountValue - the code used redeeming the voucher.
	 */
	public void setMaxDiscountValue(final SessionContext ctx, final Voucher item, final Double value)
	{
		item.setProperty(ctx, MarketplacecouponConstants.Attributes.Voucher.MAXDISCOUNTVALUE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @param value the maxDiscountValue - the code used redeeming the voucher.
	 */
	public void setMaxDiscountValue(final Voucher item, final Double value)
	{
		setMaxDiscountValue( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @param value the maxDiscountValue - the code used redeeming the voucher.
	 */
	public void setMaxDiscountValue(final SessionContext ctx, final Voucher item, final double value)
	{
		setMaxDiscountValue( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Voucher.maxDiscountValue</code> attribute. 
	 * @param value the maxDiscountValue - the code used redeeming the voucher.
	 */
	public void setMaxDiscountValue(final Voucher item, final double value)
	{
		setMaxDiscountValue( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductRestriction.productCodeList</code> attribute.
	 * @return the productCodeList - the products the given voucher is valid for.
	 */
	public String getProductCodeList(final SessionContext ctx, final ProductRestriction item)
	{
		return (String)item.getProperty( ctx, MarketplacecouponConstants.Attributes.ProductRestriction.PRODUCTCODELIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductRestriction.productCodeList</code> attribute.
	 * @return the productCodeList - the products the given voucher is valid for.
	 */
	public String getProductCodeList(final ProductRestriction item)
	{
		return getProductCodeList( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductRestriction.productCodeList</code> attribute. 
	 * @param value the productCodeList - the products the given voucher is valid for.
	 */
	public void setProductCodeList(final SessionContext ctx, final ProductRestriction item, final String value)
	{
		item.setProperty(ctx, MarketplacecouponConstants.Attributes.ProductRestriction.PRODUCTCODELIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductRestriction.productCodeList</code> attribute. 
	 * @param value the productCodeList - the products the given voucher is valid for.
	 */
	public void setProductCodeList(final ProductRestriction item, final String value)
	{
		setProductCodeList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.savedAmount</code> attribute.
	 * @return the savedAmount - the code used redeeming the voucher.
	 */
	public Double getSavedAmount(final SessionContext ctx, final VoucherInvalidation item)
	{
		return (Double)item.getProperty( ctx, MarketplacecouponConstants.Attributes.VoucherInvalidation.SAVEDAMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.savedAmount</code> attribute.
	 * @return the savedAmount - the code used redeeming the voucher.
	 */
	public Double getSavedAmount(final VoucherInvalidation item)
	{
		return getSavedAmount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @return the savedAmount - the code used redeeming the voucher.
	 */
	public double getSavedAmountAsPrimitive(final SessionContext ctx, final VoucherInvalidation item)
	{
		Double value = getSavedAmount( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @return the savedAmount - the code used redeeming the voucher.
	 */
	public double getSavedAmountAsPrimitive(final VoucherInvalidation item)
	{
		return getSavedAmountAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @param value the savedAmount - the code used redeeming the voucher.
	 */
	public void setSavedAmount(final SessionContext ctx, final VoucherInvalidation item, final Double value)
	{
		item.setProperty(ctx, MarketplacecouponConstants.Attributes.VoucherInvalidation.SAVEDAMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @param value the savedAmount - the code used redeeming the voucher.
	 */
	public void setSavedAmount(final VoucherInvalidation item, final Double value)
	{
		setSavedAmount( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @param value the savedAmount - the code used redeeming the voucher.
	 */
	public void setSavedAmount(final SessionContext ctx, final VoucherInvalidation item, final double value)
	{
		setSavedAmount( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>VoucherInvalidation.savedAmount</code> attribute. 
	 * @param value the savedAmount - the code used redeeming the voucher.
	 */
	public void setSavedAmount(final VoucherInvalidation item, final double value)
	{
		setSavedAmount( getSession().getSessionContext(), item, value );
	}
	
}
