package com.hobart.hobartqr;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class MyApplication extends Application{
    private int layoutId;
    public int getLayoutId(){
        return layoutId;
    }
    public void setLayoutId(int id){
        this.layoutId = id;
    }
}
/*public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}*/