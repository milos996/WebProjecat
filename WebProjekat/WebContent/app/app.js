
var webB = angular.module('CustomB', ['ngRoute']);

webB.config(function($routeProvider) {
	$routeProvider.when('/',
	{
		templateUrl: 'html/login.html'
	}).when('/registration',
	{
		templateUrl: 'html/register.html'
	}).when('/home',{
		
		templateUrl: 'html/home.html'
	}).when('/nologin',{
		
		templateUrl: 'html/nologin.html'
	}).when('/restaurant',{
		
		templateUrl: 'html/restaurant.html'
	}).when('/articles',{
		
		templateUrl: 'html/articles.html'
	}).when('/vehicles',{
		
		templateUrl: 'html/vehicle.html'
	}).when('/user',{
		
		templateUrl: 'html/user.html'
	}).when('/orders',{
		
		templateUrl: 'html/orders.html'
	}).when('/korisnici', {
		templateUrl: 'html/korisnici.html'
	})
});

webB.config(function($logProvider){
    $logProvider.debugEnabled(true);
});