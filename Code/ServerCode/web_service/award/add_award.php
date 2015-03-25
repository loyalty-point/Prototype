<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$award = $_POST['award'];
$token = $_POST['token'];
$award = json_decode($award); //chuyển từ string sang json.
$shopID = $award->shopID;

/* check token and return username */
if(strlen($token)!=64){
	/*echo "token not found";*/
	echo '{"error":"token not found", "bucketName":"", "fileName":""}';
	die();
}
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	/*echo "wrong token";*/
	echo '{"error":"wrong token", "bucketName":"", "fileName":""}';
	die();
}
/**/
$id = uniqid();
$bucketName = "loyalty-point-photos";
$fileName = "shops/" . $shopID . "/awards/" . $id;
$imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

$query = "insert into award values ('"
							.$id."','"
							.$award->name."','"
							.$award->point."','"
							.$award->quantity."','"
							.$award->description."','"
							.$imageLink."','"
							.$shopID."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	echo '{"error":"", "bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
}
else {
	echo '{"error":"create shop unsuccessfully", bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>