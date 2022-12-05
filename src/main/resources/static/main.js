const url = 'http://localhost:8080';//the url of the website!
let chart, networkJson, packet;
let currentRoutingTableDisplayed = "R1";
let routingTables = null;
const chartContainer = document.getElementById('container');
const routingTableDOM = document.getElementById('RoutingTable');
const createDeviceButton = document.getElementById('createDeviceButton');
const deleteDeviceButton = document.getElementById('deleteDeviceButton');
const createEdgeButton = document.getElementById('createEdgeButton');
const deleteEdgeButton = document.getElementById('deleteEdgeButton');
const displayRoutingTableButton = document.getElementById('displayRoutingTableButton');
const routePacketButton = document.getElementById('routePacketButton');
const createDeviceInput = document.getElementById('createDeviceInput');
const deleteDeviceInput = document.getElementById('deleteDeviceInput');
const createEdgeInput = document.getElementById('createEdgeInput');
const deleteEdgeInput = document.getElementById('deleteEdgeInput');
const displayRoutingTableInput = document.getElementById('displayRoutingTableInput');
const routePacketInput = document.getElementById('routePacketInput');


//used to draw network
function draw() {
    const http = new XMLHttpRequest();

    http.onreadystatechange = function(){
        if(http.readyState === 4 && http.status === 200) {
            const JsonResponse = http.responseText;
            networkJson = JSON.parse(JsonResponse);
            // create and draw a chart from the loaded data
            chart = anychart.graph(networkJson);

            //enable labels of nodes
            chart.nodes().labels().enabled(true);
            chart.edges().labels().enabled(true);

            //configure labels of nodes
            chart.nodes().labels().format("{%id}");
            chart.nodes().labels().fontSize(12);
            chart.nodes().labels().fontWeight(600);
            chart.nodes().labels().fontColor("#00bfa5");

            //configures labels of edges
            chart.edges().labels().format("{%weight}");
            chart.edges().labels().fontSize(12);
            chart.edges().labels().fontWeight(600);

            //disables interactivity (graph is static)
            chart.interactivity().enabled(false);

            // draw the chart
            chart.container("container").draw();
        }
    }


    const methodUrl = url + '/getNetworkGraph';
    http.open("GET", methodUrl);
    http.send();
}

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function getRoutingTables(){
    const http = new XMLHttpRequest();

    http.onreadystatechange = function(){
        if(http.readyState === 4 && http.status === 200) {
            const JsonResponse = http.responseText;
            routingTables = JSON.parse(JsonResponse);
            displayRoutingTable(currentRoutingTableDisplayed);
        }
    }


    const methodUrl = url + '/getRoutingTables';
    http.open("GET", methodUrl);
    http.send();
}

function findTable(routerName){
    for(const key in routingTables){
        if (routingTables[key].routerName === routerName){
            return routingTables[key];
        }
    }
    return null;
}

function displayRoutingTable(routerName){
    if (routingTables == null){
        getRoutingTables();
    } else {
        removeAllChildNodes(routingTableDOM);

        //creates the <tr> and <th> elements for the router name
        const tableHeaderRow = document.createElement('tr');
        const tableHeader = document.createElement("th");
        tableHeader.innerText = "Router Name: " + routerName;

        //appends the elements created
        tableHeaderRow.appendChild(tableHeader);
        routingTableDOM.appendChild(tableHeaderRow);

        // creates a 'keyRow' with data which lets the user know
        // what each data field is for and appends it to the table
        const keyRow = document.createElement('tr');
        keyRow.className = "keyRow";

        const destinationKey = document.createElement('td');
        destinationKey.innerHTML = "DESTINATION";
        const lineKey = document.createElement('td');
        lineKey.innerHTML = "LINE";
        const costKey = document.createElement('td');
        costKey.innerHTML = "COST(ms)";

        keyRow.appendChild(destinationKey);
        keyRow.appendChild(lineKey);
        keyRow.appendChild(costKey);


        routingTableDOM.appendChild(keyRow);

        // Dynamically populates the rest of the table with the correct routing table object fields that were
        // passed from backend

        //gets the object that corresponds to a certain routing table
        const rows = findTable(routerName).rows;

        //iterates through routing table object rows and creates corresponding <tr> elements to add to the
        //routing table in the DOM
        for (const key in rows) {
            const currentDataRow = document.createElement('tr');
            currentDataRow.className = "data-row"; //added for css selection just in case

            const destination = document.createElement('td');
            destination.innerHTML = rows[key].destination;
            currentDataRow.appendChild(destination);

            const line = document.createElement('td');
            line.innerHTML = rows[key].line;
            currentDataRow.appendChild(line);

            const cost = document.createElement('td');
            if (rows[key].cost == 1000000){
                cost.innerHTML = "UNREACHABLE";
            } else {
                cost.innerHTML = rows[key].cost;
            }
            currentDataRow.appendChild(cost);



            routingTableDOM.appendChild(currentDataRow);
        }
    }
    currentRoutingTableDisplayed = routerName;
}

