package com.myapp.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomSearch {
    private String keyword;
    private Context context;

    public CustomSearch(Context context, String keyword) {
        this.keyword = keyword;
        this.context = context;
    }

    public void fetchImage() throws IOException {
        String key = "AIzaSyCRfETOftgCDHVkETpY_d9fIS_1QMRggh8";
        String cx = "85d1fa9cad0444d68";
        String qry = this.keyword;
        String link = null;
        List<String> linkList = new ArrayList<String>();

        //Make a request to the Google Custom Search API
        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&q=" + qry
                        + "&searchType=image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        //The response is json
        //Process to find URL from json
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {

            if (output.contains("\"link\": \"")) {

                link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(),
                        output.indexOf("\","));
                System.out.println(link);
                linkList.add(link);
            }
        }
        conn.disconnect();

        //The process of downloading an image from a URL.
        URL imageURL = null;
        HttpURLConnection urlConnection = null;

        for (int i = 0; i < 10; i++) {

            try {
                imageURL = new URL(linkList.get(i));

                urlConnection = (HttpURLConnection) imageURL.openConnection();
                //If true, allow redirects
                urlConnection.setInstanceFollowRedirects(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //InputStream is a superclass of all classes that represent a byte input stream.
            InputStream in = null;

            try {
                in = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] buf = new byte[4096];
            int readSize;
            int total = 0;
            try {
                FileOutputStream fos = new FileOutputStream("C:\\Users\\user\\Downloads\\" + "test" + i + ".jpg ");

                //The read method is a value if there are no bytes to read because the stream has reached the end of the file-1 is returned.
                //The first byte read is element b[0]Stored in, the next byte is b[1]It is stored in, and so on.
                //The maximum number of bytes that can be read is the same as the length of b.
                while (((readSize = in.read(buf)) != -1)) {
                    total = total + readSize;

                    //Writes readSize bytes starting at offset position 0 of the specified byte array to this file output stream.
                    fos.write(buf, 0, readSize);
                }
                fos.flush();
                fos.close();
                in.close();
            } catch (FileNotFoundException e) {
                System.out.println("File error");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Size:" + total);
        }
    }
}