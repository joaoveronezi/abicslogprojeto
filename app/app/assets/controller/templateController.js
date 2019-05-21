var moduleTemplate = angular.module('moduleTemplate', [ 'ui.bootstrap', 'ngRoute', 'ngCookies', 'localytics.directives',
                                                        'moduleItem', 'moduleCategoria',
                                                        'moduleCotacao', 'moduleUsuario', 'moduleConfiguracaoUsuario',
                                                        'moduleGrafico',
                                                        'moduleCambio', 'moduleContato', 
                                                        'moduleSumarizacaoSincronizacao', 'moduleTutorial',
                                                        'moduleAbicsData', 'moduleDashboard', 'moduleDashboardNacional', 'moduleHistorico',
                                                        'moduleInfoNutricionais', 'moduleFonte', 'moduleLogistica', 'moduleLogisticaHistorico',
                                                        'moduleAbicsDataComtrade', 'moduleTemplateRelatorioAbicsData',
                                                        'moduleAbicsDataEmpresaProduto', 'moduleAbicsDataUSDA', 'moduleFonteAbicsData',
                                                        'moduleAbicsDataRelatorio', 'moduleAbicsDataPais', 'moduleAbicsDataRelatorioBrasil',
                                                        'moduleTemplateAbicsData', 'moduleTemplateAbicsLog', 'moduleHome']);

moduleTemplate.directive('directiveAlertaSistemaTemplate', function() {
	return {
		templateUrl : 'assets/directives/directiveAlertaSistemaTemplate.html'
	};
});

moduleTemplate.directive('directiveModalPrimeiroLogin', function() {
	return {
		templateUrl : 'assets/directives/directiveModalPrimeiroLogin.html'
	};
});

moduleTemplate.directive('directiveModalInformacoes', function() {
	return {
		templateUrl : 'assets/directives/directiveModalInformacoes.html'
	};
});

moduleTemplate.filter('filterMonetary', function () {

	return function(valorInternacional) {

		if(valorInternacional && valorInternacional != null && valorInternacional.toString().length > 0) {
			var numero = Number(valorInternacional).toFixed(2);
			return numero.toString().replace(".",",");
		}
	};
});

moduleTemplate.filter('filterMonetarySemSinal', function () {

	return function(valorInternacional) {

		if(valorInternacional && valorInternacional != null && valorInternacional.toString().length > 0) {
			
			var numero = Number(valorInternacional).toFixed(2);
			
//			console.log(numero);
//			console.log(numero < 0);
//			if(numero < 0) {
//				numero = numero * -1;
//			}
			
//			console.log(numero.toString().replace(".",",").replace("-",""));
			
			return numero.toString().replace(".",",").replace("-","");
		}
	};
});


moduleTemplate.filter('filterPercentageSemSinal', function () {

	return function(valorInternacional) {

		if(valorInternacional && valorInternacional != null && valorInternacional.toString().length > 0) {
			var numero = Number(valorInternacional).toFixed(1);
			console.log(numero);
			return numero.toString().replace(".",",").replace("-","");
		}
	};
});

moduleTemplate.filter('filterDotToComma', function () {

	return function(valor) {
		
		if(valor != null && valor.toString().length > 0) {
			return valor.toString().replace(".",",");
		};
	};
});

moduleTemplate.filter('filterPercentage', function () {

	return function(valorInternacional) {

		if(valorInternacional && valorInternacional != null && valorInternacional.toString().length > 0) {
			var numero = Number(valorInternacional).toFixed(1);
			return numero.toString().replace(".",",");
		}
	};
});
//if(valorInternacional != null) { console.log('true')} else {console.log('false')}
moduleTemplate.filter('filterBooleanI18N', function () {

	return function(tipoBoolean) {

		var retorno = "";

		if(tipoBoolean) {
			retorno = "Sim";
		} else {
			retorno = "Não";
		}

		return retorno;
	};
});

moduleTemplate.filter('filterCotacaoNacional', function () {

	return function(tipoCotacaoNacional) {

		var retorno = "";

		if(tipoCotacaoNacional == 1) {
			retorno = "Nacional";
		}

		if(tipoCotacaoNacional == 0) {
			retorno = "Internacional";
		}

		return retorno;
	};
});

