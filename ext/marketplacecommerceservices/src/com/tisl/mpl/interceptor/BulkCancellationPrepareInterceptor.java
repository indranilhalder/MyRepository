/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.bulk.service.BulkCancellationService;
import com.tisl.mpl.businesscontentimport.BusinessContentImportUtility;
import com.tisl.mpl.core.model.BulkCancellationModel;


/**
 * @author TCS
 *
 */
public class BulkCancellationPrepareInterceptor implements PrepareInterceptor
{
	private static final String UNUSED = "unused";

	@SuppressWarnings(UNUSED)
	private static final String JOBCODE = "initiateCancelForOrderCronJob";
	@SuppressWarnings(UNUSED)
	private static final String CSVSPLITBY = ",";
	@SuppressWarnings(UNUSED)
	private static final String ZEROVALUE = "0";
	@SuppressWarnings(UNUSED)
	private static final String BULKCANCELLATIONBATCH = "mpl.bulkcancellation.use";

	@Resource
	BusinessContentImportUtility businessContentImportUtil;
	private static final Logger LOG = Logger.getLogger(BulkCancellationPrepareInterceptor.class); //critical SONAR FIX
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MediaService mediaService;
	@Autowired
	private ModelService modelService;

	@Autowired
	private CronJobService cronJobService;
	@Autowired
	private BulkCancellationService bulkCancellationService;

	/**
	 * @Description This method will execute the content creation and run the cron job before uploading csv
	 */
	@Override
	public void onPrepare(final Object o, final InterceptorContext its) throws InterceptorException
	{
		if (o instanceof BulkCancellationModel)
		{
			try
			{

				//calling the uploadCSVContent with the input stream and the real file name from Media Model
				LOG.debug("Entered Into Bulk Cancellation Interceptor");
				final List<BulkCancellationProcessModel> updatedBulkCancellationProcessList = new ArrayList<BulkCancellationProcessModel>();

				final BulkCancellationModel bCancellationModel = (BulkCancellationModel) o;
				if (StringUtils.isEmpty(bCancellationModel.getCsvFile().getURL()))
				{
					throw new InterceptorException("Please upload a CSV file");
				}
				else if (!bCancellationModel.getCsvFile().getMime().equals("text/csv"))
				{
					throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
							+ bCancellationModel.getCsvFile().getMime());
				}
				//System.out.println("file name: " + bCancellationModel.getCsvFile().getURL());

				final InputStream inputStream = mediaService.getStreamFromMedia(bCancellationModel.getCsvFile());

				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				//final StringBuilder out = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null)
				{
					//out.append(line);
					final String[] lineData = line.split(CSVSPLITBY);

					if (lineData.length >= 2)
					{
						final BulkCancellationProcessModel bulkCancelProcessModel = new BulkCancellationProcessModel();
						bulkCancelProcessModel.setParentOrderNo(lineData[0]);
						bulkCancelProcessModel.setTransactionId(lineData[1]);
						bulkCancelProcessModel.setLoadStatus(ZEROVALUE);

						updatedBulkCancellationProcessList.add(bulkCancelProcessModel);
					}
				}
				//System.out.println(out.toString()); //Prints the string content read from input stream
				reader.close();

				if (updatedBulkCancellationProcessList.size() > Integer.parseInt(configurationService.getConfiguration().getString(
						BULKCANCELLATIONBATCH)))
				{

					throw new InterceptorException("Please upload a CSV file with less than or equal to "
							+ Integer.parseInt(configurationService.getConfiguration().getString(BULKCANCELLATIONBATCH)) + " entries."
							+ bCancellationModel.getCsvFile().getMime());
				}
				if (CollectionUtils.isNotEmpty(updatedBulkCancellationProcessList))
				{
					modelService.saveAll(updatedBulkCancellationProcessList);

					try
					{
						final List<CronJobModel> cronJobList = bulkCancellationService.fetchJobDetails(JOBCODE);
						for (final CronJobModel cronModel : cronJobList)
						{
							cronJobService.performCronJob(cronModel);
						}
					}
					catch (final Exception e)
					{
						LOG.error("onPrepare Bulk Cancellation Cronjob exception " + e.getMessage());
						throw new InterceptorException("Bulk Cancellation Cronjob exception has occured " + e.getMessage());
					}

				}
				LOG.debug("Exiting From Bulk Cancellation Interceptor");

			}
			catch (final Exception e)
			{
				LOG.error("onPrepare Bulk Cancellation exception " + e.getMessage());
				throw new InterceptorException("IO Exception/ File handling exception has occured" + e.getMessage());
			}

		}

	}
}