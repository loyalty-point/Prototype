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
$awardId = $_POST['awardId'];
$shopId = $_POST['shopId'];
$cardId = $_POST['cardId'];
$userId = $_POST['userId'];
$time = $_POST['time'];
$number = $_POST['number'];
$point = $_POST['point'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found"}';
    die();
}

$query = "select * from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token"}';
    die();
}

$query = "select * from customer_users where username='".$userId."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"user is not exist"}';
    die();
}

$query = "delete from customer_tickets where id='".$ticketId."'";
$query_exec = mysqli_query($localhost, $query);
if($query_exec){

    $id = uniqid();
    $query = "insert into history values ('"
                            .$id."','0','"
                            .$row['username']."','"
                            .$row['name']."','"
                            .$row['phone_number']."','"
                            .$point."','"
                            .$time."','"
                            .$awardRow['awardImage']."')";
    $query_exec = mysqli_query($localhost, $query);
    $query = "insert into buy_award_history values ('"
                            .$id."','"
                            .$awardId."','"
                            .$number."')";
    $query_exec = mysqli_query($localhost, $query);
    $query = "insert into history_card_shop values ('"
                            .$id."','"
                            .$cardId."','"
                            .$shopId."')";
    $query_exec = mysqli_query($localhost, $query);
  
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
        $message = array("type" => "shop", "message" => $message, "shopID" => $shopId, "historyID" => $id);
        $gcm = new GCM();   

        $result = $gcm->send_notification($regID, $message);

    }
    echo '{"error":""}';
}else{
    echo '{"error":"cannot delete this ticket"}';
}

mysqli_close($localhost);
?>