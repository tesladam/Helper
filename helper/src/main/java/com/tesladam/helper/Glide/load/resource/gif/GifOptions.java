package com.tesladam.helper.Glide.load.resource.gif;

import com.tesladam.helper.Glide.load.DecodeFormat;
import com.tesladam.helper.Glide.load.Option;
import com.tesladam.helper.Glide.load.Options;
import com.tesladam.helper.Glide.load.ResourceDecoder;

/** Options related to decoding GIFs. */
public final class GifOptions {

  /**
   * Indicates the {@link com.tesladam.helper.Glide.load.DecodeFormat} that will be used in conjunction
   * with the particular GIF to determine the {@link android.graphics.Bitmap.Config} to use when
   * decoding frames of GIFs.
   */
  public static final Option<DecodeFormat> DECODE_FORMAT =
      Option.memory(
          "com.tesladam.helper.Glide.load.resource.gif.GifOptions.DecodeFormat", DecodeFormat.DEFAULT);

  /**
   * If set to {@code true}, disables the GIF {@link com.tesladam.helper.Glide.load.ResourceDecoder}s
   * ({@link ResourceDecoder#handles(Object, Options)} will return {@code false}). Defaults to
   * {@code false}.
   */
  public static final Option<Boolean> DISABLE_ANIMATION =
      Option.memory("com.tesladam.helper.Glide.load.resource.gif.GifOptions.DisableAnimation", false);

  private GifOptions() {
    // Utility class.
  }
}
