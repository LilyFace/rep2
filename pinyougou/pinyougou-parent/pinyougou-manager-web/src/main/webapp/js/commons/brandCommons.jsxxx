/**
 * 品牌js
 */

//构建app模块时引入pagination模块
var app = angular.module('pinyougou', ['pagination']);//定义模块
app.controller('brandController', function ($scope, $http) {
    //获得后台数据
    $scope.findAll = function () {
        //获得后台的数据
        $http.get('/brand/findAll.do').success(
            function (response) {
                $scope.list = response;
            });
    };

    //重新加载列表数据
    $scope.reloadList = function () {
        //切换页码
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //分页控件配置
    //防止两次请求的变量
    $scope.reload = true;
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //防止两次请求
            if (!$scope.reload) {
                return;
            }
            $scope.reloadList();//重新加载
            $scope.reload = false;
            //2毫秒后可继续请求（在2毫秒中，第二次请求已经被结束）
            setTimeout(function () {
                $scope.reload = true;
            }, 200);
        }
    };
    //分页ajax
    $scope.findPage = function (page, rows) {
        $http.get('/brand/findPage.do?page=' + page + '&rows=' + rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //保存品牌
    $scope.save = function () {
        var methordName = 'addBrand';
        if ($scope.entity.id != null) {//如果有ID
            methordName = 'updateBrand';//则执行修改方法
        }
        //添加或保存品牌
        $http.post('/brand/' + methordName + '.do', $scope.entity).success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };

    //查询指定品牌
    $scope.findOneBrand = function (id) {
        $http.get('/brand/findOneBrand.do?id=' + id).success(
            function (response) {
                $scope.entity = response;
            });
    };

    $scope.selectIds = [];//选中的ID集合
    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
        }
    };

    //批量删除数据
    $scope.deleteBrand = function () {
        //获取选中的复选框
        $http.get('/brand/deleteBrand.do?ids=' + $scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    };

    //全选、全不选
    $scope.selectAll = false;
    $scope.all = function (m) {
        for (var i = 0; i < $scope.persons.length; i++) {
            $scope.persons[i].state = m === true;
        }
    };

    //条件分页查询
    $scope.searchEntity={};//定义搜索对象(处理没有条件时候Required request body is missing异常，将其定义为空json对象)
    $scope.search = function (page, rows) {
        $http.post('/brand/searchBrands.do?page=' + page + "&rows=" + rows, $scope.searchEntity).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;//总记录数
                $scope.list = response.rows;//给列表变量赋值
            }
        );
    };

});