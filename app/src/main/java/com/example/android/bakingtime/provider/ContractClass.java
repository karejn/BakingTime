package com.example.android.bakingtime.provider;

import android.net.Uri;
import android.provider.BaseColumns;



public class ContractClass {
    static final String AUTHORITY="com.example.android.bakingtime";
    static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);
    static final String PATH_PERSON="ingredtable";

    public static final class nameClass implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERSON).build();

        public static final String COLUMN_INGRED_KEY = "key";
        static final String TABLENAME = "ingredtable";
        static final String COLUMN_INGRED_VALUE = "value";
        static final String COLUMN_INGRED_QUANTITY = "quantity";
        static final String COLUMN_INGRED_MEASURE = "measure";
    }
}
