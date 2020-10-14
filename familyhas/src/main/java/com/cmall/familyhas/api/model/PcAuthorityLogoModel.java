package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 权威标识
 * @author ligj
 * @version
 * 2015-01-20 13:43
 *
 */
public class PcAuthorityLogoModel {

	@ZapcomApi(value = "权威标志说明")
    private String logoContent;
	
	@ZapcomApi(value = "权威标志图标")
    private String logoPic;
	
	@ZapcomApi(value = "位置")
    private Integer logoLocation;
	
	public String getLogoContent() {
		return logoContent;
	}
	public void setLogoContent(String logoContent) {
		this.logoContent = logoContent;
	}
	public String getLogoPic() {
		return logoPic;
	}
	public void setLogoPic(String logoPic) {
		this.logoPic = logoPic;
	}
	public Integer getLogoLocation() {
		return logoLocation;
	}
	public void setLogoLocation(Integer logoLocation) {
		this.logoLocation = logoLocation;
	}
}