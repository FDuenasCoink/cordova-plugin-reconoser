import ReconoSerOlimpia
/*
* Notes: The @objc shows that this class & function should be exposed to Cordova.
*/
@objc(ReconoSerSdk) class ReconoSerSdk : CDVPlugin {
    @objc(consultarConvenio:) // Declare your function name.
    func consultarConvenio(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidConv = command.arguments[0] as? String ?? ""
        let datosConv = command.arguments[1] as? String ?? ""
        ServiciosOlimpia.sharedInstance.consultarConvenio(guidConv: guidConv, datosConv: datosConv, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(enviarOTP:) // Declare your function name.
    func enviarOTP(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCiu = command.arguments[0] as? String ?? ""
        let tipoOTP = command.arguments[1] as? String ?? ""
        let mensaje = command.arguments[2] as? String ?? ""
        let datosOTP = DatosOTP(tipoOTP: tipoOTP, mensaje: mensaje)
        ServiciosOlimpia.sharedInstance.enviarOTP(guidCiu: guidCiu, datosOTP: datosOTP, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(validarOTP:) // Declare your function name.
    func validarOTP(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidOTP = command.arguments[0] as? String ?? ""
        let datosOTP = command.arguments[1] as? String ?? ""
        ServiciosOlimpia.sharedInstance.validarOTP(guidOTP: guidOTP, datosOTP: datosOTP, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(guardarCiudadano:) // Declare your function name.
    func guardarCiudadano(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidConv = command.arguments[0] as? String ?? ""
        let guidCiu = command.arguments[1] as? String ?? ""
        let tipoDoc = command.arguments[2] as? String ?? ""
        let numDoc = command.arguments[3] as? String ?? ""
        let email = command.arguments[4] as? String ?? ""
        let celular = command.arguments[5] as? String ?? ""
        let datosAdi = command.arguments[6] as? String ?? ""
        let procesoConvGuid = command.arguments[7] as? String ?? ""
        let asesor = command.arguments[8] as? String ?? ""
        let sede = command.arguments[9] as? String ?? ""
        let ciudadano = CiudadanoIn(guidConv: guidConv, guidCiu: guidCiu, tipoDoc: tipoDoc, numDoc: numDoc, email: email, celular: celular, datosAdi: datosAdi, procesoConvGuid: procesoConvGuid, asesor: asesor, sede: sede)
        ServiciosOlimpia.sharedInstance.guardarCiudadano(ciudadano: ciudadano, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
      })
    }
    
    @objc(consultarCiudadano:) // Declare your function name.
    func consultarCiudadano(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCiu = command.arguments[0] as? String ?? ""
        ServiciosOlimpia.sharedInstance.consultarCiudadano(guidCiu: guidCiu, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(guardarlogError:) // Declare your function name.
    func guardarlogError(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidConv = command.arguments[0] as? String ?? ""
        let texto = command.arguments[1] as? String ?? ""
        let componente = command.arguments[2] as? String ?? ""
        let usuario = command.arguments[3] as? String ?? ""
        let error = GuardarLogErrorIn(guidConv: guidConv, texto: texto, componente: componente, usuario: usuario)
        ServiciosOlimpia.sharedInstance.guardarlogError(error: error, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(anversoCapturar:) // Declare your function name.
    func anversoCapturar(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCitizen = command.arguments[0] as? String ?? ""
        let dataConv = command.arguments[1] as? String ?? ""
        let guidAgreement = command.arguments[2] as? String ?? ""
        
        let viewController = ReconoSerSdkViewController()
        viewController.commandDelegate = self.commandDelegate
        viewController.callbackId = command.callbackId
        viewController.metodo = "DOCUMENT_REQUEST_FRONT"
        viewController.guidAgreement = guidAgreement
        viewController.dataConv = dataConv
        viewController.guidCitizen = guidCitizen
        self.viewController.present(viewController, animated: true)
    }
    
    @objc(reversoCapturar:) // Declare your function name.
    func reversoCapturar(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCitizen = command.arguments[0] as? String ?? ""
        let dataConv = command.arguments[1] as? String ?? ""
        let guidAgreement = command.arguments[2] as? String ?? ""
        
        let viewController = ReconoSerSdkViewController()
        viewController.commandDelegate = self.commandDelegate
        viewController.callbackId = command.callbackId
        viewController.metodo = "DOCUMENT_REQUEST_BACK"
        viewController.guidAgreement = guidAgreement
        viewController.dataConv = dataConv
        viewController.guidCitizen = guidCitizen
        self.viewController.present(viewController, animated: true)
    }
    
    @objc(barcodeCapturar:) // Declare your function name.
    func barcodeCapturar(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCitizen = command.arguments[0] as? String ?? ""
        let dataConv = command.arguments[1] as? String ?? ""
        let guidAgreement = command.arguments[2] as? String ?? ""
        
        let viewController = ReconoSerSdkViewController()
        viewController.commandDelegate = self.commandDelegate
        viewController.callbackId = command.callbackId
        viewController.metodo = "BARCODE_DOCUMENT"
        viewController.guidAgreement = guidAgreement
        viewController.dataConv = dataConv
        viewController.guidCitizen = guidCitizen
        self.viewController.present(viewController, animated: true)
    }
    
    @objc(guardarDocumento:) // Declare your function name.
    func guardarDocumento(command: CDVInvokedUrlCommand) {
        // write the function code.
        let tipoDocumento = command.arguments[0] as? String ?? ""
        let barcode = command.arguments[1] as? String ?? ""
        ServiciosOlimpia.sharedInstance.guardarDocumento(tipoDocumento: tipoDocumento, barcode: barcode, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(guardarBiometria:) // Declare your function name.
    func guardarBiometria(command: CDVInvokedUrlCommand) {
        // write the function code.
        // Set the plugin result to fail.
        var pluginResult = CDVPluginResult (status: CDVCommandStatus_ERROR, messageAs: "{\"codigo\": 0, \"descripcion\": \"El Plugin ha fallado.\"}");
        
        let guidCiu = command.arguments[0] as? String ?? ""
        let idServicio = command.arguments[1] as? String ?? ""
        let subtipo = command.arguments[2] as? String ?? ""
        let valor = pathToBase64()
        let formato = command.arguments[4] as? String ?? ""
        let datosAdi = command.arguments[5] as? String ?? ""
        let usuario = command.arguments[6] as? String ?? ""
        let biometria = GuardarBiometriaIn(guidCiu: guidCiu, idServicio: idServicio, subtipo: subtipo, valor: valor!, formato: formato, datosAdi: datosAdi, usuario: usuario)
        
        if(valor?.count == 0){
            // Set the plugin result to fail.
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "{\"codigo\": 0, \"descripcion\": \"Ocurrió un error al cargar la imagen.\"}");
            
            // Send the function result back to Cordova.
            self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
            
            return
        }
        
        ServiciosOlimpia.sharedInstance.guardarBiometria(biometria: biometria, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(guardarBiometriaReferencia:) // Declare your function name.
    func guardarBiometriaReferencia(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCitizen = command.arguments[0] as? String ?? ""
        let imagePath = command.arguments[1] as? String ?? ""
        let guidAgreement = command.arguments[2] as? String ?? ""
        let dataConv = command.arguments[3] as? String ?? ""
        
        let viewController = ReconoSerSdkViewController()
        viewController.commandDelegate = self.commandDelegate
        viewController.callbackId = command.callbackId
        viewController.metodo = "FACE"
        viewController.guidCitizen = guidCitizen
        viewController.imagePath = imagePath
        viewController.guidAgreement = guidAgreement
        viewController.dataConv = dataConv
        self.viewController.present(viewController, animated: true)
    }
    
    @objc(validarBiometria:) // Declare your function name.
    func validarBiometria(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCiu = command.arguments[0] as? String ?? ""
        let biometria = command.arguments[1] as? String ?? ""
        let formato = command.arguments[2] as? String ?? ""
        let idServicio = command.arguments[3] as? String ?? ""
        let validarBiometria = ValidarBiometriaIn(guidCiu: guidCiu, idServicio: idServicio, biometria: biometria, formato: formato)
        ServiciosOlimpia.sharedInstance.validarBiometria(validarBiometria: validarBiometria, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(mostrarPreguntas:) // Declare your function name.
    func mostrarPreguntas(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCitizen = command.arguments[0] as? String ?? ""
        
        let viewController = ReconoSerSdkViewController()
        viewController.commandDelegate = self.commandDelegate
        viewController.callbackId = command.callbackId
        viewController.metodo = "VIEW_QUESTIONS"
        viewController.guidCitizen = guidCitizen
        self.viewController.present(viewController, animated: true)
    }
    
    @objc(solicitarPreguntasDemograficas:) // Declare your function name.
    func solicitarPreguntasDemograficas(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCiu = command.arguments[0] as? String ?? ""
        ServiciosOlimpia.sharedInstance.solicitarPreguntasDemograficas(guidCiu: guidCiu, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    @objc(validarRespuestaDemograficas:) // Declare your function name.
    func validarRespuestaDemograficas(command: CDVInvokedUrlCommand) {
        // write the function code.
        let guidCiu = command.arguments[0] as? String ?? ""
        let idCuestionario = command.arguments[1] as? String ?? ""
        let registroCuestionario = command.arguments[2] as? String ?? ""
        let respuestas = command.arguments[3] as? [Dictionary<String, String>] ?? []
        var respuestasIn = [RespuestasIn]()
        
        for item in respuestas {
            let respuesta = RespuestasIn(idPregunta: item["idPregunta"] ?? "", idRespuesta: item["idRespuesta"] ?? "")
            respuestasIn.append(respuesta)
        }
        
        ServiciosOlimpia.sharedInstance.validarRespuestaDemograficas(guidCiu: guidCiu, idCuestionario: idCuestionario, registroCuestionario: Int(registroCuestionario) ?? 0, respuestas: respuestasIn, completionHandler: {(data, error) in
            self.handleResponse(data: data?.toJSON() ?? "", error: error?.toJSON() ?? "", callbackId: command.callbackId)
        })
    }
    
    func handleResponse(data: String, error: String, callbackId: String) {
        // Set the plugin result to fail.
        var pluginResult = CDVPluginResult (status: CDVCommandStatus_ERROR, messageAs: "{\"codigo\": 0, \"descripcion\": \"El Plugin ha fallado.\"}");
        
        if (error.count > 0) {
            // Set the plugin result to fail.
            pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error);
        }else{
            // Set the plugin result to succeed.
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: data);
        }
        
        // Send the function result back to Cordova.
        self.commandDelegate!.send(pluginResult, callbackId: callbackId);
    }
    
    func pathToBase64() -> String? {
        let imageFileUrl = FileManager.default.temporaryDirectory.appendingPathComponent("reconoser_sdk_tmp.jpeg")
        do {
            let data = try Data(contentsOf: imageFileUrl)
            let image = UIImage(data: data)
            let imageData: Data? = image?.jpegData(compressionQuality: 0.4)
            let imageStr = imageData?.base64EncodedString() ?? ""
            return imageStr
        } catch {
            print("Error cargando imagen : \(error)")
            return ""
        }
    }
}
