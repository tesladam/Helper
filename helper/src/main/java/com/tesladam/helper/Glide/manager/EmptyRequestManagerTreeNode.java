package com.tesladam.helper.Glide.manager;

import androidx.annotation.NonNull;
import com.tesladam.helper.Glide.RequestManager;
import java.util.Collections;
import java.util.Set;

/** A {@link RequestManagerTreeNode} that returns no relatives. */
final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
  @NonNull
  @Override
  public Set<RequestManager> getDescendants() {
    return Collections.emptySet();
  }
}
