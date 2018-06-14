package com.ozturkburak.mapconquer.utils;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {




    public static String InputStreamToString(InputStream inputStream)
    {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
