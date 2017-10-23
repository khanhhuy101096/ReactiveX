package com.example.khanhhuy.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by yeu_thuong on 10/23/2017.
 */

public class RxActivity extends AppCompatActivity{
    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        createObservableAndObserver();
    }

    private void createObservableAndObserver() {
        mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] strings = editText.getText().toString().split("\n");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < strings.length; i++) {
                    builder.append((i+1) + ". " + strings[i] + "\n");
                }
                subscriber.onNext(builder.toString());
//                subscriber.onNext(editText.getText().toString());
                subscriber.onCompleted();
            }
        });

        mObserver = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                textView.setText(s);
            }
        };
    }

    public void onClick(View view) {
//        mObservable.subscribe(mObserver);
        mObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
}
