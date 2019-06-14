
package svu.org;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class HttpCallImage {

    public static final int GET = 1;
    public static final int POST = 2;

    private String url;
    private int methodtype;
    private HashMap<String, FileOutputStream> params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethodtype() {
        return methodtype;
    }

    public void setMethodtype(int methodtype) {
        this.methodtype = methodtype;
    }

    public HashMap<String, FileOutputStream> getParams() {
        return params;
    }

    public void setParams(HashMap<String, FileOutputStream> params) {
        this.params = params;
    }
}
