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
$cardId = $_POST['cardId'];
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

$query = "select * from customer_tickets where id='".$ticketId."'";
$query_exec = mysqli_query($localhost, $query);
$ticket_row = mysqli_fetch_array($query_exec);

if($ticket_row['id'] == ""){
    echo '{"error":"ticket is not exist"}';
    die();
}

$query = "select * from award where id='".$ticket_row['awardID']."'";
$query_exec = mysqli_query($localhost, $query);
$award_row = mysqli_fetch_array($query_exec);

if($award_row['id'] == ""){
    echo '{"error":"award is not exist"}';
    die();
}

$query = "select * from customer_card where username='".$ticket_row['username']."' and card_id='".$cardId."'";
$query_exec = mysqli_query($localhost, $query);
$customer_card_row = mysqli_fetch_array($query_exec);

if($customer_card_row['username'] == ""){
    echo '{"error":"user not follow this shop"}';
    die();
}

$query = "delete from customer_tickets where id='".$ticketId."'";
$query_exec = mysqli_query($localhost, $query);
if($query_exec){
    //update award quantity
    $new_quantity = $award_row['quantity'] + $ticket_row['quantity'];
    $query = "update award set quantity = " . $new_quantity . " where id = '".$award_row['id']."'";
    $query_exec = mysqli_query($localhost, $query);
    //update point after cancel.
    $new_point = $customer_card_row['point'] + $ticket_row['total_point'];
    $query = "update customer_card set point = " . $new_point . " where username = '".$ticket_row['username']."' and card_id = '".$cardId."'";
    $query_exec = mysqli_query($localhost, $query);

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
        $message = "cancel successfully";
        $message = array("message" => $message, "shopID" => $shopId);

        $gcm = new GCM();

        $result = $gcm->send_notification($regID, $message);

        }
}else{
    echo '{"error":"cannot delete this ticket"}';
}

mysqli_close($localhost);
?>