#pragma strict

var speed:float=1;
var wave:Transform;

var offset:float=.345;

var length:int=50;

private var waves:Transform[]=new Transform[length];
private var st:float=0;
private var wn:int=0;

function Start () 
{
	var n=0;
	while(n<length)
	{
		waves[n]=Spawn(Vector3(transform.position.x+(n-length/2)*offset,transform.position.y,transform.position.z));
		n++;
	}
}

function Update () 
{
	wn=wn%length;
	if(waves[wn].localPosition.x<-length/2*offset)
	{
		waves[wn].transform.position.x=transform.position.x+(length/2)*offset;
		wn++;
	}
}

function Spawn (pos:Vector3)
{
	var w=Instantiate(wave, pos, transform.rotation);
	var wa=w.transform.gameObject.GetComponent(Wave);
	wa.speed=speed;
	return w;
}