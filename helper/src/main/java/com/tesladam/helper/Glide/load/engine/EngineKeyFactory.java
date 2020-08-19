package com.tesladam.helper.Glide.load.engine;

import com.tesladam.helper.Glide.load.Key;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.Transformation;
import java.util.Map;

class EngineKeyFactory {

  @SuppressWarnings("rawtypes")
  EngineKey buildKey(
      Object model,
      Key signature,
      int width,
      int height,
      Map<Class<?>, Transformation<?>> transformations,
      Class<?> resourceClass,
      Class<?> transcodeClass,
      Options options) {
    return new EngineKey(
        model, signature, width, height, transformations, resourceClass, transcodeClass, options);
  }
}
