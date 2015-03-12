/**
 * SliceDto.java
 * (C) 2013,2015, Hitachi, Ltd.
 */
package org.o3project.mlo.server.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.o3project.mlo.server.logic.XmlIdAdapter;


/**
 * スライス情報のDTOクラスです。
 *
 */
public class SliceDto {
	
	/** サービス利用者が命名したスライス名 */
	@XmlElement(name = "SliceName")
	public String name;
	
	/** スライス名に紐付くID */
	@XmlElement(name = "SliceId")
	@XmlJavaTypeAdapter(XmlIdAdapter.class)
	public Integer id;
	
	/** フローを示すタグ */
	@XmlElementWrapper(name = "Flows")
	@XmlElement(name = "Flow")
	public List<FlowDto> flows;

}
