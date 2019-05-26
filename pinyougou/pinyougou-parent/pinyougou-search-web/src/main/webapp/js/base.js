// 定义模块:
var app = angular.module("pinyougou", []);
//定义过滤器
/*app.filter('trustHtml', ['$sce', function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);*/
/*$sce服务写成过滤器*/
app.filter('trustHtml', ['$sce', function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);
