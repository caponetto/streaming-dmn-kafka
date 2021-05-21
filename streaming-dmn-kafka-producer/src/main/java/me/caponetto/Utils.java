package me.caponetto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public final class Utils {

    private Utils() {
        //
    }

    public static Optional<String> readResource(final String path) {
        final StringBuilder sb = new StringBuilder();
        try (final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                return Optional.empty();
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return Optional.of(sb.toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
