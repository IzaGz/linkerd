package io.buoyant.telemetry

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonProperty}
import com.twitter.finagle.Stack
import io.buoyant.config.{ConfigInitializer, PolymorphicConfig}

/**
 * Telemeter plugins describe how to load TelemeterConfig items.
 */
trait TelemeterInitializer extends ConfigInitializer {
  type Config <: TelemeterConfig
  def configClass: Class[Config]
}

trait TelemeterConfig extends PolymorphicConfig {
  /**
   * This property must be set to true in order to use this telemeter if it is
   * experimental
   */
  @JsonProperty("experimental")
  var _experimentalEnabled: Option[Boolean] = None

  /**
   * Indicates whether this is an experimental telemeter.  Experimental
   * telemeters must have the `experimental` property set to true to be used
   */
  @JsonIgnore
  def experimentalRequired: Boolean = false

  /** If this telemeter experimental but has not set the `experimental` property. */
  @JsonIgnore
  def disabled = experimentalRequired && !_experimentalEnabled.contains(true)

  @JsonIgnore def mk(params: Stack.Params): Telemeter
}
