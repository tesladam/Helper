package com.tesladam.helper.Glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.Source;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceDecoder;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/** {@link InputStream} specific implementation of {@link BitmapImageDecoderResourceDecoder}. */
@RequiresApi(api = 28)
public final class InputStreamBitmapImageDecoderResourceDecoder
    implements ResourceDecoder<InputStream, Bitmap> {
  private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

  @Override
  public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
    return true;
  }

  @Nullable
  @Override
  public Resource<Bitmap> decode(
      @NonNull InputStream stream, int width, int height, @NonNull Options options)
      throws IOException {
    ByteBuffer buffer = ByteBufferUtil.fromStream(stream);
    Source source = ImageDecoder.createSource(buffer);
    return wrapped.decode(source, width, height, options);
  }
}
