# AAHC
# Another Android HTTP Client

## EXAMPLES

### Set your User-Agent

``` java
 AAHC
    .use(this)
    .as("MyCoolApp/2.0")
    .toGet("http://test.fluidware.it/")
    .into(
        new StringResponse() {
            @Override
            public void done(String str) {
                // str contains URL body
            }
        }
    );
```


### Fetching Files

``` java

File outFile = new File("/path/to/your/bigfile.zip");

AAHC
    .use(this)
    .toGet("http://test.fluidware.it/bigfile.zip")
    .into(
        new FileResponse(outFile) {
            @Override
            public void done(File f) {
                // Do what you need with your file f
            }
        }
    );
```

Fetching only if remote file is newer than local

``` java

final File outFile = new File("/path/to/your/localfile.zip");

AAHC
    .use(this)
    .toGet("http://test.fluidware.it/remote.zip")
    .ifModified(outFile.exists() ? outFile.lastModified() : 0)
    .into(
        new FileResponse(outFile) {
            @Override
            public void done(File f) {
                // if f != null server sent a newer file
                if(f != null) {
                    use(f);
                } else { // Server replied with 304, just use the old file
                    use(outFile);
                }
            }
        }
    );

```

Fetching big file and beeing notified of progress

``` java

File zip = new File(getFilesDir(),"big.zip");
AAHC
        .use(this)
        .toGet("http://test.fluidware.it/big-file.zip")
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
                        Log.d("AAHC", "Download completed: " + f.length());
                    }
                }
        );

```

