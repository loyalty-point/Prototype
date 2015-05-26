<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$card_id = $_POST['card_id'];
$token = $_POST['token'];
$point = $_POST['point'];

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

$isAccepted = 0;

$query = "insert into customer_card values ('"
							.$username."','"
							.$card_id ."','"
							.$point ."','"
							.$isAccepted ."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec) {
	echo '{"error":"", "data":""}'; //insert thành công.

	// Gửi notification cho admin shop
	// Từ cardID -> $admin_username (bảng admin_shop)
	$query = "select * from admin_card where card_id='".$card_id."'";
	$query_exec = mysqli_query($localhost, $query);
	$row = mysqli_fetch_array($query_exec);
	$admin_username = $row['admin_username'];

	// Từ $admin_username -> regID (bảng admin_registration)
	$query = "select * from admin_registration where username='".$admin_username."'";
	$query_exec = mysqli_query($localhost, $query);
	$row = mysqli_fetch_array($query_exec);
	$regID = $row['regID'];	
	// Gửi thông báo đến regID
	if($regID != "") {

		$regID = array($regID);
		$message = "You've received a register request";
		$message = array("type" => 'card', "message" => $message, "cardID" => $card_id);

		// put cardID vao $message


		$gcm = new GCM();

		$result = $gcm->send_notification($regID, $message);
	}
}
else 
	echo '{"error":"you are following this shop", "data":""}';; //insert không thành công vì đã có username

mysqli_close($localhost);
?>