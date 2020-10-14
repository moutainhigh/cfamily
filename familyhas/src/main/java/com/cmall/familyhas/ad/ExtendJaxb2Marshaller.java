package com.cmall.familyhas.ad;

import java.util.HashMap;
import java.util.Map;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class ExtendJaxb2Marshaller extends Jaxb2Marshaller {

	public ExtendJaxb2Marshaller()
	{
		
		/*Map<String, Object> map=new HashMap<String, Object>();
		map.put("jaxb.fragment", Boolean.TRUE);
		this.setMarshallerProperties(map);*/
	}
	
}
