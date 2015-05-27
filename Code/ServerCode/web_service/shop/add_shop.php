<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shop = $_POST['shop'];
$token = $_POST['token'];
$card_id = $_POST['card_id'];
$shop = json_decode($shop); //chuyển từ string sang json.

/* check token and return username */
if(strlen($token)!=64){
	/*echo "token not found";*/
	echo '{"error":"token not found", "shopID":"", "bucketName":"", "fileName":""}';
	die();
}
$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	/*echo "wrong token";*/
	echo '{"error":"wrong token", "shopID":"", "bucketName":"", "fileName":""}';
	die();
}
/**/
$id = uniqid();
$bucketName = "loyalty-point-photos";
$path = "shops/" . $id;
$fileName = $path . "/shopLogo";
$imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;
$backgroundLink = "http://storage.googleapis.com/" . $bucketName . "/" . $path . "/shopBackground";

$query = "insert into shop values ('"
							.$id."','"
							.$shop->name."','"
							.$shop->address."','"
							.$shop->phone_number."','"
							.$shop->category."','"
							.$shop->exchange_ratio."','"
							.$imageLink."','"
							.$backgroundLink."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);
echo mysqli_error($localhost);
if($query_exec){
	
	$query = "insert into card_shop values ('"
								.$card_id."','"
								.$id."')";  //insert vào database
	$query_exec = mysqli_query($localhost, $query);
	if($query_exec)
		echo '{"error":"","shopID":"' . $id . '","bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
	else
		echo '{"error":"create shop unsuccessfully", "shopID":"", "bucketName":"", "fileName":""}';
}	
else {
	echo '{"error":"create shop unsuccessfully", "shopID":"", "bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>