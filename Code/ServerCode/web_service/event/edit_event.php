<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$event = $_POST['event'];
$token = $_POST['token'];
$shopID = $_POST['shop_id'];
$cardID = $_POST['card_id'];
$event = json_decode($event); //chuyển từ string sang json.
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

$query = "select count(shop_id) as numofappliedshop from event_card_shop where card_id='".$cardID."' and event_id = '".$event->id."'";

$query_exec = mysqli_query($localhost, $query);
$shop_count = mysqli_fetch_array($query_exec);
$num_of_shop = $shop_count['numofappliedshop'];

if($num_of_shop == 1){
    $bucketName = "loyalty-point-photos";
    $fileName = "shops/" . $shopID . "/events/" . $event->id;
    $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

    $query = "update event " 
        		  . "set type = '" . $event->type
        		  . "', name = '" . $event->name
        		  . "', time_start = '" . $event->time_start 
        		  . "', time_end = '" . $event->time_end
        		  . "', description = '" . $event->description
        		  . "', barcode = '" . $event->barcode
        		  . "', goods_name = '" . $event->goods_name
        		  . "', ratio = '" . $event->ratio
        		  . "', number = '" . $event->number
        		  . "', point = '" . $event->point
        		  . "', image = '" . $imageLink
                  . "' where id = '" . $event->id."'";
}else if($num_of_shop > 1){
    $id = uniqid();
    $bucketName = "loyalty-point-photos";
    $fileName = "shops/" . $shopID . "/events/" . $id;
    $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

    $query = "insert into event values ('"
                                .$id."','"
                                .$event->type."','"
                                .$event->name."','"
                                .$event->time_start."','"
                                .$event->time_end."','"
                                .$event->description."','"
                                .$event->barcode."','"
                                .$event->goods_name."','"
                                .$event->ratio."','"
                                .$event->number."','"
                                .$event->point."','"
                                .$imageLink."')";  //insert vào database
}
$query_exec = mysqli_query($localhost, $query);

if($query_exec){
    if($num_of_shop > 1){
        $query = "update event_card_shop set event_id = '".$id."' where card_id = '".$cardID."' and shop_id = '".$shopID."' and event_id ='".$event->id."'";
        $query_exec = mysqli_query($localhost, $query);
    }
	echo '{"error":"", "bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
}
else {
	echo '{"error":"edit event unsuccessfully", bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>