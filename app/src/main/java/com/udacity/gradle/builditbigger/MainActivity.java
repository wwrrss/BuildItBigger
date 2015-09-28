package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.william.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import me.willermo.jokelibrary.MainJokeActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void tellJoke(View view){
        //Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
        //Joker joker = new Joker();

        //Intent intent = new Intent(MainActivity.this,MainJokeActivity.class);
        //intent.putExtra("joke",joker.getJoke());
        //startActivity(intent);
        new EndPointAsyncTask().execute(this);
    }

    public static interface EndPointInterface{
        public void onResult(String result,Exception e);
    }
    public static class  EndPointAsyncTask extends AsyncTask<Context,Void,String>{
        private static MyApi myApiService=null;
        private Context context;
        private Exception exception=null;
        private EndPointInterface endPointInterface=null;

        public EndPointInterface getEndPointInterface() {
            return endPointInterface;
        }

        public void setEndPointInterface(EndPointInterface endPointInterface) {
            this.endPointInterface = endPointInterface;
        }

        @Override
        protected String doInBackground(Context... contexts) {
            if(myApiService==null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null)
                        .setRootUrl("https://1-dot-joker-1081.appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                myApiService = builder.build();
            }
            if(contexts[0]!=null)
                context = contexts[0];

            try{
                return myApiService.sayHi().execute().getData();
            }catch (Exception e){
                exception = e;
                return e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(String s) {
            //context is null in connectedTest so in that case won't start the activity
            if(context!=null){
                Intent intent = new Intent(context,MainJokeActivity.class);
                intent.putExtra("joke", s);
                context.startActivity(intent);
            }

            if(endPointInterface!=null){
                endPointInterface.onResult(s, exception);
            }
        }
    }


}
