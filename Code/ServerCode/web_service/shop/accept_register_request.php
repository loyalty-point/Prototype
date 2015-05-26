<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$cardID = $_POST['cardID'];
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

/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"not your card"}';
}else{

    // Vào bảng card_shop, tìm dòng thỏa shop_id == $shopID && username == $customerName
    $query = "update customer_card set isAccepted = '1' where card_id='".$cardID."' and username='".$customerName."'";
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
            $message = array("type" => 'card', "message" => $message, "cardID" => $cardID);

            $gcm = new GCM();

            $result = $gcm->send_notification($regID, $message);
        }
    }
    else
        echo '{"error":"accept register request unsuccessfully"}';
}
mysqli_close($localhost);
?>