<?php

$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$shop_id = $_POST['shop_id'];
$card_id = $_POST['card_id'];
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
/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $card_id . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

$query = "select * from card_shop where card_id='".$card_id."' and shop_id = '" . $shop_id . "'";

$query_exec = mysqli_query($localhost, $query);
$shop_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"not your card", "shopID":"", "bucketName":"", "fileName":""}';
}else if($shop_rows == 0){
    echo '{"error":"not your shop", "shopID":"", "bucketName":"", "fileName":""}';
}else{
  /**/
  $query_search = "select * from shop where id='".$shop_id."'";

  $query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

  $rows = mysqli_num_rows($query_exec);

  if($rows == 0) { //Shop không có
      echo '{"error":"shop does not exist", "shopID":"", "bucketName":"", "fileName":""}';
  }
  else  {
  	// Edit shop
      $shop = $_POST['shop'];
      
      $shop = json_decode($shop); //chuyển từ string sang json.

      $id = $shop_id;
      $bucketName = "loyalty-point-photos";
      $fileName = "shops/" . $id . "/shopLogo";
      $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

      $query = "update shop " 
      		  . "set name = '" . $shop->name
      		  . "', phone_number = '" . $shop->phone_number 
      		  . "', category = '" . $shop->category 
      		  . "', exchange_ratio = '" . $shop->exchange_ratio 
      		  . "', address = '" . $shop->address 
                . "', image = '" . $imageLink 
                . "' where id = '" . $id . "'";

                /*echo $query;
                die();*/
                
  	
                 
      $query_exec = mysqli_query($localhost, $query);

      if($query_exec)
  		echo '{"error":"","shopID":"' . $id . '","bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
  	else
  		echo '{"error":"edit shop unsuccessfully", "shopID":"", "bucketName":"", "fileName":""}';

  }
}
mysqli_close($localhost);
?>