//moduleTemplate.filter('filterTipoItem', function () {
//
//	return function(tipoItem) {
//
//		var retorno = "";
//
//		if(tipoItem == 1) {
//			retorno = "Item com valor informado pelo usuário";
//		}
//
//		if(tipoItem == 2) {
//			retorno = "Item com valor calculado pelo sistema";
//		}
//
//		if(tipoItem == 3) {
//			retorno = "Item composto com valores";
//		}
//
//		return retorno;
//	};
//});

moduleTemplate.filter('filterUnidadeItem', function () {

	return function(unidadeItem) {
		var retorno = "";

		if(unidadeItem == 0) {
			retorno = "Percentual";
		}

		if(unidadeItem == 1) {
			retorno = "Real";
		}

		if(unidadeItem == 2) {
			retorno = "Dolar";
		}

		return retorno;
	};
});

//moduleTemplate.filter('filterTipoMoeda', function () {
//
//	return function(tipoMoeda) {
//
//		var retorno = "";
//
//		if(tipoMoeda == 0) {
//			retorno = "Real (R$)";
//		}
//
//		if(tipoMoeda == 1) {
//			retorno = "Dolar (US$)";
//		}
//
//		if(tipoMoeda == 2) {
//			retorno = "Euro (€)";
//		}
//
//		if(tipoMoeda == null) {
//			retorno = " % ";
//		}
//
//		return retorno;
//	};
//});


moduleTemplate.filter('filterSimboloMoeda', function () {

	return function(item) {

		var retorno = "";

//		if(unidadeItem == 0) {
//			retorno = "Percentual";
//		}
//
//		if(unidadeItem == 1) {
//			retorno = "Real";
//		}
//
//		if(unidadeItem == 2) {
//			retorno = "Dolar";
//		}

		if(item.tipoMoeda == 0) {
			retorno = "(%)";
		}

		if(item.tipoMoeda == 1) {
			retorno = "(R$)";
		}

		if(item.tipoMoeda == 2) {
			retorno = "(US$)";
		}

//		if(item.tipoMoeda == null && item.isCategoriaTotal == 0) {
//			retorno = "(" + item.porcentagem + " %)";
//		}

//		if(item.tipoMoeda == 0) {
//			retorno = "(R$)";
//		}
//
//		if(item.tipoMoeda == 1) {
//			retorno = "(US$)";
//		}
//
//		if(item.tipoMoeda == 2) {
//			retorno = "(€)";
//		}
//
//		if(item.tipoMoeda == null && item.isCategoriaTotal == 0) {
//			retorno = "(" + item.porcentagem + " %)";
//		}

		return retorno;
	};
});

moduleTemplate.filter('filterPermissaoUsuario', function () {

	return function(permissao) {

		var retorno = "";


		if(permissao == 1) {
			retorno = "Administrador";
		}

		if(permissao == 2) {
			retorno = "Diretor";
		}

		if(permissao == 3) {
			retorno = "Convidado";
		}

		if(permissao == 4) {
			retorno = "Logistica";
		}

		return retorno;
	};
});

moduleTemplate.filter('filterAtivoUsuario', function () {

	return function(ativo) {

		var retorno = "";


		if(ativo == 0) {
			retorno = "Desativado";
		}

		if(ativo == 1) {
			retorno = "Ativo";
		}

		return retorno;
	};
});


moduleTemplate.filter('filterStatus', function () {

	return function(status) {

		var retorno = "";


		if(status == 1) {
			retorno = "Atualizado";
		}

		if(status == 0) {
			retorno = "Desatualizado";
		}

		return retorno;
	};
});

moduleTemplate.filter('unixtimeDateFilter', function () {

	return function(dataUnixtime) {

		if(dataUnixtime != null) {
			if(dataUnixtime.toString().length == 10) {
				dataUnixtime = dataUnixtime * 1000;
			}
//			var data = moment(dataUnixtime);
//			return data.toDate();
			return moment(dataUnixtime).format("DD/MM/YYYY HH:mm:ss");
		}
		return dataUnixtime;
	};
});

