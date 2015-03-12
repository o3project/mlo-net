/**
 * XmlIdAdapter.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.logic;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class is the XML adapter class for ID element.
 */
public class XmlIdAdapter extends XmlAdapter<String, Integer> {
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Integer v) throws Exception {
		return (v != null) ? String.format("%1$08d", v) : null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Integer unmarshal(String v) throws Exception {
		return (v != null) ? Integer.valueOf(v) : null;
	}

}
