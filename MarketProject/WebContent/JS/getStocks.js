
        var request = myCreateXMLHttpRequest();
        
        


        function myCreateXMLHttpRequest() {
            try { return new ActiveXObject("Msxml2.XMLHTTP"); } catch (e) { }
            try { return new ActiveXObject("Microsoft.XMLHTTP"); } catch (e) { }
            try { return new XMLHttpRequest(); } catch (e) { }
            return null;
        }

        function myOnKeyUp() {
        	
        	if (request != null) {
        		var textField = document.getElementById("myInputField");
                var url = "rest/RestFeed?str=" + textField.value;

                request.open("GET", url, true);
                request.onreadystatechange = myHandleCallback;
                request.send(null); 
            }
        }
        
        
        function performStrategies() {
        	
        	if (document.getElementById("TMAradio").checked == true) {
        		chAlertTMA();
        		myOnKeyUp1();
        		console.log("TMA: "+ document.getElementById("TMAradio").checked);
        	}
        	if (document.getElementById("EMAradio").checked == true) {
        		chAlertGeneral();
        		chAlertEMA();
        		Strat2();
        		console.log("EMA: "+ document.getElementById("EMAradio").checked);
        	}
        	if (document.getElementById("PBradio").checked == true) {
        		chAlertGeneral();
        		chAlertPB();
        		Strat3();
        		console.log("PB: "+ document.getElementById("PBradio").checked);
        	}
        	if (document.getElementById("BBradio").checked == true) {
        		chAlertGeneral();
        		chAlertBB();
        		Strat4();
        		console.log("BB: "+ document.getElementById("BBradio").checked);
        	}
        }
        
        function chAlertGeneral(){
        	document.getElementById("tMAAlert").innerHTML=
        		"<div class='alert alert-info'><strong>Currently Running!</strong> There are now Strategies running!</div>";
        }

        
        function chAlertTMA(){
        	document.getElementById("tMAAlert").innerHTML=
        		"<div class='alert alert-success'><strong>Currently Running!</strong> The Two Moving Average is running</div>";
        }
        
        function chAlertEMA(){
        	document.getElementById("eMAAlert").innerHTML=
        		"<div class='alert alert-success'><strong>Currently Running!</strong> The Exponential Moving Average is running</div>";
        }
        
        function chAlertPB(){
        	document.getElementById("pBAlert").innerHTML=
        		"<div class='alert alert-success'><strong>Currently Running!</strong> The Price Breakout Strategy is running</div>";
        }
        
        function chAlertBB(){
        	document.getElementById("bBAlert").innerHTML=
        		"<div class='alert alert-success'><strong>Currently Running!</strong> The Bollinger Bands Strategy is running</div>";
        }
        
        function examinePortfolio() {
        	
        	if (document.getElementById("allPortRadio").checked == true) {
        		clearInterval('checkTMATrades()');
        		clearInterval('checkEMATrades()');
        		clearInterval('checkPBTrades()');
        		clearInterval('checkBBTrades()');
        		clearInterval('checkManualTrades()');
        		checkAllTrades();
        		setInterval('checkAllTrades()', 30000);
        		console.log("All: "+ document.getElementById("allPortRadio").checked);
        	} else if (document.getElementById("TMAPortRadio").checked == true) {
        		clearInterval('checkAllTrades()');
        		clearInterval('checkEMATrades()');
        		clearInterval('checkPBTrades()');
        		clearInterval('checkBBTrades()');
        		clearInterval('checkManualTrades()');
        		checkTMATrades();        		
        		setInterval('checkTMATrades()', 30000);
        	} else if (document.getElementById("EMAPortRadio").checked == true) {
        		clearInterval('checkAllTrades()');
        		clearInterval('checkTMATrades()');
        		clearInterval('checkPBTrades()');
        		clearInterval('checkBBTrades()');
        		clearInterval('checkManualTrades()');
        		checkEMATrades();
        		setInterval('checkEMATrades()', 30000);
        		console.log("EMA: "+ document.getElementById("EMAPortRadio").checked);
        	} else if (document.getElementById("PBPortRadio").checked == true) {
        		clearInterval('checkAllTrades()');
        		clearInterval('checkTMATrades()');
        		clearInterval('checkEMATrades()');
        		clearInterval('checkBBTrades()');
        		clearInterval('checkManualTrades()');
        		checkPBTrades();
        		setInterval('checkPBTrades()', 30000);
        		console.log("PB: "+ document.getElementById("PBPortRadio").checked);
        	} else if (document.getElementById("BBPortRadio").checked == true) {
        		clearInterval('checkAllTrades()');
        		clearInterval('checkTMATrades()');
        		clearInterval('checkEMATrades()');
        		clearInterval('checkPBTrades()');
        		clearInterval('checkManualTrades()');
        		checkBBTrades();
        		setInterval('checkBBTrades()', 30000);
        		console.log("BB: "+ document.getElementById("BBPortRadio").checked);
        	} else if (document.getElementById("MANPortRadio").checked == true) {
        		clearInterval('checkAllTrades()');
        		clearInterval('checkTMATrades()');
        		clearInterval('checkEMATrades()');
        		clearInterval('checkPBTrades()');
        		clearInterval('checkBBTrades()');
        		checkManualTrades();
        		setInterval('checkManualTrades()', 30000);
        		console.log("MAN: "+ document.getElementById("MANPortRadio").checked);
        	}
        }
        
        function checkManualTrades() {
        	var url = "rest/MANPortfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function checkAllTrades() {
        	var url = "rest/Portfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function checkTMATrades() {
        	var url = "rest/TMAPortfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function checkPBTrades() {
        	var url = "rest/PBPortfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function checkBBTrades() {
        	var url = "rest/BBPortfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function checkEMATrades() {
        	var url = "rest/EMAPortfolio"
                request.open("GET", url, true);
                request.onreadystatechange = portfolioCallback;
                request.send(null);
        }
        
        function manualTrade(tradePosition) {

            if (request != null) {
                var textField = document.getElementById("manualSymbol");
                var textField1 = document.getElementById("manualQuantity");
                var url = "rest/manualTrade?str=" + textField.value + "&quantity=" + textField1.value + "&tradePosition=" + tradePosition;
                request.open("GET", url, true);
                request.onreadystatechange = myHandleCallback;
                request.send(null); 
            }
        }

        function myOnKeyUp1() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat1?str=" + textField.value;

                request.open("GET", url, true);
                request.onreadystatechange = tMACallback;
                request.send(null);
            }
        }
        
        function tMACallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("tMAAlert");
                outputField.innerHTML = request.responseText;
            }
        }
        
        
        function Strat2() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat2?str=" + textField.value;

                request.open("GET", url, true);
                request.onreadystatechange = eMACallback;
                request.send(null);
            }
        }
        
        function eMACallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("eMAAlert");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function Strat3() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat3?str=" + textField.value;

                request.open("GET", url, true);
                request.onreadystatechange = pBCallback;
                request.send(null);
            }
        }
        
        function pBCallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("pBAlert");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function Strat4() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat4?str=" + textField.value;

                request.open("GET", url, true);
                request.onreadystatechange = bBCallback;
                request.send(null);
            }
        }
        
        function bBCallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("bBAlert");
                outputField.innerHTML = request.responseText;
            }
        }


        function Profit4() {
            if (request != null) {
                var url = "rest/GetProfit?str=Bollinger B";

                request.open("GET", url, true);
                request.onreadystatechange = ProfitCallback4;
                request.send(null);
            }
        }
  
        function ProfitCallback4() {
            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("Strat4EchoField");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function myOnKeyUp2() {

            if (request != null) {
                //var textField = document.getElementById("myInputField");
                var url = "rest/RestFeed2";

                request.open("GET", url, true);
                request.onreadystatechange = myHandleCallback2;
                request.send(null);
            }
        }
        
        setInterval('myOnKeyUp2()', 1000);
        
        

        function myHandleCallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("myEchoField");
                outputField.innerHTML = request.responseText;
                document.getElementById("goto").scrollIntoView();
            }
        }
        
        function myHandleCallback1() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("myEchoField1");
                outputField.innerHTML = "Strategy 1 activated"
            }
        }
        
        function portfolioCallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("portfolioOutput");
                outputField.innerHTML = request.responseText;
            }
        }

        function myHandleCallback2() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("myEchoField2");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function Profit1() {
            if (request != null) {
                var url = "rest/GetProfit?str=S MovingA";

                request.open("GET", url, true);
                request.onreadystatechange = ProfitCallback1;
                request.send(null);
            }
        }
        function ProfitCallback1() {
            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("myEchoField1");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function Profit2() {
            if (request != null) {
                var url = "rest/GetProfit?str=E MovingA";

                request.open("GET", url, true);
                request.onreadystatechange = ProfitCallback1;
                request.send(null);
            }
        }
        function ProfitCallback2() {
            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("Strat2EchoField");
                outputField.innerHTML = request.responseText;
            }
        }
        
        function Profit3() {
            if (request != null) {
                var url = "rest/GetProfit?str=Price Break";

                request.open("GET", url, true);
                request.onreadystatechange = ProfitCallback3;
                request.send(null);
            }
        }
        function ProfitCallback3() {
            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("Strat3EchoField");
                outputField.innerHTML = request.responseText;
            }
        }