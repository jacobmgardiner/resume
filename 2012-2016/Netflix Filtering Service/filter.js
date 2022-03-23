var OPTIONS_AUTO_LANG=0;
var OPTIONS_CUSTOM=1;
var OPTIONS_OFF=2;

var profanity = [];
var censors = ["", "*", "**", "***", "****", "*****", //0-5
                 "******", "*******", "********", "*********", "**********" //6-10
                 ];

var captionsURL = ".nflxvideo.net/?o=";

var videoID;
window.video;

var filters;

var updateInterval=100;
var waitInterval=100;

var lastVolume=1;

var muted=false;

var pauseOnPlay=true;
var playAttempt=0;

var captionWaitNumber=0;
var videoWaitNumber=0;

window.playbackRate = 1;

var overlay;
var showOverlay=false;

var COLOR_BLUE_MAIN = "#70a6ff";

var MAX_CAP_DOWNLOAD_ATTEMPTS = 45;
var MAX_VIDEO_FIND_ATTEMPTS = 45;

var filterURL;

var API_KEY = "AIzaSyCecmmfNpUP2LzpTcEeCaPhqo1ACWhXuXI";

var opt = 0;

var filterListURL = "https://raw.githubusercontent.com/yolodevelopers/clean-stream-filters/master/filter-list.json";

// category:"h***"
// clip_length:1
// clips:[25]
// description:"h*ll"
// fc_voice:false
// id:"910476"
// plot:false
// spoiler:false
// type:"audio"

//var video=document.evaluate("//*[@id="+document.getElementsByClassName("player-video-wrapper")[0].children[0].id+"]/video",document).iterateNext();
// video.playbackRate=1;

// chrome.tabs.onUpdated.addListener(function () {
	// console.log(chrome.tabs.url);
// });

onStart = function () {
	console.log("loaded video player");
	getOption(function (items) {
		opt=items["option"];
		if (opt==OPTIONS_AUTO_LANG) {
			console.log("Automatic Language Filter");
			autoLanguageFilter();
		}
		else if (opt==OPTIONS_CUSTOM) {
			//gapi.client.setApiKey(API_KEY);
			//gapi.client.load('drive', 'v3').then(function () {
					console.log("Custom Filter");
					customFilter();
			//});
		}
		else if (opt==OPTIONS_OFF) {
			console.log("Filter Off");
		}
		else {
			console.log("Filter mode not set");
			console.log("Assuming filter mode");
			autoLanguageFilter();
		}
	});
}

customFilter = function () {
	getFilterURL(function(items) {
		filterURL = items["filepath"];
		createFiltersFromFile(filterURL, function (fs) {
			filters = fs;
			console.log("created filters");
			
			showFilterOptions(function () {
						setupUI();
						setLastVolume(video.volume);
						video.playbackRate=playbackRate;
						video.play();
						console.log("starting filter check...");
						setInterval(updateFilters, updateInterval);
						//video.addEventListener("timeupdate", updateFilters);
						//setCaptionEventListener();
					});
		});
	});
}

getFilterURL = function (callback) {
	return chrome.storage.sync.get("filepath", callback);
}

