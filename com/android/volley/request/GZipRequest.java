package com.android.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GZipRequest extends StringRequest {
    public GZipRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public GZipRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    /* access modifiers changed from: protected */
    public Response<String> parseNetworkResponse(NetworkResponse response) {
        String output = "";
        try {
            GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
            InputStreamReader reader = new InputStreamReader(gStream);
            BufferedReader in = new BufferedReader(reader);
            while (true) {
                String readLine = in.readLine();
                String read = readLine;
                if (readLine != null) {
                    output = output + read;
                } else {
                    reader.close();
                    in.close();
                    gStream.close();
                    return Response.success(output, HttpHeaderParser.parseCacheHeaders(response));
                }
            }
        } catch (IOException e) {
            return Response.error(new ParseError());
        }
    }
}