package de.ctoffer.commons.io;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class NeverCloseInputStream extends InputStream {

    private interface Close {
        void close() throws IOException;
    }

    @Delegate(excludes = Close.class)
    private final InputStream wrapped;

    @Override
    public void close() {

    }
}