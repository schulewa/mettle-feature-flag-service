package com.mettle.mettlefeatureflagservice.model.entity;

import lombok.*;
import lombok.experimental.Tolerate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class FeatureFlagEntity {

  @Id
  @NotNull
  private String name;

  private boolean enabled;
  private boolean global;

  @NotNull
  private String owner;

  @Tolerate
  public FeatureFlagEntity() {

  }
}
