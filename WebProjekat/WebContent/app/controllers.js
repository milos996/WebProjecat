
webB.controller('loginController', function($scope, $location, loginFactory) {
	
	function checkIfLogged(){
		
		loginFactory.checkIfLogged().success(function(data){
			$scope.user = data;
			if(data != ""){
				$location.url('/nologin');
			}
		})
	}
	
	checkIfLogged();
	
	$scope.login = function() {
		
		var obj = {};
		obj["username"] = document.getElementById("username").value;
		obj["password"] = document.getElementById("password").value;
		loginFactory.login(obj).success(function(data) {
			
			if(data != ""){
				//Postoji korisnik sa korisnickim imenom i lozinkom
				$location.url('/home');
				
			}else{
				if(document.getElementById("err") == null){
					
				var el1 = document.getElementById("textP");
				var parent = document.getElementById("loginDiv");

				var pElement = document.createElement("p");
				var node = document.createTextNode("Unjeli ste pogresnu lozinku/korisnicko ime!");
				pElement.id = "err";
				pElement.appendChild(node);
				
				parent.insertBefore(pElement, el1);
				}
			}
		});	
	}; 
});

webB.controller('newUserController', function($scope ,$location,  newUserFactory){
	
	$scope.register = function(){
		
		var obj = {};
		obj["username"] = document.getElementById("username").value;
		obj["password"] = document.getElementById("password").value;
		obj["name"] = document.getElementById("name").value;
		obj["surname"] = document.getElementById("surname").value;
		obj["num"] = document.getElementById("num").value;
		obj["email"] = document.getElementById("email").value;
	
		var date = new Date();
		var day = date.getDate();
		var month = date.getMonth() + 1;
		var year = date.getFullYear();
		
		obj["date"] = day + "/" + month + "/" + year;
		
		newUserFactory.register(obj).success(function(data){
			if(data == "true"){
				$location.url('/');
			}else{
				var el = document.getElementById("username");
				el.placeholder = "Korisnicko ime vec posotji";
				el.value = "";
			}
		});
		
	};
	
});

