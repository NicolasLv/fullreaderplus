/*
	OpenOffice Document Reader is Android's first native ODF Viewer!
    Copyright (C) 2010  Thomas Taschauer - tomtasche@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package at.tomtasche.reader.background;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import at.tomtasche.reader.background.Document.Page;

import com.google.gson.Gson;

public class UpLoader extends AsyncTaskLoader<Document> implements FileLoader {

	private static final String SERVER_URL = "https://opendocument-engine.appspot.com/";

	private Uri uri;
	private Document document;
	private Throwable lastError;

	public UpLoader(Context context, Uri uri) {
		super(context);

		this.uri = uri;
	}

	@Override
	public Throwable getLastError() {
		return lastError;
	}

	@Override
	public Uri getLastUri() {
		return uri;
	}

	@Override
	public double getProgress() {
		return 0;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		if (document != null) {
			deliverResult(document);
		} else {
			forceLoad();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();

		onStopLoading();

		document = null;
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();

		cancelLoad();
	}

	@Override
	public Document loadInBackground() {
		if (uri == DocumentLoader.URI_INTRO) {
			cancelLoad();

			return null;
		}

		String type = getContext().getContentResolver().getType(uri);
		if (type == null)
			type = URLConnection.guessContentTypeFromName(uri.toString());

		if (type == null) {
			try {
				InputStream stream = getContext().getContentResolver()
						.openInputStream(uri);
				try {
					type = URLConnection.guessContentTypeFromStream(stream);
				} finally {
					stream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (type != null
				&& (type.equals("text/html") || type.equals("text/plain")
						|| type.equals("image/png") || type
							.equals("image/jpeg"))) {
			try {
				document = new Document(null);
				document.addPage(new Page("Document", new URI(uri.toString()),
						0));

				return document;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		String name = uri.getLastPathSegment();

		try {
			name = URLEncoder.encode(name, "UTF-8");
			type = URLEncoder.encode(type, "UTF-8");
		} catch (Exception e) {
		}

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(SERVER_URL + "file?name=" + name
				+ "&type=" + type);

		InputStream stream = null;
		try {
			stream = getContext().getContentResolver().openInputStream(uri);

			InputStreamEntity reqEntity = new InputStreamEntity(stream, -1);

			httppost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				Map<String, Object> container = new Gson().fromJson(
						EntityUtils.toString(response.getEntity()), Map.class);

				String key = container.get("key").toString();
				URI viewerUri = URI
						.create("https://docs.google.com/viewer?embedded=true&url="
								+ URLEncoder.encode(SERVER_URL + "file?key="
										+ key, "UTF-8"));

				document = new Document(null);
				document.addPage(new Page("Document", viewerUri, 0));
			} else {
				throw new RuntimeException("server couldn't handle request");
			}
		} catch (Throwable e) {
			e.printStackTrace();

			lastError = e;
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
			}

			httpclient.getConnectionManager().shutdown();
		}

		return document;
	}
}
