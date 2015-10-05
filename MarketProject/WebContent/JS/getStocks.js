
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
       

        function myOnKeyUp1() {

            if (request != null) {
                var textField = document.getElementById("myInputField1");
                var url = "rest/Strat1?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
            }
        }
        
        function Strat2() {

            if (request != null) {
                var textField = document.getElementById("Strat2InputField");
                var url = "rest/Strat2?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
            }
        }
        
        function Strat3() {

            if (request != null) {
                var textField = document.getElementById("Strat3InputField");
                var url = "rest/Strat3?str=" + textField.value;

                request.open("GET", url, true);
               // request.onreadystatechange = myHandleCallback1;
                request.send(null);
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
        
        setIncrement('myOnKeyUp2()', 300);

        function myHandleCallback() {

            if (request.readyState == 4 && request.status == 200) {
                var outputField = document.getElementById("myEchoField");
                outputField.innerHTML = request.responseText;
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