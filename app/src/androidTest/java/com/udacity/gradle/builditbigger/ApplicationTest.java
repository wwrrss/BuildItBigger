package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    CountDownLatch countDownLatch=null;
    Exception mError = null;
    String response=null;
    Context context=null;
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {

        countDownLatch = new CountDownLatch(1);



    }

    @Override
    protected void tearDown() throws Exception {

        countDownLatch.countDown();
    }
    public void testTask() throws  InterruptedException {

        MainActivity.EndPointAsyncTask endPointAsyncTask=new MainActivity.EndPointAsyncTask();
        endPointAsyncTask.setEndPointInterface(new MainActivity.EndPointInterface() {
            @Override
            public void onResult(String result,Exception e) {
                response=result;
                mError=e;
                countDownLatch.countDown();
            }
        });
        //context is null here
        endPointAsyncTask.execute(context);
        countDownLatch.await();
        assertNull(mError);
        assertNotNull(response);
    }
}