<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$ticketId = $_POST['ticketId'];
$shopId = $_POST['shopId'];
$userId = $_POST['userId'];
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

$query = "delete from buy_award_history where id='".$ticketId."'";
$query_exec = mysqli_query($localhost, $query);
if($query_exec){
    echo '{"error":""}';
    // Gửi notification cho user
    // Từ $customerName -> regID (bảng customer_registration)
    $query = "select * from customer_registration where username='".$userId."'";
    $query_exec = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($query_exec);
    $regID = $row['regID'];

        // Gửi thông báo đến regID
    if($regID != "") {

        $regID = array($regID);
        $message = "trade successfully";
        $message = array("message" => $message, "shopID" => $shopId);

        $gcm = new GCM();

        $result = $gcm->send_notification($regID, $message);

        }
}else{
    echo '{"error":"cannot delete this ticket"}';
}

mysqli_close($localhost);
?>