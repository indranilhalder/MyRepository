/**
 *
 */
package com.tisl.mpl.oms.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.omsorders.services.query.daos.SyncDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;


/**
 * @author TCS
 *
 */
public class CustomSyncQueryDao<M extends ItemModel> extends DefaultGenericDao<M> implements SyncDao<M>
{
	private final String typeCode;

	public CustomSyncQueryDao(final String typeCode)
	{
		super(typeCode);
		this.typeCode = typeCode;
	}

	public M findById(final String codeField, final Object value)
	{
		final StringBuilder builder = createQueryString();
		builder.append("WHERE {c:").append(codeField).append("} ").append("= ?codeField ");
		builder.append("AND {c:originalversion} is NULL");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), Collections.singletonMap("codeField", value));
		return (getFlexibleSearchService().searchUnique(query));
	}

	private StringBuilder createQueryString()
	{
		final StringBuilder builder = new StringBuilder(30);
		builder.append("SELECT {c:").append("pk").append("} ");
		builder.append("FROM {").append(this.typeCode).append(" AS c} ");
		return builder;
	}

}
