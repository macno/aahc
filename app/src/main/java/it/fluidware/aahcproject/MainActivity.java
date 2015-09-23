package it.fluidware.aahcproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.fluidware.aahc.AAHC;
import it.fluidware.aahc.HttpException;
import it.fluidware.aahc.impl.EmptyResponse;
import it.fluidware.aahc.impl.FileResponse;
import it.fluidware.aahc.impl.JSONArrayResponse;
import it.fluidware.aahc.impl.StringResponse;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        testTwelve();

    }

    private void showProgress(long total,long read) {
        ((TextView)findViewById(R.id.tv_test_progress)).setText(read + "/" + total);
    }

    private void showComplete() {
        TextView tv = ((TextView)findViewById(R.id.tv_test_progress));
        tv.setText(tv.getText() + " Complete");
    }

    private void setImage(int color) {
        findViewById(R.id.iv_test).setBackgroundColor(color);
    }
    private void setImage(File f) {
        Log.d("AAHC","Imagefile: " + f.exists() + " size: " + f.length());
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        ((ImageView)findViewById(R.id.iv_test)).setImageBitmap(bmp);
    }
    private void setText(String text) {
        ((TextView)findViewById(R.id.tv_test)).setText(text);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void testOne() {
        AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://aahc.fluidware.it/")
                    .withHeader("Device-Id","MyUniqueId")
                    .into(
                            new StringResponse() {

                                @Override
                                public void done(String obj) {
                                    setText(obj);
                                }
                            }
                    );



        File zip = new File(getFilesDir(),"big.zip");
        AAHC
                .use(this)
                .as("Test/1.0")
                .toGet("http://aahc.fluidware.it/assets/5M.zip")
                .whileProgress(new AAHC.ProgressListener() {

                    private long total = 0;

                    @Override
                    public void total(long bytes) {
                        this.total = bytes;
                        Log.d("AAHC", "Filesize: " + bytes);
                    }

                    @Override
                    public void progress(long read) {
                        showProgress(total, read);
                    }

                    @Override
                    public void complete() {
                        showComplete();
                    }


                })
                .into(
                        new FileResponse(zip) {

                            @Override
                            public void done(File f) {
                                Log.d("AAHC", "Download completato: " + f.length());
                            }
                        }
                );


    }

    private void testTwo() {
        Log.d(AAHC.NAME, "Starting testTwo");
        AAHC
            .use(this)
            .toGet("http://aahc.fluidware.it/api/v1/data.json")
            .into(
                    new JSONArrayResponse() {
                        @Override
                        public void done(JSONArray obj) {
                            setText("Trovati " + obj.length() + " elementi");
                        }
                    }
            );
    }

    private void testThree() {
        for(int i=0;i<30;i++) {

            final int x = i;
            AAHC
                    .use(this)
                    .toGet("http://aahc.fluidware.it/api/v1/data.json?t=" + x)
                    .into(
                            new JSONArrayResponse() {
                                @Override
                                public void done(JSONArray obj) {
                                    Log.d(AAHC.NAME, "done " + x);
                                }
                            }
                    );


        }
    }

    private void testFour() {
        for(int i=0;i<12;i++) {

            final int x = i;
            File f = new File(getFilesDir(),"Test_"+i+".zip");
            AAHC
                    .use(this)
                    .toGet("http://aahc.fluidware.it/assets/5M.zip")
                    .into(
                            new FileResponse(f) {
                                @Override
                                public void done(File obj) {
                                    Log.d(AAHC.NAME, "done " + x + " " + obj.getAbsolutePath());
                                }
                            }
                    );


        }
    }

    private void testFive() {

        File image = new File(getFilesDir(),"image.jpg");
        AAHC
                .use(this)
                .as("Test/1.0")
                .toGet("http://aahc.fluidware.it/assets/image.jpg")
                .into(
                        new FileResponse(image) {

                            @Override
                            public void done(File f) {
                                setImage(f);
                            }
                        }
                );

    }

    private void testFiveBis() {

            final File image = new File(getFilesDir(),"image.jpg");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://aahc.fluidware.it/assets/image.jpg")
                    .ifModified(image.lastModified())
                    .into(
                            new FileResponse(image) {

                                @Override
                                public void done(File f) {
                                    if (f == null) {
                                        Log.d(AAHC.NAME, "File not modified!");
                                        setImage(image);
                                    } else {
                                        setImage(f);
                                    }

                                }
                            }
                    );

    }


    /**
     *
     * Return 404
     *
     */
    private void testSix() {

            File image = new File(getFilesDir(),"prova.png");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://aahc.fluidware.it/not-found")
                    .whenError(new AAHC.ErrorListener() {

                        @Override
                        public void onError(Exception e) {
                            Log.e(AAHC.NAME, "Client Error while getting image", e);
                            setImage(Color.RED);
                        }

                    })
                    .into(
                            new FileResponse(image) {

                                @Override
                                public void done(File f) {
                                    setImage(f);
                                }
                            }
                    );

    }


    /**
     *
     * Return 500
     *
     */
    private void testSeven() {

        File image = new File(getFilesDir(),"prova.png");
        AAHC
                .use(this)
                .as("Test/1.0")
                .toGet("http://aahc.fluidware.it/error/500")
                .whenError(new AAHC.ErrorListener() {

                    @Override
                    public void onError(Exception e) {
                        Log.e(AAHC.NAME, "Server Error while getting image", e);
                        setImage(Color.RED);
                    }

                })
                .into(
                        new FileResponse(image) {

                            @Override
                            public void done(File f) {
                                setImage(f);
                            }
                        }
                );

    }

    /**
     *
     * Follow redirections
     *
     */
    private void testEight() {


        AAHC
                .use(this)
                .toGet("http://aahc.fluidware.it/redirect/302")
                .whenError(new AAHC.ErrorListener() {

                    @Override
                    public void onError(Exception e) {
                        if(e instanceof HttpException) {
                            Log.e(AAHC.NAME, "Http error following link: " + ((HttpException) e).getCode(), e);
                        } else {
                            Log.e(AAHC.NAME, "Error following link", e);
                        }

                    }

                })
                .into(
                        new StringResponse() {

                            @Override
                            public void done(String s) {
                                Log.d(AAHC.NAME,"Got: " +s);
                                setText("Followed redirect");
                            }
                        }
                );

    }

    /**
     *
     * Head
     *
     */
    private void testNine() {


        AAHC
                .use(this)
                .toHead("http://aahc.fluidware.it/redirect/302")
                .into(
                        new EmptyResponse() {

                            @Override
                            public void done(Boolean s) {
                                Log.d(AAHC.NAME, "HEAD DONE");
                                Map<String, List<String>> headers = getHeaders();

                                Iterator it = headers.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry)it.next();


                                    List<String> entries = headers.get(pair.getKey());

                                    Log.d(AAHC.NAME,""+pair.getKey());
                                    if (entries != null) {
                                        for (String val : entries) {
                                            Log.d(AAHC.NAME, ".\t\t"+val);
                                        }
                                    }

                                }
                            }
                        }
                );

    }

    /**
     * POST form
     */
    private void testTen() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key1","value1");
        params.put("key2", "value2");
        AAHC
                .use(this)
                .toPost("http://aahc.fluidware.it/post/form", false, params)
                .into(
                        new StringResponse() {

                            @Override
                            public void done(String s) {
                                Log.d(AAHC.NAME, "POST DONE");
                                Log.d(AAHC.NAME, "RESPONSE: " + s);

                            }
                        }
                );

    }


    /**
     * POST file
     */
    private void testEleven() {

        final File image = new File(getFilesDir(),"image.jpg");
        if(!image.exists()) {
            AAHC
                    .use(this)
                    .toGet("http://aahc.fluidware.it/assets/image.jpg")
                    .into(
                            new FileResponse(image) {

                                @Override
                                public void done(File f) {
                                    if (f != null) {
                                        Log.d(AAHC.NAME, "IMAGE OK");
                                        testEleven();
                                    } else {
                                        Log.d(AAHC.NAME, "IMAGE FAILED");
                                    }
                                }
                            }
                    );
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("key1","value1");
        params.put("key2", image);
        AAHC
                .use(this)
                .toPost("http://aahc.fluidware.it/post/file", true, params)
                .into(
                        new StringResponse() {

                            @Override
                            public void done(String s) {
                                Log.d(AAHC.NAME, "POST DONE");
                                Log.d(AAHC.NAME, "RESPONSE: " + s);

                            }
                        }
                );

    }

    /**
     * POST inline
     */
    private void testTwelve() {

        try {
            JSONObject o = new JSONObject();
            o.put("key1", "one").put("key2", "two");

            AAHC
                    .use(this)
                    .toPost("http://aahc.fluidware.it/post/json", "application/json", o.toString())
                    .into(
                            new StringResponse() {

                                @Override
                                public void done(String s) {
                                    Log.d(AAHC.NAME, "POST DONE");
                                    Log.d(AAHC.NAME, "RESPONSE: " + s);

                                }
                            }
                    );

        } catch (JSONException e) {
            Log.e(AAHC.NAME,e.toString(),e);
        }

    }

    @Override
    protected void onDestroy() {
        AAHC.use(this).clear();
        super.onDestroy();

    }
}
