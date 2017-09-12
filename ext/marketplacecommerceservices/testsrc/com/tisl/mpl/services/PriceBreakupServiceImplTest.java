/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewelleryPriceRowModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.OrderJewelEntryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.data.PriceBreakupData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;
import com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.PriceBreakupServiceImpl;





/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class PriceBreakupServiceImplTest
{
	private static final Logger LOG = Logger.getLogger(PriceBreakupServiceImpl.class);
	private PriceBreakupServiceImpl priceBreakupServiceImpl;
	private ConfigurationService configurationService;
	private CommonI18NService commonI18NService;
	private ModelService modelService;
	private PriceBreakupDao priceBreakupDao;
	private MplProductDao productDao;
	private Converter<ProductModel, ProductData> productConverter;
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	private MplJewelleryDao mplJewelleryDao;
	//	private List<JewelleryPriceRowModel> jPrice;
	//	private List<JewellerySellerDetailsModel> jSellerDetails;
	//	private List<JewelleryInformationModel> jewelleryInfo;
	private PriceBreakupData priceBreakupDataG;
	private PriceBreakupData priceBreakupDataS;
	private PriceBreakupData priceBreakupDataP;
	private PriceBreakupData priceBreakupDataSol;
	private PriceBreakupData priceBreakupDataD;
	private PriceBreakupData priceBreakupDataGem;
	private PriceBreakupData priceBreakupDataM;
	private PriceBreakupData priceBreakupDataW;
	private PriceData price;
	private CurrencyModel currency;
	private AbstractOrderEntryModel entry;
	private AbstractOrderEntryModel childOrderEntry;
	private OrderJewelEntryModel orderJewelEntryModel;
	private ProductModel productModel;
	private ProductData productData;
	private Configuration configuration;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.priceBreakupServiceImpl = new PriceBreakupServiceImpl();
		this.configurationService = Mockito.mock(ConfigurationService.class);
		this.commonI18NService = Mockito.mock(CommonI18NService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.priceBreakupDao = Mockito.mock(PriceBreakupDao.class);
		this.productDao = Mockito.mock(MplProductDao.class);
		this.productConverter = Mockito.mock(Converter.class);
		this.productConfiguredPopulator = Mockito.mock(ConfigurablePopulator.class);
		this.mplJewelleryDao = Mockito.mock(MplJewelleryDao.class);
		this.priceBreakupServiceImpl.setConfigurationService(configurationService);
		this.priceBreakupServiceImpl.setPriceBreakupDao(priceBreakupDao);
		this.price = Mockito.mock(PriceData.class);
		this.priceBreakupDataG = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataS = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataP = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataSol = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataD = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataGem = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataM = Mockito.mock(PriceBreakupData.class);
		this.priceBreakupDataW = Mockito.mock(PriceBreakupData.class);
		this.currency = Mockito.mock(CurrencyModel.class);
		this.entry = Mockito.mock(AbstractOrderEntryModel.class);
		this.childOrderEntry = Mockito.mock(AbstractOrderEntryModel.class);
		this.orderJewelEntryModel = Mockito.mock(OrderJewelEntryModel.class);
		this.productModel = Mockito.mock(ProductModel.class);
		this.productData = Mockito.mock(ProductData.class);
		this.configuration = Mockito.mock(Configuration.class);
	}

	@Test
	public void testGetPricebreakup()
	{
		final List<PriceBreakupData> priceBreakupDataList = new ArrayList<>();
		final List<JewelleryPriceRowModel> jPrice = new ArrayList<>();
		final List<JewellerySellerDetailsModel> jSellerDetails = new ArrayList<>();
		final List<JewelleryInformationModel> jewelleryInfo = new ArrayList<>();
		final List<String> weightRateList = new ArrayList<String>();
		Mockito.when(priceBreakupDao.getPricebreakup("123661Tanishq42000600411")).thenReturn(jPrice);
		Mockito.when(priceBreakupDao.getJewelInfo("123661Tanishq42000600411")).thenReturn(jewelleryInfo);
		Mockito.when(mplJewelleryDao.getSellerMsgForRetRefTab("123661")).thenReturn(jSellerDetails);

		Mockito.when(priceBreakupDataG.getName()).thenReturn("GOLD");
		Mockito.when(priceBreakupDataS.getName()).thenReturn("SILVER");
		Mockito.when(priceBreakupDataP.getName()).thenReturn("PLATINUM");
		Mockito.when(priceBreakupDataSol.getName()).thenReturn("SOLITIRE");
		Mockito.when(priceBreakupDataD.getName()).thenReturn("DIAMOND");
		Mockito.when(priceBreakupDataGem.getName()).thenReturn("GEM STONE");
		Mockito.when(priceBreakupDataM.getName()).thenReturn("MAKING CHARGE");
		Mockito.when(priceBreakupDataW.getName()).thenReturn("WASTAGE TAX");

		final String keyG = "12gm @ ₹2200.00/10g";
		weightRateList.add(keyG);
		Mockito.when(priceBreakupDataG.getWeightRateList()).thenReturn(weightRateList);
		Mockito.when(priceBreakupDataG.getPrice()).thenReturn(price);

		final String keyS = "11gm @ ₹2201.00/g";
		weightRateList.add(keyS);
		Mockito.when(priceBreakupDataS.getWeightRateList()).thenReturn(weightRateList);
		Mockito.when(priceBreakupDataS.getPrice()).thenReturn(price);

		final String keyP = "10gm @ ₹2202.00/g";
		weightRateList.add(keyP);
		Mockito.when(priceBreakupDataP.getWeightRateList()).thenReturn(weightRateList);
		Mockito.when(priceBreakupDataP.getPrice()).thenReturn(price);

		final String keySol = "9ct @ ₹2203.00/ct";
		weightRateList.add(keySol);
		Mockito.when(priceBreakupDataSol.getWeightRateList()).thenReturn(weightRateList);
		Mockito.when(priceBreakupDataSol.getPrice()).thenReturn(price);

		final String keyD = "8ct @ ₹22035.00/ct";
		weightRateList.add(keyD);
		Mockito.when(priceBreakupDataD.getWeightRateList()).thenReturn(weightRateList);
		Mockito.when(priceBreakupDataD.getPrice()).thenReturn(price);

		Mockito.when(priceBreakupDataGem.getPrice()).thenReturn(price);
		Mockito.when(priceBreakupDataM.getPrice()).thenReturn(price);
		Mockito.when(priceBreakupDataW.getPrice()).thenReturn(price);

		priceBreakupDataList.add(priceBreakupDataG);
		priceBreakupDataList.add(priceBreakupDataS);
		priceBreakupDataList.add(priceBreakupDataP);
		priceBreakupDataList.add(priceBreakupDataSol);
		priceBreakupDataList.add(priceBreakupDataD);
		priceBreakupDataList.add(priceBreakupDataGem);
		priceBreakupDataList.add(priceBreakupDataM);
		priceBreakupDataList.add(priceBreakupDataW);
	}

	@Test
	public void testCreatePriceSign()
	{
		final BigDecimal value = new BigDecimal(22035D);
		Mockito.when(price.getPriceType()).thenReturn(PriceDataType.BUY);
		Mockito.when(price.getValue()).thenReturn(value);
		Mockito.when(price.getDoubleValue()).thenReturn(Double.valueOf(22035D));
		Mockito.when(price.getCurrencyIso()).thenReturn("INR");
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString("site.decimal.format")).thenReturn("0.00");
		final DecimalFormat df = new DecimalFormat("0.00");
		final String totalPriceFormatted = df.format(value);
		StringBuilder stb = new StringBuilder(20);
		stb = stb.append("₹").append(totalPriceFormatted);
		Mockito.when(price.getFormattedValue()).thenReturn("₹22035.00");
	}

	@Test
	public void testCreatePricebreakupOrder()
	{
		final List<JewelleryPriceRowModel> jPrice = new ArrayList<>();
		Mockito.when(modelService.create(OrderJewelEntryModel.class)).thenReturn(orderJewelEntryModel);
		Mockito.when(priceBreakupDao.getPricebreakup("ussid")).thenReturn(jPrice);
		Mockito.when(orderJewelEntryModel.getDiamondRateType1()).thenReturn(Double.valueOf(12000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType2()).thenReturn(Double.valueOf(13000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType3()).thenReturn(Double.valueOf(14000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType4()).thenReturn(Double.valueOf(15000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType5()).thenReturn(Double.valueOf(16000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType6()).thenReturn(Double.valueOf(17000D));
		Mockito.when(orderJewelEntryModel.getDiamondRateType7()).thenReturn(Double.valueOf(18000D));
		Mockito.when(orderJewelEntryModel.getDiamondValue()).thenReturn(Double.valueOf(19000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType1()).thenReturn(Double.valueOf(10000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType2()).thenReturn(Double.valueOf(11000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType3()).thenReturn(Double.valueOf(12000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType4()).thenReturn(Double.valueOf(13000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType5()).thenReturn(Double.valueOf(14000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType6()).thenReturn(Double.valueOf(15000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType7()).thenReturn(Double.valueOf(16000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType8()).thenReturn(Double.valueOf(17000D));
		Mockito.when(orderJewelEntryModel.getStoneRateType9()).thenReturn(Double.valueOf(18000D));
		Mockito.when(orderJewelEntryModel.getStoneValue()).thenReturn(Double.valueOf(19000D));
		Mockito.when(orderJewelEntryModel.getMakingCharge()).thenReturn(Double.valueOf(10000D));
		Mockito.when(orderJewelEntryModel.getWastageTax()).thenReturn(Double.valueOf(11000D));
		Mockito.when(orderJewelEntryModel.getMetalName()).thenReturn("SOLITIRE");
		Mockito.when(orderJewelEntryModel.getMetalRate()).thenReturn(Double.valueOf(12000D));
		Mockito.when(orderJewelEntryModel.getMetalValue()).thenReturn(Double.valueOf(13000D));
		Mockito.when(orderJewelEntryModel.getSolitaireValue()).thenReturn(Double.valueOf(14000D));
		Mockito.when(orderJewelEntryModel.getSolitaireRate()).thenReturn(Double.valueOf(15000D));

		Mockito.when(productDao.findProductData("MP000000111087041")).thenReturn(productModel);
		Mockito.when(productConverter.convert(productModel)).thenReturn(productData);
		Mockito.doNothing().when(productConfiguredPopulator)
				.populate(productModel, productData, Arrays.asList(ProductOption.BASIC, ProductOption.CLASSIFICATION));
		Mockito.when(orderJewelEntryModel.getPurity()).thenReturn("goldpurityfinejwlry20k833");
		Mockito.when(orderJewelEntryModel.getStoneType1()).thenReturn("stone1");
		Mockito.when(orderJewelEntryModel.getStoneSizeType1()).thenReturn("stonesize1");
		Mockito.when(orderJewelEntryModel.getStoneType2()).thenReturn("stone2");
		Mockito.when(orderJewelEntryModel.getStoneSizeType2()).thenReturn("stonesize2");
		Mockito.when(orderJewelEntryModel.getStoneType3()).thenReturn("stone3");
		Mockito.when(orderJewelEntryModel.getStoneSizeType3()).thenReturn("stonesize3");
		Mockito.when(orderJewelEntryModel.getStoneType4()).thenReturn("stone4");
		Mockito.when(orderJewelEntryModel.getStoneSizeType4()).thenReturn("stonesize4");
		Mockito.when(orderJewelEntryModel.getStoneType5()).thenReturn("stone5");
		Mockito.when(orderJewelEntryModel.getStoneSizeType5()).thenReturn("stonesize5");
		Mockito.when(orderJewelEntryModel.getStoneType6()).thenReturn("stone6");
		Mockito.when(orderJewelEntryModel.getStoneSizeType6()).thenReturn("stonesize6");
		Mockito.when(orderJewelEntryModel.getStoneType7()).thenReturn("stone7");
		Mockito.when(orderJewelEntryModel.getStoneSizeType7()).thenReturn("stonesize7");
		Mockito.when(orderJewelEntryModel.getStoneType8()).thenReturn("stone8");
		Mockito.when(orderJewelEntryModel.getStoneSizeType8()).thenReturn("stonesize8");
		Mockito.when(orderJewelEntryModel.getStoneType9()).thenReturn("stone9");
		Mockito.when(orderJewelEntryModel.getStoneSizeType9()).thenReturn("stonesize9");
		Mockito.when(orderJewelEntryModel.getStoneType10()).thenReturn("stone10");
		Mockito.when(orderJewelEntryModel.getStoneSizeType10()).thenReturn("stonesize10");
		Mockito.when(orderJewelEntryModel.getDiamondColorType1()).thenReturn("COLOR1");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType1()).thenReturn("CLARITY1");
		Mockito.when(orderJewelEntryModel.getDiamondColorType2()).thenReturn("COLOR2");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType2()).thenReturn("CLARITY2");
		Mockito.when(orderJewelEntryModel.getDiamondColorType3()).thenReturn("COLOR3");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType3()).thenReturn("CLARITY3");
		Mockito.when(orderJewelEntryModel.getDiamondColorType4()).thenReturn("COLOR4");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType4()).thenReturn("CLARITY4");
		Mockito.when(orderJewelEntryModel.getDiamondColorType5()).thenReturn("COLOR5");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType5()).thenReturn("CLARITY5");
		Mockito.when(orderJewelEntryModel.getDiamondColorType6()).thenReturn("COLOR6");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType6()).thenReturn("CLARITY6");
		Mockito.when(orderJewelEntryModel.getDiamondColorType7()).thenReturn("COLOR7");
		Mockito.when(orderJewelEntryModel.getDiamondClarityType7()).thenReturn("CLARITY7");
		Mockito.when(orderJewelEntryModel.getAbstractOrderEntryjewel()).thenReturn(entry);
		Mockito.doNothing().when(modelService).saveAll(orderJewelEntryModel, entry);
	}
}
