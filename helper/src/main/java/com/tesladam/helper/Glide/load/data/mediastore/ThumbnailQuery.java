package com.tesladam.helper.Glide.load.data.mediastore;

import android.database.Cursor;
import android.net.Uri;

interface ThumbnailQuery {
  Cursor query(Uri uri);
}
