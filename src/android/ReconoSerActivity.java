package com.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.reconosersdk.reconosersdk.citizens.barcode.ColombianCitizenBarcode;
import com.reconosersdk.reconosersdk.citizens.barcode.ForeignBarcode;
import com.reconosersdk.reconosersdk.ui.bioFacial.views.LivePreviewActivity;
import com.reconosersdk.reconosersdk.ui.document.views.BarCodeActivity;
import com.reconosersdk.reconosersdk.ui.document.views.RequestDocumentActivity;
import com.reconosersdk.reconosersdk.ui.questions.view.QuestionActivity;
import com.reconosersdk.reconosersdk.utils.IntentExtras;
import com.reconosersdk.reconosersdk.utils.JsonUtils;

import java.util.Objects;

public class ReconoSerActivity extends Activity {
    private static final String TAG = "ReconoSerActivity";
    private static final int DOCUMENT_REQUEST = 1;
    private static final int BARCODE_DOCUMENT = 2;
    private static final int FACE = 3;
    private static final int VIEW_QUESTIONS = 1022;

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int requestCode = extras.getInt("requestCode");
            if (requestCode == DOCUMENT_REQUEST) {
                String guid_ciudadano = extras.getString("guid_ciudadano");
                String data_anverso = extras.getString("data_anverso");
                String guid_conv = extras.getString("guid_conv");
                String text_scan = extras.getString("text_scan");
                Intent intent = new Intent(this, RequestDocumentActivity.class);
                intent.putExtra(IntentExtras.GUID_CIUDADANO, guid_ciudadano);
                intent.putExtra(IntentExtras.DATA_ANVERSO, data_anverso);
                intent.putExtra("GUID_CONV", guid_conv);
                intent.putExtra(IntentExtras.TEXT_SCAN, text_scan);
                startActivityForResult(intent, DOCUMENT_REQUEST);
            }else if (requestCode == BARCODE_DOCUMENT){
                String guid_ciudadano = extras.getString("guid_ciudadano");
                String data_anverso = extras.getString("data_anverso");
                String guid_conv = extras.getString("guid_conv");
                Intent intent = new Intent(this, BarCodeActivity.class);
                intent.putExtra(IntentExtras.GUID_CIUDADANO, guid_ciudadano);
                intent.putExtra(IntentExtras.DATA_ANVERSO, data_anverso);
                intent.putExtra("GUID_CONV", guid_conv);
                startActivityForResult(intent, BARCODE_DOCUMENT);
            }else if (requestCode == FACE) {
                String guid_ciudadano = extras.getString("guid_ciudadano");
                String image64 = extras.getString("image64");
                String guid_conv = extras.getString("guid_conv");
                String data_adi = extras.getString("data_adi");
                Intent intent = new Intent(this, LivePreviewActivity.class);
                intent.putExtra(IntentExtras.GUID_CIUDADANO, guid_ciudadano);
                intent.putExtra(IntentExtras.IMAGE64, image64);
                intent.putExtra("GUID_CONV", guid_conv);
                intent.putExtra("DATA_ADI", data_adi);
                startActivityForResult(intent, FACE);
            }else if (requestCode == VIEW_QUESTIONS) {
                String guid_ciudadano = extras.getString("guid_ciudadano");
                Intent intent = new Intent(this, QuestionActivity.class);
                intent.putExtra(IntentExtras.GUID, guid_ciudadano);
                startActivityForResult(intent, VIEW_QUESTIONS);
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (resultCode == RESULT_OK){
            if (requestCode == DOCUMENT_REQUEST){
                onRespondDocumentReques(data);
            } else if (requestCode == BARCODE_DOCUMENT) {
                onRespondBarcodeDocument(data);
            } else if (requestCode == FACE) {
                onRespondFace(data);
            } else if (requestCode == VIEW_QUESTIONS){
                onRespondViewQuestions(data);
            }
        } else if (resultCode == IntentExtras.ERROR_INTENT){
            onErrorIntent(data);
        }
        if(resultCode == RESULT_CANCELED) {
            Intent intent = new Intent();
            intent.putExtra("error", "RESULT_CANCELED");
            setResult(RESULT_CANCELED, intent);
        }
        finish();// Exit of this activity !
    }

