<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$shop_id = $_POST['shop_id'];
$token = $_POST['token'];

/* check token and return username */
$query = "select username from customer_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);

$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token", "data":""}';
	die();
}
/**/

$query = "insert into customer_shop values ('"
							.$username."','"
							.$shop_id."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec)
	echo '{"error":"", "data":""}'; //insert thành công.
else 
	echo '{"error":"you are following this shop", "data":""}';; //insert không thành công vì đã có username

mysqli_close($localhost);
?>