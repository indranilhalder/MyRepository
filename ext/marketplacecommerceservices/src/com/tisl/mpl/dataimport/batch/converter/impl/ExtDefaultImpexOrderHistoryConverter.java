/**
 *
 */
package com.tisl.mpl.dataimport.batch.converter.impl;

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexConverter;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.CODReturnOMSSyncProcessModel;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.DefaultMplOrderService;


/**
 * @author TCS
 *
 */
public class ExtDefaultImpexOrderHistoryConverter extends DefaultImpexConverter
{

	@Autowired
	BusinessProcessService businessProcessService;

	@Autowired
	OrderModelService orderModelService;

	private String impexRow;

	private final static Logger LOG = Logger.getLogger(DefaultMplOrderService.class);


	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{
		String result = MplConstants.EMPTY_STRING;
		if (!MapUtils.isEmpty(row))
		{
			final StringBuilder builder = new StringBuilder();
			int copyIdx = 0;
			int idx = impexRow.indexOf(MplConstants.BRACKET_START);
			while (idx > -1)
			{
				final int endIdx = impexRow.indexOf(MplConstants.BRACKET_END, idx);
				if (endIdx < 0)
				{
					throw new SystemException("Invalid row syntax [brackets not closed]: " + impexRow);
				}
				builder.append(impexRow.substring(copyIdx, idx));
				if (impexRow.charAt(idx + 1) == MplConstants.SEQUENCE_CHAR)
				{
					builder.append(sequenceId);
				}
				else
				{
					final boolean mandatory = impexRow.charAt(idx + 1) == MplConstants.PLUS_CHAR;
					Integer mapIdx = null;
					try
					{
						mapIdx = Integer.valueOf(impexRow.substring(mandatory ? idx + 2 : idx + 1, endIdx));
					}
					catch (final NumberFormatException e)
					{
						throw new SystemException("Invalid row syntax [invalid column number]: " + impexRow, e);
					}
					final String colValue = row.get(mapIdx);
					if (mandatory && StringUtils.isBlank(colValue))
					{
						throw new IllegalArgumentException("Missing value for " + mapIdx);
					}
					if (colValue != null)
					{
						builder.append(colValue);
					}
				}
				copyIdx = endIdx + 1;
				idx = impexRow.indexOf(MplConstants.BRACKET_START, endIdx);
			}
			if (copyIdx < impexRow.length())
			{
				builder.append(impexRow.substring(copyIdx));
			}
			result = builder.toString();
		}
		makeOMSStatusUpdate(row);
		return escapeQuotes(result);
	}



	/**
	 * @param impexRow
	 *           the impexRow to set
	 */
	@Required
	@Override
	public void setImpexRow(final String impexRow)
	{
		Assert.hasText(impexRow);
		this.impexRow = impexRow;
	}

	private void makeOMSStatusUpdate(final Map<Integer, String> row)
	{
		try
		{
			OrderModel order = null;
			final String orderId = row.get(MplConstants.ORDER_COLUMN_MANUALREF);
			if (StringUtils.isNotEmpty(orderId))
			{
				order = orderModelService.getOrder(orderId);
			}
			final String transactionId = row.get(MplConstants.TRANSC_COLUMN_MANUALREF);
			final String newStatus = row.get(MplConstants.STATUS_COLUMN_MANUALREF);

			LOG.debug(" Inside  makeOMSStatusUpdate for orderId:: " + orderId + " transactionId:: " + transactionId + " newStatus:: "
					+ newStatus);

			if (StringUtils.isNotEmpty(transactionId) && StringUtils.isNotEmpty(newStatus) && null != order)
			{

				final CODReturnOMSSyncProcessModel codReturnOMSSyncProcess = (CODReturnOMSSyncProcessModel) businessProcessService
						.createProcess("codreturnomssync-process-" + System.currentTimeMillis(), "codreturnomssync-process");
				codReturnOMSSyncProcess.setOrder(order);
				codReturnOMSSyncProcess.setTransactionId(transactionId);
				codReturnOMSSyncProcess.setTransactionStatus(newStatus);
				businessProcessService.startProcess(codReturnOMSSyncProcess);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception in makeOMSStatusUpdate:: " + e.getMessage());
		}
	}


}