    private void onRespondDocumentReques(Intent data) {
        String path_file_r = (data != null ? data.getStringExtra(IntentExtras.PATH_FILE_PHOTO) : "");
        Intent intent = new Intent();
        intent.putExtra("path_file_r", path_file_r);
        if (data != null) {
            if (Objects.requireNonNull(data.getExtras()).containsKey(IntentExtras.DATA_ANVERSO)) {
                intent.putExtra("result", JsonUtils.stringObject(data.getParcelableExtra(IntentExtras.DATA_ANVERSO)));
            } else  if (data.getExtras().containsKey(IntentExtras.DATA_REVERSO)) {
                intent.putExtra("result", JsonUtils.stringObject(data.getParcelableExtra(IntentExtras.DATA_REVERSO)));
            }
        }
        setResult(RESULT_OK, intent);
    }

    private void onRespondBarcodeDocument(Intent data) {
        Intent intent = new Intent();
        String barcodeType = data.getStringExtra(IntentExtras.TYPE_BARCODE);
        String result = "";

        if (barcodeType.equals("PDF_417/TI") || barcodeType.equals("PDF_417/CC")) {
            ColombianCitizenBarcode colombianCitizenBarcode = new ColombianCitizenBarcode();
            colombianCitizenBarcode = data.getParcelableExtra(IntentExtras.DATA_DOCUMENT_CO);
            result = colombianCitizenBarcode.toString();
        } else if(barcodeType.equals("PDF_417/CE")){
            ForeignBarcode foreignBarcode = new ForeignBarcode();
            foreignBarcode = data.getParcelableExtra(IntentExtras.DATA_DOCUMENT_CO);
            Log.e("ACTIVITYBARC",foreignBarcode.toString());
            result = foreignBarcode.toString();
        }else {
            result = barcodeType;
        }

        intent.putExtra("result", result);
        intent.putExtra("type_barcode", data.getStringExtra(IntentExtras.TYPE_BARCODE));
        setResult(RESULT_OK, intent);
    }

    private void onRespondFace(Intent data) {
        try {
            String path_file_r = (data != null ? data.getStringExtra(IntentExtras.PATH_FILE_PHOTO_R) : "");
            double rekognition = (data != null ? data.getDoubleExtra(IntentExtras.REKOGNITION, 0) : 0);
            String msg = "";
            if (rekognition < 80) {
                msg = "Mensaje: La imagen no corresponde a la persona";
            }
            String result = "SIMILITUD: " + rekognition + msg;
            Intent intent = new Intent();
            intent.putExtra("path_file_r", path_file_r);
            intent.putExtra("result", result);
            setResult(RESULT_OK, intent);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void onRespondViewQuestions(Intent data) {
        Intent intent = new Intent();
        intent.putExtra("result", JsonUtils.stringObject(data.getParcelableExtra(IntentExtras.RESULT_VALIDATE)));
        setResult(RESULT_OK, intent);
    }

    private void onErrorIntent(Intent data) {
        if (Objects.requireNonNull(data.getExtras()).containsKey(IntentExtras.ERROR_MSG)) {
            Intent intent = new Intent();
            intent.putExtra("error", data.getStringExtra(IntentExtras.ERROR_MSG));
            setResult(IntentExtras.ERROR_INTENT, intent);
        }
        if (data.getExtras().get(IntentExtras.ERROR_SDK) != null) {
            Intent intent = new Intent();
            intent.putExtra("error", JsonUtils.stringObject(Objects.requireNonNull(data.getExtras().getParcelable(IntentExtras.ERROR_SDK))));
            setResult(IntentExtras.ERROR_INTENT, intent);
        }
    }
}