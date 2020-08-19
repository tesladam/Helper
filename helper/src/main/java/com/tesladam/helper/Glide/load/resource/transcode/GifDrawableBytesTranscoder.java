package com.tesladam.helper.Glide.load.resource.transcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.engine.Resource;
import com.tesladam.helper.Glide.load.resource.bytes.BytesResource;
import com.tesladam.helper.Glide.load.resource.gif.GifDrawable;
import com.tesladam.helper.Glide.util.ByteBufferUtil;
import java.nio.ByteBuffer;

/**
 * An {@link com.tesladam.helper.Glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * com.tesladam.helper.Glide.load.resource.gif.GifDrawable} into bytes by obtaining the original bytes of
 * the GIF from the {@link com.tesladam.helper.Glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
  @Nullable
  @Override
  public Resource<byte[]> transcode(
      @NonNull Resource<GifDrawable> toTranscode, @NonNull Options options) {
    GifDrawable gifData = toTranscode.get();
    ByteBuffer byteBuffer = gifData.getBuffer();
    return new BytesResource(ByteBufferUtil.toBytes(byteBuffer));
  }
}
