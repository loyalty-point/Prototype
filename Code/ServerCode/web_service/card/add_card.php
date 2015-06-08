<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$card = $_POST['card'];
$token = $_POST['token'];
$required_info = $_POST['requiredInfo'];
$required_info = json_decode($required_info);
$card = json_decode($card); //chuyển từ string sang json.

/* check token and return username */
if(strlen($token)!=64){
	/*echo "token not found";*/
	echo '{"error":"token not found", "cardId":"", "bucketName":"", "fileName":""}';
	die();
}
$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	/*echo "wrong token";*/
	echo '{"error":"wrong token", "cardId":"", "bucketName":"", "fileName":""}';
	die();
}
/**/
$id = uniqid();
$bucketName = "loyalty-point-photos";
$path = "cards/" . $id;
$fileName = $path . "/cardBackground";
$backgroundLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

$query = "insert into card values ('"
							.$id."','"
							.$card->name."','"
							.$backgroundLink."','"						
							.$card->cardnameX."','"
							.$card->cardnameY."','"
							.$card->usernameX."','"
							.$card->usernameY."','"
							.$card->qrcodeX."','"
							.$card->qrcodeY."','"
							.$card->textColor."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	
	$query = "insert into card_required_customer_info values ('"
								.$id."','"
								.$required_info->customerPhone."','"						
								.$required_info->customerEmail."','"
								.$required_info->customerFullname."','"
								.$required_info->customerAddress."','"
								.$required_info->customerIdentityNumber."')";  //insert vào database

	$query_exec = mysqli_query($localhost, $query);
	if($query_exec){

		$query = "insert into admin_card values ('"
									.$username."','"
									.$id."')";  //insert vào database
		$query_exec = mysqli_query($localhost, $query);
		if($query_exec)
			echo '{"error":"","cardId":"' . $id . '","bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
		else
			echo '{"error":"create shop unsuccessfully", "cardId":"", "bucketName":"", "fileName":""}';
	}else{
		echo '{"error":"create shop unsuccessfully", "cardId":"", "bucketName":"", "fileName":""}';
	}
}
else {
	echo '{"error":"create shop unsuccessfully", "cardId":"", "bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>