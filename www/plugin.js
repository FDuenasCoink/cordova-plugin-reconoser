var exec = require("cordova/exec");

var PLUGIN_NAME = "ReconoSerSdk";

var ReconoSerSdk = {
    consultarConvenio: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "consultarConvenio",
            args
        );
    },
    enviarOTP: function(args, successCallback, errorCallback) {
        exec(successCallback, errorCallback, PLUGIN_NAME, "enviarOTP", args);
    },
    validarOTP: function(args, successCallback, errorCallback) {
        exec(successCallback, errorCallback, PLUGIN_NAME, "validarOTP", args);
    },
    guardarCiudadano: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "guardarCiudadano",
            args
        );
    },
    consultarCiudadano: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "consultarCiudadano",
            args
        );
    },
    guardarlogError: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "guardarlogError",
            args
        );
    },
    anversoCapturar: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "anversoCapturar",
            args
        );
    },
    reversoCapturar: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "reversoCapturar",
            args
        );
    },
    barcodeCapturar: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "barcodeCapturar",
            args
        );
    },
    guardarDocumento: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "guardarDocumento",
            args
        );
    },
    guardarBiometriaReferencia: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "guardarBiometriaReferencia",
            args
        );
    },
    validarBiometria: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "validarBiometria",
            args
        );
    },
    guardarBiometria: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "guardarBiometria",
            args
        );
    },
    mostrarPreguntas: function(args, successCallback, errorCallback) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "mostrarPreguntas",
            args
        );
    },
    solicitarPreguntasDemograficas: function(
        args,
        successCallback,
        errorCallback
    ) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "solicitarPreguntasDemograficas",
            args
        );
    },
    validarRespuestaDemograficas: function(
        args,
        successCallback,
        errorCallback
    ) {
        exec(
            successCallback,
            errorCallback,
            PLUGIN_NAME,
            "validarRespuestaDemograficas",
            args
        );
    }
};

module.exports = ReconoSerSdk;
