package com.mettle.mettlefeatureflagservice.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;

@Builder
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureFlagDto {

  boolean enabled;
  boolean global;
  String name;
  String owner;

  public FeatureFlagDto(boolean enabled, boolean global, @NotNull String name, @NotNull String owner) {
    this.enabled = enabled;
    this.global = global;
    this.name = name;
    this.owner = owner;
  }

  public FeatureFlagDto(boolean global, @NotNull String name, @NotNull String owner) {
    this.enabled = false;
    this.global = global;
    this.name = name;
    this.owner = owner;
  }

  @Tolerate
  public FeatureFlagDto() {

  }
}
