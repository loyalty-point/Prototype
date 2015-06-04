<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shopsId = $_POST['shops_id'];
$cardID = $_POST['card_id'];
$token = $_POST['token'];
$award = $_POST['award'];
$award = json_decode($award);
$listShopsId = json_decode($shopsId, true);

/* check token and return username */
if(strlen($token)!=64){
	echo '{"errorr":"token not found", "bucketName":"", "fileName":""}';
	die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token", "bucketName":"", "fileName":""}';
	die();
}
/**/
/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"not your card", "bucketName":"", "fileName":""}';
}else{
	$id = uniqid();
	$bucketName = "loyalty-point-photos";
	$fileName = "shops/" . $cardID . "/awards/" . $id;
	$imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

	$query = "insert into award values ('"
							.$id."','"
							.$award->name."','"
							.$award->point."','"
							.$award->quantity."','"
							.$award->description."','"
							.$imageLink."')";  //insert vào database
	
	$query_exec = mysqli_query($localhost, $query);

	if($query_exec){
		foreach($listShopsId as $shopId) { 
			$query = "insert into award_card_shop values ('"
												.$id."','"
												.$cardID."','"
												.$shopId."')";
			$query_exec = mysqli_query($localhost, $query);
		}
		echo '{"error":"", "bucketName":"'.$bucketName.'","fileName":"'.$fileName.'"}';
	}else{
		echo '{"error":"create award unsuccessfully", bucketName":"", "fileName":""}';
	}
}
mysqli_close($localhost);
?>