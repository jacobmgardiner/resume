#pragma strict

var speed:float=1;
var rb:Rigidbody;

private var dir:Vector3;
private var colliding:boolean=false;

function Start () 
{
	dir=transform.forward*speed;
}

function Update () 
{
	if(!colliding)
	{
		//rb.velocity=dir;
		transform.Translate(dir*Time.deltaTime);
	}
}

function OnCollisionEnter (collision:Collision)
 {
     if (collision.collider.tag == "Projectile")
     {
         colliding=true;
     }
 }