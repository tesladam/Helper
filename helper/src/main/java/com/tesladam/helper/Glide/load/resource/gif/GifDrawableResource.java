package com.tesladam.helper.Glide.load.resource.gif;

import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.load.engine.Initializable;
import com.tesladam.helper.Glide.load.resource.drawable.DrawableResource;

/** A resource wrapping an {@link com.tesladam.helper.Glide.load.resource.gif.GifDrawable}. */
public class GifDrawableResource extends DrawableResource<GifDrawable> implements Initializable {
  // Public API.
  @SuppressWarnings("WeakerAccess")
  public GifDrawableResource(GifDrawable drawable) {
    super(drawable);
  }

  @NonNull
  @Override
  public Class<GifDrawable> getResourceClass() {
    return GifDrawable.class;
  }

  @Override
  public int getSize() {
    return drawable.getSize();
  }

  @Override
  public void recycle() {
    drawable.stop();
    drawable.recycle();
  }

  @Override
  public void initialize() {
    drawable.getFirstFrame().prepareToDraw();
  }
}
