/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.shorturl.service.ShortUrlService;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.OrderDetailsListRenderer;
/**
 * @author 1006687
 *
 */
public class MarketPlaceOrderDetailsListRenderer extends
		OrderDetailsListRenderer {
	
	@Autowired
	private ShortUrlService googleShortUrlService;
	
	public HtmlBasedComponent createContent(Object context, TypedObject item,
			Widget widget) {
		Div detailContainer = new Div();

		if (item != null) {
			OrderModel order = (OrderModel) item.getObject();
			detailContainer.setSclass("csObject"
					+ CssUtils.sanitizeForCss(item.getType().getCode())
					+ "Container");
			List<ColumnConfiguration> columns = getColumnConfigurations(item);
			for (ColumnConfiguration col : columns) {
				renderRow(createTitleLabel(getPropertyRendererHelper()
						.getPropertyDescriptorName(col)),
						createValueLabel(ObjectGetValueUtils.getValue(
								col.getValueHandler(), item)), detailContainer);
			}
			
			String shortUrl = getOrderShortUrl(widget, order);
			if(null != shortUrl && !shortUrl.isEmpty()) {
				renderShortUrlRow(createTitleLabel(LabelUtils.getLabel(widget,
						"shortUrl", new Object[0])),
						createValueLabel(shortUrl),
						detailContainer);
			}
			else {
				renderRow(createTitleLabel(LabelUtils.getLabel(widget, "shortUrl",
						new Object[0])), createValueLabel(MarketplacecommerceservicesConstants.HYPHEN),
						detailContainer);
			}
			
		} else {
			detailContainer.setSclass("csObject"
					+ CssUtils.sanitizeForCss(getConfigurationCode())
					+ "Container");
		}
		return detailContainer;
	}
	
	
	
	private void renderShortUrlRow(Label label, Label shortUrl,
			Div parent) {
		Div content = new Div();
		content.setSclass("csOrderDetailRow");
		if (label != null) {
			content.appendChild(label);
		}
		Toolbarbutton shortUrlLink = new Toolbarbutton(shortUrl.getValue().toString());
		shortUrlLink.setSclass("blueLink");
		shortUrlLink.addEventListener("onClick",
				createshortUrlLinkEventListener(shortUrl.getValue().toString()));
		content.appendChild(shortUrlLink);
		content.setParent(parent);
		
	}



	private EventListener createshortUrlLinkEventListener(String shortUrl) {
			return new shortUrlLinkEventListener(shortUrl);
	}
	
	protected class shortUrlLinkEventListener implements EventListener {
		private final String shortUrl;
		public shortUrlLinkEventListener(final String shortUrl) {
			this.shortUrl = shortUrl;

		}

		public void onEvent(Event event) throws InterruptedException {
			MarketPlaceOrderDetailsListRenderer.this.handleTrackUrlEvent(
					event, this.shortUrl);
		}
	}


	private String getOrderShortUrl(Widget widget, OrderModel order) {
		String shortUrl = null;
		String orderCode= null;
		if(order.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PARENTORDER)) {
			orderCode = order.getCode();
		}else {
			orderCode = order.getParentReference().getCode();
		}
		shortUrl = ((MarketPlaceOrderController) widget.getWidgetController()).getShortUrl(orderCode);
		return shortUrl;
	}

	public void handleTrackUrlEvent(Event event, String shortUrl) {

		Clients.evalJavaScript("window.open('" + shortUrl + "')");
	}



	@Override
	protected void populateKnownDetails(Object context, TypedObject item, Widget widget, Div detailContainer){
		//Overriding this method to do nothing.
	}

}
