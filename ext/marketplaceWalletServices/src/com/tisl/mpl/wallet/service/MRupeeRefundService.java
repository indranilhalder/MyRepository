/**
 *
 */
package com.tisl.mpl.wallet.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.tisl.mpl.wallet.refund.MRupeeRefundResponse;
import com.tisl.mpl.wallet.request.MRupeeRefundRequest;


/**
 * @author TCS
 *
 */
public class MRupeeRefundService
{

	private static final Logger LOG = Logger.getLogger(MRupeeRefundService.class);

	private final int connectionTimeout = 5 * 10000;
	private final int readTimeout = 5 * 1000;
	private String baseUrl;
	private String key;
	private String merchantId;
	private Environment environment;
	private String environmentSet;
	@Autowired
	private ConfigurationService configurationService;
	//	@Autowired
	//	private MrupeePaymentService mRupeePaymentService;

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}


	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}


	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}


	/**
	 * @param key
	 *           the key to set
	 */
	public void setKey(final String key)
	{
		this.key = key;
	}


	/**
	 * @return the merchantId
	 */
	public String getMerchantId()
	{
		return merchantId;
	}


	/**
	 * @param merchantId
	 *           the merchantId to set
	 */
	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}


	/**
	 * @return the environment
	 */
	public Environment getEnvironment()
	{
		return environment;
	}


	/**
	 * @param environment
	 *           the environment to set
	 */
	public void setEnvironment(final Environment environment)
	{
		this.environment = environment;
	}


	/**
	 * @return the environmentSet
	 */
	public String getEnvironmentSet()
	{
		return environmentSet;
	}


	/**
	 * @param environmentSet
	 *           the environmentSet to set
	 */
	public void setEnvironmentSet(final String environmentSet)
	{
		this.environmentSet = environmentSet;
	}


	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}


	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout()
	{
		return readTimeout;
	}


	public MRupeeRefundResponse refund(final MRupeeRefundRequest refundRequest)
	{
		MRupeeRefundResponse mRupeeRefundResponse = null;

		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		final MrupeePaymentService mRupeePaymentService = new MrupeePaymentService();
		final String checksum = mRupeePaymentService.generateCheckSum(refundRequest);
		params.put("MCODE", refundRequest.getmCode());
		params.put("NARRATION", refundRequest.getNarration());
		params.put("TXNTYPE", refundRequest.getTxnType());
		params.put("AMT", refundRequest.getAmount().toString());
		params.put("REFNO", refundRequest.getRefNo());
		params.put("PREFNO", refundRequest.getPurchaseRefNo());
		params.put("CHECKSUM", checksum);
		/* final String serializedParams = serializeParams(params); */
		final String serializedParams = "MCODE=TULA&NARRATION=uat&TXNTYPE=R&" + "AMT=" + refundRequest.getAmount().toString()
				+ "&REFNO=" + refundRequest.getRefNo() + "&PREFNO=" + refundRequest.getPurchaseRefNo() + "&CHECKSUM=" + checksum;

		//LOG.debug("MRUPEE REFUND REQUEST--------------url-----" + url + "-------request----" + serializedParams);

		//	String url = configurationService.getConfiguration().getString(MarketplaceWalletServicesConstants.MRUPEERETURNURL);

		final String url = "https://14.140.248.13/Mwallet/startpaymentgatewayS2S.do?";

		final String response = makeServiceCall(url, serializedParams);

		if (null != response)
		{
			LOG.error("Refund Response:::" + response);

			final String[] params1 = response.split("|");

			if (null != params1)
			{
				mRupeeRefundResponse = new MRupeeRefundResponse();

				final String status = params1[0];

				if (status.equalsIgnoreCase("S"))
				{
					final String requestId = params1[1];
					final String refNo = params1[2];
					final String mwrefNo = params1[3];
					final String reason = params1[4];
					mRupeeRefundResponse.setStatus("SUCCESS");
					mRupeeRefundResponse.setRequestID(requestId);
					mRupeeRefundResponse.setRefNo(refNo);
					mRupeeRefundResponse.setMwrefNo(mwrefNo);
					mRupeeRefundResponse.setReason(reason);
					mRupeeRefundResponse.setSuccess(true);
				}
				else if (status.equalsIgnoreCase("E"))
				{
					mRupeeRefundResponse.setStatus("FAILURE");
					mRupeeRefundResponse.setSuccess(false);
				}
				else
				{
					mRupeeRefundResponse.setSuccess(false);
				}
			}
		}
		return mRupeeRefundResponse;

	}





	private String serializeParams(final Map<String, String> parameters)
	{

		final StringBuilder bufferUrl = new StringBuilder();
		try
		{
			for (final Map.Entry<String, String> entry : parameters.entrySet())
			{
				bufferUrl.append(entry.getKey());
				bufferUrl.append('=');
				bufferUrl.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				bufferUrl.append('&');
			}
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.info("Encoding exception while trying to construct payload", e);
		}
		return bufferUrl.toString();
	}

	/**
	 * It opens the connection to the given endPoint and returns the http response as String.
	 *
	 * @param endPoint
	 *           - The HTTP URL of the request
	 * @return HTTP response as string
	 */
	private String makeServiceCall(final String endPoint, final String encodedParams)
	{

		final String proxyEnableStatus = "true";
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();

		try
		{

			final SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			final TrustManager[] trust_mgr = new TrustManager[]
			{ new X509TrustManager()
			{
				@Override
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				@Override
				public void checkClientTrusted(final X509Certificate[] certs, final String t)
				{
					//
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] certs, final String t)
				{
					//
				}
			} };
			ssl_ctx.init(null, // key manager
					trust_mgr, // trust manager
					new SecureRandom()); // random number generator
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				final String proxyName = "proxy.tcs.com";
				final int proxyPort = 8080;
				final SocketAddress addr = new InetSocketAddress(proxyName, proxyPort);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			else
			{
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection();
			}

			//			String encodedKey = new String(Base64.encodeBase64(this.key.getBytes()));
			//			encodedKey = encodedKey.replaceAll("\n", "");
			//			connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			/*
			 * connection.setRequestProperty("version",
			 * getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			 */
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(encodedParams);
			wr.flush();
			wr.close();

			// Read the response
			final InputStream inputStream = connection.getInputStream();
			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
			return buffer.toString();
		}
		catch (final Exception e)
		{
			//throw new AdapterException("Error with connection", e);
			LOG.error(e.getMessage(), e);
		}
		return buffer.toString();

	}



	private void disableSslVerification()
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[]
			{ new X509TrustManager()
			{
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(final X509Certificate[] certs, final String authType)
				{
					//
				}

				public void checkServerTrusted(final X509Certificate[] certs, final String authType)
				{
					//
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			final HostnameVerifier allHostsValid = new HostnameVerifier()
			{

				public boolean verify(final String hostname, final SSLSession session)
				{
					if (hostname.equals("14.140.248.13"))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}
		catch (

		final NoSuchAlgorithmException e)

		{
			LOG.error(e.getMessage(), e);
		}
		catch (

		final KeyManagementException e)

		{
			LOG.error(e.getMessage(), e);
		}

	}

	public MRupeeRefundService()
	{
		disableSslVerification();
	}

}
