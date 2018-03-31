package de.cordulagloge.android.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class SomeActivity extends Activity {

    // Names for the arguments we pass to the
    // activity when we create it
    private final static String ARG_STRING = "ARG_STRING";
    private final static String ARG_INT = "ARG_INT";

    private String stringField;
    private int intField;
    private List<Object> arrayField;

    private enum DataHolder {
        INSTANCE;

        private List<Object> mObjectList;

        public static boolean hasData() {
            return INSTANCE.mObjectList != null;
        }

        public static void setData(final List<Object> objectList) {
            INSTANCE.mObjectList = objectList;
        }

        public static List<Object> getData() {
            final List<Object> retList = INSTANCE.mObjectList;
            INSTANCE.mObjectList = null;
            return retList;
        }
    }

    @Override
    protected void onCreate(final Bundle savedState) {
        super.onCreate(savedState);

        // Get the activity intent if there is a one
        final Intent intent = getIntent();

        // And retrieve arguments if there are any
        if (intent.hasExtra(ARG_STRING)) {
            stringField = intent.getExtras().getString(ARG_STRING);
        }
        if (intent.hasExtra(ARG_INT)) {
            intField = intent.getExtras().getInt(ARG_INT);
        }
        // And we retrieve large data from enum
        if (DataHolder.hasData()) {
            arrayField = DataHolder.getData();
        }

        // Now stringField, intField fields are available
        // within the class and can be accessed directly
    }

    /**
     * /** A static method for starting activity with supplied arguments
     *
     * @param context
     *            context that starts this activity
     * @param stringArg
     *            A string argument to pass to the new activity
     * @param intArg
     *            An int argument to pass to the new activity
     * @param objectList
     *            An object list argument to pass to the new activity
     */
    public static void startActivity(final Context context, final String stringArg,
                                     final int intArg, final List<Object> objectList) {

        // Initialize a new intent
        final Intent intent = new Intent(context, SomeActivity.class);

        // To speed things up :)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        // And add arguments to the Intent
        intent.putExtra(ARG_STRING, stringArg);
        intent.putExtra(ARG_INT, intArg);

        // Now we put the large data into our enum instead of using Intent extras
        DataHolder.setData(objectList);

        context.startActivity(intent);
    }
}

