package com.tesladam.helper.Glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceDecoder;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/** Decodes {@link android.graphics.Bitmap Bitmaps} from {@link java.nio.ByteBuffer ByteBuffers}. */
public class ByteBufferBitmapDecoder implements ResourceDecoder<ByteBuffer, Bitmap> {
  private final Downsampler downsampler;

  public ByteBufferBitmapDecoder(Downsampler downsampler) {
    this.downsampler = downsampler;
  }

  @Override
  public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) {
    return downsampler.handles(source);
  }

  @Override
  public Resource<Bitmap> decode(
      @NonNull ByteBuffer source, int width, int height, @NonNull Options options)
      throws IOException {
    InputStream is = ByteBufferUtil.toStream(source);
    return downsampler.decode(is, width, height, options);
  }
}
