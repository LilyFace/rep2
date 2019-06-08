//品牌控制层
app.controller('baseController', function ($scope) {

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
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
        console.log($scope.selectIds);
    };


    $scope.jsonToString = function (jsonString, key) {

        var json = JSON.parse(jsonString);
        var value = "";

        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][key];
        }

        return value;
    };

    //清空数组
    $scope.deleteArrayElements = function () {
        $scope.selectIds = [];
    };

    //从集合中按照key的值查询对象(通用,不一定是规格,此处例子为根据指定规格名来寻找其规格对象，方便之后给其添加选中的规格属性)
    $scope.searchObjectByKey = function (list, key, keyValue) {
        //[{“attributeName”:”规格名称”,”attributeValue”:[“规格选项1”,“规格选项2”.... ]  } , ....  ]
        for (var i = 0; i < list.length; i++) {
            //取出集合中符合key的值等于我们需要的值的那个list[i]对象
            if (list[i][key] === keyValue) {
                return list[i];
            }
        }
        return null;
    };

    //通用全选、全不选
    $scope.selectAll = false;
    $scope.all = function (m) {
        if (m=== false) {
            $scope.deleteArrayElements();
        }
        for (var i = 0; i < $scope.list.length; i++) {
            //list是当前页查到的数据
            //$scope.list[i].state = m === true;
            if (m === false) {
                $scope.selectIds.push($scope.list[i].id);
            }else{
                $scope.deleteArrayElements();
            }
        }
        console.log($scope.selectIds);
    };

});	