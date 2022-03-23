#pragma strict

function Start () 
{

}

function Update () 
{

}

function OnTriggerExit (other : Collider) 
{
	if(other.tag=="Projectile"||other.tag=="Target")
    Destroy(other.gameObject);
}