webB.controller('homeController' , function($scope,$location, homeFactory){
	
	$scope.logout = function(){
		
		if($scope.user != ""){

			homeFactory.logout().success(function(data){
				location.reload();
				    
			})
		}else{
			$location.url('/');
		}
		
		
	};
	
	function init(){
		
		
		homeFactory.setUser().success(function(data){
			$scope.user = data;
			if(data != ""){
				$scope.currState = "Odjavi se";
				 var element = document.getElementById("currState");
				    element.classList.remove("btn-primary");
				    element.classList.add("btn-danger");
				    if($scope.user.type == "A"){
				    	document.getElementById("izmjena").style.visibility = "visible";
				    }else{
				    	document.getElementById("izmjena").style.visibility = "hidden";
					    
				    }
			}else{
				$scope.currState = "Prijavi se";
				 var element = document.getElementById("currState");
				    element.classList.add("btn-primary");
				    element.classList.remove("btn-danger");
				    document.getElementById("izmjena").style.visibility = "hidden";
				    
			}
			
			homeFactory.getTopJela().success(function(data){
				$scope.topJela = data;
			});
			
			homeFactory.getTopPica().success(function(data){
				$scope.topPica = data;
			});
			
			
			document.getElementById("adminDiv").style.display = "none";
			document.getElementById("btnDiv").style.display = "none";
		});
		
		homeFactory.getRestaurants().success(function(data){
			$scope.restaurants = data;
		})
		
		
	}
	
	init();
	
	
	$scope.filtriraj = function(){
		var obj = {};
		var categ = ["Domaca_kuhinja", "Rostilj", "Kineski_restoran", "Indijski_restoran", "Poslasticarnica", "Picerija"];
		obj["restName"] = document.getElementById("pretRestName").value;
		obj["restAdd"] = document.getElementById("pretRestAddress").value;
		obj["restCat"] = document.getElementById("pretRestCategories").value;
		obj["artType"] = document.getElementById("pretArtType").value;
		obj["artName"] = document.getElementById("pretArtName").value;
		obj["artPriceFrom"] = document.getElementById("pretArtPriceFrom").value;
		obj["artPriceTo"] = document.getElementById("pretArtPriceTo").value;
		
		for (var prop in obj) {
			  if(obj[prop] == "" || obj[prop] == "non")
				  obj[prop] = "empty";
		}
		if(obj.restCat != "empty"){
			for(var curr in categ){
				if(categ[curr] != obj.restCat){
					document.getElementById(categ[curr]).style.display = "none";
				}else{
					document.getElementById(categ[curr]).style.display = "block";
				}
			}
		}else{
			for(var curr in categ){
					document.getElementById(categ[curr]).style.display = "block";
			}
		}
		
		homeFactory.setFilter(obj).success(function(data){
			$scope.restaurants = data;
		})
		
	}
	
	$scope.setFavRest = function(rest){
		homeFactory.setFavRest(rest).success(function(data){
			alert("Dodali ste restoran" + rest.name + " u svoju listu omiljenih restorana");
		})
	}
	
	$scope.setCurrA = function(art){
		$scope.currArt = art;
		
		document.getElementById("article").value = art.name;
		document.getElementById((art.id+""+art.name)).classList.remove("active");
	}
	
	$scope.addBucket = function(){
		
		var stavke =  $scope.stavke;
		
		if(stavke == null)
			stavke = [];
		
		var kol = document.getElementById("kol").value;
		var articleCurr = {article : $scope.currArt , kol : kol};
		
		stavke.push(articleCurr);
		$scope.stavke = stavke;
		
	}
	
	$scope.deleteArticle = function(){
		
		$scope.stavke.pop();
	}
	
	$scope.abort = function(){
		location.reload();
	}

	$scope.order = function(){
		
		var articles = $scope.stavke;
		
		var napomena = document.getElementById("notice").value;
		if(napomena == "")
			napomena = " ";
		
		var date = new Date();
		var myDate = date.getDate() + "/" + date.getMonth()+1 + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes();
		var state = "Poruceno";
		var idBuyer = $scope.user.username;
		
		
		var orderNew = {articles : articles , notice : napomena , date : myDate, state : state, idBuyer : idBuyer}
		homeFactory.order(orderNew).success(function(data){
			location.reload();
		});
		
	}
	
});

webB.controller('restaurantController' , function($scope,$location, restaurantFactory){
	
	function init(){
		restaurantFactory.getRestaurants().success(function(data){
			$scope.restaurants = data;	
		})
		
	}
	
	init();
	
	$scope.addR = function(){
		
		var obj = {};
		obj["name"] = document.getElementById("name").value;
		obj["address"] = document.getElementById("address").value;
		obj["categories"] = document.getElementById("categories").value;
		
		restaurantFactory.addR(obj).success(function(data){
			location.reload();
		});
	}
	
	$scope.setSelectedRes = function(resta){
		document.getElementById("name").value = resta.name
		document.getElementById("address").value = resta.address;
		document.getElementById("categories").value = resta.categories;
		var elements = document.getElementsByClassName("list-group-item");
		for( var i = 0 ; i < elements.length; i++){
			elements[i].classList.remove("active");	
		}
		
		document.getElementById(resta.id).classList.add("active");
		$scope.selResta = resta;
	}
	
	$scope.editR = function(){
		
		if($scope.selResta != null){
	
		var editR = $scope.selResta;
			
		editR.name = document.getElementById("name").value;
		editR.address = document.getElementById("address").value;
		editR.categories = document.getElementById("categories").value;
		
		
		restaurantFactory.editR(editR).success(function(data){
			location.reload();
			$scope.selResta = {};
		});
		}
	}
	
	$scope.deleteR = function(){
		
		if($scope.selResta != null){
		restaurantFactory.deleteR($scope.selResta).success(function(data){
			location.reload();
			$scope.selResta = {};
		});
		}
	}
	
	$scope.clearFields = function(){
		$scope.selResta = {};
		location.reload();
	}
});

