package com.tao.mvpbaselibrary.lekcanary;

import androidx.annotation.NonNull;

import java.util.List;

import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import shark.AndroidReferenceMatchers;
import shark.LibraryLeakReferenceMatcher;
import shark.ReferenceMatcher;

/**
 * Wrapper for LeakCanary. Needed for exclude LeakCanary classes in production builds
 */
@SuppressWarnings("unused")
public class LeakCanaryWrapperImpl {

  private static final LibraryLeakReferenceMatcher MAPKIT_LEAKED_CONTEXT = createExcludedFieldLeak(
      "ru.yandex.yandexmapkit.map.FileCache",
      "mContext",
      "Well known bug with map kit");

  private static final LibraryLeakReferenceMatcher STACKED_CONTROLLER_LEAKED_STACK = createExcludedFieldLeak(
      "ru.yandex.taxi.controller.StackedController$1",
      "stack",
      "Every controller holds reference for it's main fragment in stack");

  private MaybeObjectWatcher maybeObjectWatcher;

  public void watch(@NonNull Object watchedReference) {
    watch(watchedReference, watchedReference.getClass().getSimpleName());
  }

  public void watch(@NonNull Object watchedReference, @NonNull String description) {
    AppWatcher.INSTANCE.getObjectWatcher().watch(watchedReference, description);
  }

  /**
   * Install LeakCanary watch into app
   */
  public LeakCanaryWrapperImpl() {

    LeakCanary.INSTANCE.setConfig(taxiLeakCanaryConfig());
    maybeObjectWatcher = REAL_WATCHER;

  }

  private LeakCanary.Config taxiLeakCanaryConfig() {
    LeakCanary.Config leakCanaryConfig = LeakCanary.INSTANCE.getConfig();
    List<ReferenceMatcher> referenceMatchers = leakCanaryConfig.getReferenceMatchers();
    referenceMatchers.add(MAPKIT_LEAKED_CONTEXT);
    referenceMatchers.add(STACKED_CONTROLLER_LEAKED_STACK);
    return leakCanaryConfig.newBuilder()
        .dumpHeap(true)
        .referenceMatchers(referenceMatchers)
        .build();
  }

  @NonNull
  private static LibraryLeakReferenceMatcher createExcludedFieldLeak(
      @NonNull String className,
      @NonNull String fieldName,
      @NonNull String description
  ) {
    return AndroidReferenceMatchers.Companion.staticFieldLeak(
        className,
        fieldName,
        description,
        pattern -> true
    );
  }

  public void setDumpHeap(boolean dumpHeap) {
    LeakCanary.Config config = LeakCanary.INSTANCE.getConfig().newBuilder()
        .dumpHeap(dumpHeap)
        .build();
    LeakCanary.INSTANCE.setConfig(config);

    if (dumpHeap) {
      maybeObjectWatcher = REAL_WATCHER;
    } else {
      maybeObjectWatcher = EMPTY;
    }
  }

  public String name() {
    return "real";
  }

  //solution from https://square.github.io/leakcanary/upgrading-to-leakcanary-2.0/#option-2-make-your-own-objectwatcher-interface
  private interface MaybeObjectWatcher {
    void watch(@NonNull Object watchedReference, @NonNull String description);
  }

  @NonNull
  private static final MaybeObjectWatcher EMPTY = new MaybeObjectWatcher() {
    @Override
    public void watch(@NonNull Object watchedReference, @NonNull String description) {}
  };

  @NonNull
  private static final MaybeObjectWatcher REAL_WATCHER = new MaybeObjectWatcher() {
    @Override
    public void watch(@NonNull Object watchedReference, @NonNull String description) {
      AppWatcher.INSTANCE.getObjectWatcher().watch(watchedReference, description);
    }
  };
}
