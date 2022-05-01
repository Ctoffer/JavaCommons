package de.ctoffer.commons.io;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.io.InputStream;

@RequiredArgsConstructor
public class NeverCloseInputStream extends InputStream {
    @Delegate
    private final InputStream wrapped;
}