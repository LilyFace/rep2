/**
 *  品牌控制层
 */
app.controller('brandController', function ($scope, $controller, brandService) {
    $controller('baseController', {$scope: $scope});//继承通用控制层

    /*查询所有品牌*/
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            });
    };

    /*分页查询品牌*/
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    /*保存品牌*/
    $scope.save = function () {
        var serviceObject;//服务层对象
        //防止两次请求的变量
        $scope.reload = true;
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = brandService.updateBrand($scope.entity);//则执行修改方法
        } else {
            serviceObject = brandService.addBrand($scope.entity);
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //防止两次请求
                    if (!$scope.reload) {
                        return;
                    }
                    //重新查询
                    $scope.reloadList();//重新加载
                    $scope.reload = false;
                    //2毫秒后可继续请求（在2毫秒中，第二次请求已经被结束）
                    setTimeout(function () {
                        $scope.reload = true;
                    }, 50);
                } else {
                    alert(response.message);
                }
            }
        );
    };

    /*查询指定品牌*/
    $scope.findOneBrand = function (id) {
        brandService.findOneBrand(id).success(
            function (response) {
                $scope.entity = response;
            });
    };

    /*批量删除品牌*/
    $scope.deleteBrand = function () {
        //防止两次请求的变量
        $scope.reload = true;
        brandService.deleteBrand($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    //防止两次请求
                    if (!$scope.reload) {
                        return;
                    }
                    $scope.reloadList();//刷新列表
                    //清除数组数据
                    $scope.deleteArrayElements();
                    $scope.reload = false;
                    //2毫秒后可继续请求（在2毫秒中，第二次请求已经被结束）
                    setTimeout(function () {
                        $scope.reload = true;
                    }, 50);
                } else {
                    alert("删除失败");
                    //清除数组数据
                    $scope.deleteArrayElements();
                }
            }
        );
    };

    /*条件分页查询*/
    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
});