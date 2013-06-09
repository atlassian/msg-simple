/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.msgsimple.source;

import com.github.fge.msgsimple.InternalBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Map}-based message source
 *
 * <p>This is a simple message source using a {@link Map} as a key/value pair to
 * look up messages.</p>
 *
 * <p>In order to build such a source, use {@link #newBuilder()}. Sample:</p>
 *
 * <pre>
 *     final MessageSource source = MapMessageSource.newBuilder()
 *         .put("key1", "message1").put("key2", "message2")
 *         .putAll(existingMap).build();
 * </pre>
 *
 * <p>Note that null keys or values are not allowed.</p>
 *
 * @see Builder
 */
public final class MapMessageSource
    implements MessageSource
{
    private static final InternalBundle BUNDLE
        = InternalBundle.getInstance();

    private final Map<String, String> messages;

    /**
     * Build a message source from a map directly
     *
     * @param messages the message map
     * @throws NullPointerException the map is null; or at least one key or
     * value is null
     * @deprecated use {@link #newBuilder()} instead. Will be removed in 0.5.
     * @see Builder
     */
    @Deprecated
    public MapMessageSource(final Map<String, String> messages)
    {
        this.messages = new HashMap<String, String>(checkMap(messages));
    }

    private MapMessageSource(final Builder builder)
    {
        messages = new HashMap<String, String>(builder.messages);
    }

    /**
     * Create a new builder for a map message source
     *
     * @return a {@link Builder}
     */
    public static Builder newBuilder()
    {
        return new Builder();
    }

    @Override
    public String getKey(final String key)
    {
        return messages.get(key);
    }

    /**
     * Builder class for a {@link MapMessageSource}
     */
    public static final class Builder
    {
        private final Map<String, String> messages
            = new HashMap<String, String>();

        private Builder()
        {
        }

        /**
         * Add one key/message pair
         *
         * <p>This overrides the value if the key already existed.</p>
         *
         * @param key the key
         * @param message the message
         * @return this
         * @throws NullPointerException either the key or the value is null
         */
        public Builder put(final String key, final String message)
        {
            messages.put(
                BUNDLE.checkNotNull(key, "cfg.map.nullKey"),
                BUNDLE.checkNotNull(message, "cfg.map.nullValue")
            );
            return this;
        }

        /**
         * Add a map of key/message pairs
         *
         * <p>This overrides all values of already existing keys.</p>
         *
         * @param map the map
         * @return this
         * @throws NullPointerException map is null, or either one
         */
        public Builder putAll(final Map<String, String> map)
        {
            for (final Map.Entry<String, String> entry:
                BUNDLE.checkNotNull(map, "cfg.nullMap").entrySet())
                put(entry.getKey(), entry.getValue());
            return this;
        }

        /**
         * Build a new message source from the contents of this builder
         *
         * @return a {@link MapMessageSource}
         */
        public MessageSource build()
        {
            return new MapMessageSource(this);
        }
    }

    // TODO: remove when public constructor is removed
    private static Map<String, String> checkMap(final Map<String, String> map)
    {
        BUNDLE.checkNotNull(map, "cfg.nullMap");

        for (final Map.Entry<String, String> entry: map.entrySet()) {
            BUNDLE.checkNotNull(entry.getKey(), "cfg.map.nullKey");
            BUNDLE.checkNotNull(entry.getValue(), "cfg.map.nullValue");
        }

        return map;
    }
}
