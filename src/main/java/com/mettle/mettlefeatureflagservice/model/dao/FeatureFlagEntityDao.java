package com.mettle.mettlefeatureflagservice.model.dao;

import com.mettle.mettlefeatureflagservice.model.entity.FeatureFlagEntity;
import org.springframework.data.repository.CrudRepository;

public interface FeatureFlagEntityDao extends CrudRepository<FeatureFlagEntity, String> {

}
