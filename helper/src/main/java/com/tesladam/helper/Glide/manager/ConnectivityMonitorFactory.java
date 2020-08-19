package com.tesladam.helper.Glide.manager;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * A factory class that produces a functional {@link
 * com.tesladam.helper.Glide.manager.ConnectivityMonitor}.
 */
public interface ConnectivityMonitorFactory {

  @NonNull
  ConnectivityMonitor build(
      @NonNull Context context, @NonNull ConnectivityMonitor.ConnectivityListener listener);
}
