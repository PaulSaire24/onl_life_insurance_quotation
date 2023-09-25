package com.bbva.rbvd;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.TransactionParameter;
import com.bbva.elara.domain.transaction.request.TransactionRequest;
import com.bbva.elara.domain.transaction.request.body.CommonRequestBody;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;

import com.bbva.elara.test.osgi.DummyBundleContext;

import java.util.ArrayList;

import javax.annotation.Resource;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.RBVDR304;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/elara-test.xml",
		"classpath:/META-INF/spring/RBVDT30201PETest.xml" })
public class RBVDT30201PETransactionTest {

	@Autowired
	private RBVDT30201PETransaction transaction;

	@Resource(name = "dummyBundleContext")
	private DummyBundleContext bundleContext;

	@Resource(name = "rbvdR304")
	private RBVDR304 rbvdr304;

	@Mock
	private CommonRequestHeader header;

	@Mock
	private TransactionRequest transactionRequest;

	@Before
	public void initializeClass() throws Exception {
		MockitoAnnotations.initMocks(this);

		this.transaction.start(bundleContext);
		this.transaction.setContext(new Context());

		CommonRequestBody commonRequestBody = new CommonRequestBody();
		commonRequestBody.setTransactionParameters(new ArrayList<>());

		this.transactionRequest.setBody(commonRequestBody);

		this.transactionRequest.setHeader(header);
		this.transaction.getContext().setTransactionRequest(transactionRequest);

		this.addParameter("isDataTreatment", true);
	}

	@Test
	public void testNotNull() {
		when(rbvdr304.executeBusinessLogicQuotation(anyObject())).thenReturn(new EasyesQuotationDTO());

		this.transaction.execute();

		assertEquals(Severity.OK, this.transaction.getSeverity());
	}

	@Test
	public void testNull(){
		assertNotNull(this.transaction);
		this.transaction.execute();

		assertNotNull(this.transaction.getSeverity());
		assertEquals(Severity.ENR, this.transaction.getSeverity());
	}

	private void addParameter(final String parameter, final Object value) {
		final TransactionParameter tParameter = new TransactionParameter(parameter, value);
		transaction.getContext().getParameterList().put(parameter, tParameter);
	}

}
