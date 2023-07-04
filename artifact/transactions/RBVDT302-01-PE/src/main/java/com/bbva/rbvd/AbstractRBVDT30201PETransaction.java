package com.bbva.rbvd;

import com.bbva.elara.transaction.AbstractTransaction;
import com.bbva.rbvd.dto.lifeinsrc.commons.HolderDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsuranceProductDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.RefundsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.TermDTO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.BankDTO;
import java.util.List;

/**
 * In this class, the input and output data is defined automatically through the setters and getters.
 */
public abstract class AbstractRBVDT30201PETransaction extends AbstractTransaction {

	public AbstractRBVDT30201PETransaction(){
	}


	/**
	 * Return value for input parameter product
	 */
	protected InsuranceProductDTO getProduct(){
		return (InsuranceProductDTO)this.getParameter("product");
	}

	/**
	 * Return value for input parameter holder
	 */
	protected HolderDTO getHolder(){
		return (HolderDTO)this.getParameter("holder");
	}

	/**
	 * Return value for input parameter isDataTreatment
	 */
	protected Boolean getIsdatatreatment(){
		return (Boolean)this.getParameter("isDataTreatment");
	}

	/**
	 * Return value for input parameter externalSimulationId
	 */
	protected String getExternalsimulationid(){
		return (String)this.getParameter("externalSimulationId");
	}

	/**
	 * Return value for input parameter bank
	 */
	protected BankDTO getBank(){
		return (BankDTO)this.getParameter("bank");
	}

	/**
	 * Return value for input parameter refunds
	 */
	protected List<RefundsDTO> getRefunds(){
		return (List<RefundsDTO>)this.getParameter("refunds");
	}

	/**
	 * Return value for input parameter term
	 */
	protected TermDTO getTerm(){
		return (TermDTO)this.getParameter("term");
	}

	/**
	 * Set value for String output parameter id
	 */
	protected void setId(final String field){
		this.addParameter("id", field);
	}

	/**
	 * Set value for InsuranceProductDTO output parameter product
	 */
	protected void setProduct(final InsuranceProductDTO field){
		this.addParameter("product", field);
	}

	/**
	 * Set value for HolderDTO output parameter holder
	 */
	protected void setHolder(final HolderDTO field){
		this.addParameter("holder", field);
	}

	/**
	 * Set value for Boolean output parameter isDataTreatment
	 */
	protected void setIsdatatreatment(final Boolean field){
		this.addParameter("isDataTreatment", field);
	}

	/**
	 * Set value for String output parameter externalSimulationId
	 */
	protected void setExternalsimulationid(final String field){
		this.addParameter("externalSimulationId", field);
	}

	/**
	 * Set value for BankDTO output parameter bank
	 */
	protected void setBank(final BankDTO field){
		this.addParameter("bank", field);
	}

	/**
	 * Set value for List<RefundsDTO> output parameter refunds
	 */
	protected void setRefunds(final List<RefundsDTO> field){
		this.addParameter("refunds", field);
	}

	/**
	 * Set value for TermDTO output parameter term
	 */
	protected void setTerm(final TermDTO field){
		this.addParameter("term", field);
	}
}
