//控制层
app.controller('goodsController', function ($scope, $controller, $location, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    var flag = true;//监听器是否执行代码的标记
    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function () {
        var id = $location.search()['id'];//获取参数值
        if (id == null) {
            return;
        }

        goodsService.findOne(id).success(
            function (response) {
                flag = false;
                $scope.entity = response;
                //向富文本编辑器添加商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //显示图片列表
                $scope.entity.goodsDesc.itemImages =
                    JSON.parse($scope.entity.goodsDesc.itemImages);
                //显示扩展属性
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //显示规格，规格选项
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                //SKU列表规格列转换
                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec =
                        JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    };

    //保存
    $scope.save = function () {
        //提取文本编辑器的值
        $scope.entity.goodsDesc.introduction = editor.html();
        var serviceObject;//服务层对象
        if ($scope.entity.goods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    flag = false;
                    alert('保存成功');
                    $scope.entity = {};
                    editor.html("");
                    location.href = "goods.html";
                } else {
                    alert(response.message);
                }
            }
        );
    };


    //保存
    $scope.add = function () {
        //获得富文本编辑器的内容
        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    flag = false;
                    alert('保存成功');
                    $scope.entity = {};
                    editor.html('');//清空富文本编辑器
                } else {
                    alert(response.message);
                }
            }
        );
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    /**
     * 上传图片
     */
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {//如果上传成功，取出url
                $scope.image_entity.url = response.message;//设置文件地址
            } else {
                alert(response.message);
            }
        }).error(function () {
            alert("上传发生错误");
        });
    };

    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}, itemList: []};//定义页面实体结构
    //添加图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };
    //新建清空上传框的图片
    $scope.cleanImage_entity = function () {
        $scope.image_entity = {};
    };

    //列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    };

    //展示数据(测试用)
    $scope.showTest = function (data) {
        alert(data);
    };

    //查询一级目录
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        )
    };


    //查询二级目录
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        if (newValue !== undefined && newValue !== null) {
            itemCatService.findByParentId(newValue).success(
                function (response) {
                    $scope.itemCat2List = response;
                }
            );
        }
    });
    //查询三级分类
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
            if (newValue !== undefined && newValue !== null) {
                //根据选择的值，查询三级分类
                itemCatService.findByParentId(newValue).success(
                    function (response) {
                        $scope.itemCat3List = response;
                    }
                );
            }
        }
    );

    //根据三级分类查询模板id
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        if (newValue !== undefined && newValue !== null) {
            //根据选择的值，查询模板id
            itemCatService.findOne(newValue).success(
                function (response) {
                    $scope.entity.goods.typeTemplateId = response.typeId; //更新模板ID
                }
            )

        }
    });
    //根据模板id查询模板返回种类
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        if (newValue !== undefined && newValue !== null) {
            //根据选择的值，查询模板id
            typeTemplateService.findOne(newValue).success(
                function (response) {
                    $scope.typeTemplate = response;//获取类型模板
                    $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表
                    //如果没有ID，则加载模板中的扩展数据
                    if ($location.search()['id'] == null) {
                        $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
                    }

                    //查询规格列表
                    /*
                       [
                           {"options":
                               [
                                   {"id":118,"optionName":"16G","orders":1,"specId":32},
                                   {"id":119,"optionName":"32G","orders":2,"specId":32},
                                   {"id":120,"optionName":"64G","orders":3,"specId":32},
                                   {"id":121,"optionName":"128G","orders":4,"specId":32}
                               ],
                           "id":32,"text":"机身内存"}
                       ]
                    */
                    typeTemplateService.findSpecOptionsByTypeId(newValue).success(
                        function (response) {
                            $scope.specList = response;
                        }
                    );

                }
            )

        }
    });


    //保存选中规格选项
    //[{“attributeName”:”规格名称”,”attributeValue”:[“规格选项1”,“规格选项2”.... ]  } , ....  ]
    //字段specification_items
    // noinspection JSUnusedLocalSymbols
    $scope.updateSpecAttribute = function ($event, name, value) {
        //查询指定attributeName的规格对象是否存在
        var obj = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, "attributeName", name);
        //如果存在
        if (obj !== null) {
            if ($event.target.checked) {
                //当前的规格选项被选中放入数组
                obj.attributeValue.push(value);
            } else {
                //取消选择就从数组中删除
                obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
                //如果都被取消了
                if (obj.attributeValue.length === 0) {
                    //清空当前规格对象
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(obj), 1);
                }
            }
        } else {
            //规格对象不存在,创建规格对象放入字段specificationItems
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }
    };

    //创建SKU列表
    $scope.createItemList = function () {
        //我们需要的数据的结构
        //[{spec:{"机身内存":"4g","网络":"3g"},price:0,num:99999,status:'0',isDefault:'0' }}]
        //1.初始化数据结构
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];
        //2.我们需要的数据正好在specificationItems中
        var specificationItems = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < specificationItems.length; i++) {
            //覆盖原有的$scope.entity.itemList
            $scope.entity.itemList = addColumn($scope.entity.itemList, specificationItems[i].attributeName, specificationItems[i].attributeValue);
        }
    };

    //增加一列(笛卡尔积)
    addColumn = function (list, columnName, conlumnValues) {
        //新的集合，用于将覆盖原有itemList的值(实际的itemList的值)
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆(避免重复覆盖)
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    };

    //更新一级目录时候清空其他目录
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        if (flag === false) {
            return;
        } else {
            $scope.entity.goods.category2Id = null;
            $scope.entity.goods.category3Id = null;
            $scope.entity.goods.typeTemplateId = null;
            $scope.entity.goods.isEnableSpec = null;
            $scope.typeTemplate.brandIds = [];
            $scope.entity.goodsDesc.customAttributeItems = [];
            $scope.entity.goodsDesc.specificationItems = [];
            $scope.entity.goodsDesc.itemImages = [];
            $scope.entity.itemList = [];
            $scope.specList = [];
        }
    });
    //更新2级目录清空之后的目录
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        if (flag === false) {
            return;
        } else {
            $scope.entity.goods.category3Id = null;
            $scope.entity.goods.typeTemplateId = null;
            $scope.entity.goods.isEnableSpec = null;
            $scope.typeTemplate.brandIds = [];
            $scope.entity.goodsDesc.customAttributeItems = [];
            $scope.entity.goodsDesc.specificationItems = [];
            $scope.entity.goodsDesc.itemImages = [];
            $scope.entity.itemList = [];
            $scope.specList = [];
        }
    });
    //更新3级目录清空之后的目录
    // noinspection JSUnusedLocalSymbols
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        if (flag === false) {
            flag = true;
            return;
        } else {
            $scope.entity.goods.typeTemplateId = null;
            $scope.entity.goods.isEnableSpec = null;
            $scope.typeTemplate.brandIds = [];
            $scope.entity.goodsDesc.customAttributeItems = [];
            $scope.entity.goodsDesc.specificationItems = [];
            $scope.entity.itemList = [];
            $scope.entity.goodsDesc.itemImages = [];
            $scope.specList = [];
        }
    });

    //修改状态的显示
    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];
    $scope.itemCatList = [];//商品分类列表(用于根据查到的分类id作为索引,在数组中查询分类名字显示)

    //查询商品分类列表
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    //保存分类列表到数组
                    $scope.itemCatList[response[i].id] = response[i].name;

                }
            }
        );
    };

    //判断规格与规格选项是否应该被勾选
    $scope.checkAttributeValue = function (specName, optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items, 'attributeName', specName);

        if (object != null) {
            return object.attributeValue.indexOf(optionName) >= 0;
        } else {
            return false;
        }
    };

    //上下架状态的显示
    $scope.statusUpAndDown = ['下架', '上架'];
    //上下架(只能上架审核通过的并且未上架的)
    $scope.upAndDownGoods = function (isMarketableStatus) {
        goodsService.upAndDownGoods($scope.selectIds, isMarketableStatus).success(
            function (response) {
                if (response.success) {
                    bootbox.alert(response.message);
                    //清空数组
                    $scope.deleteArrayElements();
                    //清空searchEntity
                    $scope.searchEntity = {};
                    //防止删除后复选框全选
                    $scope.selectAll = false;
                    //重新加载页面
                    $scope.reloadList();
                } else {
                    bootbox.alert(response.message);
                    //清空数组
                    $scope.deleteArrayElements();
                    //清空searchEntity
                    $scope.searchEntity = {};
                    //防止删除后复选框全选
                    $scope.selectAll = false;
                }
            }
        );
    }
});	
