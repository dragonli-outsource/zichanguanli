 

function sysMonMelodyCtl($scope,$rootScope) {
	$scope.url=$rootScope.project+"monitoring"
	console.log($scope.url);
};

app.register.controller('sysMonMelodyCtl', sysMonMelodyCtl);