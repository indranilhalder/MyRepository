/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Feb 26, 2016 10:59:48 PM                    ---
 * ----------------------------------------------------------------
 */
package com.tisl.mpl.jalo;

import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.tisl.mpl.jalo.CouponMailJob CouponMailJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCouponMailJob extends CronJob
{
	/** Qualifier of the <code>CouponMailJob.voucherCode</code> attribute **/
	public static final String VOUCHERCODE = "voucherCode";
	/** Qualifier of the <code>CouponMailJob.targetCustomer</code> attribute **/
	public static final String TARGETCUSTOMER = "targetCustomer";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(VOUCHERCODE, AttributeMode.INITIAL);
		tmp.put(TARGETCUSTOMER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponMailJob.targetCustomer</code> attribute.
	 * @return the targetCustomer - Specifies the user defined voucher code
	 */
	public Collection<Principal> getTargetCustomer(final SessionContext ctx)
	{
		Collection<Principal> coll = (Collection<Principal>)getProperty( ctx, TARGETCUSTOMER);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponMailJob.targetCustomer</code> attribute.
	 * @return the targetCustomer - Specifies the user defined voucher code
	 */
	public Collection<Principal> getTargetCustomer()
	{
		return getTargetCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponMailJob.targetCustomer</code> attribute. 
	 * @param value the targetCustomer - Specifies the user defined voucher code
	 */
	public void setTargetCustomer(final SessionContext ctx, final Collection<Principal> value)
	{
		setProperty(ctx, TARGETCUSTOMER,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponMailJob.targetCustomer</code> attribute. 
	 * @param value the targetCustomer - Specifies the user defined voucher code
	 */
	public void setTargetCustomer(final Collection<Principal> value)
	{
		setTargetCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponMailJob.voucherCode</code> attribute.
	 * @return the voucherCode - Specifies the user defined voucher code
	 */
	public String getVoucherCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VOUCHERCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponMailJob.voucherCode</code> attribute.
	 * @return the voucherCode - Specifies the user defined voucher code
	 */
	public String getVoucherCode()
	{
		return getVoucherCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponMailJob.voucherCode</code> attribute. 
	 * @param value the voucherCode - Specifies the user defined voucher code
	 */
	public void setVoucherCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VOUCHERCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponMailJob.voucherCode</code> attribute. 
	 * @param value the voucherCode - Specifies the user defined voucher code
	 */
	public void setVoucherCode(final String value)
	{
		setVoucherCode( getSession().getSessionContext(), value );
	}
	
}
