<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shop = $_POST['shop'];
$token = $_POST['token'];
$shop = json_decode($shop); //chuyển từ string sang json.

/* check token and return username */
if(strlen($token)!=64){
	echo "token not found";
	die();
}
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo "wrong token";
	die();
}
/**/
$id = uniqid();
$query = "insert into shop values ('"
							.$id."','"
							.$shop->name."','"
							.$shop->address."','"
							.$shop->phone_number."','"
							.$shop->category."','"
							.$shop->exchange_ratio."','"
							.$shop->image."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	
	$query = "insert into admin_shop values ('"
								.$username."','"
								.$id."')";  //insert vào database
	$query_exec = mysqli_query($localhost, $query);
	if($query_exec)
		echo 'true';
	else
		echo 'false';
}
else {
	echo 'false'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>