/**
 * 通用分页js
 */
//构建app模块时引入pagination模块
var app = angular.module('pinyougou', []);//定义模块
app.controller('brandController', function ($scope, $http) {
    //获得后台数据
    $scope.findAll = function () {
        //获得后台的数据
        $http.get('/brand/findAll.do').success(
            function (response) {
                $scope.list = response;
            });
    };

    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.findPage( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

    //分页
    $scope.findPage=function(page,rows){
        $http.get('/brand/findPage.do?page='+page+'&rows='+rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    };
});