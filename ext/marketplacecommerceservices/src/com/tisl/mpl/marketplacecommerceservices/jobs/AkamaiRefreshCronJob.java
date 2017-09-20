/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.tisl.mpl.model.AkamaiRefreshUrlListModel;


/**
 * @author 168108
 *
 */
public class AkamaiRefreshCronJob extends AbstractJobPerformable
{
	private static final Logger LOG = Logger.getLogger(AkamaiRefreshCronJob.class);

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		String requestJSON = "";
		HttpURLConnection purgeUrlHTTPConnection = null;
		HttpURLConnection statusUrlHTTPConnection = null;
		OutputStream os = null;
		BufferedReader reader = null;
		BufferedReader statusReader = null;
		final StringBuffer sb = new StringBuffer();
		String urlListToBerefreshed = "";

		try
		{
			/****************** Read Properties from properties file *******************/

			LOG.error("DEBUG: Going to START AkamaiRefreshCronJob .................");

			final String purgeUrl = configurationService.getConfiguration().getString("akamai.fast.purge.url"); //"https://api.ccu.akamai.com/ccu/v2/queues/default";
			final String purgeStatusUrl = configurationService.getConfiguration().getString("akamai.fast.purge.status.url"); //"https://api.ccu.akamai.com";
			final String userName = configurationService.getConfiguration().getString("akamai.fast.purge.userId"); //"debasis.mukherjee@tcs.com";
			final String password = configurationService.getConfiguration().getString("akamai.fast.purge.password"); //"Kolkata@1";
			final String purgeStatusNeededFlag = configurationService.getConfiguration().getString(
					"akamai.actual.prurge.status.needed");

			if ((purgeUrl == null || purgeUrl.trim().length() == 0)
					|| (purgeStatusUrl == null || purgeStatusUrl.trim().length() == 0)
					|| (userName == null || userName.trim().length() == 0) || (password == null || password.trim().length() == 0))
			{
				LOG.error("ERROR: !! Any of the properties not found: purgeUrl/purgeStatusUrl/userName/password ... Exiting ...");
				LOG.error("ERROR: Going to ABORT AkamaiRefreshCronJob .................");
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
			/***************** End Read **********************************************/


			/********************* Section for sending FAST PURGE REQUEST to Akamai **********************/
			int tryCount = 1;
			boolean gotSomeError = true;
			while (tryCount <= 5 && gotSomeError)
			{
				try
				{
					gotSomeError = false;
					urlListToBerefreshed = getURLListTobeRefreshed();

					if (urlListToBerefreshed != null && urlListToBerefreshed.trim().length() > 0)
					{
						requestJSON = "{" + " \"type\" : \"arl\" ," + " \"action\" : \"remove\" ," + " \"domain\" : \"production\" ,"
								+ " \"objects\": [" + urlListToBerefreshed + "]" + "}";

						//Get POST connection to fast Purge API url.
						purgeUrlHTTPConnection = getURLConnection(purgeUrl, userName, password, "POST");
						if (null != purgeUrlHTTPConnection)
						{
							purgeUrlHTTPConnection.setRequestProperty("Content-Length", Integer.toString(requestJSON.getBytes().length));

							//Get OutputStream to send JSON in request Body.
							LOG.error("DEBUG: Going to call the purgeUrlHTTPConnection.getOutputStream() .......");
							os = purgeUrlHTTPConnection.getOutputStream();
							os.write(requestJSON.getBytes());
							os.flush();
							os.close();

							//Get InputStream to read response.
							LOG.error("DEBUG: Going to call the purgeUrlHTTPConnection.getInputStream() .......");
							reader = new BufferedReader(new InputStreamReader(purgeUrlHTTPConnection.getInputStream()));

							String line;
							while (reader != null && ((line = reader.readLine()) != null))
							{
								sb.append(line);
							}

							if (sb != null && sb.toString().trim().length() > 0)
							{
								LOG.error("DEBUG: Returned Response:\n" + sb.toString());
							}
							else
							{
								LOG.error("ERROR: !!! in gtting response(null or Empty) from ..");
								LOG.error("ERROR: Request URL:: " + purgeUrl + " and requestJSON:: " + requestJSON);
								LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount + " Try");
								Thread.sleep(4000);
								LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
								gotSomeError = true;
							}
						}
						else
						{
							LOG.error("ERROR: in getting Connection to Purge Request  URL:: " + purgeUrl + " and requestJSON:: "
									+ requestJSON);
							LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount + " Try");
							Thread.sleep(4000);
							LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
							gotSomeError = true;
						}
					}
					else
					{
						LOG.error("ERROR: in getting URLs which are to be purged ::" + purgeUrl);
						//						LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount + " Try");
						//						Thread.sleep(4000);
						//						LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
						//						gotSomeError = true;
					}
				}
				catch (final Exception ex)
				{
					LOG.error("ERROR: in Conenction " + ex);
					LOG.error("ERROR: Request URL:: " + purgeUrl + " and requestJSON:: " + requestJSON);
					LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount + " Try");
					Thread.sleep(4000);
					LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
					gotSomeError = true;
					ex.printStackTrace();
				}
				tryCount++;
			}
			LOG.error("DEBUG: Already tried calling Akamai Fast Purge Service:: " + (tryCount - 1) + " times.........");
			LOG.error("DEBUG: Now going to read Status(if any) of the Akamai Purge Call.........");

			/********************** End Section for sending FAST PURGE REQUEST to Akamai ********************/


			/********************* Read the PURGE STATUS **********************/

			//perse the returned JSON into Obj.
			if (sb != null || sb.toString().trim().length() > 0)
			{
				final JSONParser parser = new JSONParser();
				final JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());

				long estimatedSeconds = 0, queueLength = 0, httpStatus = 0, pingAfterSeconds = 0;
				String progressUri = "", purgeId = "", detail = "", completionTime = "", submittedBy = "", purgeStatus = "", submissionTime = "";

				if (jsonObject.get("estimatedSeconds") != null)
				{
					estimatedSeconds = ((Long) jsonObject.get("estimatedSeconds"));
				}
				if (jsonObject.get("progressUri") != null)
				{
					progressUri = (String) jsonObject.get("progressUri");
				}
				if (jsonObject.get("queueLength") != null)
				{
					queueLength = (Long) jsonObject.get("queueLength");
				}
				if (jsonObject.get("purgeId") != null)
				{
					purgeId = (String) jsonObject.get("purgeId");
				}
				if (jsonObject.get("httpStatus") != null)
				{
					httpStatus = (Long) jsonObject.get("httpStatus");
				}
				if (jsonObject.get("detail") != null)
				{
					detail = (String) jsonObject.get("detail");
				}
				if (jsonObject.get("completionTime") != null)
				{
					completionTime = (String) jsonObject.get("completionTime");
				}
				if (jsonObject.get("submittedBy") != null)
				{
					submittedBy = (String) jsonObject.get("submittedBy");
				}
				if (jsonObject.get("purgeStatus") != null)
				{
					purgeStatus = (String) jsonObject.get("purgeStatus");
				}
				if (jsonObject.get("submissionTime") != null)
				{
					submissionTime = (String) jsonObject.get("submissionTime");
				}
				if (jsonObject.get("pingAfterSeconds") != null)
				{
					pingAfterSeconds = (Long) jsonObject.get("pingAfterSeconds");
				}

				LOG.error("DEBUG: Response after Purge API called:- | estimatedSeconds:: " + estimatedSeconds + "\n progressUri:: "
						+ progressUri + "\n queueLength:: " + queueLength + "\n purgeId:: " + purgeId + "\n httpStatus:: " + httpStatus
						+ "\n detail:: " + detail + "\n completionTime:: " + completionTime + "\n submittedBy:: " + submittedBy
						+ "\n purgeStatus:: " + purgeStatus + "\n submissionTime:: " + submissionTime + "\n pingAfterSeconds:: "
						+ pingAfterSeconds);

				if (purgeStatusUrl != null && purgeStatusUrl.trim().length() != 0 && progressUri != null
						&& progressUri.trim().length() != 0)
				{
					if (((200 <= httpStatus) && (httpStatus <= 226)) || (detail != null && detail.contains("Request accepted")))
					{
						LOG.error("DEBUG: Seems Purge Request Properly submitted, as httpStatus:: " + httpStatus
								+ " and StatusDetail::" + detail);
					}

					if ("Y".equalsIgnoreCase(purgeStatusNeededFlag)) // Status reading can be made off by this.
					{
						LOG.error("DEBUG: Going to Sleep for:: " + pingAfterSeconds + " Sec before actual Purge Status Update Call....");
						Thread.sleep(pingAfterSeconds * 1000);
						LOG.error("DEBUG: Up after " + pingAfterSeconds + " Sec and going to call for actual purge status");

						tryCount = 1;
						gotSomeError = true;
						while (tryCount <= 5 && gotSomeError)
						{
							try
							{
								gotSomeError = false;
								statusUrlHTTPConnection = getURLConnection(purgeStatusUrl + progressUri, userName, password, "GET");

								statusReader = new BufferedReader(new InputStreamReader(statusUrlHTTPConnection.getInputStream()));

								String line1;
								final StringBuffer sb1 = new StringBuffer();
								while (statusReader != null && ((line1 = statusReader.readLine()) != null))
								{
									sb1.append(line1);
								}
								if (sb1 != null && sb1.toString().trim().length() > 0)
								{
									LOG.error("DEBUG: !!!! Actuall Purge Status Response (200/201/202/2XX means OK-Purge Done):::" + sb1);
								}
								else
								{
									LOG.error("ERROR: in Getting Actual Purge Status, response is null or Empty::" + sb1);
									LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount
											+ " Try");
									Thread.sleep(4000);
									LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
									gotSomeError = true;
								}
							}
							catch (final Exception ex)
							{
								LOG.error("ERROR: Exception in Getting Actual Purge Status::" + ex);
								LOG.error("DEBUG: Going to try again after 4 Seconds....(total 5 tries) This is:: " + tryCount + " Try");
								Thread.sleep(4000);
								LOG.error("DEBUG: Trying now after 4 Seconds........This is::" + tryCount + " Try");
								gotSomeError = true;
								ex.printStackTrace();
							}
							tryCount++;
						}
					}
					else
					{
						LOG.error("DEBUG: Actual Purge Status was not needed .. so Skipping the Call ..'purgeStatusNeededFlag' ::"
								+ purgeStatusNeededFlag + ".. try hiting Website after some time arbitrarily to get status..");
					}
				}
				else
				{
					LOG.error("ERROR: in either purgeStatusUrl.. try again running the Job Separately.::" + purgeStatusUrl
							+ progressUri + " or progressUri::" + progressUri);
				}
			}
			else
			{
				LOG.error("ERROR: in JSON response in Purge Submit Call.. try again running the Job Separately.::");
			}

			/**************** End Read the Purge Status ****************/
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: ::" + e);
			LOG.error("DEBUG: Going to ABORT AkamaiRefreshCronJob .................");
			e.printStackTrace();

			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				if (reader != null)
				{
					reader.close();
				}
				if (statusReader != null)
				{
					statusReader.close();
				}
				if (purgeUrlHTTPConnection != null)
				{
					purgeUrlHTTPConnection.disconnect();
				}
				if (statusUrlHTTPConnection != null)
				{
					statusUrlHTTPConnection.disconnect();
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				LOG.error("ERROR: in finally ... :" + e);
				LOG.error("ERROR: Going to ABORT AkamaiRefreshCronJob .................");
				e.printStackTrace();
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
		}

		LOG.error("DEBUG: Going to END AkamaiRefreshCronJob .................");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private String getURLListTobeRefreshed()
	{
		List<AkamaiRefreshUrlListModel> akamaiRefreshUrlListModelList = null;
		String refreshUrl = "";
		final StringBuffer productString = new StringBuffer();
		final Set<String> urlSet = new TreeSet();

		try
		{
			//Fetch the Product ListIds from DB table 'FlashSaleProduct' and then pass it to get the PKs of those List Ids.
			final String qString = "select distinct{PK} from {AkamaiRefreshUrlList}";
			final FlexibleSearchQuery queryRefreshUrl = new FlexibleSearchQuery(qString);
			LOG.error("DEBUG: Going to Read AkamaiRefreshUrlList for Refresh URls.... :");
			akamaiRefreshUrlListModelList = flexibleSearchService.<AkamaiRefreshUrlListModel> search(queryRefreshUrl).getResult();

			if (CollectionUtils.isNotEmpty(akamaiRefreshUrlListModelList))
			{
				try
				{
					for (final AkamaiRefreshUrlListModel refreshUrlModel : akamaiRefreshUrlListModelList)
					{
						refreshUrl = refreshUrlModel.getRefreshUrl();
						if (refreshUrl != null && refreshUrl.trim().length() > 0 && !urlSet.contains(refreshUrl.trim()))
						{
							productString.append("\"" + refreshUrl.trim() + "\",");
							urlSet.add(refreshUrl.trim());
						}
					}
					if (productString != null && productString.length() > 0)
					{
						productString.deleteCharAt(productString.toString().lastIndexOf(",")); // remove the last `,` from the string.
						LOG.error("DEBUG: The Akamai refresh to be done for the URLs ::" + productString);
					}
				}
				catch (final Exception e)
				{
					LOG.error("Error..  in fetching refresh URLs::" + e);
					e.printStackTrace();
				}
			}
			else
			{
				LOG.error("ERROR !!No URL List found in DB...");
				return null;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error..  Something went wrong" + e);
			e.printStackTrace();
		}

		return productString.toString();
	}

	private HttpURLConnection getURLConnection(final String url, final String username, final String password,
			final String methodSent)
	{
		Proxy proxy = null;
		HttpURLConnection urlHTTPConn = null;

		try
		{
			final String proxyEnabled = configurationService.getConfiguration().getString("proxy.enabled");
			final String proxyAddress = configurationService.getConfiguration().getString("proxy.address");
			final String proxyPort = configurationService.getConfiguration().getString("proxy.port");
			final URL urlObj = new URL(url);
			if (proxyEnabled != null && "true".equals(proxyEnabled) && !StringUtils.isEmpty(proxyAddress)
					&& !StringUtils.isEmpty(proxyPort))
			{
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress.trim(), Integer.parseInt(proxyPort.trim())));
				urlHTTPConn = (HttpURLConnection) urlObj.openConnection(proxy);
			}
			else
			{
				urlHTTPConn = (HttpURLConnection) urlObj.openConnection();
			}
			final String authString = username + ":" + password;
			final String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			urlHTTPConn.setRequestProperty("Authorization", "Basic " + authStringEnc);

			if (null != methodSent && methodSent.trim().length() != 0)
			{
				urlHTTPConn.setRequestMethod(methodSent);
			}
			else
			{
				urlHTTPConn.setRequestMethod("GET");
			}
			urlHTTPConn.setRequestProperty("Accept", "application/json");
			urlHTTPConn.setRequestProperty("Content-type", "application/json");
			urlHTTPConn.setRequestProperty("Content-Language", "en-US");
			urlHTTPConn.setRequestProperty("charset", "utf-8");
			urlHTTPConn.setUseCaches(false);
			urlHTTPConn.setDoInput(true);
			urlHTTPConn.setDoOutput(true);

			return urlHTTPConn;
		}
		catch (final Exception e)
		{
			LOG.error("ERROR !! in getting getURLConnection() " + e);
			e.printStackTrace();
		}

		return null;
	}

}