webB.controller('articlesController', function($scope, $location, articlesFactory){
	
	function init(){
		articlesFactory.getRestaurants().success(function(data){
			$scope.restaurants = data;	
		})
		
	}
	
	init();
	
	$scope.addA = function(){
		
		var obj = {};
		obj["name"] = document.getElementById("name").value;
		obj["price"] = document.getElementById("price").value;
		obj["type"] = document.getElementById("type").value;
		obj["des"] = document.getElementById("des").value;
		obj["kol"] = document.getElementById("kol").value;
		obj["idParent"] = $scope.selResta.id;
		obj["nameRest"] = $scope.selResta.name;
		
		
		articlesFactory.addA(obj).success(function(data){
			location.reload();
		});
	}
	
	$scope.setSelectedRes = function(resta){
		var elements = document.getElementsByClassName("list-group-item");
		for( var i = 0 ; i < elements.length; i++){
			elements[i].classList.remove("active");	
		}
		
		document.getElementById(resta.id).classList.add("active");
		$scope.selResta = resta;
		document.getElementById("restoran").value = resta.name;
		
		
	}
	
	$scope.setCurrA = function(curr){
		document.getElementById("name").value = curr.name;
		document.getElementById("price").value = curr.price;
		document.getElementById("des").value = curr.des;
		document.getElementById("kol").value = curr.kol;
		document.getElementById("type").value = curr.type;
		document.getElementById("restoran").value = curr.nameRest;
		
		var elements = document.getElementsByClassName("list-group-item");
		for( var i = 0 ; i < elements.length; i++){
			elements[i].classList.remove("active");	
		}
		
		document.getElementById(curr.id + "" + curr.name).classList.add("active");
		$scope.selArt = curr;
	};
	
	
	$scope.editA = function(){
		
		if($scope.selArt != null){
	
		var editA = $scope.selArt;
			
		editA.name = document.getElementById("name").value;
		editA.kol = document.getElementById("kol").value;
		editA.price = document.getElementById("price").value;
		editA.des = document.getElementById("des").value;
		editA.type = document.getElementById("type").value;
		
		
		articlesFactory.editA(editA).success(function(data){
			location.reload();
			$scope.selArt = {};
		});
		}
	}
	
	$scope.deleteA = function(){
		
		if($scope.selArt != null){
		articlesFactory.deleteA($scope.selArt).success(function(data){
			location.reload();
			$scope.selArt = {};
		});
		}
	}
	
	$scope.clearFields = function(){
		$scope.selResta = {};
		location.reload();
	}
	
});

webB.controller('vehicleController', function($scope, $location, vehicleFactory){
	
	
	function init(){
		vehicleFactory.getVehicles().success(function(data){
			$scope.vehicles = data;	
		})
		
	}
	
	init();
	
	$scope.addV = function(){
		
		var obj = {};
		obj["mark"] = document.getElementById("mark").value;
		obj["model"] = document.getElementById("model").value;
		obj["categories"] = document.getElementById("categories").value;
		obj["regN"] = document.getElementById("regN").value;
		obj["yearP"] = document.getElementById("yearP").value;
		obj["inUse"] = "ne";
		obj["des"] = document.getElementById("des").value;
		
		vehicleFactory.addV(obj).success(function(data){
			location.reload();
		});
	}
	
	$scope.setSelectedVehicle = function(veh){
		document.getElementById("mark").value = veh.mark;
		 document.getElementById("model").value = veh.model;
		 document.getElementById("categories").value = veh.categories;
		 document.getElementById("regN").value = veh.regN;
		 document.getElementById("yearP").value = veh.yearP;
		 document.getElementById("des").value = veh.des;
		
		var elements = document.getElementsByClassName("list-group-item");
		for( var i = 0 ; i < elements.length; i++){
			elements[i].classList.remove("active");	
		}
		
		document.getElementById(veh.id).classList.add("active");
		$scope.selVeh = veh;
	}
	
	$scope.editV = function(){
		
		if($scope.selVeh != null){
	
		var editV = $scope.selVeh;
		
		editV["mark"] = document.getElementById("mark").value;
		editV["model"] = document.getElementById("model").value;
		editV["categories"] = document.getElementById("categories").value;
		editV["regN"] = document.getElementById("regN").value;
		editV["yearP"] = document.getElementById("yearP").value;
		editV["des"] = document.getElementById("des").value;
			
		
		
		vehicleFactory.editV(editV).success(function(data){
			location.reload();
			$scope.selVeh = {};
		});
		}
	}
	
	$scope.deleteV = function(){
		
		if($scope.selVeh != null){
			vehicleFactory.deleteV($scope.selVeh).success(function(data){
			location.reload();
			$scope.selVeh = {};
		});
		}
	}
	
	$scope.clearFields = function(){
		$scope.selVeh = {};
		location.reload();
	}
});

