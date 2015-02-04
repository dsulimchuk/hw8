(function() {
    'use strict';

    var _this;
    var  deferred;
    var _productService;
    var _$rootScope;
    var ProductController;
    var BidService = function (webSocketEndPoint, $q, ProductService, $rootScope) {
        // Instance attributes go here:
        _this = this;
        deferred = $q.defer();
        _productService  = ProductService;
        _$rootScope = $rootScope;

        this.active  = false;
        if (window.WebSocket) {
            this.ws = new WebSocket(webSocketEndPoint);
            this.ws.onopen = function() {
                console.log("onopen");
                deferred.promise.then(function(){
                    _this.active = true;
                });
                deferred.resolve();

            };
            this.ws.onmessage = function(e) {
                console.log("echo from server : " + e.data);
                $rootScope.$apply(function() {
                    var delimiterPos = e.data.indexOf(':');
                    var delimiter2Pos = e.data.indexOf(':', delimiterPos + 1);
                    var type = e.data.slice(0, delimiterPos);
                    var typeOfNot = e.data.slice(delimiterPos + 1, delimiter2Pos);
                    var obj = e.data.slice( delimiter2Pos + 1);
                    console.log('!!!!arrive ' + type);
                    if (type == 'product') {
                        ProductController.product = JSON.parse(obj);
                    }else if (type == 'notification') {
                        ProductController.notification = obj;
                        if (typeOfNot == 'ok') {
                            ProductController.typeNotification = 'bg-success';
                        }else if (typeOfNot == 'warning') {
                            ProductController.typeNotification = 'bg-warning';
                        }else {
                            ProductController.typeNotification = 'bg-danger';
                        }
                        ProductController.hideNotification = false;
                    }

                });

            };
            this.ws.onclose = function() {
                console.log("onclose");
                _this.active = false;
            };
            this.ws.onerror = function() {
                console.log("onerror"); };
        } else {
            console.log("WebSocket object is not supported");
        };

    };


    BidService.prototype = {
      serveForProduct: function(product, _ProductController) {
          var credentials = product.id+':'+_$rootScope.USER_ID;
          if (_this.ws.readyState === 1) {
              _this.ws.send(credentials);
          } else {
              deferred.promise.then(function(){
                  console.log("defered");
                  _this.ws.send(credentials);
              });
          }
          ProductController = _ProductController;


      },
      placeBid: function(bid){

      }
    };

    BidService.$inject = ['webSocketEndPoint', '$q', 'ProductService', '$rootScope'];
    angular.module('auction').service('BidService', BidService);
})();