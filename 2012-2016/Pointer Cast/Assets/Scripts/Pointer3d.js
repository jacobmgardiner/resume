#pragma strict

var xcal:float=0;
var ycal:float=0;
var c:Camera;
var width:float=1;
var height:float=1;

private var ao=360;

var gyroscope:boolean=false;

var clampAngle:float=360;

function Start () 
{
	Screen.sleepTimeout = SleepTimeout.NeverSleep;

	if (SystemInfo.supportsGyroscope)
	{
		Input.gyro.enabled = true;
		gyroscope=true;
	}
	else
	{
		gyroscope=false;
	}

	//c = transform.parent.gameObject.GetComponent.<RectTransform>();

	width=c.pixelWidth;
	height=c.pixelHeight;

	//height=Screen.height;
	//width=Screen.width;
}

function Update () 
{
	var x:float;
	var y:float;
	if(gyroscope)
	{
		var a=Input.gyro.attitude;

		x=360-a.eulerAngles.z;

		y=360-(Mathf.Atan2((2*a.x*a.w + 2*a.y*a.z), (1 - 2*a.x*a.x - 2*a.z*a.z)))*180/Mathf.PI;

		Debug.Log(x+","+ao);
		//Debug.Log(x+","+y);
		//Debug.Log((x-xcal)+","+(y-ycal));
	}
	else
	{
		//y=360-Input.acceleration.y;
		y=360-(Quaternion.Euler(Input.acceleration)*Quaternion.Euler(Vector3.up*(270-Input.acceleration.z))).eulerAngles.y;
		x=180-Input.acceleration.z;
	}

	if(Input.GetButtonDown("Fire2"))
	{
		xcal=x;
		ycal=y;

		if(x<270&&x>90)
			ao=1;
		else
			ao=-1;
	}

	var ymag:float=height;
	var xmag:float=width;

	var fx=x-xcal;
	if(fx<0)fx=fx+360;
	var fy=y-ycal;
	if(fy<0)fy=fy+360;

	transform.localPosition.y=(c.ScreenToWorldPoint(Vector3(0,Mathf.Clamp(Mathf.Sin((fy)*Mathf.PI/180)*ymag+height/2, 0, height),c.WorldToScreenPoint(Vector3(0,0,c.transform.position.z+.35)).z)).y-c.transform.position.y)*ao;
	transform.localPosition.x=c.ScreenToWorldPoint(Vector3(Mathf.Clamp(Mathf.Sin((fx)*Mathf.PI/180)*xmag+width/2, 0, width),0,c.WorldToScreenPoint(Vector3(0,0,c.transform.position.z+.35)).z)).x-c.transform.position.x;
}