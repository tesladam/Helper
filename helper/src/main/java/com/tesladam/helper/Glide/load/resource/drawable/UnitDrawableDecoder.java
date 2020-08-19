package com.tesladam.helper.Glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceDecoder;
import com.tesladam.helper.Glide.load.engine.Resource;

/** Passes through a {@link Drawable} as a {@link Drawable} based {@link Resource}. */
public class UnitDrawableDecoder implements ResourceDecoder<Drawable, Drawable> {
  @Override
  public boolean handles(@NonNull Drawable source, @NonNull Options options) {
    return true;
  }

  @Nullable
  @Override
  public Resource<Drawable> decode(
      @NonNull Drawable source, int width, int height, @NonNull Options options) {
    return NonOwnedDrawableResource.newInstance(source);
  }
}
