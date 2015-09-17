# AAHC
# Another Android HTTP Client




## FEATURES

* Simple
* It uses HttpURLConnection (cfg [Marshmallow behavior changes](https://developer.android.com/preview/behavior-changes.html#behavior-apache-http-client) )
* Handle BIG Files very well
* Progress handler
* Use a Pool (default 4 threads) to optimize/parallelize network requests
* No Cache (maybe an ANTIFEATURE, but not for me)

## STATUS

___ABSOLUTE ALPHA___


## EXAMPLES

To Fetch an URL and put the content into a String

``` java
    AAHC
    .use(this)
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

_Quite easy, isnt'it?_

To Fetch an URL and put the content into a JSONObject

``` java
    AAHC
    .use(this)
    .toGet("http://test.fluidware.it/api/v1/data.json")
    .into(
        new JSONObjectResponse() {
            @Override
            public void done(JSONObject json) {
                // json is the JSONObject fetched
            }
        }
    );
```


[See more example](https://github.com/macno/aahc/wiki/Examples)


## TODO

* handle 3xx responses
* handle 5xx responses
* post methods
* head methods
* delete methods
* put methods
* patch methods


## FAQ

* __Do we really need it?__  
Well, I think so.
