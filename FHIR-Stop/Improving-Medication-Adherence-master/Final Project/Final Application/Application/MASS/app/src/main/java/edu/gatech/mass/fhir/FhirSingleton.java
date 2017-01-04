package edu.gatech.mass.fhir;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


import edu.gatech.mass.model.Patient;
import edu.gatech.mass.parsers.PatientJSONParser;

public class FhirSingleton {
    private static FhirSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    //"http://polaris.i3l.gatech.edu:8080/fhir-omopv5/base/";

    private FhirSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized FhirSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FhirSingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
