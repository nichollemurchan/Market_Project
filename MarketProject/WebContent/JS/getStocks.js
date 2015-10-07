
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
        	
        	if (document.getElementById("TMAcheck").checked == true) {
        		myOnKeyUp1();
        	}
        	if (document.getElementById("EMAcheck").checked == true) {
        		Strat2();
        	}
        	if (document.getElementById("PBcheck").checked == true) {
        		Strat3();
        	}
        	if (document.getElementById("BBcheck").checked == true) {
        		Strat4();
        	}
        }
        
        function manualTrade(tradePosition) {

            if (request != null) {
                var textField = document.getElementById("manualSymbol");
                var textField1 = document.getElementById("manualQuantity");
                var url = "rest/manualTrade?str=" + textField.value + "&quantity=" + textField1.value + "&tradePosition=" + tradePosition;
                request.open("GET", url, textField1, true);
                request.onreadystatechange = myHandleCallback;
                request.send(null); 
            }
        }

        function myOnKeyUp1() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat1?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
            }
        }
        
        function Strat2() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat2?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
            }
        }
        
        function Strat3() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat3?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
            }
        }
        
        function Strat4() {

            if (request != null) {
                var textField = document.getElementById("symbol");
                var url = "rest/Strat4?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
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
        
        setInterval('myOnKeyUp2()', 300);

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