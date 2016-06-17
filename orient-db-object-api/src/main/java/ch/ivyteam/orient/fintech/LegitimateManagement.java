package ch.ivyteam.orient.fintech;

import java.util.HashSet;
import java.util.Set;

public class LegitimateManagement {
	private boolean onlyLegitimatesAreBO;
	private Set<Legitimate> legitimates = new HashSet<>();

	public boolean isOnlyLegitimatesAreBO() {
		return onlyLegitimatesAreBO;
	}

	public void setOnlyLegitimatesAreBO(boolean onlyLegitimatesAreBO) {
		this.onlyLegitimatesAreBO = onlyLegitimatesAreBO;
	}

	public Set<Legitimate> getLegitimates() {
		return legitimates;
	}

	public void setLegitimates(Set<Legitimate> legitimates) {
		this.legitimates = legitimates;
	}

}
