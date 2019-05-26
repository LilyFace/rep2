app.controller('searchController', function ($scope, searchService) {

    //通用标记
    $scope.flag = true;
    //搜索
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                $scope.buildPageLabel();//调用分页
            }
        );
    };

    //添加搜索条件
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'price': '',
        'spec': {},
        'pageNo': 1,
        'pageSize': 40
    };
    $scope.addSearchItem = function (key, value) {
        if (key === 'category' || key === 'brand' || key === 'price') {
            //如果点击的是分类或者是品牌
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        //都添加了设置flag为false然选择div不显示
        /*if ($scope.searchMap['keywords'] !== ''
            && $scope.searchMap['category'] !== ''
            && $scope.searchMap['brand'] !== '' && $scope.searchMap.spec !== '{}') {
            $scope.flag = false;
        }*/

        console.log($scope.searchMap);
        //执行搜索
        $scope.search();
    };

    //移除复合搜索条件
    $scope.removeSearchItem = function (key) {
        if (key === "category" || key === "brand" || key === 'price') {//如果是分类或品牌
            $scope.searchMap[key] = "";
        } else {//否则是规格
            delete $scope.searchMap.spec[key];//移除此属性
        }

        //执行搜索
        $scope.search();
        //都删除了设置flag为true然选择div不显示
        /* if ($scope.searchMap['keywords'] === ''
             && $scope.searchMap['category'] === ''
             && $scope.searchMap['brand'] === '' && $scope.searchMap.spec['spec'] === {}) {
             $scope.flag = true;
         }*/
    };

    //构建分页条
    $scope.buildPageLabel = function () {
        //分页数组
        $scope.pageLabel = [];
        //总页数
        $scope.maxPageNo = $scope.resultMap.totalPages;
        /* $scope.maxPageNo = 10;*/
        //开始页码
        $scope.startPage = 1;
        //结束页码
        $scope.endPage = $scope.maxPageNo;
        /* $scope.searchMap.pageNo = 4;*/
        $scope.firstDot = true;//前面有点
        $scope.lastDot = true;//后边有点

        //如果总页数大于5页，显示部分代码
        if ($scope.maxPageNo > 5) {
            //如果当前页小于等于3
            if ($scope.searchMap.pageNo <= 3) {
                $scope.endPage = 5;//前5页
                $scope.firstDot = false;//前面没点
            } else if ($scope.searchMap.pageNo > $scope.endPage - 2) {
                //当前页大于总页数-2
                $scope.startPage = $scope.maxPageNo - 4;
                $scope.lastDot = false;//后边没点
            } else { //显示当前页为中心的5页
                $scope.startPage = $scope.searchMap.pageNo - 2;
                $scope.endPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            //正好5条或小于5条
            $scope.firstDot = false;//前面没点
            $scope.lastDot = false;//后边没点
        }

        //循环产生页码标签
        for (var i = $scope.startPage; i <= $scope.endPage; i++) {
            //将页码标签压入数组
            $scope.pageLabel.push(i);
        }
        console.log($scope.pageLabel);
    };

    //根据页码查询
    $scope.queryByPage = function (pageNo) {
        pageNo = parseInt(pageNo);
        //页码验证
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    };

    //是否可以点击上一页，下一页的逻辑
    //当前在第一页(默认)
    $scope.isStartPage = true;
    $scope.isEndPage = false;
    $scope.CanOrCanNotClickStartOrEndPage = function () {
        if ($scope.searchMap.pageNo === 1) {
            //第一页可以点击下一页
            $scope.isStartPage = true;
            $scope.isEndPage = false;
        } else if ($scope.searchMap.pageNo === $scope.resultMap.totalPages) {
            //最后一页可以点击上一页
            $scope.isStartPage = false;
            $scope.isEndPage = true;
        }else {
            //中间都可以点
            $scope.isStartPage = false;
            $scope.isEndPage = false;
        }
    }
});