showFilterOptions = function (callback) {
	console.log("showing filter options");
	
	var videoCover = document.createElement("DIV");
	videoCover.id="video-cover";
	//var aspect=video.getBoundingClientRect().height/video.getBoundingClientRect().width;
	//height-(w/aspect)
	//console.log(video.getBoundingClientRect().width/video.getBoundingClientRect().height);
	var aspect=1.78;
	if(video.getBoundingClientRect().width/video.getBoundingClientRect().height<aspect) {
		var nh=video.getBoundingClientRect().width/aspect;
	}
	else {
		var nh=video.getBoundingClientRect().height;
	}
	videoCover.style.backgroundColor="white";
	videoCover.style.position="absolute";
	videoCover.style.left="0px";
	videoCover.style.right="0px";
	videoCover.style.top=Math.round((video.getBoundingClientRect().height-nh)/2)+"px";
	videoCover.style.bottom=Math.round((video.getBoundingClientRect().height-nh)/2)+"px";
	videoCover.style.display = "block";
	
	var vccontent = document.createElement("DIV");
	vccontent.id="video-cover-content";
	vccontent.style.marginTop = video.getBoundingClientRect().height/10+"px";
	// vccontent.style.position="fixed";
	// vccontent.style.top= "50%";
	// vccontent.style.left="50%";
	// vccontent.style.transform="translate(-50%, -50%)";
	//vccontent.style.background=COLOR_BLUE_MAIN;
	vccontent.style.width="50%";
	vccontent.style.margin="30px auto";
	
	var txt = document.createElement("H1");
	var vctn = document.createTextNode("Use the filters below to edit this video.");
	txt.appendChild(vctn);
	//txt.style.width="50%";
	txt.style.margin="0 auto";
	txt.style.fontSize = "25px";
	txt.style.color=COLOR_BLUE_MAIN;
	vccontent.appendChild(txt);
	
	vccontent.appendChild(document.createElement('BR'));
	
	var cb = document.createElement("BUTTON");
	var cbtn = document.createTextNode("Ready");
	//cbtn.style.color="black";
	cb.appendChild(cbtn);
	cb.style.background=COLOR_BLUE_MAIN;
	cb.style.height="50px";
	cb.style.width="100px";
	cb.style.fontSize = "20px";
	//cb.style.width="50%";
	cb.style.margin="0 auto";
	vccontent.appendChild(cb);
	
	videoCover.appendChild(vccontent);
	
	cb.onclick = function () {
		pauseOnPlay=false;
		videoCover.parentNode.removeChild(videoCover);
		video.play();
		callback();
	};
	
	video.parentNode.appendChild(videoCover);
	//video.appendChild(videoCover);
	
	
	var filterList = document.createElement("DIV");
	//filterList.style.marginTop = video.height+"px";
	filterList.style.marginTop = innerHeight+"px";
	//filterList.style.alignContent="center";
	//filterList.style.alignItems="center";
	//filterList.style.columns.columnCount = "3";
	//filterList.style.columns.columnWidth = "100px";
	filterList.id="filter-list";
	document.body.style.backgroundColor="white";
	filterList.style.color=COLOR_BLUE_MAIN;
	
	var title = document.createElement("H1");
	var ttn = document.createTextNode("Available Filters ("+filters.length+")");
	title.style.alignContent="center";
	title.style.alignItems="center";
	title.appendChild(ttn);
	filterList.appendChild(title);

	for (i=0; i<filters.length; i++) {
		filterList.appendChild(getElementFromFilter(filters[i]));
	}
	
	// var cb = document.createElement("BUTTON");
	// var cbtn = document.createTextNode("Continue");
	// cb.appendChild(cbtn);
	// filterList.appendChild(cb);
	
	//cb.onclick = callback;
	
	//video.parentNode.appendChild(filterList);
	document.body.appendChild(filterList);
	
	//callback();
}

