package com.sjmtechs.park.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjmtechs.park.R;
import com.sjmtechs.park.purchase.PurchasePresenterImpl;
import com.sjmtechs.park.purchase.PurchaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PurchaseTimeActivity extends AppCompatActivity implements PurchaseView{

    @InjectView(R.id.btnIncrease)
    ImageView btnIncrease;

    @InjectView(R.id.btnDecrease)
    ImageView btnDecrease;

    @InjectView(R.id.btnDone)
    ImageView btnDone;

    @InjectView(R.id.btnCancel)
    ImageView btnCancel;

    @InjectView(R.id.txtPrice)
    TextView txtPrice;

    @InjectView(R.id.txtTime)
    TextView txtTime;

    private PurchasePresenterImpl purchasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_time);
        ButterKnife.inject(this);

        purchasePresenter = new PurchasePresenterImpl(this);
    }

    @OnClick(R.id.btnIncrease)
    public void onIncrease(){
        purchasePresenter.onPlusClicked();
    }

    @OnClick(R.id.btnDecrease)
    public void onDecrease(){
        purchasePresenter.onMinusClicked();
    }

    @OnClick(R.id.btnDone)
    public void onDone(){
        purchasePresenter.onDoneClicked();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        purchasePresenter.onCancelClicked();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setTimeAndPrice(String time, String price) {
        txtPrice.setText("$" + price);
        txtTime.setText(time);
    }

    @Override
    public void done() {
        PurchaseTimeActivity.this.finish();
    }

    @Override
    public void cancel() {
        PurchaseTimeActivity.this.finish();
    }

}
