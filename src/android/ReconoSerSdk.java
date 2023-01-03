package com.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;

import com.reconosersdk.reconosersdk.http.OlimpiaInterface;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.CiudadanoIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.DatosOTP;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.GuardarBiometriaIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.GuardarDocumentoIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.GuardarLogErrorIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.RespuestasIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.ValidarBiometriaIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.in.ValidarRespuestaIn;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.BarcodeDocument;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ConsultarCiudadano;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ConsultarConvenio;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.EnviarOTP;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ErrorEntransaccion;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.GuardarBiometria;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.GuardarCiudadano;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.GuardarLogError;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.RespuestaTransaccion;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.SolicitarPreguntasDemograficas;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ValidarBiometria;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ValidarOTP;
import com.reconosersdk.reconosersdk.http.olimpiait.entities.out.ValidarRespuestaDemografica;
import com.reconosersdk.reconosersdk.ui.servicesOlimpia.ServicesOlimpia;
import com.reconosersdk.reconosersdk.utils.ImageUtils;
import com.reconosersdk.reconosersdk.utils.IntentExtras;
import com.reconosersdk.reconosersdk.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ReconoSerSdk extends CordovaPlugin {
    private static final String TAG = "ReconoSerSdk";
    private static final int DOCUMENT_REQUEST = 1;
    private static final int BARCODE_DOCUMENT = 2;
    private static final int FACE = 3;
    private static final int VIEW_QUESTIONS = 1022;
    private CallbackContext PUBLIC_CALLBACKS = null;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "Inicializando ReconoSerSdk");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        PUBLIC_CALLBACKS = callbackContext;
        Intent intent;
        switch (action) {
            case "consultarConvenio":
                Log.d(TAG, "consultarConvenio");
                consultarConvenio(args.getString(0), args.getString(1));
                break;
            case "enviarOTP":
                Log.d(TAG, "enviarOTP");
                enviarOTP(args.getString(0), args.getString(1), args.getString(2));
                break;
            case "validarOTP":
                Log.d(TAG, "validarOTP");
                validarOTP(args.getString(0), args.getString(1));
                break;
            case "guardarCiudadano":
                Log.d(TAG, "guardarCiudadano");
                guardarCiudadano(args);
                break;
            case "consultarCiudadano":
                Log.d(TAG, "consultarCiudadano");
                consultarCiudadano(args.getString(0));
                break;
            case "guardarlogError":
                Log.d(TAG, "guardarlogError");
                guardarlogError(args.getString(0), args.getString(1), args.getString(2), args.getString(3));
                break;
            case "anversoCapturar":
                Log.d(TAG, "anversoCapturar");
                intent = new Intent("com.plugin.ReconoSerActivity");
                intent.putExtra("guid_ciudadano", args.getString(0));
                intent.putExtra("data_anverso", args.getString(1));
                intent.putExtra("guid_conv", args.getString(2));
                intent.putExtra("text_scan", "Anverso");
                intent.putExtra("requestCode", DOCUMENT_REQUEST);
                cordova.startActivityForResult(this, intent, DOCUMENT_REQUEST);
                break;
            case "reversoCapturar":
                Log.d(TAG, "reversoCapturar");
                intent = new Intent("com.plugin.ReconoSerActivity");
                intent.putExtra("guid_ciudadano", args.getString(0));
                intent.putExtra("data_anverso", args.getString(1));
                intent.putExtra("guid_conv", args.getString(2));
                intent.putExtra("text_scan", "Reverso");
                intent.putExtra("requestCode", DOCUMENT_REQUEST);
                cordova.startActivityForResult(this, intent, DOCUMENT_REQUEST);
                break;
            case "barcodeCapturar":
                Log.d(TAG, "barcodeCapturar");
                intent = new Intent("com.plugin.ReconoSerActivity");
                intent.putExtra("guid_ciudadano", args.getString(0));
                intent.putExtra("data_anverso", args.getString(1));
                intent.putExtra("guid_conv", args.getString(2));
                intent.putExtra("requestCode", BARCODE_DOCUMENT);
                cordova.startActivityForResult(this, intent, BARCODE_DOCUMENT);
                break;
            case "guardarDocumento":
                Log.d(TAG, "guardarBiometria");
                guardarDocumento(args.getString(0), args.getString(1));
                break;
            case "guardarBiometria":
                Log.d(TAG, "guardarBiometria");
                guardarBiometria(args);
                break;
            case "guardarBiometriaReferencia":
                Log.d(TAG, "guardarBiometriaReferencia");
                intent = new Intent("com.plugin.ReconoSerActivity");
                intent.putExtra("guid_ciudadano", args.getString(0));
                intent.putExtra("image64", args.getString(1));
                intent.putExtra("guid_conv", args.getString(2));
                intent.putExtra("data_adi", args.getString(3));
                intent.putExtra("requestCode", FACE);
                cordova.startActivityForResult(this, intent, FACE);
                break;
            case "validarBiometria":
                Log.d(TAG, "validarBiometria");
                validarBiometria(args.getString(0), args.getString(1), args.getString(2), args.getString(3));
                break;
            case "mostrarPreguntas":
                Log.d(TAG, "mostrarPreguntas");
                intent = new Intent("com.plugin.ReconoSerActivity");
                intent.putExtra("guid_ciudadano", args.getString(0));
                intent.putExtra("requestCode", VIEW_QUESTIONS);
                cordova.startActivityForResult(this, intent, VIEW_QUESTIONS);
                break;
            case "solicitarPreguntasDemograficas":
                Log.d(TAG, "solicitarPreguntasDemograficas");
                solicitarPreguntasDemograficas(args.getString(0));
                break;
            case "validarRespuestaDemograficas":
                Log.d(TAG, "validarRespuestaDemograficas");
                validarRespuestaDemograficas(args.getString(0), args.getString(1), args.getString(2), args.getJSONArray(3));
                break;
        }

        // Send no result, to execute the callbacks later
        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true); // Keep callback

        return true;
    }

    private void consultarConvenio(String guidConv, String datos) {
        ServicesOlimpia.getInstance().consultarConvenio(guidConv, datos, new OlimpiaInterface.CallbackConsultAgreement() {
            @Override
            public void onSuccess(boolean estado, ConsultarConvenio serviciosConv) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(serviciosConv));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(boolean estado, RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void enviarOTP(String guidCiudadano, String datos, String mensaje) {
        DatosOTP datosOTP = new DatosOTP(datos, mensaje);
        ServicesOlimpia.getInstance().enviarOTP(guidCiudadano, datosOTP, new OlimpiaInterface.CallbackSendOTP() {
            @Override
            public void onSuccess(EnviarOTP sendOTP) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(sendOTP));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void validarOTP(String guidOTP, String oTP) {
        ServicesOlimpia.getInstance().validarOTP(guidOTP, oTP, new OlimpiaInterface.CallbackValidateOTP() {
            @Override
            public void onSuccess(ValidarOTP validarOTP) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(validarOTP));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void guardarCiudadano(JSONArray args) throws JSONException {
        CiudadanoIn ciudadano = new CiudadanoIn();
        ciudadano.setGuidConv(args.getString(0));
        ciudadano.setGuidCiu(args.getString(1));
        ciudadano.setTipoDoc(args.getString(2));
        ciudadano.setNumDoc(args.getString(3));
        ciudadano.setEmail(args.getString(4));
        ciudadano.setCelular(args.getString(5));
        ciudadano.setDatosAdi(args.getString(6));
        ciudadano.setProcesoConvenioGuid(args.getString(7));
        ciudadano.setAsesor(args.getString(8));
        ciudadano.setSede(args.getString(9));
        ServicesOlimpia.getInstance().guardarCiudadano(ciudadano, new OlimpiaInterface.CallbackSaveResident() {
            @Override
            public void onSuccess(GuardarCiudadano saveResident) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(saveResident));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void consultarCiudadano(String guidCiudadano) {
        ServicesOlimpia.getInstance().consultarCiudadano(guidCiudadano, new OlimpiaInterface.CallbackConsultResident() {
            @Override
            public void onSuccess(ConsultarCiudadano consultarCiudadano) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(consultarCiudadano));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void guardarlogError(String guidConv, String texto, String componente, String usuario) {
        GuardarLogErrorIn guardarLogErrorIn = new GuardarLogErrorIn(guidConv, texto, componente, usuario);
        ServicesOlimpia.getInstance().guardarlogError(guardarLogErrorIn, new OlimpiaInterface.CallbackSaveLogError() {
            @Override
            public void onSuccess(GuardarLogError saveLogError) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(saveLogError));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(List<ErrorEntransaccion> transactionResponse) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(transactionResponse));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void guardarDocumento(String guidCiu, String response) {
        // QA
        // GuardarDocumentoIn guardarDocumentoIn = new GuardarDocumentoIn(guidCiu, response, "91ff7896-8d5e-4669-a0cc-c44ac5ddf5cc");

        // PROD
        GuardarDocumentoIn guardarDocumentoIn = new GuardarDocumentoIn(guidCiu, response, "2327eafa-ce16-4fc3-908d-f28b83e6bfe3");

        ServicesOlimpia.getInstance().guardarDocumento(guardarDocumentoIn, new OlimpiaInterface.CallbackSaveDocument() {
            @Override
            public void onSuccess(BarcodeDocument barcodeDocument) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(barcodeDocument));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(List<ErrorEntransaccion> transactionResponse) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(transactionResponse));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void guardarBiometria(JSONArray args) throws JSONException {
        GuardarBiometriaIn guardarBiometriaIn = new GuardarBiometriaIn();
        guardarBiometriaIn.setGuidCiu(args.getString(0));
        guardarBiometriaIn.setIdServicio(args.getInt(1));
        guardarBiometriaIn.setSubTipo(args.getString(2));
        guardarBiometriaIn.setValor(ImageUtils.getEncodedBase64FromFilePath(args.getString(3)));
        guardarBiometriaIn.setFormato(args.getString(4));
        guardarBiometriaIn.setDatosAdi(args.getString(5));
        guardarBiometriaIn.setUsuario(args.getString(6));
        ServicesOlimpia.getInstance().guardarBiometria(guardarBiometriaIn, new OlimpiaInterface.CallbackSaveBiometry() {
            @Override
            public void onSuccess(GuardarBiometria saveBiometry) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(saveBiometry));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void validarBiometria(String guidCiudadano, String pathFace, String formato, String idServicio) {
        ValidarBiometriaIn data = new ValidarBiometriaIn();
        data.setGuidCiudadano(guidCiudadano);
        data.setFormato(formato);
        data.setIdServicio(Integer.parseInt(idServicio));
        data.setBiometria(ImageUtils.convert64String(pathFace));

        ServicesOlimpia.getInstance().validarBiometria(data, new OlimpiaInterface.CallbackValidateBiometry() {
            @Override
            public void onSuccess(ValidarBiometria validateBiometry) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(validateBiometry));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion, int intentos) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void solicitarPreguntasDemograficas(String guidCiudadano) {
        ServicesOlimpia.getInstance().solicitarPreguntasDemograficas(guidCiudadano, new OlimpiaInterface.CallbackRequestQuestions() {
            @Override
            public void onSuccess(SolicitarPreguntasDemograficas requestQuestions) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(requestQuestions));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    private void validarRespuestaDemograficas(String guidCiudadano, String idCuestionario, String registroCuestionario, JSONArray respuestas) {
        ValidarRespuestaIn validarRespuestaIn = new ValidarRespuestaIn();
        validarRespuestaIn.setGuidCiudadano(guidCiudadano);
        validarRespuestaIn.setIdCuestionario(idCuestionario);
        validarRespuestaIn.setRegistroCuestionario(Integer.parseInt(registroCuestionario));
        List<RespuestasIn> respuestasIns = new ArrayList<>();
        for (int i = 0; i < respuestas.length(); i++) {
            try {
                JSONObject resultJsonObject = new JSONObject(respuestas.get(i).toString());
                respuestasIns.add(new RespuestasIn(resultJsonObject.get("idPregunta").toString(), resultJsonObject.get("idRespuesta").toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        validarRespuestaIn.setRespuestas(respuestasIns);

        ServicesOlimpia.getInstance().validarRespuestaDemograficas(validarRespuestaIn, new OlimpiaInterface.CallbackValidateResponse() {
            @Override
            public void onSuccess(ValidarRespuestaDemografica validateResponse) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, JsonUtils.stringObject(validateResponse));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }

            @Override
            public void onError(RespuestaTransaccion respuestaTransaccion) {
                final PluginResult result = new PluginResult(PluginResult.Status.ERROR, JsonUtils.stringObject(respuestaTransaccion));
                PUBLIC_CALLBACKS.sendPluginResult(result);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();// Get data sent by the Intent
            String path_file_r = extras != null ? extras.getString("path_file_r") : null; // data parameter will be send from the other activity.
            String result = extras != null ? extras.getString("result") : null; // data parameter will be send from the other activity.
            String type_barcode = extras != null ? extras.getString("type_barcode") : null; // data parameter will be send from the other activity.
            JSONObject args = new JSONObject();
            try {
                args.put("success", true);
                args.put("canceled", false);
                args.put("path_file_r", path_file_r);
                args.put("result", result);
                args.put("type", type_barcode);
            } catch (JSONException e) {
                e.printStackTrace();
                PluginResult resultado = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                resultado.setKeepCallback(true);
                PUBLIC_CALLBACKS.sendPluginResult(resultado);
                return;
            }
            PluginResult resultado = new PluginResult(PluginResult.Status.OK, args);
            resultado.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(resultado);
            return;
        } else if (resultCode == IntentExtras.ERROR_INTENT) {
            Bundle extras = data.getExtras();// Get data sent by the Intent
            String error = extras != null ? extras.getString("error") : null; // data parameter will be send from the other activity.
            JSONObject args = new JSONObject();
            try {
                args.put("success", false);
                args.put("canceled", false);
                args.put("error", error);
            } catch (JSONException e) {
                e.printStackTrace();
                PluginResult resultado = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                resultado.setKeepCallback(true);
                PUBLIC_CALLBACKS.sendPluginResult(resultado);
                return;
            }
            PluginResult resultado = new PluginResult(PluginResult.Status.ERROR, args);
            resultado.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(resultado);
            return;
        }else if(resultCode == RESULT_CANCELED){
            JSONObject args = new JSONObject();
            try {
                args.put("success", true);
                args.put("canceled", true);
            } catch (JSONException e) {
                e.printStackTrace();
                PluginResult resultado = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                resultado.setKeepCallback(true);
                PUBLIC_CALLBACKS.sendPluginResult(resultado);
                return;
            }
            PluginResult resultado = new PluginResult(PluginResult.Status.OK, args);
            resultado.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(resultado);
            return;
        }
        // Handle other results if exists.
        super.onActivityResult(requestCode, resultCode, data);
    }

}
