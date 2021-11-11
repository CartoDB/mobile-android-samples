package com.carto.advancedmap.sections.geocoding.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.advancedmap.sections.geocoding.GeocodingUtils;
import com.carto.advancedmap.sections.geocoding.adapter.GeocodingResultAdapter;
import com.carto.advancedmap.utils.Colors;
import com.carto.geocoding.GeocodingRequest;
import com.carto.geocoding.GeocodingResult;
import com.carto.geocoding.GeocodingResultVector;
import com.carto.geocoding.GeocodingService;
import com.carto.geocoding.MapBoxOnlineGeocodingService;
import com.carto.geocoding.PackageManagerGeocodingService;
import com.carto.geocoding.PeliasOnlineGeocodingService;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for non-reverse (type address) geocoding
 */
public class GeocodingBaseActivity extends BaseGeocodingActivity {

    EditText inputField;
    ListView resultTable;
    GeocodingResultAdapter adapter;

    protected GeocodingService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputField = new EditText(this);
        inputField.setTextColor(Color.WHITE);
        inputField.setBackgroundColor(Colors.DARK_TRANSPARENT_GRAY);
        inputField.setHint("Type address...");
        inputField.setHintTextColor(Color.LTGRAY);
        inputField.setSingleLine();
        inputField.setImeOptions(EditorInfo.IME_ACTION_DONE);

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = (int)(5 * getResources().getDisplayMetrics().density);

        int width = totalWidth - 2 * padding;
        int height = (int)(45 * getResources().getDisplayMetrics().density);

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, height);
        parameters.setMargins(padding, padding, 0, 0);
        addContentView(inputField, parameters);

        resultTable = new ListView(this);
        resultTable.setBackgroundColor(Colors.LIGHT_TRANSPARENT_GRAY);

        height = (int)(200 * getResources().getDisplayMetrics().density);

        parameters = new RelativeLayout.LayoutParams(width, height);
        parameters.setMargins(padding, 2 * padding + inputField.getLayoutParams().height, 0, 0);

        addContentView(resultTable, parameters);

        adapter = new GeocodingResultAdapter(this);
        adapter.width = width;
        resultTable.setAdapter(adapter);

        hideTable();
    }

    @Override
    public void onResume() {
        super.onResume();

        contentView.mapView.setMapEventListener(new MapEventListener(){
            @Override
            public void onMapClicked(MapClickInfo mapClickInfo) {
                GeocodingBaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeKeyboard();
                        hideTable();
                    }
                });
            }
        });

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { }
            @Override

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String text = charSequence.toString();

                if (text.isEmpty()) {
                    return;
                }

                showTable();
                text = inputField.getText().toString();
                boolean autocomplete = true;

                geocode(text, autocomplete);
            }
        });

        inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    onEditingEnded(true);
                }
                return false;
            }
        });

        resultTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GeocodingResult result = adapter.items.get(i);
                showResult(result);
                onEditingEnded(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        contentView.mapView.setMapEventListener(null);
        inputField.addTextChangedListener(null);
        inputField.setOnEditorActionListener(null);
        resultTable.setOnItemClickListener(null);
    }

    public void showResult(GeocodingResult result) {
        String title = "";
        String description = GeocodingUtils.getPrettyAddress(result.getAddress());
        boolean goToPosition = true;

        showResult(result, title, description, goToPosition);
    }

    public void onEditingEnded(boolean geocode) {
        closeKeyboard();
        hideTable();

        if (geocode) {
            String text = inputField.getText().toString();
            boolean autocomplete = false;
            geocode(text, autocomplete);
        }

        clearInput();
    }

    int searchRequestId = 0;
    int displayRequestId = 0;
    List<GeocodingResult> addresses = new ArrayList<>();

    public void geocode(final String text, final boolean autocomplete) {
        int currentRequestIdInitial = 0;
        synchronized (this) {
            searchRequestId += 1;
            currentRequestIdInitial = searchRequestId;
        }
        final int currentRequestId = currentRequestIdInitial;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GeocodingRequest request = new GeocodingRequest(contentView.projection, text);

                if (service instanceof PackageManagerGeocodingService) {
                    ((PackageManagerGeocodingService)service).setAutocomplete(autocomplete);
                } else {
                    ((MapBoxOnlineGeocodingService)service).setAutocomplete(autocomplete);
                }

                GeocodingResultVector resultsInitial = null;
                try {
                    resultsInitial = service.calculateAddresses(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                synchronized (GeocodingBaseActivity.this) {
                    if (displayRequestId > currentRequestId) {
                        return; // a newer request has already finished
                    }
                    displayRequestId = currentRequestId;
                }

                final GeocodingResultVector results = resultsInitial;
                final int count = (int)results.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // In autocomplete mode just fill the autocomplete address list and reload tableview
                        // In full geocode mode, show the result
                        if (autocomplete) {
                            addresses.clear();

                            for (int i = 0; i < count; i++) {
                                GeocodingResult result = results.get(i);
                                addresses.add(result);
                            }

                            updateList();

                        } else if (count > 0) {
                            showResult(results.get(0));
                        }
                    }
                });
            }
        });

        thread.start();
    }

    public void updateList() {
        adapter.items = addresses;
        adapter.notifyDataSetChanged();
    }

    public void closeKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            Object service = getSystemService(INPUT_METHOD_SERVICE);
            InputMethodManager manager = (InputMethodManager)service;
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showTable() {
        resultTable.setVisibility(View.VISIBLE);
    }

    public void hideTable() {
        resultTable.setVisibility(View.INVISIBLE);
    }

    public void clearInput() {
        inputField.setText("");
    }
}
