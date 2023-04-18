package com.bbva.rbvd.lib.r304;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.impl.RBVDR304Impl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR304-app.xml",
		"classpath:/META-INF/spring/RBVDR304-app-test.xml",
		"classpath:/META-INF/spring/RBVDR304-arc.xml",
		"classpath:/META-INF/spring/RBVDR304-arc-test.xml" })
public class RBVDR304Test {

	private final RBVDR304 rbvdr304 = new RBVDR304Impl();

	@Before
	public void setUp() {
		ThreadContext.set(new Context());
	}

	@Test
	public void executeTest(){
		EasyesQuotationDTO validation = rbvdr304.executeBusinessLogicEasyesQutation(new EasyesQuotationDTO());
		assertNotNull(validation);
	}
	
}
