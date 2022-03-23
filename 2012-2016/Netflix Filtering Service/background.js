console.log("background:");

// chrome.tabs.onUpdated.addListener(function(details) {
	// console.log(details);
	// chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, function (tabs) {
		// var url = tabs[0].url;
	// });
	// console.log(url);
    // chrome.tabs.executeScript(null,{file:"filter.js"});
// });

var URL = ".netflix.com/watch/";
var watching = false;

chrome.webNavigation.onHistoryStateUpdated.addListener(function(details) {
	// chrome.tabs.query({'active': true, 'lastFocusedWindow': true}, function (tabs) {
		// var url = tabs[0].url;
	// });
	var url = details.url;
	console.log(url);
	if(url.indexOf(URL)!==-1) {
		console.log("refreshing page. . .");
		
		chrome.tabs.query({ currentWindow: true, active: true },function (tabArray) { 
			chrome.tabs.reload(tabArray[0].id); 
		});
		
		watching = true;
	}
	else {
		if(watching) {
			console.log("leaving watch page. . .");
			console.log("refreshing page. . .");
		
			// chrome.tabs.query({ currentWindow: true, active: true },function (tabArray) { 
				// chrome.tabs.reload(tabArray[0].id); 
			// });
			
			watching = false;
		}
		//console.log("Stopping filter script.");
		// stop script
	}
});