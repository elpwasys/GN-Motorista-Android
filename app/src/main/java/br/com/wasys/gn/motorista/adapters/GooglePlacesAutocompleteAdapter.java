package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import br.com.wasys.gn.motorista.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by fernandamoncores on 4/29/16.
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<String> resultList;
    private static final String LOG_TAG = "Google Places Autocomplete";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private final String apiKey;


    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        apiKey = context.getString(R.string.google_maps_key);
    }

    @Override
    public int getCount() {
        return resultList != null ? resultList.size() : 0;
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    public ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            System.out.println("Entrei aquiii");
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + apiKey);
            sb.append("&components=country:br");
            sb.append("&language=pt-BR");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            System.out.println("URL:"+sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            System.out.println("Resultados:"+jsonResults.length());
        } catch (MalformedURLException e) {
            System.out.println("Erro 1");
            return resultList;
        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error connecting to Places API", e);
            System.out.println("Erro 2");
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<String>();
            if(predsJsonArray.length() == 0)
            {
                resultList.add("Não foram encontrados resultados!");
            }

            // Extract the Place descriptions from the results

            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println(predsJsonArray.getJSONObject(i));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            System.out.println("To caindo no log 3 seu boiola");
            //Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
