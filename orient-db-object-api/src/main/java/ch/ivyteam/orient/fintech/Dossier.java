package ch.ivyteam.orient.fintech;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Dossier {
	@ManyToOne
	AccountHolder accountHolder;
	@OneToMany
	Set<BeneficialOwner> beneficialOwners = new HashSet<>();
	@OneToOne
	ControllingPersonManagement controllingPersonMgmt;
	@OneToOne
	LegitimateManagement legitimateMgmt;

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Set<BeneficialOwner> getBeneficialOwners() {
		return beneficialOwners;
	}

	public void setBeneficialOwners(Set<BeneficialOwner> beneficialOwners) {
		this.beneficialOwners = beneficialOwners;
	}

	public ControllingPersonManagement getControllingPersonMgmt() {
		return controllingPersonMgmt;
	}

	public void setControllingPersonMgmt(ControllingPersonManagement controllingPersonMgmt) {
		this.controllingPersonMgmt = controllingPersonMgmt;
	}

	public LegitimateManagement getLegitimateMgmt() {
		return legitimateMgmt;
	}

	public void setLegitimateMgmt(LegitimateManagement legitimateMgmt) {
		this.legitimateMgmt = legitimateMgmt;
	}

}
