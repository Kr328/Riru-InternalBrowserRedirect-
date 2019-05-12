# Riru - InternalBrowserRedirect

A module of [Riru](https://github.com/RikkaApps/Riru). Redirect internal browser activity to ACTION_VIEW.



## Requirements

* [Riru](https://github.com/RikkaApps/Riru) > 7 installed.
* Android 9.0 (8.0/8.1 not tested)



## Feature

Redirect some application's internal browser to external browser (eg. TIM's QQBrowser to Chrome)

**Double click same url in 5s will fallback to internal browser**

Currently support apps

- WeChat
- QQ
- TIM
- Bilibili



## Build

  1.Install JDK ,Gradle ,Android SDK ,Android NDK

  2.Configure with your environment [local.properties](https://github.com/Kr328/Riru-InternalBrowserRedirect/blob/master/local.properties)

  3.Run command 

``` Gradle 
./gradlew build
```
  4.Pick riru-internal-browser-redirect.zip from module/build/outputs