getElementFromFilter = function (filter) {
	//var br = document.createElement("br");
	var fe = document.createElement("DIV");
	fe.style.color = COLOR_BLUE_MAIN;

	// if(opt === OPTIONS_AUTO_LANG) {
		// var title = editMiddle(filter.title);
	// }
	// else {
		// var title = filter.title;
	// }
	
	var title = editMiddle(filter.title);
	var fetn = document.createTextNode(title);
	var span = document.createElement('span');
	span.style.fontSize = "medium";
	span.style.fontWeight = 'bold';
	span.appendChild(fetn);
	//fetn.appendChild(span);
	
	var d = document.createElement("DIV");
	d.style.fontSize = "small";
	d.style.marginLeft = "15px";
	
	var fetn1 = document.createTextNode(filter.description);
	var fetn2 = document.createTextNode("	start time: "+Math.trunc(filter.startTime/60)+":"+Math.floor(filter.startTime%60)+":"+Math.round(filter.startTime%1*100));
	var fetn3 = document.createTextNode("	end time: "+Math.trunc(filter.endTime/60)+":"+Math.floor(filter.endTime%60)+":"+Math.round(filter.endTime%1*100));
	
	var onChange = function (cb) {
		if (cb.checked==true) {
			filter.enabled=true;
			fe.style.color = COLOR_BLUE_MAIN;
		}
		else {
			filter.enabled=false;
			fe.style.color = "gray";
		}
	}
	
	var cb = document.createElement('input');
	cb.type = "checkbox";
	cb.name = "cb";
	cb.checked = "true";
	cb.onchange = function () {onChange(cb)};
	
	d.appendChild(fetn1);
	d.appendChild(document.createElement('BR'));
	d.appendChild(fetn2);
	d.appendChild(document.createElement('BR'));
	d.appendChild(fetn3);
	d.appendChild(document.createElement('BR'));
	
	fe.appendChild(document.createElement('BR'));
	fe.appendChild(span);
	fe.appendChild(d);
	fe.appendChild(cb);
	
	return fe;
}

turnOnCaptions = function () {
	
}

loadVideoPlayer = function (callback) {
	videoID = getVideoId();
	if(videoID) {
		video = getVideo(videoID);
	
		if(video) {
			// setTimeout(function () {
				// console.log("pausing video");
				// video.pause();
				// console.log(video.paused);
				// callback();
			// }, 1000);
			
			// console.log("pausing video");
			// video.pause();
			// callback();
			
			video.addEventListener("play", function () {
				if (pauseOnPlay) {
					video.pause();
					console.log("stopped automatic play attempt "+(++playAttempt));
					if(playAttempt>1)pauseOnPlay=false;
				}
			});
			callback();
		}
		else {
			videoWaitNumber++;
			if(videoWaitNumber>MAX_VIDEO_FIND_ATTEMPTS) {
				console.log("Took too long waiting for video. You may need to refresh the page.");
				alert("Clean Stream encountered an error. Please refresh the page.");
			}
			else {
				console.log("waiting for video...");
			setTimeout(function () {loadVideoPlayer(callback);},waitInterval);
			}
			// console.log("waiting for video...");
			// setTimeout(function () {loadVideoPlayer(callback);},waitInterval);
		}
	}
	else {
		console.log("waiting for video...");
		setTimeout(function () {loadVideoPlayer(callback);},waitInterval);
	}
}

autoLanguageFilter = function () {
	getFilterWords(function (items) {
		if(items["filterwords"]) {
			profanity=items["filterwords"];
			//console.log("using user filter list: "+profanity);
		}
		else {
			console.log("using default filter list: "+profanity);
			setFilterWords(profanity);
		}
		turnOnCaptions();
		getCaptions(function (caps) {
			console.log("loaded captions");
			filters = createFiltersFromCaptions(caps);
			console.log("created filters");
			
			showFilterOptions(function () {
				setupUI();
				setLastVolume(video.volume);
				video.playbackRate=playbackRate;
				video.play();
				console.log("starting filter check...");
				setInterval(updateFilters, updateInterval);
				//video.addEventListener("timeupdate", updateFilters);
				//setCaptionEventListener();
			});
		});
	});
}

