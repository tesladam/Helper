package com.tesladam.helper.Glide.load.resource.bitmap;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.tesladam.helper.Glide.Glide;
import com.tesladam.helper.Glide.load.engine.bitmap_recycle.BitmapPool;

/**
 * An {@link com.tesladam.helper.Glide.load.ResourceDecoder} that can decode a thumbnail frame {@link
 * android.graphics.Bitmap} from a {@link android.os.ParcelFileDescriptor} containing a video.
 *
 * @see android.media.MediaMetadataRetriever
 * @deprecated Use {@link VideoDecoder#parcel(BitmapPool)} instead. This class may be removed and
 *     {@link VideoDecoder} may become final in a future version of Glide.
 */
@Deprecated
public class VideoBitmapDecoder extends VideoDecoder<ParcelFileDescriptor> {

  @SuppressWarnings("unused")
  public VideoBitmapDecoder(Context context) {
    this(Glide.get(context).getBitmapPool());
  }

  // Public API
  @SuppressWarnings("WeakerAccess")
  public VideoBitmapDecoder(BitmapPool bitmapPool) {
    super(bitmapPool, new ParcelFileDescriptorInitializer());
  }
}
