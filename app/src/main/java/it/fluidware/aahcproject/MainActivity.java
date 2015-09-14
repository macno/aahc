package it.fluidware.aahcproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;

import it.fluidware.aahc.AAHC;
import it.fluidware.aahc.Response;
import it.fluidware.aahc.impl.FileResponse;
import it.fluidware.aahc.impl.JSONArrayResponse;
import it.fluidware.aahc.impl.JSONObjectResponse;
import it.fluidware.aahc.impl.StringResponse;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        testSix();

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
        try {


            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://catalist.fluidware.it/api/v1/metadata")
                    .into(
                            new StringResponse() {

                                @Override
                                public void done(String obj) {
                                    setText(obj);
                                }
                            }
                    );

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }



        try {
            File zip = new File(getFilesDir(),"big.zip");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://longisland.fluidware.it/clienti/elfin/1407866221_01-Imagine.mp3")
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

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }
    }

    private void testTwo() {
        Log.d(AAHC.NAME,"Starting testTwo");
        try {


            AAHC
                    .use(this)
                    .toGet("http://catalist.fluidware.it/api/v1/metadata")
                    .into(
                            new JSONArrayResponse() {
                                @Override
                                public void done(JSONArray obj) {
                                    setText("Trovati " + obj.length() + " elementi");
                                }
                            }
                    );

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }
    }

    private void testThree() {
        for(int i=0;i<30;i++) {
            try {
                final int x = i;
                AAHC
                        .use(this)
                        .toGet("http://catalist.fluidware.it/api/v1/metadata?t="+x)
                        .into(
                                new JSONArrayResponse() {
                                    @Override
                                    public void done(JSONArray obj) {
                                        Log.d(AAHC.NAME,"done " + x);
                                    }
                                }
                        );

            } catch (MalformedURLException e) {
                Log.e("AAHC", "MalformedURLException: " + e.toString());
            }
        }
    }

    private void testFour() {
        for(int i=0;i<12;i++) {
            try {
                final int x = i;
                File f = new File(getFilesDir(),"Test_"+i+".zip");
                AAHC
                        .use(this)
                        .toGet("http://catalist.fluidware.it/resources/v1/a2501c8b-3829-47c2-966a-bdcb617d5b50/base.zip")
                        .into(
                                new FileResponse(f) {
                                    @Override
                                    public void done(File obj) {
                                        Log.d(AAHC.NAME, "done " + x + " " + obj.getAbsolutePath());
                                    }
                                }
                        );

            } catch (MalformedURLException e) {
                Log.e("AAHC", "MalformedURLException: " + e.toString());
            }
        }
    }

    private void testFive() {
        try {
            File image = new File(getFilesDir(),"prova.png");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://catalist.fluidware.it/resources/v1/a2501c8b-3829-47c2-966a-bdcb617d5b50/assets/cover.png")
                    .into(
                            new FileResponse(image) {

                                @Override
                                public void done(File f) {
                                    setImage(f);
                                }
                            }
                    );

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }
    }

    private void testFiveBis() {
        try {
            final File image = new File(getFilesDir(),"prova.png");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://catalist.fluidware.it/resources/v1/a2501c8b-3829-47c2-966a-bdcb617d5b50/assets/cover.png")
                    .ifModified(image.lastModified())
                    .into(
                            new FileResponse(image) {

                                @Override
                                public void done(File f) {
                                    if(f == null) {
                                        Log.d(AAHC.NAME,"File not modified!");
                                        setImage(image);
                                    } else {
                                        setImage(f);
                                    }

                                }
                            }
                    );

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }
    }


    /**
     *
     * Return 404
     *
     */
    private void testSix() {
        try {
            File image = new File(getFilesDir(),"prova.png");
            AAHC
                    .use(this)
                    .as("Test/1.0")
                    .toGet("http://catalist.fluidware.it/resources/v1/a2501c8b-3829-47c2-966a-bdcb617d5b50/assets/coverx1s.png")
                    .whenError(new AAHC.ErrorListener() {

                        @Override
                        public void onError(Exception e) {
                            Log.e(AAHC.NAME,"Error while getting image",e);
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

        } catch (MalformedURLException e) {
            Log.e("AAHC", "MalformedURLException: " + e.toString());
        }
    }
    @Override
    protected void onDestroy() {
        AAHC.use(this).clear();
        super.onDestroy();

    }
}