moduleTemplate.filter('unixtimeDateWithoutHourFilter', function () {

	return function(dataUnixtime) {

		if(dataUnixtime != null) {
			if(dataUnixtime.toString().length == 10) {
				dataUnixtime = dataUnixtime * 1000;
			}
//			var data = moment(dataUnixtime);
//			return data.toDate();
			return moment(dataUnixtime).format("DD/MM/YYYY");
		}
		return dataUnixtime;
	};
});

moduleTemplate.config(function($routeProvider, $locationProvider, $httpProvider) {

	$routeProvider.when('/item', {
		templateUrl : 'assets/pages/item.html',
		controller  : 'controllerItem'
	}).when('/categoria', {
		templateUrl : 'assets/pages/categoria.html',
		controller  : 'controllerCategoria',
	}).when('/cotacao', {
		templateUrl : 'assets/pages/cotacao.html',
		controller  : 'controllerCotacao',
	}).when('/usuario', {
		templateUrl : 'assets/pages/usuario.html',
		controller  : 'controllerUsuario',
	}).when('/grafico', {
		templateUrl : 'assets/pages/grafico.html',
		controller  : 'controllerGrafico',
	}).when('/configuracaoUsuario', {
		templateUrl : 'assets/pages/configuracaoUsuario.html',
		controller  : 'controllerConfiguracaoUsuario',
	}).when('/cambio', {
		templateUrl : 'assets/pages/cambio.html',
		controller  : 'controllerCambio',
	}).when('/contato', {
		templateUrl : 'assets/pages/contato.html',
		controller  : 'controllerContato',
	}).when('/sumarizacaoSincronizacao', {
		templateUrl : 'assets/pages/sumarizacaoSincronizacao.html',
		controller  : 'controllerSumarizacaoSincronizacao',
	}).when('/tutorial', {
		templateUrl : 'assets/pages/tutorial.html',
		controller  : 'controllerTutorial',
	}).when('/abicsData', {
		templateUrl : 'assets/pages/abicsData.html',
		controller  : 'controllerAbicsData',
	}).when('/abicsDataPais', {
		templateUrl : 'assets/pages/abicsDataPais.html',
		controller  : 'controllerAbicsDataPais',
	}).when('/abicsDataRelatorio', {
		templateUrl : 'assets/pages/abicsDataRelatorio.html',
		controller  : 'controllerAbicsDataRelatorio',
	}).when('/abicsDataRelatorioBrasil', {
		templateUrl : 'assets/pages/abicsDataRelatorioBrasil.html',
		controller  : 'controllerAbicsDataRelatorioBrasil',
	}).when('/abicsDataComtrade', {
		templateUrl : 'assets/pages/abicsDataComtrade.html',
		controller  : 'controllerAbicsDataComtrade',
	}).when('/abicsDataEmpresaProduto', {
		templateUrl : 'assets/pages/abicsDataEmpresaProduto.html',
		controller  : 'controllerAbicsDataEmpresaProduto',
	}).when('/abicsDataUSDA', {
		templateUrl : 'assets/pages/abicsDataUSDA.html',
		controller  : 'controllerAbicsDataUSDA',
	}).when('/dashboard', {
		templateUrl : 'assets/pages/dashboard.html',
		controller  : 'controllerDashboard',
	}).when('/dashboardNacional', {
		templateUrl : 'assets/pages/dashboardNacional.html',
		controller  : 'controllerDashboardNacional',
	}).when('/templateRelatorioAbicsData', {
		templateUrl : 'assets/pages/templateRelatorioAbicsData.html',
		controller  : 'controllerTemplateRelatorioAbicsData',
	}).when('/historico', {
		templateUrl : 'assets/pages/historico.html',
		controller  : 'controllerHistorico',
	}).when('/fonte', {
		templateUrl : 'assets/pages/fonte.html',
		controller  : 'controllerFonte',
	}).when('/fonteAbicsData', {
		templateUrl : 'assets/pages/fonteAbicsData.html',
		controller  : 'controllerFonteAbicsData',
	}).when('/logistica', {
		templateUrl : 'assets/pages/logistica.html',
		controller  : 'controllerLogistica',
	}).when('/logisticaHistorico', {
		templateUrl : 'assets/pages/logisticaHistorico.html',
		controller  : 'controllerLogisticaHistorico',
	}).when('/templateAbicsData', {
		templateUrl : 'assets/pages/templateAbicsData.html',
		controller  : 'controllerTemplateAbicsData',
	}).when('/templateAbicsLog', {
		templateUrl : 'assets/pages/templateAbicsLog.html',
		controller  : 'controllerTemplateAbicsLog',
	}).when('/cadastroDadosAbicsData', {
	    templateUrl : 'assets/pages/cadastroDadosAbicsData.html',
	    controller  : 'controllerCadastroDadosAbicsData',
	}).when('/home', {
		templateUrl : 'assets/pages/home.html',
		controller  : 'controllerHome',
	}).otherwise({
        redirectTo: "/"
    });

	$locationProvider.html5Mode({
	  enabled: true,
	  requireBase: false
	});

});

