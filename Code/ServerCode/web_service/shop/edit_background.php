<?php

$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shop = $_POST['shop_id'];
$token = $_POST['token'];

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token", "shopID":"", "bucketName":"", "fileName":""}';
	die();
}
/**/

/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_shop where admin_username='".$username."' and shop_id='".$shop."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['admin_username'];
$shop_id = $row['shop_id'];

if($shop_id == ""){
	echo '{"error":"not your shop", "shopID":"", "bucketName":"", "fileName":""}';
	die();
}
/**/

$query_search = "select * from shop where id='".$shop_id."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) { //Shop không có
    echo '{"error":"shop does not exist", "shopID":"", "bucketName":"", "fileName":""}';
}
else  {

    $id = $shop_id;
    $bucketName = "loyalty-point-photos";
    $fileName = "shops/" . $id . "/shopBackground";
    $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

    $query = "update shop " 
    		      . "set background = '" . $imageLink
              . "' where id = '" . $id . "'";

              /*echo $query;
              die();*/
              
	
               
    $query_exec = mysqli_query($localhost, $query);

    if($query_exec)
		echo '{"error":"","shopID":"' . $id . '","bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
	else
		echo '{"error":"update shop background unsuccessfully", "shopID":"", "bucketName":"", "fileName":""}';

}
mysqli_close($localhost);
?>