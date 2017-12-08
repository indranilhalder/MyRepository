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

import com.tisl.mpl.core.model.SizeGuideUploadModel;


/**
 * @author TCS
 *
 */
public class SizeGuideUploadPrepareInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(SizeGuideUploadPrepareInterceptor.class);

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

		if (o instanceof SizeGuideUploadModel)
		{
			final SizeGuideUploadModel sizeGuideUploadModel = (SizeGuideUploadModel) o;
			try
			{

				if (StringUtils.isEmpty(sizeGuideUploadModel.getCsvFile().getURL()))
				{
					throw new InterceptorException("Please upload a CSV file");
				}
				else if (!sizeGuideUploadModel.getCsvFile().getMime().equals("text/csv"))
				{
					throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
							+ sizeGuideUploadModel.getCsvFile().getMime());
				}
				LOG.debug("Input Stream starting");

				final String header = configurationService.getConfiguration().getString("sizeGuide.upload.header");
				final InputStream inputStream = mediaService.getStreamFromMedia(sizeGuideUploadModel.getCsvFile());

				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				final StringBuilder script = new StringBuilder();

				script.append(header);

				String valueLine = null;
				while ((valueLine = reader.readLine()) != null)
				{
					script.append("\n" + ";" + valueLine.replaceAll(",", ";"));
				}

				LOG.debug("Impex for sizeGuideUpload is - " + script.toString());

				final ImportConfig config = new ImportConfig();
				config.setLegacyMode(Boolean.FALSE);
				config.setScript(script.toString());
				final ImportResult result = importService.importData(config);

				if (null != result.getCronJob())
				{
					sizeGuideUploadModel.setStatus("ERROR - Refer job  " + result.getCronJob().getCode());
				}
				else
				{
					sizeGuideUploadModel.setStatus("Import Finished Successfully");

				}


			}
			catch (final Exception e)
			{
				sizeGuideUploadModel.setStatus("Error in performing import, check logs");
				LOG.error("onPrepare Size Guide Upload exception ");
				throw new InterceptorException("exception has occured...." + e.getMessage());
			}
		}

	}
}
