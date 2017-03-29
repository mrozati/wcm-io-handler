/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.handler.media.spi;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.osgi.annotation.versioning.ConsumerType;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import io.wcm.handler.commons.spisupport.SpiMatcher;
import io.wcm.handler.media.format.MediaFormat;
import io.wcm.handler.media.markup.DummyImageMediaMarkupBuilder;
import io.wcm.handler.media.markup.SimpleImageMediaMarkupBuilder;
import io.wcm.handler.mediasource.dam.DamMediaSource;

/**
 * Provides application-specific configuration information required for media handling.
 * <p>
 * This interface has to be implemented as OSGi service.
 * </p>
 * TODO: think about even more sensible defaults?
 */
@ConsumerType
public abstract class MediaHandlerConfig implements SpiMatcher {

  /**
   * Default value for JPEG quality.
   */
  public static final double DEFAULT_JPEG_QUALITY = 0.98d;

  private static final Set<MediaFormat> DEFAULT_DOWNLOAD_MEDIA_FORMATS = ImmutableSet.of();

  private static final List<Class<? extends MediaSource>> DEFAULT_MEDIA_SOURCES = ImmutableList.<Class<? extends MediaSource>>of(
      DamMediaSource.class);

  private static final List<Class<? extends MediaMarkupBuilder>> DEFAULT_MEDIA_MARKUP_BUILDERS = ImmutableList.<Class<? extends MediaMarkupBuilder>>of(
      SimpleImageMediaMarkupBuilder.class,
      DummyImageMediaMarkupBuilder.class);

  /**
   * @return Media format names for downloads that are allowed as target for links
   */
  public Set<MediaFormat> getDownloadMediaFormats() {
    return DEFAULT_DOWNLOAD_MEDIA_FORMATS;
  }

  /**
   * @return Supported media sources
   */
  public List<Class<? extends MediaSource>> getSources() {
    return DEFAULT_MEDIA_SOURCES;
  }

  /**
   * @return Available media markup builders
   */
  public List<Class<? extends MediaMarkupBuilder>> getMarkupBuilders() {
    return DEFAULT_MEDIA_MARKUP_BUILDERS;
  }

  /**
   * @return List of media metadata pre processors (optional). The processors are applied in list order.
   */
  public List<Class<? extends MediaProcessor>> getPreProcessors() {
    // no processors
    return ImmutableList.of();
  }

  /**
   * @return List of media metadata post processors (optional). The processors are applied in list order.
   */
  public List<Class<? extends MediaProcessor>> getPostProcessors() {
    // no processors
    return ImmutableList.of();
  }

  /**
   * Get the default quality for images in this app generated with the Layer API.
   * The meaning of the quality parameter for the different image formats is described in
   * {@link com.day.image.Layer#write(String, double, java.io.OutputStream)}.
   * @param mimeType MIME-type of the output format
   * @return Quality factor
   */
  public double getDefaultImageQuality(String mimeType) {
    if (StringUtils.isNotEmpty(mimeType)) {
      String format = StringUtils.substringAfter(mimeType.toLowerCase(), "image/");
      if (StringUtils.equals(format, "jpg") || StringUtils.equals(format, "jpeg")) {
        return DEFAULT_JPEG_QUALITY;
      }
      else if (StringUtils.equals(format, "gif")) {
        return 256d; // 256 colors
      }
    }
    // return quality "1" for all other mime types
    return 1d;
  }

}
