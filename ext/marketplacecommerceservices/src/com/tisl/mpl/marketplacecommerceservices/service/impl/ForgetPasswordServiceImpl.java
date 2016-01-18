/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.security.impl.DefaultSecureTokenService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ForgetPasswordDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;


/**
 * @author 682160
 *
 */
public class ForgetPasswordServiceImpl extends DefaultSecureTokenService implements ForgetPasswordService

{
	@Autowired
	private ForgetPasswordDao forgetPasswordDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ExtendedUserService userService;
	@Autowired
	private SecureTokenService secureTokenService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private ConfigurationService configurationService;

	private long tokenValiditySeconds;
	private static final String PASSWORDENCODING = "default.password.encoder.key";
	private static final String TOKENENCODING = "default.token.encoder.key";
	private static final Logger LOG = Logger.getLogger(ForgetPasswordServiceImpl.class);
	private byte[] signatureKeyBytes;
	private byte[] encryptionKeyBytes;

	/**
	 * @return the forgetPasswordDao
	 */
	public ForgetPasswordDao getForgetPasswordDao()
	{
		return forgetPasswordDao;
	}

	public void setForgetPasswordDao(final ForgetPasswordDao forgetPasswordDao)
	{
		this.forgetPasswordDao = forgetPasswordDao;
	}

	@Override
	public List<CustomerModel> getCustomer(final String email)
	{
		return forgetPasswordDao.findCustomer(email);
	}

	protected long getTokenValiditySeconds()
	{
		return this.tokenValiditySeconds;
	}

	@Override
	public void afterPropertiesSet() throws DecoderException, NoSuchAlgorithmException
	{
		this.signatureKeyBytes = decodeHexString(getSignatureKeyHex());
		this.encryptionKeyBytes = decodeHexString(getEncryptionKeyHex());

	}


	@Override
	protected byte[] getSignatureKeyBytes()
	{
		return this.signatureKeyBytes;
	}

	@Override
	protected byte[] getEncryptionKeyBytes()
	{
		return this.encryptionKeyBytes;
	}