setupUI = function () {
	overlay = document.createElement("DIV");
	overlay.id="overlay";
	overlay.style.position="absolute";
	overlay.style.left="5em";
	//overlay.style.right="0px";
	overlay.style.top="2em";
	//overlay.style.bottom="10px";
	//overlay.style.margin="100px 100px";
	overlay.style.display = "block";
	overlay.style.backgroundColor="white";
	overlay.style.fontSize = "25px";
	overlay.style.color = COLOR_BLUE_MAIN;
	
	txt1 = document.createElement("DIV");
	txt1.id="txt1";
	txt1.style.margin="10px 10px";
	txt1.style.fontSize = "25px";
	txt1.style.color = COLOR_BLUE_MAIN;
	
	var tn = document.createTextNode("Clean Stream is ON");
	txt1.appendChild(tn);
	
	overlay.appendChild(txt1);
	
	video.parentNode.appendChild(overlay);
	
	window.MutationObserver = window.MutationObserver
		|| window.WebKitMutationObserver
		|| window.MozMutationObserver;
	// Find the element that you want to "watch"
	var target = document.querySelector("[class~=player-back-to-browsing]"),//document.querySelector('.player-back-to-browsing no-select container-icon-player-back-to-browse')/*document.getElementsByClassName("player-back-to-browsing no-select container-icon-player-back-to-browse opacity-transparent")[0]*/,
	// create an observer instance
	observer = new MutationObserver(function(mutation) {
		//console.log("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		//console.log(mutation[0].target.className);
		// mutations.forEach(function(mutation) {
			// console.log(mutation.type);
		// });
		
		if(mutation[0].target.className!=="player-back-to-browsing no-select container-icon-player-back-to-browse opacity-transparent display-none") {
			//console.log("ui shown");
			if(!showOverlay) {
				overlay.style.display = 'block';
				showOverlay=true;
			}
		}
		else {
			//console.log("ui hidden");
			if(showOverlay) {
				overlay.style.display = 'none';
				showOverlay=false;
			}
		}
	}),
	// configuration of the observer:
	config = {
		attributes: true, // this is to watch for attribute changes.
	};
	// pass in the element you wanna watch as well as the options
	observer.observe(target, config);
	console.log("observing: "+target);
	// later, you can stop observing
	// observer.disconnect();
}

setCaptionEventListener = function() {
    var c=document.getElementsByClassName("player-timedtext")[0];
    //console.log(c);
    if(c) {
        console.log("set listener on captions");
        c.addEventListener("DOMSubtreeModified", function () {
			//console.log("DOMSubtreeModified");
			//if(muted) {
				editCaptions();
			//}
		});
    }
}

setFilterWords = function (fw) {
	chrome.storage.sync.set({"filterwords": fw}, function() {
		//console.log("words: "+fw);
	});
}

getFilterWords = function (callback) {
	return chrome.storage.sync.get("filterwords", callback);
}

updateFilters = function () {
	for (n=0; n<filters.length; n++) {
		if (filters[n].isWithinTime(video.currentTime) && filters[n].enabled) {
			filters[n].apply();
		}
	}
	
	if (muted) {
		//console.log("muted");
		if (video.volume!==0)setLastVolume(video.volume);
		video.volume=0;
		editCaptions();
		muted=false;
	}
	else {
		video.volume=getLastVolume();
	}
}

editCaptions = function () {
	console.log("editing captions");
	var cs=document.getElementsByClassName("player-timedtext-text-container");
    if(!cs)return;
    for (n=0; n<cs.length; n++) {
		// for (i=0; i<profanity.length; i++) {
			// cs[n].innerHTML=cs[n].innerHTML.replace(cs[n].textContent,cs[n].textContent.replace(profanity[i], censors[Math.min(profanity[i].length, censors.length-1)]));
			// cs[n].innerHTML=cs[n].innerHTML.replace(cs[n].textContent,cs[n].textContent.replace(profanity[i].charAt(0).toUpperCase() + profanity[n].slice(1), censors[Math.min(profanity[i].length, censors.length-1)]));
			// cs[n].innerHTML=cs[n].innerHTML.replace(cs[n].textContent,cs[n].textContent.replace(profanity[i].toUpperCase(), censors[Math.min(profanity[i].length, censors.length-1)]));
			
			// cs[n].innerHTML = edit(cs[n].innerHTML);
		// }
		
		var end = (cs[n].innerHTML.split(">")[1]);
		var txt = end.substring(0 ,end.indexOf("<"));
		cs[n].innerHTML = cs[n].innerHTML.split(">")[0]+">"+edit(txt)+"</span>";
    }
}