webB.controller('userController', function($scope, $location, userFactory){
	
	function init(){
		
		userFactory.getUser().success(function(data){
			$scope.user = data;
		})
	}
	
	init();
});

webB.controller('ordersController', function($scope, $location , ordersFactory){
	
	function init(){
		
		ordersFactory.getOrders().success(function(data){
			$scope.orders = data;
		})
		
		ordersFactory.getRestaurants().success(function(data){
			$scope.restaurants = data;
		})
		
		ordersFactory.getUsers().success(function(data){
			var sel1 = document.getElementById("buyer");
			var sel2 = document.getElementById("deliver");
			for(var i = 0; i < data.length; i++){
			
				
				if(data[i].type == "D"){
					if(data[i].vehicle == null){
						var option2 =  document.createElement("option");
						option2.text = data[i].username;
						sel2.add(option2);
					}
				}
				
				if(data[i].type == "K"){
					var option1 = document.createElement("option");
					option1.text = data[i].username;
					sel1.add(option1);
				}
			}
			
		});
		
		ordersFactory.getVehicles().success(function(data){
			var sel = document.getElementById("vehicleSel");
				for(var i = 0; i < data.length; i++){
					if(data[i].inUse == "ne"){			
						var option = document.createElement("option");
						option.text = data[i].mark + " " + data[i].model;
						option.value = data[i].id;
						sel.add(option);
					}
				}
		})
	}
	
	init();
	
	
	$scope.setCurrA = function(art){
		$scope.currArt = art;
		
		document.getElementById("article").value = art.name;
		document.getElementById((art.id+""+art.name)).classList.remove("active");
	
	}
	
	$scope.setCurrOrder = function(order){
		
		$scope.currOrder = order.id;
		$scope.stavke = [];	
		for(var artC in order.articles){
			$scope.stavke.push(order.articles[artC]);
		}
		
		document.getElementById("notice").value = order.notice;
		document.getElementById("buyer").value = order.idBuyer;
		document.getElementById("deliver").value = order.idDeliver;
	
	}
	
	$scope.editOrder = function(){
		var articles = $scope.stavke;
		
		var napomena = document.getElementById("notice").value;
		if(napomena == "")
			napomena = " ";
		
		var date = new Date();
		var myDate = date.getDate() + "/" + date.getMonth()+1 + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes();
		var state = "Dostava u toku";
		var idBuyer = document.getElementById("buyer").value;
		var idDeliverer = document.getElementById("deliver").value;
		var vehicleID = document.getElementById("vehicleSel").value;
		
		if(idDeliverer == "")
			idDelivere = "0";
		
		var orderNew = {articles : articles , notice : napomena , date : myDate, state : state, idBuyer : idBuyer, idDeliver: idDeliverer, vehID: vehicleID}
		ordersFactory.edit(orderNew).success(function(data){
			location.reload();
		});
	}
	
	$scope.deleteOrder = function(order){
		ordersFactory.deleteO($scope.currOrder).success(function(data){
			location.reload();
		})
	}
	
	$scope.addBucket = function(){
		
		var stavke =  $scope.stavke;
		
		if(stavke == null)
			stavke = [];
		
		var kol = document.getElementById("kol").value;
		var articleCurr = {article : $scope.currArt , kol : kol};
		
		stavke.push(articleCurr);
		$scope.stavke = stavke;
		
	}
	
	$scope.deleteArticle = function(){
		
		$scope.stavke.pop();
	}
	
	$scope.abort = function(){
		location.reload();
	}

	$scope.order = function(){
		
		var articles = $scope.stavke;
		
		var napomena = document.getElementById("notice").value;
		if(napomena == "")
			napomena = " ";
		
		var date = new Date();
		var myDate = date.getDate() + "/" + date.getMonth()+1 + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes();
		var state = "Poruceno";
		var idBuyer = document.getElementById("buyer").value;
		var idDeliverer = document.getElementById("deliver").value;
		var vehicleID = document.getElementById("vehicleSel").value;
		
		if(idDeliverer == "")
			idDelivere = "0";
		
		var orderNew = {articles : articles , notice : napomena , date : myDate, state : state, idBuyer : idBuyer, idDeliver: idDeliverer, vehID: vehicleID}
		if(articles.length > 0 && idBuyer != ""){
		ordersFactory.order(orderNew).success(function(data){
			location.reload();
		});
		}else{
			alert("Mora te unjeti barem jedan artikl i kupca da bi porucili!");
		}
	}
	
	$scope.takeOrder = function(order){
		if(order.state != "Otkazano"){
			if(order.idDeliver == "0"){
				ordersFactory.takeVeh().success(function(data){
					if(data == "true"){
						ordersFactory.takeOrder(order).success(function(data){
							alert("Preuzeta porudzbina");
							location.reload();
						});
					}else{
						alert("Nema trenutno dostupnog vozila ili je dostavljac vec zauzet");
					}
				});
			}else{
				alert("Dostava je krenula");
			}
		}
	}
	
	$scope.checkOrder = function(order){
		if(order.state != "Otkazano"){
			if(order.idDeliver != "0"){
				ordersFactory.checkOrder(order).success(function(data){
					if(data == "true"){
						ordersFactory.backVeh().success(function(data){
							if(data == "false"){
								alert("Nekim cudom");
								
							}else{
								alert("Uspjesno izvrsena porudzbina!!");
								location.reload();
							}
						})
					}else{
						alert("Nisi ti na ovoj dostavi rasporedjen");
					}
				})
			}else{
				alert("Dostava nije u toku pa ne moze biti ni gotova");
			}
		}
	}
	
	$scope.cancelOrder = function(order){
		if(order.state != "Dostavljeno" && order.state != "Otkazano"){
			ordersFactory.cancelOrder(order).success(function(data){
				ordersFactory.backVeh().success(function(data){
					alert("Porudzbina je otkazana");
					location.reload();
				});
			});
		}
			
		
	}
	
});

webB.controller('korisniciController', function($scope,korisniciFactory){
	
	function init(){
		
		korisniciFactory.getUsers().success(function(data){
			$scope.users = data;
		})
		
		$scope.setAdmin = function(user){
			
			if(user.type != "A"){
				
				user.type = "A";
				
				korisniciFactory.setAdmin(user).success(function(data){
					
					$scope.users = data;
				});
			}
		}
		$scope.setKupac = function(user){
			
			if(user.type != "K"){	
				
				user.type = "K";
				
				korisniciFactory.setKupac(user).success(function(data){
				
					$scope.users = data;
					
				});
			}
		}
		$scope.setDostavljac = function(user){
			
			if(user.type != "D"){
			
				user.type = "D";
				
				korisniciFactory.setDostavljac(user).success(function(data){
					
					$scope.users = data;
					
				});
				
			}
		}

	}
	
	init();
})