<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$post = $_POST['user'];
$user = json_decode($post); //chuyển từ string sang json.

$query = "insert into customer_users values ('"
							.$user->username."','"
							.$user->password."','"
							.$user->phone."','"
							.$user->email."','"
							.$user->fullname."','"
							.$user->address."','"
							.$user->identity_number."','"
							.$user->barcode."','"
							.$user->avatar."','"
							.$user->token."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec)
	echo 'true';
else
	echo 'false'; //insert không thành công vì đã có username

mysqli_close($localhost);
?>