package com.tesladam.helper.Glide.load.resource.transcode;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.load.resource.bytes.BytesResource;
import java.io.ByteArrayOutputStream;

/**
 * An {@link com.tesladam.helper.Glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * android.graphics.Bitmap}s into byte arrays using {@link android.graphics.Bitmap#compress
 * (android.graphics.Bitmap.CompressFormat, int, java.io.OutputStream)}.
 */
public class BitmapBytesTranscoder implements ResourceTranscoder<Bitmap, byte[]> {
  private final Bitmap.CompressFormat compressFormat;
  private final int quality;

  public BitmapBytesTranscoder() {
    this(Bitmap.CompressFormat.JPEG, 100);
  }

  // Public API.
  @SuppressWarnings("WeakerAccess")
  public BitmapBytesTranscoder(@NonNull Bitmap.CompressFormat compressFormat, int quality) {
    this.compressFormat = compressFormat;
    this.quality = quality;
  }

  @Nullable
  @Override
  public Resource<byte[]> transcode(
      @NonNull Resource<Bitmap> toTranscode, @NonNull Options options) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    toTranscode.get().compress(compressFormat, quality, os);
    toTranscode.recycle();
    return new BytesResource(os.toByteArray());
  }
}
