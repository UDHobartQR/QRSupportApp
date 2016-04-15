package com.hobart.hobartqr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ImageButton mButton, button1;
    private Button nButton;
    public String param[];
    public String model_serial[];
    public String serialnum[];
    public String modelnum[];
    public String Serial;
    public String Model;

    private ListView lv;
    private String docUrl;

    private MobileServiceClient mClient;
    private MobileServiceTable<DocumentTable> mDocumentTable;
    private MobileServiceTable<RegistrationTable> mRegistrationTable;
    private EditText mZip;
    public RegistrationTable itemToRegister = new RegistrationTable("","","","");
    public String registered;
    public ArrayList<String> modelOptions = new ArrayList<String>();
    public ArrayList<String> modelDocuments = new ArrayList<String>();

    private ProgressBar progressBar;
    public TextView homeTextView;


    private GoogleApiClient client;

    @Override
    public void setContentView(int layoutResID) {
        ((MyApplication) this.getApplication()).setLayoutId(layoutResID);
        Log.d("MyMessage", Integer.toString(((MyApplication) this.getApplication()).getLayoutId()));
        super.setContentView(layoutResID);
    }

    /*
            Handles the permissions request, if permission is denied the application exits as all requested
        permissions are required for app to function correctly.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0) {
            for(int i = 0; grantResults.length > i; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(),
                            "App Requires This Permission to Function, The App will now exit.", Toast.LENGTH_LONG)
                            .show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }, 2000);
                }
            }
        }
    }

    /*
            Main Create method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Current layout", Integer.toString(((MyApplication) this.getApplication()).getLayoutId()));
        /*
                Checks the running SDK version and if it is greater than 23 permissions
            are checked and requested as appropriate
         */
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                        }
                    }
                }
            }
        }
        /*
                Checks if there was a previous state of the app to indicate that
            the app was previously running and a screen change occurred. If a previous
            version is found that state is reloaded to retrieve any previous variables.
         */
        if(savedInstanceState != null)
        {
            if (((MyApplication) this.getApplication()).getLayoutId() == R.layout.activity_main) {
                setContentView(R.layout.activity_main);
                mButton = (ImageButton) findViewById(R.id.button);
                homeTextView = (TextView) findViewById(R.id.textView);
                homeTextView.setText("Scan QR Code");
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                        integrator.initiateScan();
                    }
                });
            }
            else
            {
                setContentView(R.layout.registration);
                button1 = (ImageButton) findViewById(R.id.button1);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                        popup.getMenuInflater()
                                .inflate(R.menu.menu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId() == R.id.one)
                                {
                                    setContentView(R.layout.activity_main);
                                    mButton = (ImageButton) findViewById(R.id.button);
                                    mButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                                            integrator.initiateScan();
                                        }
                                    });
                                }
                                return true;
                            }
                        });

                        popup.show();
                    }
                });

                nButton = (Button) findViewById(R.id.button2);
                nButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.document_viewer);
                        button1 = (ImageButton) findViewById(R.id.button1);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                                popup.getMenuInflater()
                                        .inflate(R.menu.menu, popup.getMenu());

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getItemId() == R.id.one) {
                                            setContentView(R.layout.activity_main);
                                            mButton = (ImageButton) findViewById(R.id.button);
                                            mButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                                                    integrator.initiateScan();
                                                }
                                            });
                                        }
                                        return true;
                                    }
                                });

                                popup.show();
                            }
                        });
                    }
                });
            }

        }
        else
        {
            Log.d("MyMessage4","Here");
            setContentView(R.layout.activity_main);
            mButton = (ImageButton) findViewById(R.id.button);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.initiateScan();
                }
            });
        }


        /*
                Attempt to create the connection to the Mobile service
         */
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://hobartdocumentstest.azure-mobile.net/",
                    "UBGKtAqFaKNELsXsqBsULPNOKZGmHq54",
                    this); //.withFilter(new ProgressFilter());

            // Get the Mobile Service Table instance to use
            mDocumentTable = mClient.getTable(DocumentTable.class);
            mRegistrationTable = mClient.getTable(RegistrationTable.class);
        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*
            Method for handling the scan result of the camera/QR library
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Progress Bar
        mButton = (ImageButton) findViewById(R.id.button);
        mButton.setVisibility(View.GONE);
        homeTextView = (TextView) findViewById(R.id.textView);
        homeTextView.setText("Retrieving Info");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(10);
        progressBar.setProgress(0);

        String contents = null;
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        progressBar.setProgress(2);
        if (scanResult != null) {
            String re = scanResult.getContents();
            contents = re;
        }
        if(contents != null) {
            progressBar.setProgress(4);
            /*
                    Checks that a valid QR code was scanned and if so splits the
                relevant information into the model and serial numbers
             */
            if (contents.contains("hobartqrsupport")) {
                try {
                    param = contents.split("\\?");
                    model_serial = param[1].split("\\&");
                    serialnum = model_serial[1].split("\\=");
                    modelnum = model_serial[0].split("\\=");
                    Model = modelnum[1];
                    Serial = serialnum[1];
                    registered = "not checked";
                    progressBar.setProgress(6);
                } catch (Exception e) {
                    progressBar.setVisibility(ProgressBar.GONE);
                    homeTextView.setText("Scan QR Code");
                    mButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Not a valid Hobart QR, Please:\n Try Again,try another QR or contact Support", Toast.LENGTH_LONG)
                            .show();
                }
                new AsyncTask<String, Integer, String>() {

                    @Override
                    protected String doInBackground(String... args) {
                        try {
                            publishProgress(8);
                            final MobileServiceList<RegistrationTable> result = mRegistrationTable
                                    .where().field("serial_number").eq(Serial).execute().get();
                            publishProgress(10);

                            if (!result.isEmpty()) {
                                // True that item is registered
                                return "True";
                            } else {
                                // False that item is registered
                                return "False";
                            }
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            return exception.getMessage();
                        }
                    }

                    protected void onProgressUpdate(Integer... values) {
                        progressBar.setProgress(values[0]);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        progressBar.setVisibility(View.GONE);
                        if (result == "True") {
                            getDocuments();
                        } else if (result == "False") {
                            registrationPage();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error checking registration, Please Try Scanning Again", Toast.LENGTH_LONG).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    setContentView(R.layout.activity_main);
                                    mButton = (ImageButton) findViewById(R.id.button);
                                    mButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                                            integrator.initiateScan();
                                        }
                                    });
                                }
                            }, 2000);
                        }
                        ;
                    }

                }.execute();
            } else {
                progressBar.setVisibility(ProgressBar.GONE);
                homeTextView.setText("Scan QR Code");
                mButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),
                        "Not a valid Hobart QR, Please:\n Try Again,try another QR or contact Support", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
            homeTextView.setText("Scan QR Code");
            mButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),
                    "Not a valid Hobart QR, Please:\n Try Again,try another QR or contact Support", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void registrationPage( ) {
        setContentView(R.layout.registration);
        TextView model = (TextView) findViewById(R.id.model);
        model.setText(Model);
        Log.d("codeR", Model);
        TextView serial = (TextView) findViewById(R.id.serial);
        serial.setText(Serial);
        Log.d("codeR", Serial);

        itemToRegister.setModel(Model);
        itemToRegister.setSerial(Serial);

        button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                popup.getMenuInflater()
                        .inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.one) {
                            setContentView(R.layout.activity_main);
                            mButton = (ImageButton) findViewById(R.id.button);
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                                    integrator.initiateScan();
                                }
                            });
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
        nButton = (Button) findViewById(R.id.button2);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZip = (EditText) findViewById(R.id.zip);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mZip.getWindowToken(), 0);

                if (mZip.getText().toString().equals("")) {

                } else {
                    itemToRegister.setZip(mZip.getText().toString());
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                mRegistrationTable.insert(itemToRegister).get();
                            } catch (Exception exception) {
                                createAndShowDialog(exception, "Error");
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void Void) {
                            getDocuments();
                        }

                    }.execute();

                }
            }
        });
    }

    public void getDocuments () {
        Log.d("getDocuments:", "getDocuments Called");
        AsyncTask mAsync2 = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MobileServiceList<DocumentTable> result = mDocumentTable
                            .where().field("model_number").eq(Model).execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if ( !result.isEmpty() ) {
                                modelOptions.clear();
                                modelDocuments.clear();

                                if ( !result.get(0).getManual().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getManual() );
                                    modelOptions.add( "Product Manual" );
                                }
                                if ( !result.get(0).getSpecSheet().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getSpecSheet() );
                                    modelOptions.add( "Spec Sheet" );
                                }
                                if ( !result.get(0).getPartsNumber().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getPartsNumber() );
                                    modelOptions.add( "Parts Catalog" );
                                }
                                if ( !result.get(0).getSupport().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getSupport() );
                                    modelOptions.add( "Support" );
                                }
                                if ( !result.get(0).getOperatorManual().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getOperatorManual() );
                                    modelOptions.add( "Operator Manual" );
                                }
                                if ( !result.get(0).getQuickStart().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getQuickStart() );
                                    modelOptions.add( "Quick Start Guide" );
                                }
                                if ( !result.get(0).getSupervisorMan().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getSupervisorMan() );
                                    modelOptions.add( "Supervisor Manual" );
                                }
                                if ( !result.get(0).getReplacementURL().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getReplacementURL() );
                                    modelOptions.add( "Replacement Machine" );
                                }
                                if ( !result.get(0).getInstallationMan().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getInstallationMan() );
                                    modelOptions.add( "Installation Manual" );
                                }
                                if ( !result.get(0).getAccInstructions().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getAccInstructions() );
                                    modelOptions.add( "Accessory Instructions" );
                                }
                                if ( !result.get(0).getWallChart().isEmpty() ) {
                                    modelDocuments.add( result.get(0).getWallChart() );
                                    modelOptions.add( "Wall Chart" );
                                }
                            }
                        }
                    });
                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                documentPage();
            }


        }.execute();

    }

    public void documentPage() {

        setContentView(R.layout.document_viewer);
        TextView model = (TextView) findViewById(R.id.model1);
        model.setText(Model);
        TextView serial = (TextView) findViewById(R.id.serial1);
        serial.setText(Serial);

        lv = (ListView) findViewById(R.id.listView);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, modelOptions);

        // Assign adapter to ListView
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                docUrl = modelDocuments.get( modelOptions.indexOf(itemValue) );

                try {
                    if(itemValue != "Support") {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(docUrl));
                        String[] split1 = docUrl.split("\\/");
                        String[] split2 = split1[split1.length - 1].split("\\.");
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Model + "-" + split2[0]);
                        r.allowScanningByMediaScanner();
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                    }
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(getApplicationContext(),
                            "Not a valid url for " + itemValue, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                popup.getMenuInflater()
                        .inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.one) {
                            setContentView(R.layout.activity_main);
                            mButton = (ImageButton) findViewById(R.id.button);
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                                    integrator.initiateScan();
                                }
                            });
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        createAndShowDialog(exception.toString(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ToDo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.hobart.hobartqr/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ToDo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.hobart.hobartqr/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}