<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'");

$token = $_POST['token'];
$card_id = $_POST['card_id'];
$customerName = $_POST['username'];

if(strlen($token)!=64){
    echo '{"error":"token not found","user":{}}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
        echo '{"error":"wrong token","user":{}}';
        die();
}

// Lấy tất cả các user thuộc shop có user.username == $customerName
$query = "select * from customer_card where card_id = '".$card_id."' AND username = '" . $customerName . "'";
$query_exec = mysqli_query($localhost,$query);
$num_row = mysqli_num_rows($query_exec);

if($num_row == 0) {
    echo '{"error":"not your user","user":{}}';
    die();    
}

// Lấy user có user.username == $customerName ra, echo về cho client
$query = "select * from customer_users where username = '" . $customerName . "'";
$query_exec = mysqli_query($localhost,$query);
$query_exec = mysqli_fetch_array($query_exec);

$result = '{"error":"", "user":';
        $result = $result . '{"username":"'.$query_exec['username'].
            '","password":"","fullname":"'.$query_exec['name'].
            '","phone":"'.$query_exec['phone_number'].
            '","email":"'.$query_exec['email'].
            '","address":"'.$query_exec['address'].
            '","identity_number":"'.$query_exec['identity_number'].
            '","barcode":"'.$query_exec['barcode'].
            '","avatar":"'.$query_exec['avatar'].
            '","token":"'.$query_exec['token'].'"}}';

        echo $result;

mysqli_close($localhost);
?>