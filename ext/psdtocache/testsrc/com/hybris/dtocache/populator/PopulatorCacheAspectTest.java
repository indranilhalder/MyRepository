/**
 *
 */
package com.hybris.dtocache.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.platform.core.PK;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hybris.dtocache.testmodel.TestData;
import com.hybris.dtocache.testmodel.TestModel;


/**
 * @author i309277
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/psdtocache-test1.xml", "classpath:/psdtocache-spring.xml", })

public class PopulatorCacheAspectTest
{
	@Autowired
	private TestConverter testConverter;

	@Autowired
	private AnotherTestConverter AnotherTestConverter;

	@Before
	public void resetCount()
	{
		TestPopulator.resetCallCount();
		AnotherTestPopulator.resetCallCount();
	}

	@Test
	public void testCachedPopulator()
	{
		final String p1 = "property 1";
		final Date p2 = new Date();
		final Long p3 = Long.valueOf(304909483L);
		final Float p4 = Float.valueOf(3.454f);
		final Double p5 = Double.valueOf(345.3098403948);


		final TestModel aModel = new TestModel();
		aModel.setPk(PK.fromLong(209820938L));
		aModel.setP1(p1);
		aModel.setP2(p2);
		aModel.setP3(p3);
		aModel.setP4(p4);
		aModel.setP5(p5);

		assertEquals("Converter shouldn't have been called yet", 0, TestPopulator.getCallCount());
		final TestData d1 = getTestConverter().convert(aModel);
		assertEquals("Converter should have been called once", 1, TestPopulator.getCallCount());
		assertEquals(aModel.getP1(), d1.getP1());
		assertEquals(aModel.getP2(), d1.getP2());
		assertEquals(aModel.getP3(), d1.getP3());
		assertEquals(aModel.getP4(), d1.getP4());
		assertEquals(aModel.getP5(), d1.getP5());
		final TestData d2 = getTestConverter().convert(aModel);
		assertEquals("Converter should have been called once", 1, TestPopulator.getCallCount());
		assertEquals(aModel.getP1(), d2.getP1());
		assertEquals(aModel.getP2(), d2.getP2());
		assertEquals(aModel.getP3(), d2.getP3());
		assertEquals(aModel.getP4(), d2.getP4());
		assertEquals(aModel.getP5(), d2.getP5());
	}

	@Test
	public void testNonCachedPopulator()
	{
		final String p1 = "property 1";
		final Date p2 = new Date();
		final Long p3 = Long.valueOf(304909483L);
		final Float p4 = Float.valueOf(3.454f);
		final Double p5 = Double.valueOf(345.3098403948);


		final TestModel aModel = new TestModel();
		aModel.setPk(PK.fromLong(209820938L));
		aModel.setP1(p1);
		aModel.setP2(p2);
		aModel.setP3(p3);
		aModel.setP4(p4);
		aModel.setP5(p5);

		assertEquals("Converter shouldn't have been called yet", 0, AnotherTestPopulator.getCallCount());
		final TestData d1 = getAnotherTestConverter().convert(aModel);
		assertEquals(aModel.getP1(), d1.getP1());
		assertEquals(aModel.getP2(), d1.getP2());
		assertEquals(aModel.getP3(), d1.getP3());
		assertEquals(aModel.getP4(), d1.getP4());
		assertEquals(aModel.getP5(), d1.getP5());
		assertEquals("Converter should have been called once", 1, AnotherTestPopulator.getCallCount());
		final TestData d2 = getAnotherTestConverter().convert(aModel);
		assertEquals("Converter should have been called once", 2, AnotherTestPopulator.getCallCount());
		assertEquals(aModel.getP1(), d2.getP1());
		assertEquals(aModel.getP2(), d2.getP2());
		assertEquals(aModel.getP3(), d2.getP3());
		assertEquals(aModel.getP4(), d2.getP4());
		assertEquals(aModel.getP5(), d2.getP5());
	}

	@Test
	public void testCacheNullObjectPopulator()
	{
		final TestData d1 = getTestConverter().convert(null);
		assertNull(d1);
	}

	@Test
	public void testCacheNullPkPopulator()
	{
		final String p1 = "property 1";
		final Date p2 = new Date();
		final Long p3 = Long.valueOf(304909483L);
		final Float p4 = Float.valueOf(3.454f);
		final Double p5 = Double.valueOf(345.3098403948);


		final TestModel aModel = new TestModel();
		aModel.setPk(null);
		aModel.setP1(p1);
		aModel.setP2(p2);
		aModel.setP3(p3);
		aModel.setP4(p4);
		aModel.setP5(p5);

		assertEquals("Converter shouldn't have been called", 0, TestPopulator.getCallCount());
		getTestConverter().convert(aModel);
		assertEquals("Converter should have been called once", 1, TestPopulator.getCallCount());
		getTestConverter().convert(aModel);
		assertEquals("Converter should have been called twice", 2, TestPopulator.getCallCount());


	}


	/**
	 * @return the testConverter
	 */
	public TestConverter getTestConverter()
	{
		return testConverter;
	}

	/**
	 * @return the anotherTestConverter
	 */
	public AnotherTestConverter getAnotherTestConverter()
	{
		return AnotherTestConverter;
	}

}
