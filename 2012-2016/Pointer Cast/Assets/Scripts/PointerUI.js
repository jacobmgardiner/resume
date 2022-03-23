#pragma strict

var xcal:float=0;
var ycal:float=0;
var c:RectTransform;

var clampAngle:float=360;

function Start () 
{
	Input.gyro.enabled = true;

	c = transform.parent.gameObject.GetComponent.<RectTransform>();
}

function Update () 
{
	//var rot=Quaternion.Euler(Vector3(Mathf.Clamp(Input.gyro.attitude.eulerAngles.x,-clampAngle,clampAngle),0,Mathf.Clamp(Input.gyro.attitude.eulerAngles.y,-clampAngle,clampAngle))) * Quaternion.Euler(Vector3.up * (/**cam.rotation.y*/270-Input.gyro.attitude.eulerAngles.z));
	//rot.eulerAngles.y=0;

	//var y=rot.y;
	//var x=rot.x;

	//var y=360-Input.gyro.attitude.eulerAngles.y;
	var a=Input.gyro.attitude;
	var y=360-(Mathf.Atan2((2*a.x*a.w + 2*a.y*a.z), (1 - 2*a.x*a.x - 2*a.z*a.z)))*180/Mathf.PI;
	var x=180-Input.gyro.attitude.eulerAngles.z;

	if(Input.GetButtonDown("Fire2"))
	{
		xcal=x;
		ycal=y;
	}

	var ymag:float=c.rect.height*2;
	var xmag:float=c.rect.width*2;

	transform.position.y=Mathf.Clamp(Mathf.Sin((y-ycal)*Mathf.PI/180)*ymag+c.rect.height/2, 0, c.rect.height);
	transform.position.x=Mathf.Clamp(Mathf.Sin((x-xcal)*Mathf.PI/180)*xmag+c.rect.width/2, 0, c.rect.width);
}