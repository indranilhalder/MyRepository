/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;


/**
 * @author TCS
 *
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class BuyBoxServiceImplTest
{
	@Resource
	private BuyBoxDao buyBoxDao;

	private final BuyBoxServiceImpl serviceImpl = new BuyBoxServiceImpl();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testbuyboxPrice()
	{
		//TISSEC-50
		final List<BuyBoxModel> buyBoxList = new ArrayList<BuyBoxModel>();
		final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter productCode
		assertNotNull(buyBoxDao.buyBoxPrice(productCode));
		assertFalse(buyBoxDao.buyBoxPrice(productCode).isEmpty());
		assertEquals(buyBoxList, buyBoxDao.buyBoxPrice(productCode));
	}

	@Test
	public void testBuyboxPricesForSearch()
	{
		final List<BuyBoxModel> buyBoxList = new ArrayList<BuyBoxModel>();
		final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product code
		assertNotNull(buyBoxDao.getBuyboxPricesForSearch(productCode));
		assertFalse(buyBoxDao.getBuyboxPricesForSearch(productCode).isEmpty());
		assertEquals(buyBoxList, buyBoxDao.getBuyboxPricesForSearch(productCode));
	}

	@Test
	public void testBuyboxInventoryForSearch()
	{
		final List<BuyBoxModel> buyBoxList = new ArrayList<BuyBoxModel>();
		final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product code
		assertNotNull(buyBoxDao.getBuyboxAvailableInventoryForSearch(productCode, "variant"));
		assertNotNull(buyBoxDao.getBuyboxAvailableInventoryForSearch(productCode, "variant"));
		assertEquals(buyBoxList, buyBoxDao.getBuyboxAvailableInventoryForSearch(productCode, "variant"));
	}

	@Test
	public void testInvalidatePkofBuybox() throws Exception
	{
		final List<BuyBoxModel> buyBoxList = new ArrayList<BuyBoxModel>();
		final String dateStr = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter date
		final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		final Date currenttime = formatter.parse(dateStr);
		assertNotNull(buyBoxDao.invalidatePkofBuybox(currenttime));
		assertFalse(buyBoxDao.invalidatePkofBuybox(currenttime).isEmpty());
		assertEquals(buyBoxList, buyBoxDao.invalidatePkofBuybox(currenttime));
	}

	@Test
	public void testBuyBoxPriceNoStock()
	{
		final List<BuyBoxModel> buyBoxList = new ArrayList<BuyBoxModel>();
		final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product code
		assertNotNull(buyBoxDao.buyBoxPriceNoStock(productCode));
		assertFalse(buyBoxDao.buyBoxPriceNoStock(productCode).isEmpty());
		assertEquals(buyBoxList, buyBoxDao.buyBoxPriceNoStock(productCode));
	}

	@Test
	public void testRichAttributeData()
	{
		final RichAttributeModel richAttributeModel = new RichAttributeModel();
		final String ussid = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter ussid
		assertNotNull(buyBoxDao.getRichAttributeData(ussid));
		assertEquals(richAttributeModel, buyBoxDao.getRichAttributeData(ussid));
	}

	@Test
	public void getsellersDetails()
	{
		final Set<Map<BuyBoxModel, RichAttributeModel>> resultset = new HashSet<Map<BuyBoxModel, RichAttributeModel>>();
		final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product code
		//TPR-3809
		final String productCatType = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product Category Type
		/*assertNotNull(buyBoxDao.getsellersDetails(productCode));
		assertFalse(buyBoxDao.getsellersDetails(productCode).isEmpty());
		assertEquals(resultset, buyBoxDao.getsellersDetails(productCode));
		*/
		assertNotNull(buyBoxDao.getsellersDetails(productCode,productCatType));
		assertFalse(buyBoxDao.getsellersDetails(productCode,productCatType).isEmpty());
		assertEquals(resultset, buyBoxDao.getsellersDetails(productCode,productCatType));
	}

	@Test
	public void testGetpriceForUssid()
	{
		final BuyBoxModel buyBox = new BuyBoxModel();
		final String ussid = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter ussid
		assertNotNull(buyBoxDao.priceForUssid(ussid));
		assertEquals(buyBox, buyBoxDao.priceForUssid(ussid));
	}
}
