/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.dao.MplSlaveMasterDAO;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.SellerSlaveDTO;
import com.tisl.mpl.wsdto.SlaveInfoDTO;


/**
 * @author TECHOUTS Service class which implements MPLSlaveMasterService interface.
 *
 */
public class MplSlaveMasterServiceImpl implements MplSlaveMasterService
{

	private static final Logger LOG = Logger.getLogger(MplSlaveMasterServiceImpl.class);

	private static final String coutryiso = "IN";

	@Autowired
	private MplSlaveMasterDAO mplSlaveMasterDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	/**
	 * @author TECH This method is to insert or update slaves based on slaveId.
	 *
	 * @param sellerSlaveDto
	 * @return status, if successful insert/update then return success or failure flag.
	 */
	@Override
	public String insertUpdate(final SellerSlaveDTO sellerSlaveDto)
	{
		LOG.debug("from insertUpdate SlaveMaster ServiceImpl");
		String status = MarketplacecommerceservicesConstants.SUCCESSS_RESP;
		final List<SlaveInfoDTO> list = sellerSlaveDto.getSlaveInfo();
		CountryModel countryModel = null;
		if (!list.isEmpty())
		{
			for (final SlaveInfoDTO slaveInfoDto : list)
			{
				final String slaveId = slaveInfoDto.getSlaveid();
				//call dao to check existing slave
				PointOfServiceModel posModel;
				try
				{
					posModel = mplSlaveMasterDao.checkPOSForSlave(slaveId);
				}
				catch (final EtailNonBusinessExceptions e2)
				{
					status = e2.getErrorCode();
					return status;
				}
				if (posModel != null)
				{
					if (StringUtils.isNotEmpty(slaveInfoDto.getType())
							&& slaveInfoDto.getType().equalsIgnoreCase(MarketplacewebservicesConstants.SLV_TYPE_STORE))
					{
						//update the store
						if (StringUtils.isNotEmpty(slaveInfoDto.getSellerid()))
						{
							if (!slaveInfoDto.getSellerid().equalsIgnoreCase(posModel.getSellerId()))
							{
								posModel.setSellerId(slaveInfoDto.getSellerid());
							}

						}
						//Set Image urls
						if (StringUtils.isNotBlank(slaveInfoDto.getStoreImage()))
						{
							//posModel.setMplStoreImage(slaveInfoDto.getStoreImage());
							setMplImageUrls(slaveInfoDto, posModel);
						}

						//set POS displayName to slave Name.
						if (StringUtils.isNotEmpty(slaveInfoDto.getName()))
						{
							posModel.setDisplayName(slaveInfoDto.getName());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getType()))
						{
							posModel.setType(PointOfServiceTypeEnum.STORE);
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getClicknCollect()))
						{
							if (!slaveInfoDto.getClicknCollect().equalsIgnoreCase(posModel.getClicknCollect()))
							{
								posModel.setClicknCollect(slaveInfoDto.getClicknCollect());
							}

						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLatitude()))
						{
							if (null == posModel.getLatitude())
							{
								posModel.setLatitude(new Double(Double.parseDouble(slaveInfoDto.getLatitude())));
							}
							else
							{
								if (!slaveInfoDto.getLatitude().equalsIgnoreCase(posModel.getLatitude().toString()))
								{
									posModel.setLatitude(new Double(Double.parseDouble(slaveInfoDto.getLatitude())));
								}
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLongitude()))
						{
							if (null == posModel.getLongitude())
							{
								posModel.setLongitude(new Double(Double.parseDouble(slaveInfoDto.getLongitude())));
							}
							else
							{
								if (!slaveInfoDto.getLongitude().equalsIgnoreCase(posModel.getLongitude().toString()))
								{
									posModel.setLongitude(new Double(Double.parseDouble(slaveInfoDto.getLongitude())));
								}
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getRemark()))
						{
							if (!slaveInfoDto.getRemark().equalsIgnoreCase(posModel.getRemark()))
							{
								posModel.setRemark(slaveInfoDto.getRemark());
							}

						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getIsReturnable()))
						{
							if (!slaveInfoDto.getIsReturnable().equalsIgnoreCase(posModel.getIsReturnable()))
							{
								posModel.setIsReturnable(slaveInfoDto.getIsReturnable());
							}

						}
						if (Integer.valueOf(slaveInfoDto.getOrderAcceptanceTAT()) != posModel.getOrderAcceptanceTAT())
						{
							posModel.setOrderAcceptanceTAT(Integer.valueOf(slaveInfoDto.getOrderAcceptanceTAT()));
						}
						if (Integer.valueOf(slaveInfoDto.getOrderProcessingTAT()) != posModel.getOrderProcessingTAT())
						{
							posModel.setOrderProcessingTAT(Integer.valueOf(slaveInfoDto.getOrderProcessingTAT()));
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOrderCutoffTimeHD()))
						{
							if (!slaveInfoDto.getOrderCutoffTimeHD().equalsIgnoreCase(posModel.getOrderCutoffTimeHD()))
							{
								posModel.setOrderCutoffTimeHD(slaveInfoDto.getOrderCutoffTimeHD());
							}

						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOrderCutoffTimeED()))
						{
							if (!slaveInfoDto.getOrderCutoffTimeED().equalsIgnoreCase(posModel.getOrderCutoffTimeED()))
							{
								posModel.setOrderCutoffTimeED(slaveInfoDto.getOrderCutoffTimeED());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getParkingAvailable()))
						{
							if (!slaveInfoDto.getParkingAvailable().equalsIgnoreCase(posModel.getParkingAvailable()))
							{
								posModel.setParkingAvailable(slaveInfoDto.getParkingAvailable());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLocation()))
						{
							if (!slaveInfoDto.getLocation().equalsIgnoreCase(posModel.getLocation()))
							{
								posModel.setLocation(slaveInfoDto.getLocation());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getStoreSize()))
						{
							if (!slaveInfoDto.getStoreSize().equalsIgnoreCase(posModel.getStoreSize()))
							{
								posModel.setStoreSize(slaveInfoDto.getStoreSize());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getFootFall()))
						{
							if (!slaveInfoDto.getFootFall().equalsIgnoreCase(posModel.getFootFall()))
							{
								posModel.setFootFall(slaveInfoDto.getFootFall());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getNormalRetailSalesOfStore()))
						{
							if (!slaveInfoDto.getNormalRetailSalesOfStore().equalsIgnoreCase(posModel.getNormalRetailSalesOfStore()))
							{
								posModel.setNormalRetailSalesOfStore(slaveInfoDto.getNormalRetailSalesOfStore());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOwnership()))
						{
							if (!slaveInfoDto.getOwnership().equalsIgnoreCase(posModel.getOwnerShip()))
							{
								posModel.setOwnerShip(slaveInfoDto.getOwnership());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getManagerName()))
						{
							if (!slaveInfoDto.getManagerName().equalsIgnoreCase(posModel.getManagerName()))
							{
								posModel.setManagerName(slaveInfoDto.getManagerName());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getManagerPhone()))
						{
							if (!slaveInfoDto.getManagerPhone().equalsIgnoreCase(posModel.getManagerPhone()))
							{
								posModel.setManagerPhone(slaveInfoDto.getManagerPhone());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getStoreContactNo()))
						{
							if (!slaveInfoDto.getStoreContactNo().equalsIgnoreCase(posModel.getStoreContactNumber()))
							{
								posModel.setStoreContactNumber(slaveInfoDto.getStoreContactNo());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnstoreID()))
						{
							if (!slaveInfoDto.getReturnstoreID().equalsIgnoreCase(posModel.getReturnstoreID()))
							{
								posModel.setReturnstoreID(slaveInfoDto.getReturnstoreID());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnAddress1()))
						{
							if (!slaveInfoDto.getReturnAddress1().equalsIgnoreCase(posModel.getReturnAddress1()))
							{
								posModel.setReturnAddress1(slaveInfoDto.getReturnAddress1());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnAddress2()))
						{
							if (!slaveInfoDto.getReturnAddress2().equalsIgnoreCase(posModel.getReturnAddress2()))
							{
								posModel.setReturnAddress2(slaveInfoDto.getReturnAddress2());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnState()))
						{
							if (!slaveInfoDto.getReturnState().equalsIgnoreCase(posModel.getReturnState()))
							{
								posModel.setReturnState(slaveInfoDto.getReturnState());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnCity()))
						{
							if (!slaveInfoDto.getReturnCity().equalsIgnoreCase(posModel.getReturnCity()))
							{
								posModel.setReturnCity(slaveInfoDto.getReturnCity());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnPin()))
						{
							if (!slaveInfoDto.getReturnPin().equalsIgnoreCase(posModel.getReturnPin()))
							{
								posModel.setReturnPin(slaveInfoDto.getReturnPin());
							}
						}
						posModel.setActive(MarketplacewebservicesConstants.ACTIVE);

						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail0()))
						{
							if (!slaveInfoDto.getEmail0().equalsIgnoreCase(posModel.getEmail0()))
							{
								posModel.setEmail0(slaveInfoDto.getEmail0());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail1()))
						{
							if (!slaveInfoDto.getEmail1().equalsIgnoreCase(posModel.getEmail1()))
							{
								posModel.setEmail1(slaveInfoDto.getEmail1());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail2()))
						{
							if (!slaveInfoDto.getEmail2().equalsIgnoreCase(posModel.getEmail2()))
							{
								posModel.setEmail2(slaveInfoDto.getEmail2());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail3()))
						{
							if (!slaveInfoDto.getEmail3().equalsIgnoreCase(posModel.getEmail3()))
							{
								posModel.setEmail3(slaveInfoDto.getEmail3());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail4()))
						{
							if (!slaveInfoDto.getEmail4().equalsIgnoreCase(posModel.getEmail4()))
							{
								posModel.setEmail4(slaveInfoDto.getEmail4());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail5()))
						{
							if (!slaveInfoDto.getEmail5().equalsIgnoreCase(posModel.getEmail5()))
							{
								posModel.setEmail5(slaveInfoDto.getEmail5());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail6()))
						{
							if (!slaveInfoDto.getEmail6().equalsIgnoreCase(posModel.getEmail6()))
							{
								posModel.setEmail6(slaveInfoDto.getEmail6());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail7()))
						{
							if (!slaveInfoDto.getEmail7().equalsIgnoreCase(posModel.getEmail7()))
							{
								posModel.setEmail7(slaveInfoDto.getEmail7());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail8()))
						{
							if (!slaveInfoDto.getEmail8().equalsIgnoreCase(posModel.getEmail8()))
							{
								posModel.setEmail8(slaveInfoDto.getEmail8());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail9()))
						{
							if (!slaveInfoDto.getEmail9().equalsIgnoreCase(posModel.getEmail9()))
							{
								posModel.setEmail9(slaveInfoDto.getEmail9());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo0()))
						{
							if (!slaveInfoDto.getPhoneNo0().equalsIgnoreCase(posModel.getPhoneNo0()))
							{
								posModel.setPhoneNo0(slaveInfoDto.getPhoneNo0());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo1()))
						{
							if (!slaveInfoDto.getPhoneNo1().equalsIgnoreCase(posModel.getPhoneNo1()))
							{
								posModel.setPhoneNo1(slaveInfoDto.getPhoneNo1());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo2()))
						{
							if (!slaveInfoDto.getPhoneNo2().equalsIgnoreCase(posModel.getPhoneNo2()))
							{
								posModel.setPhoneNo2(slaveInfoDto.getPhoneNo2());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo3()))
						{
							if (!slaveInfoDto.getPhoneNo3().equalsIgnoreCase(posModel.getPhoneNo3()))
							{
								posModel.setPhoneNo3(slaveInfoDto.getPhoneNo3());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo4()))
						{
							if (!slaveInfoDto.getPhoneNo4().equalsIgnoreCase(posModel.getPhoneN4()))
							{
								posModel.setPhoneN4(slaveInfoDto.getPhoneNo4());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo5()))
						{
							if (!slaveInfoDto.getPhoneNo5().equalsIgnoreCase(posModel.getPhoneNo5()))
							{
								posModel.setPhoneNo5(slaveInfoDto.getPhoneNo5());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo6()))
						{
							if (!slaveInfoDto.getPhoneNo6().equalsIgnoreCase(posModel.getPhoneNo6()))
							{
								posModel.setPhoneNo6(slaveInfoDto.getPhoneNo6());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo7()))
						{
							if (!slaveInfoDto.getPhoneNo7().equalsIgnoreCase(posModel.getPhoneNo7()))
							{
								posModel.setPhoneNo7(slaveInfoDto.getPhoneNo7());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo8()))
						{
							if (!slaveInfoDto.getPhoneNo8().equalsIgnoreCase(posModel.getPhoneNo8()))
							{
								posModel.setPhoneNo8(slaveInfoDto.getPhoneNo8());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo9()))
						{
							if (!slaveInfoDto.getPhoneNo9().equalsIgnoreCase(posModel.getPhoneNo9()))
							{
								posModel.setPhoneNo9(slaveInfoDto.getPhoneNo9());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOpeningTime()))
						{
							if (!slaveInfoDto.getOpeningTime().equalsIgnoreCase(posModel.getMplOpeningTime()))
							{
								posModel.setMplOpeningTime(slaveInfoDto.getOpeningTime());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getClosingTime()))
						{
							if (!slaveInfoDto.getClosingTime().equalsIgnoreCase(posModel.getMplClosingTime()))
							{
								posModel.setMplClosingTime(slaveInfoDto.getClosingTime());
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getWorkingDays()))
						{
							if (!slaveInfoDto.getWorkingDays().equalsIgnoreCase(posModel.getMplWorkingDays()))
							{
								posModel.setMplWorkingDays(slaveInfoDto.getWorkingDays());
							}
						}
						//check exsiting address for pos
						AddressModel addModel = posModel.getAddress();
						if (addModel != null)
						{
							if (StringUtils.isNotEmpty(slaveInfoDto.getAddress1()))
							{
								if (!slaveInfoDto.getAddress1().equalsIgnoreCase(addModel.getLine1()))
								{
									addModel.setLine1(slaveInfoDto.getAddress1());
								}
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getAddress2()))
							{
								if (!slaveInfoDto.getAddress2().equalsIgnoreCase(addModel.getLine2()))
								{
									addModel.setLine2(slaveInfoDto.getAddress2());
								}
							}
							//create CountryModel
							countryModel = addModel.getCountry();
							if (StringUtils.isNotEmpty(slaveInfoDto.getCountry()))
							{
								addModel.setCountry(countryModel);
							}

							if (StringUtils.isNotEmpty(slaveInfoDto.getState()))
							{
								if (!slaveInfoDto.getState().equalsIgnoreCase(addModel.getState()))
								{
									addModel.setState(slaveInfoDto.getState());
								}
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getCity()))
							{
								if (!slaveInfoDto.getCity().equalsIgnoreCase(addModel.getCity()))
								{
									addModel.setCity(slaveInfoDto.getCity());
								}
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getPin()))
							{
								if (!slaveInfoDto.getPin().equalsIgnoreCase(addModel.getPostalcode()))
								{
									addModel.setPostalcode(slaveInfoDto.getPin());
								}
							}
						}
						else
						{
							addModel = (AddressModel) modelService.create(AddressModel.class);
							if (StringUtils.isNotEmpty(slaveInfoDto.getAddress1()))
							{
								addModel.setLine1(slaveInfoDto.getAddress1());
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getAddress2()))
							{
								addModel.setLine2(slaveInfoDto.getAddress2());
							}
							//create CountryModel
							countryModel = commonI18NService.getCountry(coutryiso);
							if (StringUtils.isNotEmpty(slaveInfoDto.getCountry()))
							{
								addModel.setCountry(countryModel);
							}

							if (StringUtils.isNotEmpty(slaveInfoDto.getState()))
							{
								addModel.setState(slaveInfoDto.getState());
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getCity()))
							{
								addModel.setCity(slaveInfoDto.getCity());
							}
							if (StringUtils.isNotEmpty(slaveInfoDto.getPin()))
							{
								addModel.setPostalcode(slaveInfoDto.getPin());
							}
						}
						try
						{
							//create Address for Pos
							modelService.save(addModel);
							//update the POS model with address
							posModel.setAddress(addModel);
							modelService.save(posModel);
						}
						catch (final ModelSavingException e)
						{
							status = MarketplacecommerceservicesConstants.ERROR_FLAG;
							LOG.debug("Exception while saving model: " + e.getMessage());
						}
					}
					else if (StringUtils.isNotEmpty(slaveInfoDto.getType())
							&& slaveInfoDto.getType().equalsIgnoreCase(MarketplacewebservicesConstants.SLV_TYPE_WAREHOUSE))
					{
						posModel.setActive(MarketplacewebservicesConstants.INACTIVE);
						modelService.save(posModel);
					}
					else
					{
						status = MarketplacecommerceservicesConstants.ERROR_FLAG;
						LOG.debug("Update PointOfService only if input type is  " + slaveInfoDto.getType());
						LOG.debug("Make Store as Inactive if input type is " + slaveInfoDto.getType());
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(slaveInfoDto.getType())
							&& slaveInfoDto.getType().equalsIgnoreCase(MarketplacewebservicesConstants.SLV_TYPE_STORE))
					{
						posModel = (PointOfServiceModel) modelService.create(PointOfServiceModel.class);

						//first insert PointOfService data with address
						if (StringUtils.isNotEmpty(slaveInfoDto.getSellerid()))
						{
							posModel.setSellerId(slaveInfoDto.getSellerid());
						}
						//Set store images
						if (StringUtils.isNotBlank(slaveInfoDto.getStoreImage()))
						{
							//posModel.setMplStoreImage(slaveInfoDto.getStoreImage());
							setMplImageUrls(slaveInfoDto, posModel);

						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getSlaveid()))
						{
							posModel.setSlaveId(slaveInfoDto.getSlaveid());
							//As per discussion we need to store slave id to POS name.
							posModel.setName(slaveInfoDto.getSlaveid());
						}
						//set display Name.
						if (StringUtils.isNotEmpty(slaveInfoDto.getName()))
						{
							posModel.setDisplayName(slaveInfoDto.getName());
						}
						//Added logic to handle POS to connect to BaseStore.
						if (StringUtils.isNotEmpty(slaveInfoDto.getType()))
						{
							posModel.setType(PointOfServiceTypeEnum.STORE);
							//Set BaseStore Type also
							final BaseStoreModel baseStoreModel = baseStoreService.getCurrentBaseStore();
							posModel.setBaseStore(baseStoreModel);
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getClicknCollect()))
						{
							posModel.setClicknCollect(slaveInfoDto.getClicknCollect());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLatitude()))
						{
							posModel.setLatitude(new Double(Double.parseDouble(slaveInfoDto.getLatitude())));
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLongitude()))
						{
							posModel.setLongitude(new Double(Double.parseDouble(slaveInfoDto.getLongitude())));
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getRemark()))
						{
							posModel.setRemark(slaveInfoDto.getRemark());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getIsReturnable()))
						{
							posModel.setIsReturnable(slaveInfoDto.getIsReturnable());
						}
						posModel.setOrderAcceptanceTAT(Integer.valueOf(slaveInfoDto.getOrderAcceptanceTAT()));
						posModel.setOrderProcessingTAT(Integer.valueOf(slaveInfoDto.getOrderProcessingTAT()));
						if (StringUtils.isNotEmpty(slaveInfoDto.getOrderCutoffTimeHD()))
						{
							posModel.setOrderCutoffTimeHD(slaveInfoDto.getOrderCutoffTimeHD());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOrderCutoffTimeED()))
						{
							posModel.setOrderCutoffTimeED(slaveInfoDto.getOrderCutoffTimeED());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getParkingAvailable()))
						{
							posModel.setParkingAvailable(slaveInfoDto.getParkingAvailable());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getLocation()))
						{
							posModel.setLocation(slaveInfoDto.getLocation());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getStoreSize()))
						{
							posModel.setStoreSize(slaveInfoDto.getStoreSize());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getFootFall()))
						{
							posModel.setFootFall(slaveInfoDto.getFootFall());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getNormalRetailSalesOfStore()))
						{
							posModel.setNormalRetailSalesOfStore(slaveInfoDto.getNormalRetailSalesOfStore());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOwnership()))
						{
							posModel.setOwnerShip(slaveInfoDto.getOwnership());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getManagerName()))
						{
							posModel.setManagerName(slaveInfoDto.getManagerName());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getManagerPhone()))
						{
							posModel.setManagerPhone(slaveInfoDto.getManagerPhone());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getStoreContactNo()))
						{
							posModel.setStoreContactNumber(slaveInfoDto.getStoreContactNo());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnstoreID()))
						{
							posModel.setReturnstoreID(slaveInfoDto.getReturnstoreID());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnAddress1()))
						{
							posModel.setReturnAddress1(slaveInfoDto.getReturnAddress1());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnAddress2()))
						{
							posModel.setReturnAddress2(slaveInfoDto.getReturnAddress2());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnState()))
						{
							posModel.setReturnState(slaveInfoDto.getReturnState());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnCity()))
						{
							posModel.setReturnCity(slaveInfoDto.getReturnCity());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getReturnPin()))
						{
							posModel.setReturnPin(slaveInfoDto.getReturnPin());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getActive()))
						{
							posModel.setActive(slaveInfoDto.getActive());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail0()))
						{
							posModel.setEmail0(slaveInfoDto.getEmail0());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail1()))
						{
							posModel.setEmail1(slaveInfoDto.getEmail1());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail2()))
						{
							posModel.setEmail2(slaveInfoDto.getEmail2());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail3()))
						{
							posModel.setEmail3(slaveInfoDto.getEmail3());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail4()))
						{
							posModel.setEmail4(slaveInfoDto.getEmail4());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail5()))
						{
							posModel.setEmail5(slaveInfoDto.getEmail5());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail6()))
						{
							posModel.setEmail6(slaveInfoDto.getEmail6());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail7()))
						{
							posModel.setEmail7(slaveInfoDto.getEmail7());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail8()))
						{
							posModel.setEmail8(slaveInfoDto.getEmail8());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getEmail9()))
						{
							posModel.setEmail9(slaveInfoDto.getEmail9());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo0()))
						{
							posModel.setPhoneNo0(slaveInfoDto.getPhoneNo0());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo1()))
						{
							posModel.setPhoneNo1(slaveInfoDto.getPhoneNo1());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo2()))
						{
							posModel.setPhoneNo2(slaveInfoDto.getPhoneNo2());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo3()))
						{
							posModel.setPhoneNo3(slaveInfoDto.getPhoneNo3());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo4()))
						{
							posModel.setPhoneN4(slaveInfoDto.getPhoneNo4());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo5()))
						{
							posModel.setPhoneNo5(slaveInfoDto.getPhoneNo5());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo6()))
						{
							posModel.setPhoneNo6(slaveInfoDto.getPhoneNo6());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo7()))
						{
							posModel.setPhoneNo7(slaveInfoDto.getPhoneNo7());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo8()))
						{
							posModel.setPhoneNo8(slaveInfoDto.getPhoneNo8());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPhoneNo9()))
						{
							posModel.setPhoneNo9(slaveInfoDto.getPhoneNo9());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getOpeningTime()))
						{
							posModel.setMplOpeningTime(slaveInfoDto.getOpeningTime());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getClosingTime()))
						{
							posModel.setMplClosingTime(slaveInfoDto.getClosingTime());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getWorkingDays()))
						{
							posModel.setMplWorkingDays(slaveInfoDto.getWorkingDays());
						}
						//create Address for Pos
						final AddressModel addModel = (AddressModel) modelService.create(AddressModel.class);
						if (StringUtils.isNotEmpty(slaveInfoDto.getAddress1()))
						{
							addModel.setLine1(slaveInfoDto.getAddress1());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getAddress2()))
						{
							addModel.setLine2(slaveInfoDto.getAddress2());
						}
						//create CountryModel
						countryModel = commonI18NService.getCountry(coutryiso);
						if (countryModel != null)
						{
							if (StringUtils.isNotEmpty(slaveInfoDto.getCountry()))
							{
								addModel.setCountry(countryModel);
							}
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getState()))
						{
							addModel.setState(slaveInfoDto.getState());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getCity()))
						{
							addModel.setCity(slaveInfoDto.getCity());
						}
						if (StringUtils.isNotEmpty(slaveInfoDto.getPin()))
						{
							addModel.setPostalcode(slaveInfoDto.getPin());
						}
						addModel.setOwner(posModel);
						//save address for pos
						try
						{
							modelService.save(addModel);
							//update the POS model with address
							posModel.setAddress(addModel);
							modelService.save(posModel);
						}
						catch (final ModelSavingException e)
						{
							status = MarketplacecommerceservicesConstants.ERROR_FLAG;
							LOG.debug("Exception while saving model: " + e.getMessage());
						}
					}
					else
					{
						status = MarketplacecommerceservicesConstants.ERROR_FLAG;
						LOG.debug("PointOfService type only supports " + slaveInfoDto.getType());
					}
				}

			}
		}
		return status;
	}

	/**
	 * Set Mpl image urls.
	 *
	 * @param slaveInfoDto
	 * @param posModel
	 *           POS model.
	 */
	private void setMplImageUrls(final SlaveInfoDTO slaveInfoDto, final PointOfServiceModel posModel)
	{
		final String[] imgArr = slaveInfoDto.getStoreImage().split("#");
		final List<String> mplUrls = new ArrayList<>();
		for (final String imgUrl : imgArr)
		{
			if (StringUtils.isNotBlank(imgUrl))
			{
				mplUrls.add(imgUrl);
			}
		}

		if (CollectionUtils.isNotEmpty(mplUrls))
		{
			posModel.setMplImageUrls(mplUrls);
		}
	}

	/**
	 * @author TECH This method calls cao to get POS ,given sellerId and Store Name.
	 * @param sellerId
	 * @param storeName
	 *
	 * @return pos model.
	 */
	@Override
	public PointOfServiceModel findPOSBySellerAndSlave(final String sellerId, final String storeName)
	{
		LOG.debug("in service of findPOSBySellerAndSlave  ");
		try
		{
			return mplSlaveMasterDao.findPOSForSellerAndSlave(sellerId, storeName);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception when retriving POS with sellerId and slaveId" + e.getMessage());
			return null;
		}
	}

	/**
	 * @author TECH This method calls dao to get POS for given posName.
	 * @param posName
	 * @return pos model
	 */
	@Override
	public PointOfServiceModel findPOSByName(final String posName)
	{
		LOG.debug("from findPOSByName method in service");
		//call dao to get pos model
		try
		{
			return mplSlaveMasterDao.findPOSForStoreName(posName);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception when retriving POS with sellerId and slaveId" + e.getMessage());
			return null;
		}
	}
}
