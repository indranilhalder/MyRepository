/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integration.oms.OrderWrapper;


/**
 * @author TCS
 *
 */
public interface CustomOmsSyncAdapter<Source extends OrderWrapper, Target extends ItemModel>
{
	Target update(Source paramSource, ItemModel paramItemModel);
}