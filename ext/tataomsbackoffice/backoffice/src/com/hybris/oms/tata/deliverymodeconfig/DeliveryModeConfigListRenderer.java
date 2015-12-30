package com.hybris.oms.tata.deliverymodeconfig;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.deliverymode.dto.DeliveryModeConfig;


/**
 * @author Saood
 * 
 * 
 */
public class DeliveryModeConfigListRenderer implements ListitemRenderer
{



	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final DeliveryModeConfig deliveryModeConfig = (DeliveryModeConfig) value;

		// keep value in listitem
		listitem.setValue(value);

		addListcell(listitem, deliveryModeConfig.getDeliverymode());
		addListcell(listitem, deliveryModeConfig.getCutofftime());
		addListcell(listitem, customDateFormat(deliveryModeConfig.getDfmtat()));
		addListcell(listitem, customDateFormat(deliveryModeConfig.getSellerresponsetat()));
		addListcell(listitem, customDateFormat(deliveryModeConfig.getHotctat()));
		addListcell(listitem, customDateFormat(deliveryModeConfig.getShiptat()));


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label cellLabel = new Label(value);
		cellLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

	public String customDateFormat(final String timeDuration)
	{
		StringBuilder hhmm = new StringBuilder();
		final int timeDurationinInt = Integer.parseInt(timeDuration);
		final int hours = timeDurationinInt / 60; //since both are ints,
		final int minutes = timeDurationinInt % 60;
		//System.out.println(hours + minutes);
		hhmm = hhmm.append(hours).append(" Hr ").append(minutes).append(" min");
		return hhmm.toString();

	}

}
