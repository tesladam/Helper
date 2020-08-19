package com.tesladam.helper.Glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.load.engine.bitmap_recycle.BitmapPool;
import com.tesladam.helper.Glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.tesladam.helper.Glide.util.Preconditions;

/**
 * An {@link com.tesladam.helper.Glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * android.graphics.Bitmap}s into {@link android.graphics.drawable.BitmapDrawable}s.
 */
public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {
  private final Resources resources;

  // Public API.
  @SuppressWarnings("unused")
  public BitmapDrawableTranscoder(@NonNull Context context) {
    this(context.getResources());
  }

  /** @deprecated Use {@link #BitmapDrawableTranscoder(Resources)}, {@code bitmapPool} is unused. */
  @Deprecated
  public BitmapDrawableTranscoder(
      @NonNull Resources resources, @SuppressWarnings("unused") BitmapPool bitmapPool) {
    this(resources);
  }

  public BitmapDrawableTranscoder(@NonNull Resources resources) {
    this.resources = Preconditions.checkNotNull(resources);
  }

  @Nullable
  @Override
  public Resource<BitmapDrawable> transcode(
      @NonNull Resource<Bitmap> toTranscode, @NonNull Options options) {
    return LazyBitmapDrawableResource.obtain(resources, toTranscode);
  }
}
