package com.tesladam.helper.Glide.module;

import android.content.Context;
import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.Glide;
import com.tesladam.helper.Glide.Registry;

/**
 * Registers a set of components to use when initializing Glide within an app when Glide's
 * annotation processor is used.
 *
 * <p>Any number of LibraryGlideModules can be contained within any library or application.
 *
 * <p>LibraryGlideModules are called in no defined order. If LibraryGlideModules within an
 * application conflict, {@link AppGlideModule}s can use the {@link
 * com.tesladam.helper.Glide.annotation.Excludes} annotation to selectively remove one or more of the
 * conflicting modules.
 */
@SuppressWarnings("deprecation")
public abstract class LibraryGlideModule implements RegistersComponents {
  @Override
  public void registerComponents(
      @NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
    // Default empty impl.
  }
}
