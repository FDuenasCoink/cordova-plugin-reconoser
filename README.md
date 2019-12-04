# Cordova Plugin ReconoSer

======

Plugin para conectar con SDK ReconoSer

======

# ¿Qué es un plugin de Cordova?

Cordova es un conjunto de herramientas que funcionan como puente para crear aplicaciones nativas e hibridas que se comunican a tráves de código Javascript.

Ese puente nos permite hacer cosas sencillas o tan complejas nativas que no se incorporan a los estandares de la Web.

Construir plugins de Cordova significa qu estamos escribiendo algo de JavaScript para invocar a algún código nativo (Objetive-c, Java, entre otros) que tambien deberemos de escribir y devolver el resultado a nuestro JavaScript.

En resumen, construimos un plugin de Cordova cuando queremos hacer algo nativo que aún no puede realizar el WebKit, como acceder a los datos de HealthKit, al scanner de huella, a la conexión bluetooth o a un SDK de terceros que permiten la conexión con dispositivos como impresoras y lectores.

# Construcción de nuestro propio Plugin:

Frameworks como Ionic cuentan con una libreria extensa de herramientas nativas en las cuales posiblemente encontrarás lo que buscas [IonicFramework](https://ionicframework.com/docs/native/), pero ¿cómo hacer uno propio? Bueno Cordova habilita una documentación para este fin: [Plugin Development Guide](https://cordova.apache.org/docs/en/7.x/guide/hybrid/plugins/index.html), sin embargo despues de mucho buscar y probar diferentes alternativas me guié finalmente por esta página: [How to write Cordova Plugins](https://medium.com/ionic-and-the-mobile-web/how-to-write-cordova-plugins-864e40025f2) la cúal se basa en una plantilla ya creada por el equipo de IONIC-TEAM [ionic-team/cordova-plugin-template](https://github.com/ionic-team/cordova-plugin-template) para clonar e instalar en nuestro proyecto, con el fin de entender y documentar un poco más, me decidi a en base a dicha plantilla crear una propia.

## Paso 1:

Clonar el proyecto de Ionic: 

`git clone https://bitbucket.org/reconoser_id/reconoser-ionic.git`

Seleccionar proyecto: 

`cd reconoSer-ionic/`

## Paso 2:

Asegurate de tener instalados los siguientes Plugins en package.json: 

`ionic cordova plugin add https://bitbucket.org/reconoser_id/cordova-plugin-reconoser.git`

`ionic cordova plugin add cordova-plugin-android-permissions`

`npm install @ionic-native/android-permissions`

## Paso 3:

Asegurate de tener moficiado el archivo **appp.module.ts** con AndroidPermissions:

## appp.module.ts:

```
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { AndroidPermissions } from '@ionic-native/android-permissions/ngx'; // Esta línea
import { WebView } from '@ionic-native/ionic-webview/ngx';

@NgModule({
  declarations: [AppComponent],
  entryComponents: [],
  imports: [BrowserModule, IonicModule.forRoot(), AppRoutingModule],
  providers: [
    AndroidPermissions, // Esta línea
    WebView, // Esta línea
    StatusBar,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
```

## Paso 4:

Asegurate de tener el archivo **config.xml** de la siguiente manera:

## config.xml:

```
...
<preference name="android-minSdkVersion" value="22" />
<platform name="ios">
    <preference name="deployment-target" value="12.0" />
    ...
</platform>
```

## Paso 5:

Ejecutar para iOS: `ionic cordova platform add ios` o para Android: `ionic cordova platform add android`.

Posteriormente puedes ir al directorio **platforms** donde encontrarás la carpeta de cada sistema operativo y podrás abrirlo en Xcode o Android Studio compilar y continuar con los siguientes cambios.

## Android

## Paso 1:

Modificar el archivo **android/graddle.properties** de la siguiente manera:

## android/graddle.properties:

```
...
cdvMinSdkVersion=22
android.useAndroidX = true
android.enableJetifier = true
authToken=jp_6p9i7udra8u1h1p71mau771n7p 
```

## Paso 2:

Modificar el archivo **android/build.gradle** de la siguiente manera:

## android/build.gradle:

```
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://jitpack.io' // Esta línea
            credentials { username authToken } // Esta línea
        }
    }

    //This replaces project.properties w.r.t. build settings
    project.ext {
      defaultBuildToolsVersion="28.0.3" //String
      defaultMinSdkVersion=19 //Integer - Minimum requirement is Android 4.4
      defaultTargetSdkVersion=28 //Integer - We ALWAYS target the latest by default
      defaultCompileSdkVersion=28 //Integer - We ALWAYS compile with the latest by default
    }
}
```

## Paso 3:

Modificar el archivo **android/app/build.gradle** de la siguiente manera:

## android/app/build.gradle:

```
android {
    ....
    dataBinding {
        enabled = true // Esta línea
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: '*.jar')
    // SUB-PROJECT DEPENDENCIES START
    implementation(project(path: ":CordovaLib"))
    implementation 'androidx.annotation:annotation:1.0.0'
    // SUB-PROJECT DEPENDENCIES END
    // SDK ReconoSer
    implementation 'org.bitbucket.jamacado.sdk_reconoser:reconosersdk:1.1.21' // Esta línea
}
```

## Paso 4:

Modificar el archivo **AndroidManifest** de la siguiente manera para agregar `com.application.App` al `<application>`:

## AndroidManifest:

```
<?xml version='1.0' encoding='utf-8'?>
<manifest android:hardwareAccelerated="true" android:versionCode="1" android:versionName="0.0.1" package="io.ionic.starter" xmlns:android="http://schemas.android.com/apk/res/android">
    <supports-screens android:anyDensity="true" android:largeScreens="true" android:normalScreens="true" android:resizeable="true" android:smallScreens="true" android:xlargeScreens="true" />
    <uses-permission android:name="android.permission.INTERNET" />
    <application android:hardwareAccelerated="true" android:icon="@mipmap/ic_launcher" android:label="@string/app_name" android:name="com.application.App" android:networkSecurityConfig="@xml/network_security_config" android:supportsRtl="true"> // Esta línea ( android:name="com.application.App")
        <activity android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|smallestScreenSize|screenLayout|uiMode" android:label="@string/activity_name" android:launchMode="singleTop" android:name="MainActivity" android:theme="@android:style/Theme.DeviceDefault.NoActionBar" android:windowSoftInputMode="adjustResize">
            <intent-filter android:label="@string/launcher_name">
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:label="ReconoSer Activity" android:name="com.plugin.ReconoSerActivity">
            <intent-filter>
                <action android:name="com.plugin.ReconoSerActivity" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
    </application>
</manifest>

```

## Paso 5:

Modificar o crear el archivo **com.application** de la siguiente manera:


```
package com.application;

import android.app.Application;

import com.reconosersdk.reconosersdk.ui.LibraryReconoSer;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LibraryReconoSer.init(this, "BFA3AB5F-BAEB-42DE-986E-0DA98F45F90F", ""); // Esta línea
    }
}
```
## Paso 6:

Limpia y Reconstruye el proyecto

`Tab Build > Clean Project`

`Tab Build > Rebuild Project`

## Posibles Errores:

**Android:**
* Error: package android.support.annotation does not exist
> Cambia el package: `import android.support.annotation.RequiresApi;` por: `import androidx.annotation.RequiresApi;`. Luego repita el Paso 6

## Ejecutar Android

Primero debes actualizar la carpeta www con el siguiente comando

`ionic cordova prepare android`

Luego ejecuta el proyecto en el dispositivo conectado

## iOS

## Consejo:

Realizar los cambios por medio de un editor de texto y abrir el proyecto para continuar con el paso 5


## Paso 1:

Modificar archivo **pods-debug.xcconfig** de la siguiente manera:


```
// DO NOT MODIFY -- auto-generated by Apache Cordova
#include "Pods/Target Support Files/Pods-ReconoSer/Pods-ReconoSer.debug.xcconfig"
```


## Paso 2:

Modificar archivo **pods-release.xcconfig** de la siguiente manera:


```
// DO NOT MODIFY -- auto-generated by Apache Cordova
#include "Pods/Target Support Files/Pods-ReconoSer/Pods-ReconoSer.release.xcconfig"
```


## Paso 3:

Modificar archivo **Podfile** de la siguiente manera:


```
# DO NOT MODIFY -- auto-generated by Apache Cordova

platform :ios, '12.0'

target 'ReconoSer' do
  use_frameworks!
  project 'ReconoSer.xcodeproj'
  
  pod 'ReconoSerOlimpia'
  pod 'MBProgressHUD'

end
```


## Paso 4:

Actualiza los Pods: 

`cd platforms/ios/`

`pod install`


## Paso 5: 

Actualiza la versión de Swift

Selecciona el proyecto y luego ve a los Targets. Tab **Build Settings** > busca **Swift Language Version** Selecciona Swift 5


## Paso 6: 

Actualiza el Team

Selecciona un **Team** con un **Bundle Identifier** que te permita firmar la apliacion en modo Debug **Targets** > **Signing & Capabilities**


## Paso 7:

Modificar archivo **Info.plist** de la siguiente manera **Reconoser** > **Resources** > **ReconoSer-Info.plist.** click derecho y abrir como código


```
...
<key>NSAppleMusicUsageDescription</key>
<string>${PRODUCT_NAME} would like to access your Media Library.</string>
<key>NSCameraUsageDescription</key>
<string>This will allow ${PRODUCT_NAME} to verify your identity</string>
<key>NSPhotoLibraryAddUsageDescription</key>
<string>${PRODUCT_NAME} would like to  save to your Photo Library.</string>
<key>NSPhotoLibraryUsageDescription</key>
<string>${PRODUCT_NAME} would like to access your Photo Library.</string>
```

## Paso 8:

Limpia y Reconstruye el proyecto

`Tab Product > Clean Build Folder`

`Tab Product > Build`

En caso de que salga `Build operations are disabled:` Cierra el proyecto y abralo de nuevo para refrescar los archivos


## Posibles Errores:

**iOS:**


* Could not find module 'ReconoSerOlimpia' for target 'x86_64-apple-ios-simulator'; found: arm64, arm64-apple-ios
> Conecta un dispositivo iPhone por medio de cable para que pueda compilar y asegurate de seleccionarlo para correr la aplicación en el.

* An empty identity is not valid when signing a binary for the product type 'Application' in xcode version 10.2
> Asegurate de tener haber seleccionado un **Team** con un **Bundle Identifier** que te permita firmar la apliacion en modo Debug **Targets** > **Signing & Capabilities**

* Error: Multiple commands produce
> https://stackoverflow.com/questions/50718018/xcode-10-error-multiple-commands-produce

* Command /bin/sh failed with exit code 1
> https://stackoverflow.com/questions/13006464/how-to-fix-the-issue-command-bin-sh-failed-with-exit-code-1-in-iphone

* Ionic 4 ios fails to build due to swift version 3
> https://stackoverflow.com/questions/55381659/ionic-4-ios-fails-to-build-due-to-swift-version-3

* Fail to find Firebase module
> https://github.com/firebase/firebase-ios-sdk/issues/16#issuecomment-449701843


## Ejecutar iOS

Primero debes actualizar la carpeta www con el siguiente comando

`ionic cordova prepare ios`

Luego ejecuta el proyecto en el dispositivo conectado


**NOTA:** En tu proyecto podrás observar las carpetas que nos interesa entender y modificar **www/** y **src/** en la primera encontraremos el código Javascript que pone en uso la libreria de Cordova y nos permite definir las funciones que pondremos a disposición para nuestra aplicación.

```
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ReconoSerSdk';

var ReconoSerSdk = {
  consultarConvenio: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "consultarConvenio", args);
  },
  enviarOTP: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "enviarOTP", args);
  },
  validarOTP: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "validarOTP", args);
  },
  guardarCiudadano: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "guardarCiudadano", args);
  },
  consultarCiudadano: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "consultarCiudadano", args);
  },
  guardarlogError: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "guardarlogError", args);
  },
  anversoCapturar: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "anversoCapturar", args);
  },
  reversoCapturar: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "reversoCapturar", args);
  },
  barcodeCapturar: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "barcodeCapturar", args);
  },
  guardarDocumento: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "guardarDocumento", args);
  },
  guardarBiometriaReferencia: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "guardarBiometriaReferencia", args);
  },
  validarBiometria: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "validarBiometria", args);
  },
  guardarBiometria: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "guardarBiometria", args);
  },
  mostrarPreguntas: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "mostrarPreguntas", args);
  },
  solicitarPreguntasDemograficas: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "solicitarPreguntasDemograficas", args);
  },
  validarRespuestaDemograficas: function (args, successCallback, errorCallback){
    exec(successCallback, errorCallback, PLUGIN_NAME, "validarRespuestaDemograficas", args);
  }
};

module.exports = ReconoSerSdk;
```

**Ejemplo de llamada:**
```
(<any>window).ReconoSerSdk.METODO(
  [ARGS],
  this.successCallback.bind(this),
  this.errorCallback.bind(this)
);
```

**Ejemplo de consultarConvenio:**
```
(<any>window).ReconoSerSdk.consultarConvenio(
  [this.guidConv, this.data],
  this.successCallback.bind(this),
  this.errorCallback.bind(this)
);
```

En la segunda carpeta encontraremos en el caso de iOS tres archivos: **ReconoSerSdk.swift**, **ReconoSerSdkViewController.swift** y **ReconoSerSdkViewController.xib** los cuales como te imaginaras son los archivos donde definiremos las funciones nativas que recibiran parametros y devolveran una respusta. En el caso de Android encontrarás una tres archivos: **ReconoSerActivity.java**, **ReconoSerActivity.java** y **live_preview_layout.xml** donde de igual manera encontrarás las funciones para cuando ejecutes en Android.


## Autor

- **@betancourtYeison**