getVideoId = function() {
    var e=document.getElementsByClassName("player-video-wrapper")[0];
	if (e) {
		var c=e.children[0];
		if (c) {
			var id=c.id;
			if (id) {
				return id;
			}
		}
	}
}

getVideo = function (vid) {
	return document.evaluate("//*[@id="+vid+"]/video",document).iterateNext();
}

createFiltersFromCaptions = function (caps) {
	var fs=[];
	for (n=0; n<caps.length/3; n++) {
		// if (isprofanityed(caps[n*3+2])) {
			// fs.push(new Filter(caps[n*3], caps[n*3+1], 0, 0, "", mute,
				// function () {
					// skip(endTime);
				// },
				// video));
		// }
		// else {
			
		// }
		var b = false;
		var p = [];
		var c = [];
		for (i = 0; i<profanity.length/*&&!b*/; i++) {
			////contains lowercase profanity
			// if((index=caps[n*3+2].indexOf(profanity[i]))!==-1) {
				// b=checkForVariants(caps[n*3+2], index, profanity[i]);
			// }
			////contains all caps profanity
			// else if((index=caps[n*3+2].indexOf(profanity[i].toUpperCase()))!==-1) {
				// b=checkForVariants(caps[n*3+2], index, profanity[i]);
			// }
			////contains capped profanity
			// else if((index=caps[n*3+2].indexOf(profanity[i].charAt(0).toUpperCase() + profanity[i].slice(1)))!==-1) {
				// b=checkForVariants(caps[n*3+2], index, profanity[i]);
			// }

			if((containsProfanity(caps[n*3+2], profanity[i])).length>1) {
				p.push(i);
				c.push(n);
				//--i;
			}
		}
		if (p.length>0) {
			// fs.push(new Filter(caps[n*3], caps[n*3+1], 0, 0, profanity[i-1], "Automatically generated filter. \""+caps[n*3+2].replace(profanity[i-1], censors[Math.min(profanity[i-1].length, censors.length-1)]).replace(profanity[i-1].charAt(0).toUpperCase() + profanity[i-1].slice(1), censors[Math.min(profanity[i-1].length, censors.length-1)]).replace(profanity[i-1].toUpperCase(), censors[Math.min(profanity[i-1].length, censors.length-1)])+"\"", mute,
				// function () {
					// skip(this.endTime);
				// },
				// video));
			//console.log(p);
			for (ii = 0; ii<p.length; ii++) {
				
				fs.push(new Filter(caps[c[ii]*3], caps[c[ii]*3+1], "audio", 0, profanity[p[ii]], "Automatically generated filter. \""+edit(caps[c[ii]*3+2])+/*caps[c[ii]*3+2].replace(profanity[p[ii]], censors[Math.min(profanity[p[ii]].length, censors.length-1)]).replace(profanity[p[ii]].charAt(0).toUpperCase() + profanity[p[ii]].slice(1), censors[Math.min(profanity[p[ii]].length, censors.length-1)]).replace(profanity[p[ii]].toUpperCase(), censors[Math.min(profanity[p[ii]].length, censors.length-1)])+*/"\"", mute,
					function () {
						skip(this.endTime);
					},
					video));
			}
		}
	}
	return fs;
}

createFiltersFromFile = function (url, callback) {
	getURLText(url, function (txt) {
		console.log(txt);
		var fs=[];
		var lines = txt.split("\n");
		for (n=0; n<lines.length/13; n++) {
			fs.push(new Filter(parseInt(lines[n*12+9+n]), parseInt(lines[n*12+11+n]), lines[n*12+3+n], lines[n*12+5+n], lines[n*12+1+n], lines[n*12+7+n], mute,
					function () {
						skip(this.endTime);
					},
					video));
		}
		console.log(fs);
		callback(fs);
	});
}

