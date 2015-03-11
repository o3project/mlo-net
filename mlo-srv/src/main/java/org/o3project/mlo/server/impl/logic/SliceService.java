/**
 * SliceService.java
 * (C) 2014, Hitachi Solutions, Ltd.
 */
package org.o3project.mlo.server.impl.logic;

import java.util.ArrayList;
import java.util.List;

import org.o3project.mlo.server.dto.SliceDto;


/**
 * This class is the volatile data repository class of slice entity.
 */
class SliceService {
	/**
	 * Slice entity list.
	 */
	private final List<SliceEntity> sliceEntities;
	
	/**
	 * Current maximum slice ID.
	 */
	private Integer maxSliceId;
	
	/**
	 * Current maximum flow ID.
	 */
	private Integer maxFlowId;

	/**
	 * A constructor. 
	 * @param sliceDtos Slice DTO list. 
	 * @param maxSliceId Initial value of current maximum slice ID.
	 * @param maxFlowId Initial value of current maximum flow ID.
	 */
	SliceService(List<SliceEntity> sliceDtos, Integer maxSliceId, Integer maxFlowId) {
		this.sliceEntities = sliceDtos;
		this.maxSliceId = maxSliceId;
		this.maxFlowId = maxFlowId;
	}
	
	/**
	 * A constructor.
	 */
	SliceService() {
		this(new ArrayList<SliceEntity>(), 0, 0);
	}

	/**
	 * Obtains new slice ID.
	 * @return ID
	 */
	public Integer getNewSliceId() {
		maxSliceId += 1;
		return maxSliceId;
	}

	/**
	 * Obtains new flow ID.
	 * @return ID
	 */
	public Integer getNewFlowId() {
		maxFlowId += 1;
		return maxFlowId;
	}
	
	/**
	 * Searches slice entities by owner.
	 * If not found, returns empty list.
	 * @param owner Slice owner. 
	 * @return Slice entity list.
	 */
	public List<SliceEntity> findSliceEntities(String owner) {
		List<SliceEntity> entities = new ArrayList<SliceEntity>();
		for (SliceEntity entity : sliceEntities) {
			if (entity.getOwner().equals(owner)) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	/**
	 * Searches slice entity by slice name and owner.
	 * If not found, returns null.
	 * @param name Slice name. 
	 * @param owner owner.
	 * @return Entity.
	 */
	public SliceEntity findSliceEntity(String name, String owner) {
		SliceEntity sliceEnt = null;
		for (SliceEntity entity : sliceEntities) {
			if (entity.getSliceDto().name.equals(name) && entity.getOwner().equals(owner)) {
				sliceEnt = entity;
				break;
			}
		}
		return sliceEnt;
	}
	
	/**
	 * Searches slice entity by slice ID and owner.
	 * If not found, returns null.
	 * @param name Slice name. 
	 * @param owner owner.
	 * @return Entity.
	 */
	public SliceEntity findSliceEntity(Integer id, String owner) {
		SliceEntity sliceEnt = null;
		for (SliceEntity entity : sliceEntities) {
			if (entity.getSliceDto().id.equals(id) && entity.getOwner().equals(owner)) {
				sliceEnt = entity;
				break;
			}
		}
		return sliceEnt;
	}
	
	/**
	 * Adds a slice entity.
	 * @param sliceEntity Slice entity.
	 * @return the result.
	 */
	public boolean addSliceEntity(SliceEntity sliceEntity) {
		return sliceEntities.add(sliceEntity);
	}
	
	/**
	 * Deletes a slice entity.
	 * @param sliceEntity Entiry.
	 * @return The result.
	 */
	public boolean removeSliceEntity(SliceEntity sliceEntity) {
		return sliceEntities.remove(sliceEntity);
	}
}

/**
 * This class is the slice entity class.
 */
class SliceEntity {
	private final SliceDto sliceDto;
	
	private final String owner;
	
	/**
	 * A constructor. 
	 * @param sliceDto Slice DTO.
	 * @param owner Owner.
	 */
	SliceEntity(SliceDto sliceDto, String owner) {
		this.sliceDto = sliceDto;
		this.owner = owner;
	}
	
	/**
	 * Obtains the owner of the slice.
	 * @return The owner. 
	 */
	String getOwner() {
		return owner;
	}
	
	/**
	 * Obtains the slice DTO.
	 * @return Slice DTO.
	 */
	SliceDto getSliceDto() {
		return sliceDto;
	}
}
