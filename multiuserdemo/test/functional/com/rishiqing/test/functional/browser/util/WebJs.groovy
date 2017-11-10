package com.rishiqing.test.functional.browser.util

import geb.js.JavascriptInterface

/**
 * 封装了Page中的js的基本操作，使用单例的方式，一个页面中只允许存在一个test info bar
 * Created by  on 2017/8/17.Wallace
 */
class WebJs {
    JavascriptInterface js
    String elementId


    WebJs(JavascriptInterface js) {
        this.js = js
    }

    def showTestInfo(String text){
        if(elementId){
            removeTestInfoBar()
        }
        addTestInfoBar(text)
    }

    def removeTestInfo(){
        removeTestInfoBar()
    }

    def addTestInfoBar(String text){
        elementId = "testInfoBlock"
        js.exec("""
var info = document.createElement("div");
info.setAttribute("id", "${elementId}");
info.style.cssText = "position:absolute;top:0;left:0;right:0;background:#f00;text-align:center;padding:10px 0;";
info.innerHTML = "${text}";
document.body.appendChild(info);
""")

    }

    def removeTestInfoBar(){
        js.exec("""
var ele = document.getElementById("${elementId}");
if(ele){
document.body.removeChild(ele);
}
""")
        elementId = null
    }

    /**
     * 在指定id的元素上加红框提示
     */
    def addAlert(String selector){
        js.exec("""
var st = document.createElement('style');
st.setAttribute('id', 'testStyle');
st.setAttribute('type', 'text/css');
st.innerHTML = '.testAlertRect {position:absolute;z-index:999999;border:solid 1px rgba(255,0,0,0.3);background:transparent;animation:pulse 1s infinite;}@keyframes pulse {0%{transform:scale(1,1);}100%{transform:scale(1.2,1.2);}}';
document.body.appendChild(st);

var icon = document.querySelector('${selector}');
var rec = icon.getBoundingClientRect();
var mask = document.createElement('div');
mask.setAttribute('id', 'testMask');
mask.className='testAlertRect';
mask.style.cssText = 'left:'+(rec.left-10) +'px;top:'+(rec.top-10)+'px;width:'+(rec.right-rec.left+20)+'px;height:'+(rec.bottom-rec.top+20)+'px;';
document.body.appendChild(mask);

""")
    }
}