function getURLText(url, callback){
	getFilterList(function (fs) {
		
		
		var request = new XMLHttpRequest();
		
		// console.log(url);
		// url=url.replace("www.dropbox.com", "dl.dropboxusercontent.com");
		// url=url.replace("drive.google.com/file/d/", "drive.google.com/uc?export=download&id=");
		// url=url.replace("/view?usp=sharing", "");
		// console.log(url);
		
		var exists = false;
		fs = fs.filters;
		
		if (!url||url==="") {
			url = videoID;
			for (var i=0; i<fs.length && !exists; i++) {
				if (fs[i].netflix_id===url) {
					exists = true;
					console.log("found url: "+url);
					url = fs[i].url;
				}
			}
		}
		else {
			if (url.indexOf("http")===-1) {
				console.log("finding url from given input: "+url);
				if (url.indexOf(".filter")!==-1) { //test.filter
					for (var i=0; i<fs.length && !exists; i++) {
						for (var v=0; v<fs[i].title_variants.length && !exists; v++) {
							if (fs[i].filename[v]===url) {
								exists = true;
								console.log("found url: "+url);
								url = fs[i].url;
							}
						}
					}
				}
				else { //Test
					console.log("finding url from title matching: "+url);
					for (var i=0; i<fs.length && !exists; i++) {
						console.log(fs[i].title_variants);
						for (var v=0; v<fs[i].title_variants.length && !exists; v++) {
							console.log(fs[i].title_variants[v]);
							if (fs[i].title_variants[v]===url) {
								exists = true;
								url = fs[i].url;
								console.log("found url: "+url);
							}
						}
					}
				}
			}
			else { //https://raw.githubusercontent.com/yolodevelopers/clean-stream-filters/master/test-2.filter
				console.log("using given url: "+url);
			}
		}
		
		if (!exists) {
			console.log("couldn't find filter for: "+url);
			alert("Couldn't find a filter for \""+url+"\"");
		}
		else {
			request.open('GET', url, true);
			request.send(null);
			request.onreadystatechange = function () {
				if (request.readyState === 4 && request.status === 200) {
					var type = request.getResponseHeader('Content-Type');
					if (type.indexOf("text") !== 1) {
						callback(request.responseText);
					}
				}
			}
		}
	});
}

// isFilter = function (url) {
	// for (var i=0; i<fs.length && !exists; i++) {
		// console.log(fs[i].title_variants);
		// for (var v=0; v<fs[i].title_variants.length && !exists; v++) {
			// console.log(fs[i].title_variants[v]);
			// if (fs[i].title_variants[v]===url) {
				// exists = true;
				// url = fs[i].url;
			// }
		// }
	// }
// }

getFilterList = function (callback) {
	var request = new XMLHttpRequest();
	request.open('GET', filterListURL, true);
	request.send(null);
	request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            var type = request.getResponseHeader('Content-Type');
            if (type.indexOf("text") !== 1) {
				console.log("Available Filters: ");
				console.log(JSON.parse(request.responseText));
                callback(JSON.parse(request.responseText));
            }
        }
    }
}

edit = function (string) {
	// var censor;
	// var cstring=string;
	// for (i=0; i<profanity.length; i++) {
		// censor = censors[Math.min(profanity[i].length, censors.length-1)];
		// cstring = cstring.replace(profanity[i], censor).replace(profanity[i].charAt(0).toUpperCase() + profanity.slice(1), censor).replace(profanity[i].toUpperCase(), censor);
	// }
	// return cstring;
	
	var censor;
	var cstring=string;
	var i=0;
	for (i=0; i<profanity.length; i++) {
		var p;
		if ((p = containsProfanity(cstring, profanity[i])).length>0) {
			censor = censors[Math.min(profanity[i].length, censors.length-1)];
			//cstring = cstring.replace(profanity[i], censor).replace(profanity[i].charAt(0).toUpperCase() + profanity.slice(1), censor).replace(profanity[i].toUpperCase(), censor);
			if(p[0]<cstring.length) {
				cstring = cstring.substring(0, p[0])+censor+cstring.substring(p[1], cstring.length)
				//console.log(p+", "+cstring);
			}
			
			--i;
		}
	}
	return cstring;
}