	/**
	 * @description method is called to set the TokenValiditySeconds of the Password
	 * @param tokenValiditySeconds
	 */
	@Required
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0L)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0014);
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	/**
	 * @description method is called to change the Password of the customer through Email
	 */
	@Override
	public void forgottenPasswordEmail(final CustomerModel customerModel, final String securePasswordUrl)
	{
		try
		{
			ServicesUtil.validateParameterNotNullStandardMessage(MplConstants.CUSTOMERMODEL, customerModel);
			final long timeStamp = (getTokenValiditySeconds() > 0L) ? new Date().getTime() : 0L;
			final SecureToken data = new SecureToken(customerModel.getOriginalUid(), timeStamp);
			//final String token = secureTokenService.encryptData(data);	SONAR Fix
			final String token = getSecureTokenService().encryptData(data);
			customerModel.setToken(token);
			getModelService().save(customerModel);

			//final ForgottenPasswordProcessModel forgottenPasswordProcessModel = (ForgottenPasswordProcessModel) businessProcessService	SONAR Fix
			final ForgottenPasswordProcessModel forgottenPasswordProcessModel = (ForgottenPasswordProcessModel) getBusinessProcessService()
					.createProcess(
							MplConstants.FORGOTTEN_PASSWORD + customerModel.getUid() + MplConstants.HYPHEN + System.currentTimeMillis(),
							MplConstants.FORGOTTEN_PASSWORD_EMAIL_PROCESS);
			//forgottenPasswordProcessModel.setSite(baseSiteService.getCurrentBaseSite());	SONAR Fix
			forgottenPasswordProcessModel.setSite(getBaseSiteService().getCurrentBaseSite());
			forgottenPasswordProcessModel.setCustomer(customerModel);
			forgottenPasswordProcessModel.setToken(customerModel.getToken());
			//forgottenPasswordProcessModel.setLanguage(commonI18NService.getCurrentLanguage());	SONAR Fix
			//forgottenPasswordProcessModel.setCurrency(commonI18NService.getCurrentCurrency());	SONAR Fix
			//forgottenPasswordProcessModel.setStore(baseStoreService.getCurrentBaseStore());	SONAR Fix
			forgottenPasswordProcessModel.setLanguage(getCommonI18NService().getCurrentLanguage());
			forgottenPasswordProcessModel.setCurrency(getCommonI18NService().getCurrentCurrency());
			forgottenPasswordProcessModel.setStore(getBaseStoreService().getCurrentBaseStore());
			forgottenPasswordProcessModel.setForgetPasswordUrl(securePasswordUrl);

			//if (baseStoreService.getCurrentBaseStore() != null)	SONAR Fix
			if (getBaseStoreService().getCurrentBaseStore() != null)
			{
				//forgottenPasswordProcessModel.setStore(baseStoreService.getCurrentBaseStore());	SONAR Fix
				forgottenPasswordProcessModel.setStore(getBaseStoreService().getCurrentBaseStore());
			}
			getModelService().save(forgottenPasswordProcessModel);
			//businessProcessService.startProcess(forgottenPasswordProcessModel);	SONAR Fix
			getBusinessProcessService().startProcess(forgottenPasswordProcessModel);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to initialize the Event
	 * @param event
	 * @param customerModel
	 * @return AbstractCommerceUserEvent
	 */
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		try
		{
			//final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();	SONAR Fix
			final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
			if (currentBaseStore != null)
			{
				event.setBaseStore(currentBaseStore);
			}
			//event.setSite(baseSiteService.getCurrentBaseSite());	SONAR Fix
			event.setSite(getBaseSiteService().getCurrentBaseSite());
			event.setCustomer(customerModel);
			//event.setLanguage(commonI18NService.getCurrentLanguage());	SONAR Fix
			//event.setCurrency(commonI18NService.getCurrentCurrency());	SONAR Fix
			event.setLanguage(getCommonI18NService().getCurrentLanguage());
			event.setCurrency(getCommonI18NService().getCurrentCurrency());
			return event;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to retrieve token
	 */
	@Override
	public String getOriginalToken(String tokenFromURL, final CustomerModel customerModel)
	{
		try
		{
			ServicesUtil.validateParameterNotNullStandardMessage(MplConstants.CUSTOMERMODEL, customerModel);
			final String originalToken = customerModel.getToken();
			tokenFromURL = originalToken;
			return tokenFromURL;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to update the Password
	 */
	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		try
		{
			Assert.hasText(token, MarketplacecommerceservicesConstants.TOKEN_CANNOT_BE_EMPTY);
			Assert.hasText(newPassword, MarketplacecommerceservicesConstants.NEWPASSWORD_CANNOT_BE_EMPTY);

			final SecureToken data = decryptData(token);
			if (getTokenValiditySeconds() > 0L)
			{
				final long delta = new Date().getTime() - data.getTimeStamp();
				if (delta / 1000L > getTokenValiditySeconds())
				{
					throw new IllegalArgumentException(MarketplacecommerceservicesConstants.B0013);
				}
			}

			final CustomerModel customer = getUserService().getUserForUid(data.getData());
			if (customer == null)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0012);
			}
			if (!(token.equals(customer.getToken())))
			{
				throw new TokenInvalidatedException();
			}
			customer.setToken(null);
			customer.setLoginDisabled(false);
			getModelService().save(customer);
			getUserService().setPassword(data.getData(), newPassword,
					configurationService.getConfiguration().getString(PASSWORDENCODING));
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0010);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to decrypt the token for the particular user
	 */
	@Override
	public SecureToken decryptData(final String token)
	{
		if ((token == null) || (StringUtils.isBlank(token)))
		{
			throw new IllegalArgumentException("missing token");
		}
		try
		{
			final byte[] decryptedBytes = decrypt(token, getEncryptionKeyBytes());

			final int decryptedBytesDataLength = decryptedBytes.length - 16;

			if (!(validateSignature(decryptedBytes, 0, decryptedBytesDataLength, decryptedBytes, decryptedBytesDataLength,
					getSignatureKeyBytes())))
			{
				throw new IllegalArgumentException("Invalid signature in cookie");
			}
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes, 0, decryptedBytesDataLength);
			final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

			skipPadding(dataInputStream);

			final String userIdentifier = dataInputStream.readUTF();
			final String userChecksum = dataInputStream.readUTF();
			if ((userChecksum == null) || (!(userChecksum.equals(createChecksum(userIdentifier)))))
			{
				throw new IllegalArgumentException("invalid token");
			}
			final long timeStampInSeconds = dataInputStream.readLong();

			return new SecureToken(userIdentifier, timeStampInSeconds);
		}
		catch (final IOException e)
		{
			LOG.error("Could not decrypt token", e);
			throw new SystemException(e.toString(), e);
		}
		catch (final GeneralSecurityException e)
		{
			LOG.warn("Could not decrypt token: " + e.toString());
			throw new IllegalArgumentException("Invalid token", e);
		}
	}

	/**
	 * @description method is called to decrypt the token for the particular user as per URL in the email
	 */
	@Override
	protected byte[] decrypt(final String encryptedText, final byte[] encryptionKeyBytes) throws GeneralSecurityException
	{
		final byte[] encryptedBytes = Base64.decodeBase64(encryptedText.getBytes());

		if ((encryptedBytes == null) || (encryptedBytes.length < 16))
		{
			throw new IllegalArgumentException("Encrypted data too short");
		}

		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		final IvParameterSpec ivSpec = new IvParameterSpec(encryptedBytes, 0, 16);
		cipher.init(2, buildSecretKey(encryptionKeyBytes), ivSpec);

		return cipher.doFinal(encryptedBytes, 16, encryptedBytes.length - 16);
	}

	/**
	 * @description method as a part of token decryption
	 */
	@Override
	protected boolean validateSignature(final byte[] dataBytes, final int dataOffset, final int dataLength,
			final byte[] signatureBytes, final int signatureOffset, final byte[] signatureKeyBytes) throws NoSuchAlgorithmException
	{
		final byte[] computedSig = generateSignature(dataBytes, dataOffset, dataLength, signatureKeyBytes);
		return arrayEquals(signatureBytes, signatureOffset, computedSig, 0, 16);
	}

	/**
	 * @description method as a part of token decryption
	 */
	@Override
	protected boolean arrayEquals(final byte[] array1, final int offset1, final byte[] array2, final int offset2, final int length)
	{
		boolean arrayFlag = true;
		if ((array1 == null) || (array2 == null) || (array1.length - offset1 < length) || (array2.length - offset2 < length))
		{
			//return false;	SONAR Fix
			arrayFlag = false;
		}

		for (int i = 0; i < length; ++i)
		{
			if (array1[(offset1 + i)] != array2[(offset2 + i)])
			{
				//return false;	SONAR Fix
				arrayFlag = false;
			}
		}

		return arrayFlag;
	}

	/**
	 * @description method as a part of token decryption
	 */
	@Override
	protected byte[] generateSignature(final byte[] data, final int offset, final int length, final byte[] signatureKeyBytes)
			throws NoSuchAlgorithmException
	{
		final MessageDigest md5Digest = MessageDigest.getInstance(configurationService.getConfiguration().getString(TOKENENCODING));

		md5Digest.update(signatureKeyBytes);

		md5Digest.update(data, offset, length);

		final byte[] md5SigBytes = md5Digest.digest();
		if (md5SigBytes.length != 16)
		{
			throw new IllegalArgumentException("MD5 Signature incorrect length [" + md5SigBytes.length + "]");
		}

		return md5SigBytes;
	}

	/**
	 * @description method as a part of token decryption and token padding
	 */
	@Override
	protected void skipPadding(final DataInputStream dataInputStream) throws IOException
	{
		final int firstByte = dataInputStream.readUnsignedByte();

		final int length = firstByte & 0x7;

		for (int i = 0; i < length; ++i)
		{
			dataInputStream.readByte();
		}
	}

	/**
	 * @description method is called to create SMS for forgotten Password
	 */
	@Override
	public String forgottenPasswordSMS(final CustomerModel customerModel)
	{
		try
		{
			ServicesUtil.validateParameterNotNullStandardMessage(MplConstants.CUSTOMERMODEL, customerModel);
			final long timeStamp = (getTokenValiditySeconds() > 0L) ? new Date().getTime() : 0L;
			final SecureToken data = new SecureToken(customerModel.getOriginalUid(), timeStamp);
			//final String token = secureTokenService.encryptData(data);	SONAR Fix
			final String token = getSecureTokenService().encryptData(data);
			customerModel.setToken(token);
			getModelService().save(customerModel);
			return token;
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the userService
	 */
	public ExtendedUserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final ExtendedUserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the secureTokenService
	 */
	public SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	/**
	 * @param secureTokenService
	 *           the secureTokenService to set
	 */
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @param signatureKeyBytes
	 *           the signatureKeyBytes to set
	 */
	public void setSignatureKeyBytes(final byte[] signatureKeyBytes)
	{
		this.signatureKeyBytes = signatureKeyBytes;
	}

	/**
	 * @param encryptionKeyBytes
	 *           the encryptionKeyBytes to set
	 */
	public void setEncryptionKeyBytes(final byte[] encryptionKeyBytes)
	{
		this.encryptionKeyBytes = encryptionKeyBytes;
	}




}
