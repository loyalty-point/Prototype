<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$token = $_POST['token'];
$user_id = $_POST['userId'];
$identity_number = $_POST['identityNumber'];

$query_search = "select * from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//Không có token này trong db
    echo '{"error":"token not found"}';
}
else  {
    $query_search = "select * from customer_users where username='".$user_id."' and identity_number='".$identity_number."'";

    $query_exec = mysqli_query($localhost,$query_search);

    $rows = mysqli_num_rows($query_exec);
    if($rows == 0){
        echo '{"error":"wrong identity number"}';
    }else{
        echo '{"error":""}';
    }
}
mysqli_close($localhost);
?>