/**
 *
 */
package com.hybris.dtocache.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hybris.dtocache.cache.key.impl.TimestampKeyStrategy;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/psdtocache-test1.xml", "classpath:/psdtocache-spring.xml" })
public class CacheDescriptorFactoryTest
{
	@Autowired
	private CacheDescriptorFactory cacheDescriptorFactory;



	@Test
	public void testTestPopulator()
	{
		final CacheDescriptor cacheDescriptor = this.cacheDescriptorFactory
				.getCacheDescriptor("com.hybris.dtocache.populator.TestPopulator");
		assertNotNull(cacheDescriptor);
		//assertEquals(DefaultCacheKey.class, cacheDescriptor.getKeyStrategy().getClass());
		assertEquals(new String[]
		{ "p1", "p2", "p3", "p4", "p5" }, cacheDescriptor.getProperties().toArray());
		assertFalse(cacheDescriptor.isDisabled());
	}

	@Test
	public void testAnotherTestPopulator()
	{
		final CacheDescriptor cacheDescriptor = this.cacheDescriptorFactory
				.getCacheDescriptor("com.hybris.dtocache.populator.AnotherTestPopulator");
		assertNotNull(cacheDescriptor);
		//assertEquals(DefaultCacheKey.class, cacheDescriptor.getKeyStrategy().getClass());
		assertEquals(0, cacheDescriptor.getProperties().size());
		assertTrue(cacheDescriptor.isDisabled());
	}


	@Test
	public void testNonExistingPopulator()
	{
		final CacheDescriptor cacheDescriptor = this.cacheDescriptorFactory
				.getCacheDescriptor("com.hybris.dtocache.testmodel.NonExistingPopulator");
		//assertEquals(DefaultCacheKey.class, cacheDescriptor.getKeyStrategy().getClass());
		assertEquals(0, cacheDescriptor.getProperties().size());
		assertTrue(cacheDescriptor.isDisabled());
	}







}
