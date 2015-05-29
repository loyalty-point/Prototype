<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'");

$token = $_POST['token'];   
$card_id = $_POST['card_id'];
$shop_id = $_POST['shop_id'];

if(strlen($token)!=64){
    echo '{"error":"token not found","user_list":[],"award_list":[],"event_list":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token","user_list":[],"award_list":[],"event_list":[]}';
    die();
}
$query = "select * from customer_card where card_id='".$card_id."' and isAccepted != '0' limit 3";
$query_exec = mysqli_query($localhost, $query);
$user_result = "";
while($row = mysqli_fetch_array($query_exec)){
    $query_search = "select * from customer_users where username = '".$row['username']."'";

    $query_exec1 = mysqli_query($localhost,$query_search);

    while($row1 = mysqli_fetch_array($query_exec1)){
        $user_result = $user_result . '{"username":"'.$row1['username'].
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
//get 3 newest event
$query = "select * from event where id in (select event_id from event_card_shop where card_id='".$card_id."' and shop_id='".$shop_id."') ORDER BY id DESC limit 3";
$query_exec = mysqli_query($localhost, $query);
$event_result = "";
while($row = mysqli_fetch_array($query_exec)){
    $event_result = $event_result . '{' 
            . '"id":"' . $row['id'] . '",'
            . '"type":"' . $row['type'] . '",'
            . '"name":"' . $row['name'] . '",'
            . '"time_start":"' . $row['time_start'] . '",'
            . '"time_end":"' . $row['time_end'] . '",'
            . '"description":"' . $row['description'] . '",'
            . '"barcode":"' . $row['barcode'] . '",'
            . '"goods_name":"' . $row['goods_name'] . '",'
            . '"ratio":"' . $row['ratio'] . '",'
            . '"number":"' . $row['number'] . '",'
            . '"point":"' . $row['point'] . '",'
            . '"image":"' . $row['image'] . '"},';
}

$query = "select * from award where id in (select award_id from award_card_shop where card_id='".$card_id."' and shop_id='".$shop_id."') ORDER BY id DESC limit 3";
$query_exec = mysqli_query($localhost, $query);
$award_result = "";
while($row = mysqli_fetch_array($query_exec)){
    $award_result = $award_result . '{' 
            . '"id":"' . $row['id'] . '",'
            . '"name":"' . $row['name'] . '",'
            . '"point":"' . $row['point'] . '",'
            . '"quantity":"' . $row['quantity'] . '",'
            . '"description":"' . $row['description'] . '",'
            . '"image":"' . $row['image'] . '",'
            . '"shopID":"' . $row['shopID'] . '"},';
}
echo '{"error":"","user_list":['.$user_result.'],"award_list":['.$award_result.'],"event_list":['.$event_result.']}';
mysqli_close($localhost);
?>