package com.tesladam.helper.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tesladam.helper.Glide.manager.RequestManagerRetriever;
import com.tesladam.helper.Glide.module.AppGlideModule;
import java.util.Set;

/**
 * Allows {@link AppGlideModule}s to exclude {@link com.tesladam.helper.Glide.annotation.GlideModule}s to
 * ease the migration from {@link com.tesladam.helper.Glide.annotation.GlideModule}s to Glide's annotation
 * processing system and optionally provides a {@link
 * com.tesladam.helper.Glide.manager.RequestManagerRetriever.RequestManagerFactory} impl.
 */
abstract class GeneratedAppGlideModule extends AppGlideModule {
  /** This method can be removed when manifest parsing is no longer supported. */
  @NonNull
  abstract Set<Class<?>> getExcludedModuleClasses();

  @Nullable
  RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
    return null;
  }
}
