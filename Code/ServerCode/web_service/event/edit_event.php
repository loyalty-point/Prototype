<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$event = $_POST['event'];
$token = $_POST['token'];
$shopid = $POST['shop_id'];
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

$bucketName = "loyalty-point-photos";
$fileName = "shops/" . $shopid . "/events/" . $event->id;
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
              . "' where id = '" . $event->id 
              . "' and shop_id = '".$shopid."'";
 
$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	echo '{"error":"", "bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
}
else {
	echo '{"error":"edit event unsuccessfully", bucketName":"", "fileName":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>