#pragma strict

var projectile:Rigidbody;
var launchForce:float=1000;
var launchToTarget:boolean=false;
var pointer:Transform;
var yoffset:float=-.25;

function Start () 
{
	
}

function Update () 
{
	if(Input.GetButtonDown("Fire1"))
	{
		var p:Rigidbody=Instantiate(projectile, Vector3(transform.position.x, transform.position.y+yoffset, transform.position.z), Quaternion.identity);
		var d=(pointer.position-transform.position).normalized;
		p.AddForce(d*launchForce);
	}
}