#pragma strict

var speed:float=0.3;

var bobSpeed:float=.5;
var bobHeight:float=.1;

private var dir:Vector3;
//private var sp:Vector3;
private var colliding:boolean=false;
private var y:float;

function Start () 
{
	//sp=transform.position;
	dir=-(transform.forward*speed*Time.deltaTime);
	y=transform.position.y;
}

function Update () 
{
	transform.Translate(dir);
	transform.position.y=y+bobHeight*Mathf.Sin(Time.time);
}