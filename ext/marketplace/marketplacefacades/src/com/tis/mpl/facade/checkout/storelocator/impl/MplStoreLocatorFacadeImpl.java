/**
 *
 */
package com.tis.mpl.facade.checkout.storelocator.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.facade.checkout.storelocator.MplStoreLocatorFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.ATSResponseData;
import com.tisl.mpl.facades.data.FreebieProduct;
import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.sellerinfo.facades.MplSellerInformationFacade;


/**
 * MplStoreLocatorFacade interface impl class.
 *
 * @author TECH
 *
 *
 */
public class MplStoreLocatorFacadeImpl implements MplStoreLocatorFacade
{
	private static final Logger LOG = Logger.getLogger(MplStoreLocatorFacadeImpl.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private MplSellerInformationFacade mplSellerInformationFacade;

	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "pincodeService")
	private PincodeService pincodeService;

	@Autowired
	private MplConfigFacade mplConfigFacade;

	/**
	 * This method is to add freebie products to the map.
	 *
	 * @param parentCartEntry
	 * @return map holding ussid and qty.
	 */
	@Override
	public Map<String, Long> addFreebieProducts(final AbstractOrderEntryModel parentCartEntry)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateFreebieProducts method");
		}
		final Map<String, Long> freebieProductsWithQuant = new HashMap<String, Long>();
		if (parentCartEntry != null && CollectionUtils.isNotEmpty(parentCartEntry.getAssociatedItems()))
		{
			for (final String ussid : parentCartEntry.getAssociatedItems())
			{
				//find parent cart Entry for ussid
				final AbstractOrderEntryModel childCartEntry = getCartEntry(ussid);
				if (childCartEntry != null && childCartEntry.getGiveAway() != null && childCartEntry.getGiveAway().booleanValue())
				{
					LOG.info("Freebie Parent Product USSID" + parentCartEntry.getSelectedUSSID());
					LOG.info("Freebie Product USSID" + ussid);

					//display Freebie on store locator page, only if it has only one parent.
					if (childCartEntry.getAssociatedItems().size() == 1)
					{
						freebieProductsWithQuant.put(ussid, childCartEntry.getQuantity());
					}
				}
			}
		}
		return freebieProductsWithQuant;
	}

	/**
	 * This method is to filter only those stores which has qty more than ordered qty.
	 *
	 * @param abstractCartEntry
	 * @param storeLocationResponseData
	 */
	@Override
	public void filterStoresWithQtyGTSelectedUserQty(final AbstractOrderEntryModel abstractCartEntry,
			final StoreLocationResponseData storeLocationResponseData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from filterStoresWithQtyGTSelectedUserQty method");
		}
		final List<ATSResponseData> storesAtsGTorderedQty = new ArrayList<ATSResponseData>();
		if (null != abstractCartEntry)
		{
			final int orderQty = abstractCartEntry.getQuantity().intValue();
			//filter only those stores which has qty greater then user selected qty
			for (final ATSResponseData storeAts : storeLocationResponseData.getAts())
			{
				final int availableQty = storeAts.getQuantity();
				if (availableQty >= orderQty)
				{
					storesAtsGTorderedQty.add(storeAts);
				}
			}
		}
		//set new atsList having quantity greater than requested for each store
		storeLocationResponseData.setAts(storesAtsGTorderedQty);
	}

	/**
	 * This method populates product with stores for a ussid and oms response object storeLocationResponseData.
	 *
	 * @param ussid
	 * @param model
	 * @param freebieProductsWithQuant
	 * @param posModelList
	 * @param sellerInfoModel
	 * @return product with stores.
	 */
	@Override
	public ProudctWithPointOfServicesData populateProductWithStoresForUssid(final String ussid, final Model model,
			final Map<String, Long> freebieProductsWithQuant, final List<PointOfServiceModel> posModelList,
			final SellerInformationModel sellerInfoModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateProductWithStoresForUssid method");
		}
		List<FreebieProduct> freebieProducts = new ArrayList<FreebieProduct>();
		final ProudctWithPointOfServicesData pwPOS = new ProudctWithPointOfServicesData();
		List<PointOfServiceData> posDataList = new ArrayList<PointOfServiceData>();

		if (sellerInfoModel != null)
		{
			ProductData productData = null;
			final ProductModel productModel = sellerInfoModel.getProductSource();
			if (null != productModel)
			{
				productData = productFacade.getProductForOptions(productModel,
						Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
			}
			if (!freebieProductsWithQuant.isEmpty())
			{
				freebieProducts = populateFreebieProductsData(freebieProductsWithQuant);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Total freebie products are:::::::" + freebieProducts.size());
			}
			//set freebie product to productData
			if (null != productData && !freebieProducts.isEmpty())
			{
				productData.setFreebieProducts(freebieProducts);
			}
			//get stores from commerce from ats response
			posDataList = getStoresDataFromCommerce(posModelList);
			//BUG-ID: TISRLEE-1561 04-01-2017
			String sellerName = null;
			final List<RichAttributeModel> richAttributeModelList = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
			if (richAttributeModelList.get(0).getDeliveryFulfillModes().getCode()
					.equalsIgnoreCase(MarketplacecommerceservicesConstants.FULFILMENT_TYPE_BOTH)
					&& richAttributeModelList.get(0).getDeliveryFulfillModeByP1().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIPCODE)
					|| richAttributeModelList.get(0).getDeliveryFulfillModes().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIPCODE))
			{
				sellerName = MarketplaceFacadesConstants.TATA_CLIQ;
			}
			else
			{
				sellerName = sellerInfoModel.getSellerName();
			}
			//get qty for a product from cart
			final Long productQty = getQtyForProduct(ussid);

			pwPOS.setUssId(ussid);
			pwPOS.setSellerName(sellerName);
			pwPOS.setQuantity(productQty);
			pwPOS.setProduct(productData);
			pwPOS.setPointOfServices(posDataList);

			model.addAttribute("ussid", ussid);
		}
		return pwPOS;
	}

	/**
	 * This method saves stores for a normal product as well as freebie at store locator page.
	 *
	 * @param posModel
	 * @param ussId
	 * @return yes if successfully saved else no.
	 */
	@Override
	public String saveStoreForSelectedProduct(final PointOfServiceModel posModel, final String ussId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from saveStoreForSelectedProduct method ");
		}
		String result = MarketplaceFacadesConstants.SAVE_STORE_TOPORUDCT_SUCCESS_MSG;
		//call service to retrieve POSModel for given posName from commerce

		//get sellerInformation for ussid
		final SellerInformationModel sellerInfoModel = mplSellerInformationFacade.getSellerDetail(ussId);
		try
		{
			//find parent cartEntry based on given ussid
			final AbstractOrderEntryModel parentCartEntry = getCartEntry(ussId);
			if (null != parentCartEntry)
			{
				final String collectionDays = mplSellerInformationFacade.getSellerColloctionDays(sellerInfoModel.getSellerID());
				parentCartEntry.setCollectionDays(Integer.valueOf(collectionDays));
				//save store for normal product
				parentCartEntry.setDeliveryPointOfService(posModel);
				modelService.save(parentCartEntry);
				//save store for freebie products
				saveStoreForFreebieProducts(parentCartEntry, posModel);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while saving stores for a product at store locator page" + e.getMessage());
			LOG.error("USSID is::::::::::::" + ussId + "Seller Id is:::::::::::" + sellerInfoModel.getSellerID());
			result = MarketplaceFacadesConstants.SAVE_STORE_TOPORUDCT_FAIL_MSG;
		}
		return result;
	}

	/**
	 * This method calls commerce to get stores from ats response store id with valid inventory.
	 *
	 * @param posModelList
	 * @return list of PointOfServiceData
	 */
	private List<PointOfServiceData> getStoresDataFromCommerce(final List<PointOfServiceModel> posModelList)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getStoresDataFromCommerce method");
		}
		final List<PointOfServiceData> posDataList = new ArrayList<PointOfServiceData>();

		if (CollectionUtils.isNotEmpty(posModelList))
		{
			//populate model to data
			for (final PointOfServiceModel pointOfServiceModel : posModelList)
			{
				if (null != pointOfServiceModel)
				{
					posDataList.add(pointOfServiceConverter.convert(pointOfServiceModel));
				}
			}
		}
		return posDataList;
	}

	/**
	 * This methods populates freebie product data from map to list of freebieProduct object.
	 *
	 * @param freebieProductsWithQuant
	 * @return list of freebie product
	 */
	private List<FreebieProduct> populateFreebieProductsData(final Map<String, Long> freebieProductsWithQuant)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateFreebieProductsData method");
		}
		final List<FreebieProduct> freebieProducts = new ArrayList<FreebieProduct>();
		for (final Map.Entry<String, Long> entry : freebieProductsWithQuant.entrySet())
		{
			final FreebieProduct freebieProductData = new FreebieProduct();
			final String uss = entry.getKey();
			final Long qty = entry.getValue();
			final SellerInformationModel sellerInfo = mplSellerInformationFacade.getSellerDetail(uss);
			if (null != sellerInfo)
			{
				LOG.info("Associated Product USSID " + uss);
				final String assoicatedProductSellerName = sellerInfo.getSellerName();
				final ProductModel associateProductModel = sellerInfo.getProductSource();
				if (null != associateProductModel)
				{
					LOG.info("Associated ProductCode " + associateProductModel.getCode());
				}
				final ProductData associateProductData = productFacade.getProductForOptions(associateProductModel,
						Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
				freebieProductData.setProduct(associateProductData);
				freebieProductData.setQty(qty);
				freebieProductData.setSellerName(assoicatedProductSellerName);
				freebieProducts.add(freebieProductData);
			}
		}
		return freebieProducts;
	}

	/**
	 * Gets qty for a product from cart entry for a ussid.
	 *
	 * @param ussId
	 * @return qty for a product.
	 */
	private Long getQtyForProduct(final String ussId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateFreebieProductsData method");
		}
		final CartModel cartModel = cartService.getSessionCart();
		Long quantity = 0l;
		for (final AbstractOrderEntryModel abstractCartEntry : cartModel.getEntries())
		{
			if (null != abstractCartEntry)
			{
				if (abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId) && abstractCartEntry.getGiveAway() != null
						&& !abstractCartEntry.getGiveAway().booleanValue())
				{
					quantity = abstractCartEntry.getQuantity();
				}
			}
		}
		return quantity;
	}

	/**
	 * Saves stores for a freebie product at store locator page.
	 *
	 * @param cartEntryModel
	 * @param posModel
	 */
	private void saveStoreForFreebieProducts(final AbstractOrderEntryModel cartEntryModel, final PointOfServiceModel posModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from saveStoreForFreebieProducts method");
		}
		//get associated items for main product
		//iterate over associated items
		for (final String ussid : cartEntryModel.getAssociatedItems())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Total Associated items are::::::" + cartEntryModel.getAssociatedItems().size());
			}
			//call to save store for freebie products
			saveStoreForFreebieABgetC(ussid, cartEntryModel, posModel);
		}
	}

	/**
	 * Finds parent for freebie in case of ABgetC and saves stores for it.
	 *
	 * @param ussid
	 * @param cartEntryModel
	 * @param posModel
	 */
	private void saveStoreForFreebieABgetC(final String ussid, final AbstractOrderEntryModel cartEntryModel,
			final PointOfServiceModel posModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from saveStoreForFreebieABgetC method");
		}
		final AbstractOrderEntryModel childCartEntry = getCartEntry(ussid);
		if (null != childCartEntry)
		{
			if (childCartEntry.getGiveAway() != null && childCartEntry.getGiveAway().booleanValue())
			{
				LOG.info("Save Store for freebie product " + childCartEntry.getSelectedUSSID());
				if (cartEntryModel.getAssociatedItems().size() == 1)
				{
					childCartEntry.setDeliveryPointOfService(posModel);
				}
				else
				{
					//save store for ABgetC
					saveStoreForABgetC(childCartEntry);
				}
				modelService.save(childCartEntry);
			}
		}
	}

	/**
	 * This method finds delivery mode for freebie if it has more than one parents.
	 *
	 * @param entryModel
	 * @param abstractOrderModel
	 * @return delivery mode
	 */
	private String findParentUssId(final AbstractOrderEntryModel entryModel, final CartModel abstractOrderModel)
	{
		final Long ussIdA = getQuantity(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		final Long ussIdB = getQuantity(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		final String ussIdADelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		final String ussIdBDelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		String deliveryMode = null;
		if (ussIdA.doubleValue() < ussIdB.doubleValue())
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		return deliveryMode;
	}

	/**
	 * This method finds delivery mode for freebie.
	 *
	 * @param ussid
	 * @param abstractOrderModel
	 * @return delivery mode code
	 */
	private String getDeliverModeForABgetC(final String ussid, final CartModel abstractOrderModel)
	{
		String deliveryMode = null;
		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		{
			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			{
				deliveryMode = cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
			}
		}
		return deliveryMode;
	}

	/**
	 * Finds qty for freebie products.
	 *
	 * @param ussid
	 * @param abstractOrderModel
	 * @return
	 */
	private Long getQuantity(final String ussid, final AbstractOrderModel abstractOrderModel)
	{
		// YTODO Auto-generated method stub
		Long qty = null;
		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		{
			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			{
				qty = cartEntry.getQuantity();
				cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
			}

		}
		return qty;
	}

	/**
	 * This method finds cart entry for a ussid.
	 *
	 * @param ussId
	 * @return abstractCartEntry
	 */
	public AbstractOrderEntryModel getCartEntry(final String ussId)
	{
		final CartModel cartModel1 = cartService.getSessionCart();
		//find cartEntry for ussid
		if (cartModel1 != null)
		{
			for (final AbstractOrderEntryModel abstractCartEntry : cartModel1.getEntries())
			{
				if (null != abstractCartEntry && abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId))
				{
					return abstractCartEntry;
				}
			}
		}
		return null;
	}

	/**
	 * Saves stores for freebie ABgetC
	 *
	 * @param childCartEntry
	 */
	private void saveStoreForABgetC(final AbstractOrderEntryModel childCartEntry)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from saveStoreForABgetC method");
		}
		final CartModel cartModel = cartService.getSessionCart();
		//find parent for ABgetC
		final String freebieParentUssId = findParentUssId(childCartEntry, cartModel);
		if (null != freebieParentUssId)
		{
			//find cartEntry for parentUssid
			final AbstractOrderEntryModel parentCartEntry = getCartEntry(freebieParentUssId);
			if (null != parentCartEntry)
			{
				//find parent store
				final PointOfServiceModel freebiePosModel = parentCartEntry.getDeliveryPointOfService();
				childCartEntry.setDeliveryPointOfService(freebiePosModel);
			}

		}
	}
}