moduleTemplate.factory('userService', function() {

	var usuarioService = {};
	var _id = 0;
	var _nome = '';
	var _email = '';
	var _login = '';
	var _primeiroLogin = '';
	var _tipoRelatorio = 0;
	var _permissao = 0;

	usuarioService.setId = function(id) {
		_id = id;
	}

	usuarioService.getId = function() {
		return _id;
	}

	usuarioService.setNome = function(nome) {
		_nome = nome;
	}

	usuarioService.getNome = function() {
		return _nome;
	}

	usuarioService.setEmail = function(email) {
		_email = email;
	}

	usuarioService.getEmail = function() {
		return _email;
	}
	
	usuarioService.setLogin = function(login) {
		_login = login;
	}

	usuarioService.getLogin = function() {
		return _login;
	}

	usuarioService.setPrimeiroLogin = function(primeiroLogin) {
		_primeiroLogin = primeiroLogin;
	}
	usuarioService.getPrimeiroLogin = function() {
		return _primeiroLogin;
	}

	usuarioService.setTipoRelatorio = function(tipoRelatorio) {
		_tipoRelatorio = tipoRelatorio;
	}
	usuarioService.getTipoRelatorio = function() {
		return _tipoRelatorio;
	}

	usuarioService.setPermissao = function(permissao) {
		_permissao = permissao;
	}
	usuarioService.getPermissao = function() {
		return _permissao;
	}

	usuarioService.getUsuario = function() {

		var usuario = {};
		usuario.id = _id;
		usuario.nome = _nome;
		usuario.email = _email;
		usuario.login = _login;
		usuario.primeiroLogin = _primeiroLogin;
		usuario.tipoRelatorio = _tipoRelatorio;
		usuario.permissao = _permissao;

		return usuario;
	}

	usuarioService.clearUsuario = function () {

		usuarioService = {};
		_id = 0;
		_nome = '';
		_login = '';
		_email = '';
		_primeiroLogin = '';
		_tipoRelatorio = 0;
		_tipoPermissao = 0;
	}

	return usuarioService;

});

moduleTemplate.factory('dashboardService', function() {

	var dashboardService = {};
	var _cotacao = {};
	var _dataSelecionada = {};
	var _tipoMoeda = '';
	var _isVoltar = false;
	var _isAplicarLogistica = false;

	dashboardService.setCotacao = function(cotacao) {
		_cotacao = cotacao;
	}

	dashboardService.getCotacao = function() {
		return _cotacao;
	}

	dashboardService.setDataSelecionada = function(dataSelecionada) {
		_dataSelecionada = dataSelecionada;
	}

	dashboardService.getDataSelecionada = function() {
		return _dataSelecionada;
	}

	dashboardService.setTipoMoeda = function(tipoMoeda) {
		_tipoMoeda = tipoMoeda;
	}

	dashboardService.getTipoMoeda = function() {
		return _tipoMoeda;
	}

	dashboardService.setIsVoltar = function(isVoltar) {
		_isVoltar = isVoltar;
	}

	dashboardService.getIsVoltar = function() {
		return _isVoltar;
	}

	dashboardService.setIsAplicarLogistica = function(isAplicarLogistica) {
		_isAplicarLogistica = isAplicarLogistica;
	}

	dashboardService.getIsAplicarLogistica = function() {
		return _isAplicarLogistica;
	}

	return dashboardService;
});

