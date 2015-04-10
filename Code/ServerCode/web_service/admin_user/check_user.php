<?php
include_once '../GCM/GCM.php';

$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$username = $_POST['username'];
$hashpass = $_POST['hashpass'];

$query_search = "select * from admin_users where username='".$username."'";
$query_exec = mysqli_query($localhost,$query_search);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];


/*$regID = 'APA91bHzuUss50zBC1Q5V2OCaF_5Evfa-IMjHvn3VOVr1cZiGMF5lcBHxM1VW7EPa4ctityDm63uHLW_wofHFHjTVomtXzXXkwa06aThGp8zvD_1u-dCk-31b6ayEtk1lAEg5kTiPKrWzdZiSaINr9-nDgAg5HbL0y24fT8yolYDBG0HqKdRRUw';
$regID = array($regID);
$message = "hello world";
$message = array("message" => $message);

$gcm = new GCM();

$result = $gcm->send_notification($regID, $message);
die();*/

if($username == '') { //Sai tài khoản
    echo '{"error":"Username does not exist","token":""}';
}
else {
    if($hashpass != $row['password']){  //Sai password
        echo '{"error":"Wrong password or username","token":""}';
    } else{ //Đúng password và sinh token (md5 của username nối vối md5 của thời gian hệ thống)
        $time = date("F j, Y, g:i a");
        $token = md5($username).md5($time);
        $query_update = "update admin_users set token='".$token."' where username='".$username."'";
        $query_exec = mysqli_query($localhost, $query_update);
        echo '{"error":"","token":"'.$token.'"}';
    }
}
mysqli_close($localhost);
?>			