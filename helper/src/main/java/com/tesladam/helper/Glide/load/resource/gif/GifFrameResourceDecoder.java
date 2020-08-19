package com.tesladam.helper.Glide.load.resource.gif;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.gifdecoder.GifDecoder;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceDecoder;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.load.engine.bitmap_recycle.BitmapPool;
import com.tesladam.helper.Glide.load.resource.bitmap.BitmapResource;

/**
 * Decodes {@link Bitmap}s from {@link GifDecoder}s representing a particular frame of a particular
 * GIF image.
 */
public final class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap> {
  private final BitmapPool bitmapPool;

  public GifFrameResourceDecoder(BitmapPool bitmapPool) {
    this.bitmapPool = bitmapPool;
  }

  @Override
  public boolean handles(@NonNull GifDecoder source, @NonNull Options options) {
    return true;
  }

  @Override
  public Resource<Bitmap> decode(
      @NonNull GifDecoder source, int width, int height, @NonNull Options options) {
    Bitmap bitmap = source.getNextFrame();
    return BitmapResource.obtain(bitmap, bitmapPool);
  }
}
