//
//  ReconoSerSdkViewController.swift
//  ReconoSer
//
//  Created by Yeison Betancourt Solis on 28/11/19.
//

import UIKit
import ReconoSerOlimpia
import MBProgressHUD

class ReconoSerSdkViewController: UIViewController {
    
    var commandDelegate: CDVCommandDelegate?
    var callbackId: String?
    var metodo: String?
    var guidAgreement: String?
    var dataConv: String?
    var guidCitizen: String?
    var imagePath: String?

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        switch metodo {
            case "DOCUMENT_REQUEST_FRONT":
                self.callDocumentReader(mode: .front)
            case "DOCUMENT_REQUEST_BACK":
                self.callDocumentReader(mode: .back)
            case "BARCODE_DOCUMENT":
                self.callBarcodeReader()
            case "FACE":
                self.callBiometricReader()
            case "VIEW_QUESTIONS":
                self.callFormQuestions()
            default:
                self.handleError(error: "{\"codigo\": 0, \"descripcion\": \"Metodo no especificado.\"}")
        }
    }
    
    func handleResponse(image: UIImage?, result: String?, error: ErrorEntransaccion?)  {
        // Set the plugin result to fail.
        var pluginResult = CDVPluginResult (status: CDVCommandStatus_ERROR, messageAs: "{\"codigo\": 0, \"descripcion\": \"El Plugin ha fallado.\"}");
        
        var path_file_r = ""
        if(image != nil){
            path_file_r = self.saveImageTmp(image: image!)!
            if(path_file_r.count == 0){
                self.handleError(error: "{\"codigo\": 0, \"descripcion\": \"El Plugin ha fallado.\"}")
                return
            }
        }
        
        let response: Dictionary<String, Any>
        
        if (error != nil) {
            // Set the plugin result to fail.
            response = [
                "success": false,
                "canceled": false,
                "path_file_r": "",
                "result": error?.toJSON() ?? ""
            ]
        }else{
            // Set the plugin result to succeed.
            response = [
                "success": true,
                "canceled": false,
                "path_file_r": path_file_r,
                "result": result?.description ?? ""
            ]
        }
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: response);
        
        // Send the function result back to Cordova.
        self.commandDelegate!.send(pluginResult, callbackId: self.callbackId);
        self.dismiss(animated: true, completion: nil)
    }
    
    func handleError(error: String) {
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error);
        
        // Send the function result back to Cordova.
        self.commandDelegate!.send(pluginResult, callbackId: self.callbackId);
        self.dismiss(animated: true, completion: nil)
    }
    
    func handleCancel(view: UIViewController) {
        view.dismiss(animated: true, completion: nil)
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "{\"success\":true, \"canceled\":true}");
        
        // Send the function result back to Cordova.
        self.commandDelegate!.send(pluginResult, callbackId: self.callbackId);
        self.dismiss(animated: true, completion: nil)
    }
    
    func callDocumentReader(mode: DocumentReaderMode){
        self.showLoader(withTitle: "Espere...", and: "")
        let documentReader:DocumentReader = DocumentReader()
        documentReader.getDocumentReaderViewController(guidAgreement: guidAgreement ?? "", dataConv: dataConv ?? "", guidCitizen: guidCitizen ?? "", delegate: self, mode: mode, completionHandler: {(documentReaderViewController, error) in
            self.hideLoader()
            
            if (error != nil) {
                //Implementación en caso de error
                self.handleError(error: error?.toJSON() ?? "{\"codigo\": 0, \"descripcion\": \"Error inesperado.\"}")
                return
            }
            
            documentReaderViewController?.modalPresentationStyle = .overFullScreen //For iOS 13
            self.present(documentReaderViewController!, animated:false, completion: nil)
        })
    }
    
    func callBarcodeReader(){
        self.showLoader(withTitle: "Espere...", and: "")
        let barcodeReader:BarcodeReader = BarcodeReader()
        barcodeReader.getBarcodeReaderViewController(guidAgreement: guidAgreement ?? "", dataConv: dataConv ?? "", delegate: self, completionHandler: {(barcodeReaderViewController, error) in
            self.hideLoader()

            if (error != nil) {
                //Implementación para caso de error
                self.handleError(error: error?.toJSON() ?? "{\"codigo\": 0, \"descripcion\": \"Error inesperado.\"}")
                return
            }

            barcodeReaderViewController?.modalPresentationStyle = .overFullScreen //For iOS 13
            self.present(barcodeReaderViewController!, animated:false, completion: nil)
        })
    }
    
    func callBiometricReader(){
        self.showLoader(withTitle: "Espere...", and: "")
        let biometricReader:BiometricReader = BiometricReader()
        let imageToCompare = self.loadImage()
        biometricReader.getBiometricReaderViewController(guidAgreement: guidAgreement ?? "", dataConv: dataConv ?? "", guidCitizen: guidCitizen ?? "", delegate: self, imageToCompare: imageToCompare, mode: .validate, completionHandler: {(biometricReaderViewController, error) in
            self.hideLoader()
            
            if (error != nil) {
                //Implementación en caso de error
                self.handleError(error: error?.toJSON() ?? "{\"codigo\": 0, \"descripcion\": \"Error inesperado.\"}")
                return
            }

            biometricReaderViewController?.modalPresentationStyle = .overFullScreen //For iOS 13
            self.present(biometricReaderViewController!, animated:false, completion: nil)
        })
    }
    
    func callFormQuestions(){
        self.showLoader(withTitle: "Espere...", and: "")
        let formQuestions:FormQuestions = FormQuestions()
        formQuestions.getFormQuestionViewController(guidCitizen: guidCitizen ?? "", delegate: self, completionHandler: { (formQuestionViewController, error) in
            self.hideLoader()

            if (error != nil) {
                //Implementación ...
                self.handleError(error: error?.toJSON() ?? "{\"codigo\": 0, \"descripcion\": \"Error inesperado.\"}")
                return
            }

            let navController = UINavigationController(rootViewController: formQuestionViewController!)
            navController.modalPresentationStyle = .overFullScreen //For iOS 13
            self.present(navController, animated:true, completion: nil)

        })
    }
    
    func saveImageTmp(image: UIImage) -> String? {
        if let data = image.pngData() {
            let imageFileUrl = FileManager.default.temporaryDirectory.appendingPathComponent("reconoser_sdk_tmp.jpeg")
            do {
                try data.write(to: imageFileUrl)
                print("La imagen ha sido guardada en path: \(imageFileUrl)")
                return imageFileUrl.absoluteString
            } catch {
                print("Error guardando imagen: \(error)")
                return ""
            }
        }
        return ""
    }
    
    func loadImage() -> UIImage? {
        let imageFileUrl = FileManager.default.temporaryDirectory.appendingPathComponent("reconoser_sdk_tmp.jpeg")
        do {
            let data = try Data(contentsOf: imageFileUrl)
            return UIImage(data: data)
        } catch {
            print("Error cargando imagen : \(error)")
            return nil
        }
    }
}

