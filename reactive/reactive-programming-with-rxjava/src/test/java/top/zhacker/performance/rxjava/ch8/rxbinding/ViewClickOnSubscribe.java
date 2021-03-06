package top.zhacker.performance.rxjava.ch8.rxbinding;

import android.view.View;
import top.zhacker.performance.rxjava.ch8.rxandroid.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static top.zhacker.performance.rxjava.ch8.rxandroid.MainThreadSubscription.verifyMainThread;

final class ViewClickOnSubscribe implements Observable.OnSubscribe<Void> {
  final View view;

  ViewClickOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnClickListener(null);
      }
    });

    view.setOnClickListener(listener);
  }
}