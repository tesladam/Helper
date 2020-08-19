package com.tesladam.helper.Glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.load.EncodeStrategy;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceEncoder;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.File;

/** Encodes {@link android.graphics.drawable.BitmapDrawable}s. */
public class BitmapDrawableEncoder implements ResourceEncoder<BitmapDrawable> {

  private final BitmapPool bitmapPool;
  private final ResourceEncoder<Bitmap> encoder;

  public BitmapDrawableEncoder(BitmapPool bitmapPool, ResourceEncoder<Bitmap> encoder) {
    this.bitmapPool = bitmapPool;
    this.encoder = encoder;
  }

  @Override
  public boolean encode(
      @NonNull Resource<BitmapDrawable> data, @NonNull File file, @NonNull Options options) {
    return encoder.encode(new BitmapResource(data.get().getBitmap(), bitmapPool), file, options);
  }

  @NonNull
  @Override
  public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
    return encoder.getEncodeStrategy(options);
  }
}
