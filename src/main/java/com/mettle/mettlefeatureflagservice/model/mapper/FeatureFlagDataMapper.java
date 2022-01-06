package com.mettle.mettlefeatureflagservice.model.mapper;

import com.mettle.mettlefeatureflagservice.model.entity.FeatureFlagEntity;
import com.mettle.mettlefeatureflagservice.rest.model.FeatureFlagDto;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagDataMapper {

  public FeatureFlagEntity toDomainEntity(FeatureFlagDto restEntity) {

    return FeatureFlagEntity.builder()
        .enabled(restEntity.isEnabled())
        .global(restEntity.isGlobal())
        .name(restEntity.getName())
        .owner(restEntity.getOwner())
        .build();
  }

  public FeatureFlagDto toRestEntity(FeatureFlagEntity domainEntity) {
    return new FeatureFlagDto(domainEntity.isEnabled(),
                            domainEntity.isGlobal(),
                            domainEntity.getName(),
                            domainEntity.getOwner());
  }

}
