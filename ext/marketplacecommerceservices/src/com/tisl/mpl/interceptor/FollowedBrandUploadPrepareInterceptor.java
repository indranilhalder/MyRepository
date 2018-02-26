/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.FollowedBrandUploadModel;


/**
 * @author TCS
 *
 */
public class FollowedBrandUploadPrepareInterceptor implements PrepareInterceptor
{
	private static final Logger LOG = Logger.getLogger(FollowedBrandUploadPrepareInterceptor.class);

	@Autowired
	private MediaService mediaService;

	@Autowired
	private ImportService importService;

	@Autowired
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final Object o, final InterceptorContext iCxt) throws InterceptorException
	{
		// YTODO Auto-generated method stub

		if (o instanceof FollowedBrandUploadModel)
		{
			final FollowedBrandUploadModel followedBrandUploadModel = (FollowedBrandUploadModel) o;
			try
			{

				if (StringUtils.isEmpty(followedBrandUploadModel.getCsvFile().getURL()))
				{
					throw new InterceptorException("Please upload a CSV file");
				}
				else if (!followedBrandUploadModel.getCsvFile().getMime().equals("text/csv"))
				{
					throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
							+ followedBrandUploadModel.getCsvFile().getMime());
				}
				LOG.debug("Input Stream starting");

				final String header1 = "INSERT_UPDATE FollowedBrandGender;uniqueId[unique='true'];brandCode;gender";

				final String header2 = "INSERT_UPDATE BrandMaster;brandCode[unique='true'];logoImageUrl";

				final InputStream inputStream = mediaService.getStreamFromMedia(followedBrandUploadModel.getCsvFile());

				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				final StringBuilder script1 = new StringBuilder();
				final StringBuilder script2 = new StringBuilder();

				script1.append(header1);
				script2.append(header2);

				String valueLine = null;
				while ((valueLine = reader.readLine()) != null)
				{
					if (StringUtils.isNotEmpty(valueLine.split(",")[0]) && StringUtils.isNotEmpty(valueLine.split(",")[1])
							&& StringUtils.isNotEmpty(valueLine.split(",")[2]))
					{
						script1.append("\n" + ";" + valueLine.split(",")[0] + ";" + valueLine.split(",")[1] + ";"
								+ valueLine.split(",")[2]);
					}
					if (StringUtils.isNotEmpty(valueLine.split(",")[1]) && StringUtils.isNotEmpty(valueLine.split(",")[3]))
					{
						script2.append("\n" + ";" + valueLine.split(",")[1] + ";" + valueLine.split(",")[3]);
					}
				}

				script1.append("\n" + script2);

				LOG.debug("Impex for Followed Brand Upload is - " + script1.toString());

				final ImportConfig config = new ImportConfig();
				config.setLegacyMode(Boolean.FALSE);
				config.setScript(script1.toString());
				final ImportResult result = importService.importData(config);

				if (null != result.getCronJob())
				{
					followedBrandUploadModel.setStatus("ERROR - Refer job  " + result.getCronJob().getCode());
				}
				else
				{
					followedBrandUploadModel.setStatus("Import Finished Successfully");

				}


			}
			catch (final Exception e)
			{
				followedBrandUploadModel.setStatus("Error in performing import, check logs");
				LOG.error("onPrepare Followed Brand Upload exception ");
				throw new InterceptorException("exception has occured...." + e.getMessage());
			}
		}
	}

}
