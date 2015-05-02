/*
 * Copyright 2014 University Of Wisconsin Parkside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwp.cs.edu.parkingtracker.network;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import uwp.cs.edu.parkingtracker.CONSTANTS;

/**
 * Simple implementation of a REST Client in Java.
 *
 * @author <a href="mailto:hello@mateo.io">Francisco Mateo</a>
 * @version 0.0.2
 * @modified David Krawchuk 11/2014
 * @modified Nate Eisner 3/23/2015
 */
public class RESTClient extends AsyncTask<ArrayList<String>, Void, String> {

    // Instance Begin
    private URL url = null;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private String result = null;
    private String httpMethod = null;
    // Instance End

    /**
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(ArrayList<String>... params) {
        String toGetOrPut = params[0].get(1).toString();
        try {
            if (params[0].size() > 2) {
                throw new IllegalArgumentException(
                        "You have passed in more than (2) params for the String arraylist: "
                                + params[0].size());
            } else if (params[0].size() < 2) {
                throw new IllegalArgumentException(
                        "You have passed in less than the required length (2) of params for the String arraylist.");
            } else {

                httpMethod = params[0].get(0).toUpperCase();

                switch (httpMethod) {

                    case CONSTANTS.GET:
                        result = this.get(toGetOrPut);
                        break;
                    case CONSTANTS.PUT:
                        this.put(toGetOrPut);
                        result = "";
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "First param in String arraylist is neither GET nor PUT.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }


    /**
     * Calls the REST API. PUTs the passed parameters.
     * <p/>
     * <p>
     * This method only takes one parameter, the String URL to desired path to
     * the REST service. Parameters are passed in the URL String and are
     * processed on the server side.
     * </p>
     * <p/>
     * <h2>Plain Java</h2>
     * <p/>
     * <pre>
     * RESTClient client = new RESTClient();
     * client.post(&quot;v1/professors/insert/&quot; + name + &quot;/&quot; + dept);
     * </pre>
     * <p/>
     * <h2>Extending Android AsyncTask</h2>
     * <p/>
     * <pre>
     * ArrayList&lt;String&gt; params = new ArrayList&lt;&gt;();
     * params.add(&quot;GET&quot;); // Http method
     * params.add(&quot;nope/7/7/0/1/0&quot;); // Pass in params
     *
     * new RESTClient(REST_API).execute(params); // Execute
     * </pre>
     *
     * @param apiCall the route of the REST API desired
     * @return String message of success
     */
    private String put(String apiCall) {

        // fetch data
        try {
            pushResultsToDatabase(apiCall);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns a JSON string of objects from the server that can be parsed with
     * desired JSON library.
     *
     * @param apiCall the route of the REST API desired
     * @return JSON string of objects from server
     * @see #//put()
     */
    private String get(String apiCall) {

        try {
            fetchResultsFromDatabase(apiCall);

        } catch (MalformedURLException | ProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    /**
     * @param apiCall
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    private void fetchResultsFromDatabase(String apiCall) throws MalformedURLException, ProtocolException, IOException {
        this.url = new URL(CONSTANTS.REST_URL + apiCall);

        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection = doLogin(this.connection);

        if (this.connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP CONNECTION != 200: "
                    + this.connection.getResponseCode());
        }

        this.reader = new BufferedReader(new InputStreamReader(
                this.connection.getInputStream()));

        char ch;
        do {
            reader.mark(1);
            ch = (char) reader.read();
        } while (ch == '\r' || ch == '\n');

        // Reset reader position.
        this.reader.reset();

        result = this.reader.readLine();
        connection.disconnect();
    }

    /**
     * @param apiCall
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    private void pushResultsToDatabase(String apiCall) throws MalformedURLException, ProtocolException, IOException {
        this.url = new URL(CONSTANTS.REST_URL + apiCall);
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection = doLogin(this.connection);
        Log.i("APICALL: ", apiCall);
        Log.i("URL: ", this.url.toString());
        if (this.connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP CONNECTION != 200 "
                    + this.connection.getResponseCode());
        } else {
            // display error
            connection.disconnect();
            result = "Success";
        }
    }

    /**
     * Logs in to server
     * @param urlConnection
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    private HttpURLConnection doLogin(HttpURLConnection urlConnection) throws MalformedURLException, ProtocolException, IOException {

        byte[] auth = (CONSTANTS.USER + ":" + CONSTANTS.PASS).getBytes();
        String basic = Base64.encodeToString(auth, Base64.NO_WRAP);
        urlConnection.setRequestProperty("Authorization", "Basic " + basic);

//        //throws Exception
//        int response = urlConnection.getResponseCode();
//        Log.i("TEST URL: ", url.toString());
//        Log.i("Login Response: ",String.valueOf(response));
        return urlConnection;
    }
}