<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'");

$token = $_POST['token'];
$card_id = $_POST['card_id'];

if(strlen($token)!=64){
    echo '{"error":"token not found","listUsers":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
        echo '{"error":"wrong token","listUsers":[]}';
        die();
}
/**/

$query = "select username, point from customer_card where card_id = '".$card_id."' and isAccepted != '0'";
$query_exec = mysqli_query($localhost,$query);
$result = '{"error":"", "listUsers":[';
while($row = mysqli_fetch_array($query_exec)){

    $query_search = "select * from customer_users where username = '".$row['username']."'";

    $query_exec1 = mysqli_query($localhost,$query_search);

    while($row1 = mysqli_fetch_array($query_exec1)){
        $result = $result . '{"username":"'.$row1['username'].
            '","fullname":"'.$row1['name'].
            '","phone":"'.$row1['phone_number'].
            '","email":"'.$row1['email'].
            '","address":"'.$row1['address'].
            '","identity_number":"'.$row1['identity_number'].
            '","barcode":"'.$row1['barcode'].
            '","avatar":"'.$row1['avatar'].
            '","point":"'.$row['point'].'"},';
    }
}
$result = $result . ']}';
echo $result;
mysqli_close($localhost);
?>