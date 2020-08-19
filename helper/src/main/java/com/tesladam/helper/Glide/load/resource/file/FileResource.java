package com.tesladam.helper.Glide.load.resource.file;

import com.tesladam.helper.Glide.load.resource.SimpleResource;
import java.io.File;

/** A simple {@link com.tesladam.helper.Glide.load.engine.Resource} that wraps a {@link File}. */
// Public API.
@SuppressWarnings("WeakerAccess")
public class FileResource extends SimpleResource<File> {
  public FileResource(File file) {
    super(file);
  }
}
