/**
 * 品牌服务层js
 */
app.service('brandService', function ($http) {
    /*查询所有品牌业务ajax*/
    this.findAll = function () {
        //获得后台的数据
        return $http.get('../brand/findAll.do');
    };

    /*分页ajax*/
    this.findPage = function (page, rows) {
        //获得后台的数据
        return $http.get('../brand/findPage.do?page=' + page + '&rows=' + rows);
    };

    /*增加品牌ajax*/
    this.addBrand = function (entity) {
        return $http.post('../brand/addBrand.do', entity);
    };

    /*修改品牌ajax*/
    this.updateBrand = function (entity) {
        return $http.post('../brand/updateBrand.do', entity);
    };

    /*查询指定品牌ajax*/
    this.findOneBrand = function (id) {
        return $http.get('../brand/findOneBrand.do?id=' + id);
    };

    /*批量删除品牌ajax*/
    this.deleteBrand = function (selectIds) {
        //获取选中的复选框
       return $http.get('../brand/deleteBrand.do?ids=' + selectIds);
    };

    /*条件分页查询ajax*/
    this.search = function (page, rows,searchEntity) {
       return $http.post('../brand/searchBrands.do?page=' + page + "&rows=" + rows,searchEntity);
    };

    //获得品牌id和品牌name
    this.selectOptionList = function () {
        return $http.get('../brand/selectOptionList.do');
    }
});