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
$query = "select * from event_card_shop where card_id='".$card_id."' and shop_id='".$shop_id."'";
$query_exec = mysqli_query($localhost, $query);
while($row = mysqli_fetch_array($query_exec)){
    $query = "select * from event where id = '" . $row['event_id'] . "' ORDER BY id DESC limit 3";
    $query_exec = mysqli_query($localhost, $query);
    $event_result = "";
    while($row1 = mysqli_fetch_array($query_exec)){
        $event_result = $event_result . '{' 
                . '"id":"' . $row1['id'] . '",'
                . '"type":"' . $row1['type'] . '",'
                . '"name":"' . $row1['name'] . '",'
                . '"time_start":"' . $row1['time_start'] . '",'
                . '"time_end":"' . $row1['time_end'] . '",'
                . '"description":"' . $row1['description'] . '",'
                . '"barcode":"' . $row1['barcode'] . '",'
                . '"goods_name":"' . $row1['goods_name'] . '",'
                . '"ratio":"' . $row1['ratio'] . '",'
                . '"number":"' . $row1['number'] . '",'
                . '"point":"' . $row1['point'] . '",'
                . '"image":"' . $row1['image'] . '"},';
    }
}
$query = "select * from award_card_shop where card_id='".$card_id."' and shop_id='".$shop_id."'";
$query_exec = mysqli_query($localhost, $query);
while($row = mysqli_fetch_array($query_exec)){
    $query = "select * from award where id = '" . $row['award_id'] . "' ORDER BY id DESC limit 3";
    $query_exec = mysqli_query($localhost, $query);
    $award_result = "";
    while($row1 = mysqli_fetch_array($query_exec)){
        $award_result = $award_result . '{' 
                . '"id":"' . $row1['id'] . '",'
                . '"name":"' . $row1['name'] . '",'
                . '"point":"' . $row1['point'] . '",'
                . '"quantity":"' . $row1['quantity'] . '",'
                . '"description":"' . $row1['description'] . '",'
                . '"image":"' . $row1['image'] . '",'
                . '"shopID":"' . $row1['shopID'] . '"},';
    }
}
echo '{"error":"","user_list":['.$user_result.'],"award_list":['.$award_result.'],"event_list":['.$event_result.']}';
mysqli_close($localhost);
?>