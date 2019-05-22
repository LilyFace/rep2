//广告控制层
app.controller("contentController",function($scope,contentService){
    $scope.contentList=[];//广告集合
    $scope.findByCategoryId=function(categoryId){
        contentService.findByCategoryId(categoryId).success(
            function(response){
                $scope.contentList[categoryId]=response;
               /* $scope.contentList[1] -> 轮播图
                $scope.contentList[2] –> 今日推荐*/
            }
        );
    }
});
