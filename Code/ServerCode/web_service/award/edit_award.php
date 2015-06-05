<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$award = $_POST['award'];
$token = $_POST['token'];
$shopID = $_POST['shop_id'];
$cardID = $_POST['card_id'];
$award = json_decode($award); //chuyển từ string sang json.

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
$query = "select count(shop_id) as numofappliedshop from award_card_shop where card_id='".$cardID."' and award_id = '".$award->id."'";

$query_exec = mysqli_query($localhost, $query);
$shop_count = mysqli_fetch_array($query_exec);
$num_of_shop = $shop_count['numofappliedshop'];

if($num_of_shop == 1){
    $bucketName = "loyalty-point-photos";
    $fileName = "shops/" . $shopID . "/awards/" . $award->id;
    $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

    $query = "update award " 
    		  . "set name = '" . $award->name
    		  . "', point = '" . $award->point
    		  . "', quantity = '" . $award->quantity 
    		  . "', description = '" . $award->description
    		  . "', image = '" . $imageLink 
              . "' where id = '" . $award->id . "'";
}else if($num_of_shop > 1){
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
							.$imageLink."')";  //insert vào database
}
$query_exec = mysqli_query($localhost, $query);

if($query_exec){
    if($num_of_shop > 1){
        $query = "update award_card_shop set award_id = '".$id."' where card_id = '".$cardID."' and shop_id = '".$shopID."' and award_id ='".$award->id."'";
        $query_exec = mysqli_query($localhost, $query);
    }
	echo '{"error":"", "bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
}
else {
	echo '{"error":"edit award unsuccessfully", bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>