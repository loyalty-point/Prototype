<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$token = $_POST['token'];
$regID = $_POST['regID'];

/* check token and return username */
if(strlen($token)!=64){
	/*echo "token not found";*/
	echo '{"error":"token not found"}';
	die();
}
$query = "select username from customer_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token"}';
	die();
}

// add or update into customer_registration
$query = "select username from customer_registration where username='".$username."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$usernameTest = $row['username'];

if($usernameTest == "") { // Chưa có thông tin đăng kí, insert vào
	$query = "insert into customer_registration values ('"
							.$username."','"
							.$regID."')";  

	$query_exec = mysqli_query($localhost, $query);

	if($query_exec)
		echo '{"error":""}';
	else
		echo '{"error":"insert registration info unsuccessfully"}'; 

	
}else { // Đã có thông tin đăng kí, update lại
	$query = "update customer_registration set regID = '" . $regID . "' where username = '" . $username . "'";

	$query_exec = mysqli_query($localhost, $query);

	if($query_exec)
		echo '{"error":""}';
	else
		echo '{"error":"update registration info unsuccessfully"}'; 
}

mysqli_close($localhost);
?>