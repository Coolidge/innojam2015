/*
 * Copyright (C) 2012 Holger Oehm
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.holger_oehm.xfd.jenkins;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class ProxyHandler {
    private final Gson gson = new Gson();
    private HttpClient client = null;
    private HttpGet httpGet;
    private final String url = "https://eyelikei062070trial.hanatrial.ondemand.com/EyeLikeServlet/MainServlet";

    public ProxyHandler() {
        client = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("proxy", 8080, "http");
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        httpGet = new HttpGet(url);
    }

    public State state() throws ClientProtocolException, IOException {
        final HttpResponse response = client.execute(httpGet);
        final HttpEntity entity = response.getEntity();
        if (null == entity) {
            throw new IllegalStateException("no response from " + url);
        }
        final InputStream instream = entity.getContent();
        try {
            DTO data = gson.fromJson(new InputStreamReader(instream), DTO.class);
            return data.getState();
        } finally {
            instream.close();
        }
    }

}
