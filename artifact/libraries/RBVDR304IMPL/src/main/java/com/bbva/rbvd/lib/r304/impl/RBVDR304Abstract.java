package com.bbva.rbvd.lib.r304.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.RBVDR304;

public abstract class RBVDR304Abstract extends AbstractLibrary implements RBVDR304 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected PISDR350 pisdR350;

	protected RBVDR303 rbvdR303;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param pisdR350 the this.pisdR350 to set
	*/
	public void setPisdR350(PISDR350 pisdR350) {
		this.pisdR350 = pisdR350;
	}

	/**
	* @param rbvdR303 the this.rbvdR303 to set
	*/
	public void setRbvdR303(RBVDR303 rbvdR303) {
		this.rbvdR303 = rbvdR303;
	}

}