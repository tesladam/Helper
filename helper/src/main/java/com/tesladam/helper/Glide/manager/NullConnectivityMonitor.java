package com.tesladam.helper.Glide.manager;

/** A no-op {@link com.tesladam.helper.Glide.manager.ConnectivityMonitor}. */
class NullConnectivityMonitor implements ConnectivityMonitor {

  @Override
  public void onStart() {
    // Do nothing.
  }

  @Override
  public void onStop() {
    // Do nothing.
  }

  @Override
  public void onDestroy() {
    // Do nothing.
  }
}
