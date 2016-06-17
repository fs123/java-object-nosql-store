package ch.ivyteam.orient.fintech;

import java.util.HashSet;
import java.util.Set;

public class ControllingPersonManagement {
	boolean fiduciaryHoldingAssets;
	Set<ControllingPerson> controllingPersons = new HashSet<>();

	public boolean isFiduciaryHoldingAssets() {
		return fiduciaryHoldingAssets;
	}

	public void setFiduciaryHoldingAssets(boolean fiduciaryHoldingAssets) {
		this.fiduciaryHoldingAssets = fiduciaryHoldingAssets;
	}

	public Set<ControllingPerson> getControllingPersons() {
		return controllingPersons;
	}

	public void setControllingPersons(Set<ControllingPerson> controllingPersons) {
		this.controllingPersons = controllingPersons;
	}

}
