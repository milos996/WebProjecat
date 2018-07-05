webB.directive('categories', function() {
        return {
            restrict: 'E',
            templateUrl: 'directives/categories.html'
        };
});

webB.directive('order',function(){
	return{
		restrict : 'E',
		templateUrl : 'directives/edit_order.html'
	}
});