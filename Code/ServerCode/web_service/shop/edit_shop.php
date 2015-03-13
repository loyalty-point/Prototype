<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);

$shop = $_POST['shop_id'];
$token = $_POST['token'];

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo "wrong token";
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
	echo "not your shop";
	die();
}
/**/

$query_search = "select * from shop where id='".$shop_id."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) { //Shop không có
    echo "shop does not exist";
}
else  {
	// Edit shop
    $shop = $_POST['shop'];
    
    $shop = json_decode($shop); //chuyển từ string sang json.

    $query = "update shop " 
    		  . "set name = '" . $shop->name 
    		  . "', phone_number = '" . $shop->phone_number 
    		  . "', category = '" . $shop->category 
    		  . "', exchange_ratio = '" . $shop->exchange_ratio 
    		  . "', address = '" . $shop->address 
              . "' where id = '" . $shop_id . "'";

    // echo($shop->name);
    // die();   

    // echo($query);
    // die();

    $query_exec = mysqli_query($localhost, $query);

    if($query_exec)
		echo 'true';
	else
		echo 'false';

}
mysqli_close($localhost);
?>