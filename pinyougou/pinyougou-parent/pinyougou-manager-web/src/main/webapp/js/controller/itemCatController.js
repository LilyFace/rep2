//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            $scope.entity.parentId = $scope.parentId;//赋予父节点id
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findByParentId($scope.parentId);//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.findByParentId($scope.parentId);//重新加载
                    //清除数组数据
                    $scope.deleteArrayElements();
                }else{
                    alert("删除失败,请先删除子目录");
                    //清除数组数据
                    $scope.deleteArrayElements();
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    $scope.parentId = 0;
    //根据父id查询
    $scope.findByParentId = function (parentId) {
        $scope.parentId = parentId;
        itemCatService.findByParentId(parentId).success(
            function (response) {
                //保存上级id
                $scope.itemList = response;
            }
        )
    };

    //定义目录等级(默认为1)
    $scope.grade = 1;
    //定义当前查询结果对象
    //$scope.entity = null;
    //定义2,3级目录对应的父节点对象
    $scope.entity_1 = null;
    $scope.entity_2 = null;
    //设置级别
    $scope.setGrade = function (grade) {
        $scope.grade = grade;
    };
    //读取当前返回的数据
    $scope.selectList = function (p_entity) {
        //$scope.entity = p_entity;
        //当前目录是一级目录(将2,3级目录置空不显示,上级为顶级)
        if ($scope.grade === 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        //当前目录是二级目录
        if ($scope.grade === 2) {
            //二级目录的父节点是一级目录,取其id值赋予二级目录
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }
        //当前目录是三级目录
        if ($scope.grade === 3) {
            //二级目录的父节点是一级目录,取其id值赋予二级目录
            $scope.entity_2 = p_entity;
        }
        //查询当前目录的数据
        $scope.findByParentId(p_entity.id);
    };
});
