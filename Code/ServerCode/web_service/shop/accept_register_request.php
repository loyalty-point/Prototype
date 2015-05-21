<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shopID'];
$customerName = $_POST['username'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found"}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token"}';
	die();
}

// kiểm tra xem shopID có thuộc user có user.token == token hay không
/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_shop where admin_username='".$username."' and shop_id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$shopID = $row['shop_id'];

if($shopID == ""){
    echo '{"error":"not your shop"}';
	die();
}

$query = "select * from card_shop where shop_id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$card_id = $row['card_id'];

// Vào bảng card_shop, tìm dòng thỏa shop_id == $shopID && username == $customerName
$query = "update customer_card set isAccepted = '1' where card_id='".$card_id."' and username='".$customerName."'";
$query_exec = mysqli_query($localhost, $query);

// Vào bảng customer_shop, tìm dòng thỏa shop_id == $shopID && username == $customerName
$query = "update customer_shop set isAccepted = '1' where shop_id='".$shopID."' and username='".$customerName."'";
$query_exec = mysqli_query($localhost, $query);
if($query_exec) {
    echo '{"error":""}';

    // Gửi notification cho user
    // Từ $customerName -> regID (bảng customer_registration)
    $query = "select * from customer_registration where username='".$customerName."'";
    $query_exec = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($query_exec);
    $regID = $row['regID']; 

    // Gửi thông báo đến regID
    if($regID != "") {

        $regID = array($regID);
        $message = "request accepted";
        $message = array("message" => $message, "shopID" => $shopID);

        $gcm = new GCM();

        $result = $gcm->send_notification($regID, $message);
    }
}
else
    echo '{"error":"accept register request unsuccessfully"}';

mysqli_close($localhost);
?>