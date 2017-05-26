package com.tisl.mpl.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.jalo.AbstractPromotionAction;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.servicelayer.internal.jalo.order.JaloOnlyItemHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class CachedCustomPromotionOrderAddFreeGiftAction extends GeneratedCachedCustomPromotionOrderAddFreeGiftAction implements
		JaloOnlyItem
{

	private static final Logger LOG = Logger.getLogger(CachedCustomPromotionOrderAddFreeGiftAction.class.getName());
	private JaloOnlyItemHelper data;

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final Item.ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		final Set missing = new HashSet();

		if (!(checkMandatoryAttribute("markedApplied", allAttributes, missing)))
		{
			throw new JaloInvalidParameterException("missing parameters " + missing + " to create a cart ", 0);
		}
		final Class cl = type.getJaloClass();
		try
		{
			final CachedCustomPromotionOrderAddFreeGiftAction newOne = (CachedCustomPromotionOrderAddFreeGiftAction) cl
					.newInstance();
			newOne.setTenant(type.getTenant());
			newOne.data = new JaloOnlyItemHelper((PK) allAttributes.get(PK), newOne, type, new Date(), null);

			return newOne;
		}
		catch (final ClassCastException e)
		{
			throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type.getCode() + " : "
					+ e, 0);
		}
		catch (final InstantiationException e)
		{
			throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type.getCode() + " : "
					+ e, 0);
		}
		catch (final IllegalAccessException e)
		{
			throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type.getCode() + " : "
					+ e, 0);
		}
	}

	@Override
	public Boolean isMarkedApplied(final SessionContext ctx)
	{
		return ((Boolean) this.data.getProperty(ctx, "markedApplied"));
	}

	@Override
	public void setMarkedApplied(final SessionContext ctx, final Boolean markedApplied)
	{
		this.data.setProperty(ctx, "markedApplied", markedApplied);
	}

	@Override
	public String getGuid(final SessionContext ctx)
	{
		return ((String) this.data.getProperty(ctx, "guid"));
	}

	@Override
	public void setGuid(final SessionContext ctx, final String guid)
	{
		this.data.setProperty(ctx, "guid", guid);
	}

	@Override
	public PromotionResult getPromotionResult(final SessionContext ctx)
	{
		return ((PromotionResult) this.data.getProperty(ctx, "promotionResult"));
	}

	@Override
	public void setPromotionResult(final SessionContext ctx, final PromotionResult promotionResult)
	{
		this.data.setProperty(ctx, "promotionResult", promotionResult);
	}



	AbstractPromotionAction deepClone(final SessionContext ctx)
	{
		try
		{
			final Map values = getAllAttributes(ctx);

			values.remove(Item.PK);
			values.remove(Item.MODIFIED_TIME);
			values.remove(Item.CREATION_TIME);
			values.remove("savedvalues");
			values.remove("customLinkQualifier");
			values.remove("synchronizedCopies");
			values.remove("synchronizationSources");
			values.remove("alldocuments");
			values.remove(Item.TYPE);
			values.remove(Item.OWNER);
			values.remove("promotionResult");

			deepCloneAttributes(ctx, values);

			final ComposedType type = TypeManager.getInstance().getComposedType(CustomPromotionOrderAddFreeGiftAction.class);
			try
			{
				return ((AbstractPromotionAction) type.newInstance(ctx, values));
			}
			catch (final JaloGenericCreationException ex)
			{
				LOG.warn("deepClone [" + this + "] failed to create instance of " + super.getClass().getSimpleName(), ex);
			}
			catch (final JaloAbstractTypeException ex)
			{
				LOG.warn("deepClone [" + this + "] failed to create instance of " + super.getClass().getSimpleName(), ex);
			}
		}
		catch (final JaloSecurityException ex)
		{
			LOG.warn("deepClone [" + this + "] failed to getAllAttributes", ex);
		}
		return null;
	}

	@Override
	public final ComposedType provideComposedType()
	{
		return this.data.provideComposedType();
	}

	@Override
	public final Date provideCreationTime()
	{
		return this.data.provideCreationTime();
	}

	@Override
	public final Date provideModificationTime()
	{
		return this.data.provideModificationTime();
	}

	@Override
	public final PK providePK()
	{
		return this.data.providePK();
	}

	@Override
	public void removeJaloOnly() throws ConsistencyCheckException
	{
		this.data.removeJaloOnly();
	}

	@Override
	public Object doGetAttribute(final SessionContext ctx, final String attrQualifier) throws JaloInvalidParameterException,
			JaloSecurityException
	{
		return this.data.doGetAttribute(ctx, attrQualifier);
	}

	@Override
	public void doSetAttribute(final SessionContext ctx, final String attrQualifier, final Object value)
			throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
	{
		this.data.doSetAttribute(ctx, attrQualifier, value);
	}

	@Override
	public Product getFreeProduct(final SessionContext ctx)
	{
		return ((Product) this.data.getProperty(ctx, "freeProduct"));
	}

	@Override
	public void setFreeProduct(final SessionContext ctx, final Product product)
	{
		this.data.setProperty(ctx, "freeProduct", product);
	}

	@Override
	public String getProductUSSID(final SessionContext ctx)
	{
		return ((String) this.data.getProperty(ctx, "productUSSID"));
	}

	@Override
	public void setProductUSSID(final SessionContext ctx, final String value)
	{
		this.data.setProperty(ctx, "productUSSID", value);
	}

	@Override
	public boolean isIsBuyAGetPromoAsPrimitive(final SessionContext ctx)
	{
		return ((Boolean) this.data.getProperty(ctx, "isBuyAGetPromo")).booleanValue();
	}

	@Override
	public void setIsBuyAGetPromo(final SessionContext ctx, final Boolean value)
	{
		this.data.setProperty(ctx, "isBuyAGetPromo", value);
	}

	@Override
	public Double getFreeGiftQuantity(final SessionContext ctx)
	{
		return ((Double) this.data.getProperty(ctx, "freeGiftQuantity"));
	}

	@Override
	public void setFreeGiftQuantity(final SessionContext ctx, final Double value)
	{
		this.data.setProperty(ctx, "freeGiftQuantity", value);
	}

	@Override
	public Map<String, Product> getAllFreeGiftInfoMap(final SessionContext ctx)
	{
		return ((Map<String, Product>) this.data.getProperty(ctx, FREEGIFTINFOMAP));
	}

	@Override
	public void setAllFreeGiftInfoMap(final SessionContext ctx, final Map<String, Product> value)
	{
		this.data.setProperty(ctx, FREEGIFTINFOMAP, value);
	}


}
