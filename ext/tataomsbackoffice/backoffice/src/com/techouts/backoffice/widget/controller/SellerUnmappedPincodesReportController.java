
package com.techouts.backoffice.widget.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.reports.ReportsGenerateFacade;
import com.hybris.oms.tata.facade.SellersInfoFacade;
import com.hybris.oms.tata.services.FilePathProviderService;
import com.techouts.backoffice.render.UnmappedPincodesListItemRenderer;
import com.tisl.mpl.model.SellerInformationModel;

import net.sourceforge.pmd.util.StringUtil;


/**
 * @author techouts
 *
 */
public class SellerUnmappedPincodesReportController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(SellerUnmappedPincodesReportController.class);

	@WireVariable("sellersInfoFacade")
	private SellersInfoFacade sellersInfoFacade;
	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;
	@WireVariable("reportRequestRestClient")
	private ReportsGenerateFacade reportsGenerateFacade;
	SellerInformationModel s = null;

	private Grid rendergrid;
	private Textbox sellerId;
	private List<String> sellers;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		sellers = sellersInfoFacade.getAllSellers();
		LOG.debug("Avilable sellers in SellerMaster {}", sellers);
		rendergrid.setModel(new ListModelList<String>(sellers));
		rendergrid.setRowRenderer(new UnmappedPincodesListItemRenderer(filePathProviderService, reportsGenerateFacade));
	}

	@ViewEvent(componentID = "search", eventName = Events.ON_CLICK)
	public void search()
	{
		final String seller = sellerId.getValue();
		LOG.debug("serched for SellerUnmappedPincodesReport ID - [{}]", seller);
		if (StringUtil.isNotEmpty(seller))
		{
			if (sellers.contains(seller))
			{
				final List<String> sellers = new ArrayList<String>();
				sellers.add(seller);
				rendergrid.setModel(new ListModelList<>(sellers));
				rendergrid.setRowRenderer(new UnmappedPincodesListItemRenderer(filePathProviderService, reportsGenerateFacade));
			}
			else
			{
				Messagebox.show("SellerId does not exist . please enter valid sellerId");
			}
		}
		else
		{
			rendergrid.setModel(new ListModelList<>(sellers));
			rendergrid.setRowRenderer(new UnmappedPincodesListItemRenderer(filePathProviderService, reportsGenerateFacade));
		}


	}

}