function buildPacket(sourceName, destinationName){
    packet = {source : sourceName, destination : destinationName};
}

function refresh(){
    removeAllChildNodes(chartContainer);
    draw();
}

function createDevice(){
        if (createDeviceInput.value.match(/R[0-9]+/)) {
            if (findTable(createDeviceInput.value) == null) {
                let args = createDeviceInput.value;
                if (args === '') {
                    args = 'default';
                }
                let http = new XMLHttpRequest();

                http.onreadystatechange = function () {
                    if (http.readyState === 4 && http.status === 200) {
                        createDeviceInput.value = http.responseText;
                        refresh();
                        routingTables = null;
                        displayRoutingTable(currentRoutingTableDisplayed);
                    }
                }


                let methodUrl = url + '/createDevice';

                methodUrl += "?deviceName=" + args;
                http.open("GET", methodUrl);
                http.send();
            } else {
                createDeviceInput.value = "Device Already Exists";
            }
        } else {
            createDeviceInput.value = "Bad Format";
        }
}

function deleteDevice(){
    if (deleteDeviceInput.value.match(/R[0-9]+/)) {
        let args = deleteDeviceInput.value;
        let http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState === 4 && http.status === 200) {
                deleteDeviceInput.value = http.responseText;
                refresh();
                routingTables = null;
                displayRoutingTable(currentRoutingTableDisplayed);
            }
        }


        let methodUrl = url + '/deleteDevice';

        methodUrl += "?deviceName=" + args;
        http.open("GET", methodUrl);
        http.send();
    } else {
        deleteDeviceInput.value = "Bad Format";
    }
}

function createEdge(){
    if (createEdgeInput.value.match(/R[0-9]+,R[0-9]+,[0-9]+/)) {
        let args = createEdgeInput.value;
        if (args === '') {
            createEdgeInput.value = "Incorrect Format";
            return;
        }
        args = args.split(",");
        args = "source=" + args[0] + "&dest=" + args[1] + "&cost=" + args[2];

        let http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState === 4 && http.status === 200) {
                createEdgeInput.value = http.responseText;
                refresh();
                routingTables = null;
                displayRoutingTable(currentRoutingTableDisplayed);
            }
        }


        let methodUrl = url + '/createEdge';

        methodUrl += "?" + args;
        http.open("GET", methodUrl);
        http.send();
    } else {
        createEdgeInput.value = "Bad Format";
    }
}

function deleteEdge(){
    if(deleteEdgeInput.value.match(/R[0-9]+,R[0-9]+/)) {
        let args = deleteEdgeInput.value;
        if (args === '') {
            deleteEdgeInput.value = "Incorrect Format";
            return;
        }
        args = args.split(",");
        args = "r1=" + args[0] + "&r2=" + args[1];

        let http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState === 4 && http.status === 200) {
                deleteDeviceInput.value = http.responseText;
                refresh();
                routingTables = null;
                displayRoutingTable(currentRoutingTableDisplayed);
            }
        }


        let methodUrl = url + '/deleteEdge';

        methodUrl += "?" + args;
        http.open("GET", methodUrl);
        http.send();
    } else {
        deleteDeviceInput.value = "Bad Format";
    }
}

function displayRoutingTableFromListener(){
    if (displayRoutingTableInput.value.match(/R[0-9]+/)) {
        if (findTable(displayRoutingTableInput.value) == null) {
            displayRoutingTableInput.value = 'Device does not exist'
        }else {
            displayRoutingTable(displayRoutingTableInput.value);
            displayRoutingTableInput.value = "Success!";
        }
    }else displayRoutingTableInput.value = `Bad Format`;
}

//methodStub
function routePacket(){
    if(routePacketInput.value.match(/R[0-9]+,R[0-9]+/)){

    } else {
        routePacketInput.value = "Bad Format";
    }
}


//event listeners for all of our buttons
createDeviceButton.addEventListener('click', createDevice);
deleteDeviceButton.addEventListener('click', deleteDevice);
createEdgeButton.addEventListener('click', createEdge);
deleteEdgeButton.addEventListener('click', deleteEdge);
displayRoutingTableButton.addEventListener('click', displayRoutingTableFromListener);
routePacketButton.addEventListener('click', routePacket);

anychart.onDocumentReady(draw());
getRoutingTables();


//DO NOT TOUCH ABOVE THIS LINE LOL