extension ReconoSerSdkViewController:DocumentReaderDelegate {
    func onScanDocument(_ image: UIImage?, imageData: Data?, mode: DocumentReaderMode, result: String?, error: ErrorEntransaccion?) {
        print("DocumentReaderMode", result ?? "")
        self.handleResponse(image: image!, result: result, error: error)
    }
    
    func onCloseDocumentReader(view: UIViewController) {
        print("onCloseDocumentReader")
        self.handleCancel(view: view)
    }
}

extension ReconoSerSdkViewController:BarcodeReaderDelegate {
    func onScanBarcode(result: String) {
        print("onScanBarcode", result )
        self.handleResponse(image: nil, result: result, error: nil)
    }
    
    func onCloseBarcodeReaderDelegate(view: UIViewController) {
        print("onCloseBarcodeReaderDelegate")
        self.handleCancel(view: view)
    }
}

extension ReconoSerSdkViewController:BiometricReaderDelegate {
    func onScanFace(_ image: UIImage?, imageData: Data?, mode: BiometricReaderMode?, result: CompararRostro?, validateBiometrics: ValidarBiometria?, error: ErrorEntransaccion?) {
        print("onScanFace", result ?? "")
        self.handleResponse(image: image!, result: result?.toJSON(), error: error)
    }
    
    func onCloseBiometricReader(view: UIViewController) {
        print("onCloseBiometricReader")
        self.handleCancel(view: view)
    }
}

extension ReconoSerSdkViewController:FormQuestionViewControllerDelegate {
    func dataAnswerSend(_ idQuestionnaire: String, questionnaireRegistration: Int, answers: [RespuestasIn]) {
        print("onScanFace", answers)
        // Set the plugin result to success.
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: answers);
        
        // Send the function result back to Cordova.
        self.commandDelegate!.send(pluginResult, callbackId: self.callbackId);
        self.dismiss(animated: true, completion: nil)
    }
    
    func onCloseFormQuestion(view: UIViewController) {
        print("onCloseFormQuestion")
        self.handleCancel(view: view)
    }
    
    func onErrorFormQuestion(view: UIViewController) {
        print("onErrorFormQuestion")
        self.handleCancel(view: view)
    }
}

extension UIViewController {
    func showLoader(withTitle title: String, and Description:String) {
        let Indicator = MBProgressHUD.showAdded(to: self.view, animated: true)
        Indicator.label.text = title
        Indicator.isUserInteractionEnabled = false
        Indicator.detailsLabel.text = Description
        Indicator.show(animated: true)
   }
    
   func hideLoader() {
        MBProgressHUD.hide(for: self.view, animated: true)
   }
}
