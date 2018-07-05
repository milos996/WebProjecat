webB.factory('loginFactory', function($http) {
	
	var factory = {} ;

	factory.login = function(user) {
		return $http.post('/WebProjekat/rest/login/checkUser/'+user.username+','+user.password);
	};
	
	factory.checkIfLogged = function(){
		return $http.get('/WebProjekat/rest/login/logged');
	}
	
	return factory;
	
});

webB.factory('newUserFactory', function($http){
	var factory = {};
	
	factory.register = function(newUser){
		return $http.post('/WebProjekat/rest/login/add', newUser);
	}
	
	return factory;
});

webB.factory('korisniciFactory', function($http){
	var factory = {};
	
	factory.getUsers = function(){
		return $http.get('/WebProjekat/rest/login/getKorisnike');
	}
	
	factory.setKupac = function(user){
		return $http.post('/WebProjekat/rest/login/changeKor/'+user.username+","+user.type);
	}
	factory.setDostavljac = function(user){
		return $http.post('/WebProjekat/rest/login/changeKor/'+user.username+","+user.type);
	}
	
	factory.setAdmin = function(user){
		return $http.post('/WebProjekat/rest/login/changeKor/'+user.username+","+user.type);
	}
	
	return factory;
	
});
webB.factory('homeFactory', function($http){
	var factory = {};
	
	factory.logout = function(){
		return $http.post('/WebProjekat/rest/login/logout');
		
	}
	
	factory.setUser = function(){
		return $http.get('/WebProjekat/rest/login/logged');
	}
	
	factory.getRestaurants = function(){
		return $http.get('/WebProjekat/rest/restaurants/getR');
	}
	
	factory.setFavRest = function(curr){
		return $http.post('/WebProjekat/rest/login/addFavRest', curr);
	}
	
	factory.order = function(articles){
		return $http.post('/WebProjekat/rest/orders/add' , articles);
	}
	
	factory.getTopJela = function(){
		return $http.get('/WebProjekat/rest/orders/getTopJela');
	}
	
	factory.getTopPica = function(){
		return $http.get('/WebProjekat/rest/orders/getTopPica');
	}
	
	factory.setFilter = function(filter){
		return $http.get('/WebProjekat/rest/restaurants/filter/'+filter.restName+","+filter.restAdd +","+
															filter.restCat+","+filter.artType +","+
															filter.artName + "," + filter.artPriceFrom+","+filter.artPriceTo);
	}
	return factory;
	
});

webB.factory('restaurantFactory' , function($http){
	
	var factory = {};
	
	factory.getRestaurants = function(){
		
		return $http.get('/WebProjekat/rest/restaurants/getR');
	}
	
	factory.addR = function(restaurant){
		
		return $http.post('/WebProjekat/rest/restaurants/addR', restaurant);
		
	}
	
	factory.editR = function(restaurant){
			return $http.post('/WebProjekat/rest/restaurants/editR', restaurant);
		
	}
	
	factory.deleteR = function(restaurant){
			return $http.post('/WebProjekat/rest/restaurants/deleteR',restaurant);
	}
	return factory;
});

webB.factory('articlesFactory', function($http){
	var factory = {};
	
	factory.getRestaurants = function(){
		
		return $http.get('/WebProjekat/rest/restaurants/getR');
	}
	
	factory.addA = function(article){
		
		return $http.post('/WebProjekat/rest/articles/addA', article);
		
	}
	
	factory.editA = function(article){
		return $http.post('/WebProjekat/rest/articles/editA', article);
	}
	
	factory.deleteA = function(articleDel){
		return $http.post('/WebProjekat/rest/articles/deleteA', articleDel);
	}
	return factory;
});

webB.factory('vehicleFactory', function($http){
	var factory = {};
	
	
	factory.getVehicles = function(){
		
		return $http.get('/WebProjekat/rest/vehicles/getV');
	}
	
	factory.addV = function(veh){
		
		return $http.post('/WebProjekat/rest/vehicles/addV', veh);
		
	}
	
	factory.editV = function(veh){
			return $http.post('/WebProjekat/rest/vehicles/editV', veh);
		
	}
	
	factory.deleteV = function(veh){
			return $http.post('/WebProjekat/rest/vehicles/deleteV',veh);
	}
	
	return factory;
});

webB.factory('userFactory', function($http){
	var factory = {};
	
	factory.getUser = function(){
		
		return $http.get('/WebProjekat/rest/login/logged');
	}
	return factory;
})

webB.factory('ordersFactory', function($http){
	var factory = {};
	
	factory.getOrders= function(){
		
		return $http.get('/WebProjekat/rest/orders/getO');
	}
	
	factory.takeVeh = function(){
		return $http.post('/WebProjekat/rest/vehicles/takeVeh');
	}
	
	factory.takeOrder = function(order){
		
		return $http.post('/WebProjekat/rest/orders/takeOrder', order);
	}
	
	factory.checkOrder = function(order){
		
		return $http.post('/WebProjekat/rest/orders/checkOrder', order);
	}
	
	factory.backVeh = function(){
		return $http.post('/WebProjekat/rest/vehicles/backVeh');
	}
	
	factory.cancelOrder = function(order){
		return $http.post('/WebProjekat/rest/orders/cancelOrder', order);
	}
	
	factory.getRestaurants = function(){
		return $http.get('/WebProjekat/rest/restaurants/getR');
	}
	factory.getUsers = function(){
		return $http.get('/WebProjekat/rest/login/getKorisnike');

	}
	
	factory.order = function(ord){
		return $http.post('/WebProjekat/rest/orders/adminAdd', ord);
	}
	
	factory.getVehicles = function(){
		return $http.get('/WebProjekat/rest/vehicles/getV');
	}
	
	factory.edit = function(order){
		return $http.post('/WebProjekat/rest/orders/editAdmin', order);
	}
	
	factory.deleteO = function(orderId){
		return $http.post('/WebProjekat/rest/orders/delAdmin/'+orderId);
	}
	return factory;
})
