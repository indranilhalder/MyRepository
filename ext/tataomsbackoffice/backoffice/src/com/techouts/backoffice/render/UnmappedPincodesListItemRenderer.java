
package com.techouts.backoffice.render;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.hybris.oms.api.reports.ReportsGenerateFacade;
import com.hybris.oms.domain.buc.reports.dto.ReportRequest;
import com.hybris.oms.tata.services.FilePathProviderService;


/**
 * @author Techouts
 *
 */
public class UnmappedPincodesListItemRenderer implements RowRenderer<String>
{
	private static final Logger LOG = LoggerFactory.getLogger(UnmappedPincodesListItemRenderer.class);

	private final FilePathProviderService filePathProviderService;
	private final ReportsGenerateFacade reportsGenerateFacade;

	public UnmappedPincodesListItemRenderer(final FilePathProviderService filePathProviderService,
			final ReportsGenerateFacade reportsGenerateFacade)
	{
		this.filePathProviderService = filePathProviderService;
		this.reportsGenerateFacade = reportsGenerateFacade;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.zkoss.zul.RowRenderer#render(org.zkoss.zul.Row, java.lang.Object, int)
	 */
	@Override
	public void render(final Row row, final String seller, final int paramInt) throws Exception
	{
		final String saveFilePath = filePathProviderService.getUnmappedpincodesDwnldPath();
		final File file = new File(saveFilePath.trim(), seller + ".csv");
		final Label sellerIdLabel = new Label();
		sellerIdLabel.setValue(seller);
		row.appendChild(sellerIdLabel);
		final Label dateLabel = new Label();
		final Div d = new Div();
		final Button thumbBtn = new Button("Download");
		thumbBtn.setParent(d);
		final Div d2 = new Div();
		final Button genBtn = new Button("Generate");
		genBtn.setParent(d2);
		genBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>()
		{
			public void onEvent(final Event arg0)
			{
				final ReportRequest reportRequest = new ReportRequest();
				reportRequest.setSellerId(seller);
				if (reportsGenerateFacade != null)
				{
					final String responseFile = reportsGenerateFacade.generateUnMappedPincodes(reportRequest);//seller + ".csv";
					if (responseFile.contains(".csv"))
					{
						final File file1 = new File(saveFilePath.trim(), responseFile);
						if (file1.exists())
						{
							final Date date = new Date(file.lastModified());
							final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
							dateLabel.setValue(dateFormat.format(date));
							thumbBtn.setDisabled(false);
							thumbBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>()
							{
								public void onEvent(final Event arg0)
								{
									try
									{
										Filedownload.save(file, null);
									}
									catch (final FileNotFoundException e)
									{
										// YTODO Auto-generated catch block

									}
								}
							});
						}
						else
						{
							Messagebox.show("unable to generate file . please contact admin .");
						}
					}
					else
					{
						Messagebox.show("Seller does not mapped with any pincode . So unable to generate file .");

					}
				}
				else
				{
					Messagebox.show("Server Error.");
					LOG.warn("ReportsGenerateFacade Object is null ");
				}
			}
		});

		if (file.exists())
		{
			final Date date = new Date(file.lastModified());
			final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			dateLabel.setValue(dateFormat.format(date));
			thumbBtn.setDisabled(false);
			thumbBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>()
			{
				public void onEvent(final Event arg0)
				{
					try
					{
						Filedownload.save(file, null);
					}
					catch (final FileNotFoundException e)
					{
						// YTODO Auto-generated catch block

					}
				}
			});
		}
		else
		{
			thumbBtn.setDisabled(true);
		}
		row.appendChild(dateLabel);
		row.appendChild(d);
		row.appendChild(d2);

	}

}