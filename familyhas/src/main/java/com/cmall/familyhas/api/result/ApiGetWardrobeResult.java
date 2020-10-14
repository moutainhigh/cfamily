package com.cmall.familyhas.api.result;

import com.cmall.familyhas.api.model.Wardrobe;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetWardrobeResult extends RootResult{

	@ZapcomApi(value="衣橱信息")
	private Wardrobe wardrobe = new Wardrobe();

	public Wardrobe getWardrobe() {
		return wardrobe;
	}

	public void setWardrobe(Wardrobe wardrobe) {
		this.wardrobe = wardrobe;
	}
	
	
	
}