editMiddle = function (string) {
	var censor;
	var cstring=string;
	var i=0;
	for (i=0; i<profanity.length; i++) {
		var p;
		if ((p = containsProfanity(cstring, profanity[i])).length>0) {
			censor = censors[Math.min(profanity[i].length-2, censors.length-1)];
			//console.log(cstring+", "+p[0]+", "+p[1]);
			if(p[0]+1<cstring.length) {
				cstring = cstring.substring(0, p[0]+1)+censor+cstring.substring(p[1]-1, cstring.length)
			}
			
			--i;
		}
	}
	return cstring;
}

containsProfanity = function (captions, profanity) {
	var index = -1;
	var p=new Array();
	//contains lowercase profanity
	if((index=captions.indexOf(profanity))!==-1) {
		p=checkForVariants(captions, index, profanity);
	}
	//contains all caps profanity
	else if((index=captions.indexOf(profanity.toUpperCase()))!==-1) {
		p=checkForVariants(captions, index, profanity.toUpperCase());
	}
	//contains capped profanity
	else if((index=captions.indexOf(profanity.charAt(0).toUpperCase() + profanity.slice(1)))!==-1) {
		p=checkForVariants(captions, index, profanity.charAt(0).toUpperCase() + profanity.slice(1));
	}
	return p;
}

checkForVariants = function (captions, index, profanity) {
	var p=new Array();
	//conatins word at beginning
	if(index===0) {
		p=checkForEndVariants(captions, index, profanity);
	}
	//conatins word beginning with space
	else if(captions.charAt(index-1)===(" ")) {
		p=checkForEndVariants(captions, index, profanity);
	}
	
	return p;
}

checkForEndVariants = function (captions, index, profanity) {
	var p=new Array();
	
	//conatins word surrounded by spaces
	if(captions.charAt(index+profanity.length)===" ") {
		p.push(index);
		p.push(index+profanity.length);
	}
	//contains word at end of line
	else if(captions.length <= index+profanity.length) {
		p.push(index);
		p.push(index+profanity.length);
	}
	//conatins word ended by punctuation
	else if(captions.charAt(index+profanity.length)===".") {
		p.push(index);
		p.push(index+profanity.length);
	}
	else if(captions.charAt(index+profanity.length)==="!") {
		p.push(index);
		p.push(index+profanity.length);
	}
	else if(captions.charAt(index+profanity.length)===",") {
		p.push(index);
		p.push(index+profanity.length);
	}
	else if(captions.charAt(index+profanity.length)==="?") {
		p.push(index);
		p.push(index+profanity.length);
	}
	else {
		//console.log(captions.substring(index, index+profanity.length));
	}
	
	return p;
}

mute = function () {
	muted=true;
	//editCaptions(this.bw);
}

skip = function (endTime) {
	var scr = document.createElement("script");
	scr.type="text/javascript";
	scr.innerHTML = "netflix.cadmium.UiEvents.events.resize[1].scope.events.dragend[1].handler(null, {value: "+endTime+", pointerEventData: {playing: false}});";
	document.body.appendChild(scr);
}

isProfanity = function (w) {
	var b = false;
	for (n = 0; n<profanity.length&&!b; n++) {
        if(~w.indexOf(profanity[n])) {
            b=true;
        }
        else if(~w.indexOf(profanity[n].charAt(0).toUpperCase() + profanity[n].slice(1))) {
            b=true;
        }
    }
	return b;
}

