
function getCurrentTabUrl(callback) {
  var queryInfo = {
    active: true,
    currentWindow: true
  };

  chrome.tabs.query(queryInfo, function(tabs) {
    var tab = tabs[0];

    var url = tab.url;

    console.assert(typeof url == 'string', 'tab.url should be a string');

    callback(url);
  });
}

function renderStatus(statusText) {
  document.getElementById('status').textContent = statusText;
}

document.addEventListener('DOMContentLoaded', function() {
	getCurrentTabUrl(function(url) {

		rboptions=[document.getElementById("rbalf"), document.getElementById("rbcf"), document.getElementById("rboff")];
		rboptions[0].addEventListener("click", onALFClick);
		rboptions[1].addEventListener("click", onOffClick);
		rboptions[2].addEventListener("click", onCFClick);
		
		getFilterWords(function (items) {
			if (items.filterwords.length>1) {
				console.log(items.filterwords);
			}
			else {
				//console.log(items.filterwords);
				//var fwb = [];
				//setFilterWords(fwb);
				// console.log("creating filter words");
				// getVAFilterWords(function (fw) {
					// for (var i = 0; i<45; i++) {
						////addFilterWord(fw[i].id);
						// fwb.push(fw[i].id);
					// }
					// setFilterWords(fwb);
				// });
			}
		});

		var option=0;

		getOption(function (items) {
			console.log(items["option"]);
			if(items["option"]) {
				option=items["option"];
			}
			rboptions[option].checked=true;
		});
		
		var ab = document.getElementById("ab");
		ab.addEventListener("click", onAddClick);
		
		var rb = document.getElementById("rb");
		rb.addEventListener("click", onRemoveClick);

		var fpt = document.getElementById("filepathtxt");
		getFilePath(function (items) {
			if(items["filepath"]) {
				fpt.value = items["filepath"];
			}
		});
		
		var sb = document.getElementById("sb");
		sb.addEventListener("click", onSubmitFPClick);
		
		document.getElementById("st").style.visibility = "hidden";
	});
});

getVAFilterWords = function (callback) {
	function reqListener () {
		callback(JSON.parse(this.responseText).results);
	}
	
	var param1 = "include_rentable_works=true";
	var oReq = new XMLHttpRequest();
	oReq.addEventListener("load", reqListener);
	oReq.open("GET", "https://api.vidangel.com/api/tag-categories/"+"?"+param1);
	oReq.send();
}

onAddClick = function () {
	var e = document.getElementById("addtxt");
	addFilterWord(e.value);
	//getFilterWords(function (items) {
	//	console.log(items.filterwords);
	//});
}

onRemoveClick = function () {
	var e = document.getElementById("removetxt");
	removeFilterWord(e.value);
}

isValidFile = function () {
	
}

getFilePath = function (callback) {
	return chrome.storage.sync.get("filepath", callback);
}

setFilePath = function (fp) {
	chrome.storage.sync.set({ "filepath": fp }, function(){console.log("set file path to "+fp);});
}

getFilterWords = function (callback) {
	return chrome.storage.sync.get({"filterwords": []}, callback);
}

setFilterWords = function (fw) {
	chrome.storage.sync.set({"filterwords": fw}, function() {
		console.log("words: "+fw);
	});
}

addFilterWord = function (fw) {
	getFilterWords(function (items) {
		items.filterwords.push(fw);
		setFilterWords(items.filterwords);
	});
}

removeFilterWord = function (fw) {
	getFilterWords(function (items) {
		var index=items.filterwords.indexOf(fw);
		if (index!=-1) {
			items.filterwords.splice(index, 1);
		}
		setFilterWords(items.filterwords);
	});
}

getOption = function (callback) {
	return chrome.storage.sync.get("option", callback);
}

setOption = function (opt) {
	chrome.storage.sync.set({ "option": opt }, function(){console.log("set option to "+opt);});
}

onALFClick = function () {
	console.log("onALFClick");
	setOption(0);
}

onCFClick = function () {
	console.log("onCFClick");
	setOption(1);
}

onOffClick = function () {
	console.log("onOffClick");
	setOption(1);
}

onSubmitFPClick = function () {
	console.log("onSubmitFPClick");
	var t = document.getElementById("filepathtxt").value;
	if (isFileExistent(t)) {
		document.getElementById("st").style.visibility = "hidden";
	}
	else {
		document.getElementById("st").style.visibility = "visible";
	}
	if (t) setFilePath(t);
}

isFileExistent = function (fp) {
	return false;
}

getVAProfanity = function (callback) {
	function reqListener () {
		callback(JSON.parse(this.responseText));
	}

	var oReq = new XMLHttpRequest();
	oReq.addEventListener("load", reqListener);
	oReq.open("GET", "https://api.vidangel.com/api/works/"+"?"+param1+"&"+param2);
	oReq.send();
}