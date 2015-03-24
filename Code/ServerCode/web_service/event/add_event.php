<?php

$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shop = $_POST['shop_id'];
$token = $_POST['token'];
$event = $_POST['event'];
$event = json_decode($event);

/* check token and return username */
if(strlen($token)!=64){
	echo "token not found";
	die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo "wrong token";
	die();
}
/**/

/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_shop where admin_username='".$username."' and shop_id='".$shop."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['admin_username'];
$shop_id = $row['shop_id'];

if($shop_id == ""){
	echo "not your shop";
	die();
}
/**/

$id = uniqid();
$query = "insert into event values ('"
							.$shop."','"
							.$id."','"
							.$event->type."','"
							.$event->name."','"
							.$event->time_start."','"
							.$event->time_end."','"
							.$event->description."','"
							.$event->barcode."','"
							.$event->goods_name."','"
							.$event->ratio."','"
							.$event->number."','"
							.$event->point."','"
							.$event->image."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	echo 'true';
}else{
	echo 'false';
}

mysqli_close($localhost);
?>