getCaptions = function (callback) {
	getCaptionsXML(function (xml) {
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(xml, "text/xml");
		
		var caps=[];
		var ps=xmlDoc.getElementsByTagName("p");
		for (n=0; n<ps.length; n++) {
			caps.push(parseFloat(ps[n].attributes.begin.nodeValue)/10000000);
			caps.push(parseFloat(ps[n].attributes.end.nodeValue)/10000000);
			caps.push(ps[n].textContent);
		}
		
		callback(caps);
	});
}

getCaptionsXML = function(callback) {
	var url;
	
	if (document.getElementById("iscript")) {
		//console.log("using existing script");
		var src = document.getElementById("iscript");
		src.parentNode.removeChild(src);
		
		src = document.createElement("script");
		src.id="iscript";
		src.type="text/javascript";
	}
	else {
		//console.log("using new script");
		var src = document.createElement("script");
		src.id="iscript";
		src.type="text/javascript";
	}
	src.innerHTML = "var urls = [];"+
		"var e=window.performance.getEntries();"+
		"for (n=0; n<e.length; n++) {"+
		"	if(~e[n].name.indexOf(\""+captionsURL+"\")){"+
		"		urls.push(e[n].name);"+
		"	}"+
		"} "+
		//"console.log(\"retrieving captions from \"+urls);"+

		"var url;"+
		"if(document.getElementById(\"captionsURL\")) {"+
		"	url=document.getElementById(\"captionsURL\");"+
		"	url.textContent=urls[0];"+
		"}"+
		"else {"+
		"	url=document.createElement(\'div\');"+
		"	url.textContent=urls[0];"+
		"	url.id=\"captionsURL\";"+
		"	document.body.appendChild(url);"+
		"}";
		
	document.body.appendChild(src);

	url = document.getElementById("captionsURL").textContent;

	if(url!=="") {
		console.log("retrieving captions from "+url);
		var xmlHttp = new XMLHttpRequest();
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.onreadystatechange = function() { 
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
				callback(xmlHttp.response);
		}
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}
	else {
		
		captionWaitNumber++;
		if(captionWaitNumber>MAX_CAP_DOWNLOAD_ATTEMPTS) {
			console.log("Took too long waiting for captions. You may need to refresh the page.");
			alert("Clean Stream encountered an error. Please refresh the page.");
		}
		else {
			console.log("waiting for captions to load...");
			setTimeout(function () {getCaptionsXML(callback);},waitInterval);
		}
		// setTimeout(function () {getCaptionsXML(callback);},waitInterval);
	}
}

getOption = function (callback) {
	return chrome.storage.sync.get("option", callback);
}

setLastVolume = function (lv) {
	lastVolume=lv;
}

getLastVolume = function () {
	return lastVolume;
}

function Filter(startTime, endTime, type, category, title, description, onMute, onSkip, video) {
	this.startTime = startTime;
	this.endTime = endTime;
	this.type = type;
	this.category = category;
	this.title = title;
	this.description = description;
	
	this.enabled=true;
	
	this.TYPE_AUDIO="audio";
	this.TYPE_VIDEO="audiovisual";
	
	this.getStartTime = function () {
		return this.startTime;
	}
	
	this.setStartTime = function (startTime) {
		this.startTime = startTime;
	}
	
	this.getFilterAsString = function () {
		return null;
	}
	
	this.isWithinTime = function (time) {
		if(time>this.startTime&&time<this.endTime) {
			return true;
		}
		else {
			return false;
		}
	}
	
	this.apply = function () {
		this.onApply();
	}
	
	this.onApply = function () { //can be defined
		if (this.type===this.TYPE_AUDIO) {
			//console.log("muting");
			this.onMute();
		}
		else if (this.type===this.TYPE_VIDEO) {
			//console.log("skipping");
			this.onSkip();
		}
	}
	
	//must be defined!!! filter.prototype.onMute = function () {video.volume=0;};
	//must be defined!!! filter.prototype.onSkip = function () {video.time=endTime;};
	this.onMute = onMute;
	this.onSkip = onSkip;
}

loadVideoPlayer(onStart);
