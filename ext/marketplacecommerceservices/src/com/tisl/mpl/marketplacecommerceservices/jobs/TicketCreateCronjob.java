/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.MplTicketService;
import com.tisl.mpl.model.CRMTicketCreationJobModel;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.service.TicketCreationCRMservice;


/**
 * @author TCS
 *
 */
public class TicketCreateCronjob extends AbstractJobPerformable<CRMTicketCreationJobModel>
{
	private final static Logger LOG = Logger.getLogger(TicketCreateCronjob.class);

	@Autowired
	private MplTicketService mplTicketService;

	@Autowired
	private TicketCreationCRMservice crmticketCreationService;

	@Autowired
	private ModelService modelService;

	@Override
	public PerformResult perform(final CRMTicketCreationJobModel cronjob)
	{

		LOG.info("Fetching all ticket Detail for those which have not been created in CRM");
		final List<CRMTicketDetailModel> unCreatedtickets = mplTicketService.findCRMTicketDetail(false);
		LOG.info("Found " + unCreatedtickets.size() + " tickets which has not been created!!!");
		if (unCreatedtickets.size() > 0)
		{
			for (final CRMTicketDetailModel ticketDetail : unCreatedtickets)
			{
				if (StringUtils.isNotEmpty(ticketDetail.getCRMRequest()))
				{
					int responseCode = 0;
					String errorMessage = "";
					try
					{
						responseCode = crmticketCreationService.createTicketInCRM(ticketDetail.getCRMRequest());
						errorMessage = String.valueOf(responseCode);
					}
					catch (final JAXBException e)
					{
						LOG.error(" ****** JAXBException occured in CRM ticket cron job due to  ", e);
						errorMessage = e.getMessage();
					}
					catch (final Exception e)
					{
						LOG.error(" ****** Exception occured in CRM ticket cron job due to  ", e);
						errorMessage = e.getMessage();
					}
					if (responseCode == 200)
					{
						ticketDetail.setCRMResponse(String.valueOf(responseCode));
						ticketDetail.setIsTicketCreatedInCRM(Boolean.TRUE);
					}
					else
					{
						ticketDetail.setCRMResponse(errorMessage.substring(0, 3999));
					}
					modelService.save(ticketDetail);
				}
			}

		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}



}
