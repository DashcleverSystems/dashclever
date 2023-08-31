package pl.dashclever.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ToInputStreamConverter {
    static InputStream fromString(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }
}
