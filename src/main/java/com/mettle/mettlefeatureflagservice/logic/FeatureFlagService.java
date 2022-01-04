package com.mettle.mettlefeatureflagservice.logic;

import com.mettle.mettlefeatureflagservice.model.dao.FeatureFlagEntityDao;
import com.mettle.mettlefeatureflagservice.model.entity.FeatureFlagEntity;
import com.mettle.mettlefeatureflagservice.model.mapper.FeatureFlagDataMapper;
import com.mettle.mettlefeatureflagservice.rest.model.FeatureFlagDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FeatureFlagService {

  private final FeatureFlagEntityDao featureFlagEntityDao;
  private final FeatureFlagDataMapper mapper;

  public FeatureFlagService(FeatureFlagEntityDao featureFlagEntityDao, FeatureFlagDataMapper mapper) {
    this.featureFlagEntityDao = featureFlagEntityDao;
    this.mapper = mapper;
  }

  public List<FeatureFlagDto> getAll() {
    List<FeatureFlagDto> flags = new ArrayList<>();
    Iterable<FeatureFlagEntity> entities = featureFlagEntityDao.findAll();
    StreamSupport.stream(entities.spliterator(), false)
        .forEach(e -> flags.add(mapper.toRestEntity(e)));
    return flags;
  }

  public List<FeatureFlagDto> getForUser(String user) {
    List<FeatureFlagDto> flags = new ArrayList<>();
    Iterable<FeatureFlagEntity> entities = featureFlagEntityDao.findAll();
    StreamSupport.stream(entities.spliterator(), false)
        .filter(e -> e.getOwner().equals(user))
        .forEach(e -> flags.add(mapper.toRestEntity(e)));
    return flags;
  }

  public FeatureFlagDto create(FeatureFlagDto newFeatureFlag) {
    FeatureFlagEntity toBeSaved = mapper.toDomainEntity(newFeatureFlag);
    Optional<FeatureFlagEntity> existing = featureFlagEntityDao.findById(newFeatureFlag.getName());

    if (existing.isPresent()) {
      throw new FeatureFlagApplicationException("Feature flag with name " + newFeatureFlag.getName() + " already exists");
    }

    FeatureFlagEntity saved = featureFlagEntityDao.save(toBeSaved);
    return mapper.toRestEntity(saved);
  }

  public boolean enable(@NotNull String name) {
    return changeStatus(name, true);
  }

  public boolean disable(@NotNull String name) {
    return changeStatus(name, false);
  }

  private boolean changeStatus(String name, boolean desiredStatus) {
    Optional<FeatureFlagEntity> found = featureFlagEntityDao.findById(name);
    if (found.isEmpty()) {
      throw new FeatureFlagApplicationException("No feature flag found for name " + name);
    } else if (!found.get().isGlobal()) {
      // TODO for use case where the flag is not global we need to get current user to see if it is owned by user
      //      if not global and not owned by the current user then throw an exception
    }
    FeatureFlagEntity toBeSaved = found .get();
    toBeSaved.setEnabled(desiredStatus);
    featureFlagEntityDao.save(toBeSaved);
    return true;
  }
}
