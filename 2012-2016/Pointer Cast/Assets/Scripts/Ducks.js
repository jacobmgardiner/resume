#pragma strict

var speed:float=1;
var duck:Rigidbody;

var respawn:boolean=true;
var spawnRate:float=2.5;
var spawnDelay:float=0;

private var st:float;

function Start () 
{
	st=0;
}

function Update () 
{
	if(Time.time-st>spawnRate)
	{
		Spawn();
		st=Time.time;
	}
}

function Spawn ()
{
	var d:Rigidbody=Instantiate(duck, Vector3(transform.position.x, transform.position.y, transform.position.z), transform.rotation);
	var du=d.transform.gameObject.GetComponent(Duck);
	du.speed=speed;
}