moduleTemplate.controller('controllerTemplate', ['$scope', '$http', '$modal', '$timeout', '$window', '$location', '$q', '$cookies', 'userService',
						function($scope, $http, $modal, $timeout, $window, $location, $q, $cookies, userService) {

	$scope.alertaSistemaTemplate = {};
	$scope.primeiroLogin = 0;
	$scope.nomeUsuarioLogado = '';
	$scope.permissaoUsuarioLogado = 0;
////	$scope.dataUltimoLogin = '';
//
	$http.get('/templateController/usuarioLogado').success(function(usuarioResponse) {

		userService.setNome(usuarioResponse.nome);
		userService.setLogin(usuarioResponse.login);
		userService.setEmail(usuarioResponse.email);
		userService.setPrimeiroLogin(usuarioResponse.primeiroLogin);
		userService.setTipoRelatorio(usuarioResponse.tipoRelatorio);
		userService.setPermissao(usuarioResponse.permissao);

		$scope.nomeUsuarioLogado = usuarioResponse.nome;
		$scope.primeiroLogin = usuarioResponse.primeiroLogin;
		$scope.permissaoUsuarioLogado = usuarioResponse.permissao;
		
		if($scope.primeiroLogin == 0) {
			$location.path('/configuracaoUsuario');
		} else {
			$location.path('/home');
		}
	});

	$scope.logoutSistema = function (userChanged) {

		$http.get('/templateController/logoutSistema').success(function(data) {

			if(userChanged) {
				$scope.alertaSistemaTemplate.message = 'Dados atualizados. Você será redirecionado...'
		        	$scope.alertaSistemaTemplate.type = 'success';
		        	$scope.alertaSistemaTemplate.show = true;
		        	$scope.alertaSistemaTemplate.icon = 'thumbs-up';
					$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
					
				setTimeout(function(){ 
					$window.location.href = '/';
				}, 5000);
			} else {
				$window.location.href = '/';
			}
			
//			$scope.usuarioLogado = {};
//			userService.clearUsuario();
		}).error(function(data) {
//			$scope.alert = {type: 'alert-danger', message: 'Erro ao fazer logout!', show: true};
		});
	};
	
	$scope.abrirModalCadastrarUsuario = function() {

		var modalInstance = $modal.open({
            templateUrl: 'idModalCadastrarUsuario',
            controller: modalControlerCadastrarUsuario,
            size: 'md'
        });

		modalInstance.result.then(function (abstractResponse) {

        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);

		}, function (parametro) {
	    });
	};

	$scope.abrirModalInformacoes = function() {

		var modalInstance = $modal.open({
            templateUrl: 'idModalInformacoes',
            controller: modalControlerInformacoes,
            size: 'lg',
            resolve: {
            	informacoesParametro: function () {
                    return $scope.$$childTail.informacoes;
                },
                caminhoVideoParametro: function () {
                    return $scope.$$childTail.caminhoVideo;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
		}, function (parametro) {
	    });
	};

}]);

var modalControlerCadastrarUsuario = function ($scope, $modalInstance, $http, $timeout, $sce) {

	$scope.objetoUsuario = {};

	$scope.cadastrarUsuario = function() {

        $http.post('/usuarioController/cadastrarUsuario', $scope.objetoUsuario).success(function(abstractResponse) {

        	$scope.objetoUsuario = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerInformacoes = function ($scope, $modalInstance, $http, $timeout, $sce, informacoesParametro, caminhoVideoParametro) {

	$scope.informacoesModal = informacoesParametro;
	$scope.caminhoVideoModal = caminhoVideoParametro;

	$scope.fechar = function() {

    	$modalInstance.close();
	}
};
