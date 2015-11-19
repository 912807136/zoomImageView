var objs=document.getElementsByTagName("img");
var urls = new Array();
for(var i=0;i<objs.length;i++){
urls[i] = objs[i].src;
objs[i].onclick=function(){
listener.showImage(urls,this.src);
};
}