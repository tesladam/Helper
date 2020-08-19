package com.tesladam.helper.Glide.load.engine;

import com.tesladam.helper.Glide.load.Key;

interface EngineJobListener {

  void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> resource);

  void onEngineJobCancelled(EngineJob<?> engineJob, Key key);
}
