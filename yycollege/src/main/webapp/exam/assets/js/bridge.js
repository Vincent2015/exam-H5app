(function(root) {
  var ua = window.navigator.userAgent;
  //桥接器的命名空间
  var ns = {
    _initialized: false,
    // 探测操作系统浏览器
    ios: (/iPhone|iPad|iPod/i).test(ua),
    android: (/Android/i).test(ua),
    // 由于桥的初始化需要时间，不知道是否已经ok，这适用于在开始就要执行的bridge函数
    // 桥接器的ready函数，只在初始化的时候才调用，先把回调函数都追加到一个列表中
    ready: function(callback) {
      if (callback) {
        if (ns._initialized) {
          // 立即执行
          callback();
        } else {
         // 延迟执行
         ns.readycallback = callback;
       }
      }
    }
  };

  // 构造一个回调函数
  var wrapMethod = function(bridge, data){
    // 第一个参数为原生功能调用完成的callback，经常用
    // 第二个参数为原生功能在使用过程中需要调用的回调
    var ret = function(callback, bridgeCallback, parameters) {
      if (bridge) {
        if(!callback) {
          // the default callback to fullfill bridge's requirement
          callback = function(responseData){ };
        }
        // 检查是否有回调函数
        if (bridgeCallback && typeof(bridgeCallback) == 'function'
         && data.parameters && data.parameters.callback) {
          var fn = data.parameters.callback;
          // 把window对象上注册的函数注册进原生
          bridge.registerHandler(fn, bridgeCallback);
        }
        // 检查额外的参数，并且合并
        if (parameters && typeof(parameters) === 'object') {
          if (data.parameters) {
            // 合并参数
            for( var key in parameters) {
              data.parameters[key] = parameters[key];
            }
          } else {
            data.parameters = parameters;
          }
        }
        bridge.send(JSON.stringify(data), callback);
      } else {
        console.log("js bridge has not been initialized yet.");
      }
    };
    return ret;
  };
  // generate function in the namespace
  var generator = function(ns, bridge) {
    // 定义桥调用协议
    var methods = {
      "hiddenMenu" : {
        function: 'hiddenMenu'
      },
      "closePage": {
        function: 'closePage'
      },
      "setHeader": {
        function: 'navRightItemList'
      },
      "scanQRCode": {
        function: 'scanQRCode',
        parameters: {
          type: 0,
          callback: 'scanQRCodeCallback'
        }
      },
      "scanQRCodeMore": {
        function: 'scanQRCode',
        parameters: {
          type: 1,
          callback: 'scanQRCodeCallback'
        }
      },
      "openWindow": {
        function:'openWindow'
      }
    };
    // 根据方法定义生成方法
    for( var method in methods) {
      var data = methods[method];
      ns[method] = wrapMethod(bridge, data);
    }
    // other special method
  };
  // 初始化函数
  var connectWebViewJavascriptBridge = function(callback) {
      if (window.WebViewJavascriptBridge) {
          callback(WebViewJavascriptBridge);
      } else {
          document.addEventListener('WebViewJavascriptBridgeReady', function() {
              callback(WebViewJavascriptBridge);
          }, false);
      }
  };
  // 初始化JS Bridge
  connectWebViewJavascriptBridge(function(bridge){
    bridge.init(function(message, responseCallback) {});
    // 确保桥接器绑定到window对象
    if(!window.WebViewJavascriptBridge) {
      window.WebViewJavascriptBridge = bridge;
    }
    // 如果有延迟执行回调的情况，执行它
    ns._initialized = true; // 标识为已经初始化
    if(ns.readycallback) {
      ns.readycallback();
      delete ns.readycallback; // 执行完清理掉
    }
    // 生成函数
    generator(ns, bridge);
    // 其他依赖原生的初始化动作
    // 默认隐藏掉页头
  ns.hiddenMenu();
    // 对于企业空间ios，启用特殊适配样式
    if (ns.ios) {
      // document.body.className = "platform-ios";
    }
  });

  // bind methos to window's yyesn object
  window.yyesn = root.yyesn = ns;
  //
  return ns;
}(this));
