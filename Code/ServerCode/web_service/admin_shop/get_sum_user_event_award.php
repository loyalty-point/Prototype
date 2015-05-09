<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'");

$token = $_POST['token'];   
$shop_id = $_POST['shop_id'];

if(strlen($token)!=64){
    echo '{"error":"token not found","user":"","award":"","event":""}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token","user":"","award":"","event":""}';
    die();
}

$query = "select count(username) as numofuser from customer_shop where shop_id='".$shop_id."' and isAccepted != '0'";

$query_exec = mysqli_query($localhost, $query);
$num_of_user = mysqli_fetch_array($query_exec);

$query = "select count(id) as numofevent from event where shop_id='".$shop_id."'";

$query_exec = mysqli_query($localhost, $query);
$num_of_event = mysqli_fetch_array($query_exec);

$query = "select count(id) as numofaward from award where shopID='".$shop_id."'";

$query_exec = mysqli_query($localhost, $query);
$num_of_award = mysqli_fetch_array($query_exec);

echo '{"error":"","user":"'.$num_of_user['numofuser'].'","award":"'.$num_of_award['numofaward'].'","event":"'.$num_of_event['numofevent'].'"}';

mysqli_close($localhost);
?>