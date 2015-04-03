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
	echo '{"error":"token not found", "quantity":""}';
	die();
}
$query = "select username from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	/*echo "wrong token";*/
	echo '{"error":"wrong token", "quantity":""}';
	die();
}
/**/

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

$query_exec = mysqli_query($localhost, $query);

if($query_exec){
	echo '{"error":"", "quantity":"'.$award->quantity.'"}';
}
else {
	echo '{"error":"edit shop unsuccessfully", "quantity":""}'; //insert không thành công vì đã có username
}

mysqli_close($localhost);
?>