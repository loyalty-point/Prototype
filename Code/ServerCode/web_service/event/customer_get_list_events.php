<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shopID'];
$cardID = $_POST['cardID'];

if(strlen($token)!=64){
    echo '{"error":"token not found", "listEvents":[]}';
    die();
}

/* check token and return username */
$query = "select username from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listEvents":[]}';
	die();
}
/**/

$query = "select * from event where id in (select distinct event_id from event_card_shop where card_id = '".$cardID."')";
$query_exec2 = mysqli_query($localhost, $query);   
$listEvents = "";
$listShops = "";
while($row = mysqli_fetch_array($query_exec2)){
    $query = "select * from shop where id in (select shop_id from event_card_shop where card_id = '".$cardID."' and event_id = '".$row['id']."')";
    $query_exec = mysqli_query($localhost, $query);   
    $listShops = $listShops . "[";
    while($row1 = mysqli_fetch_array($query_exec)){
        $listShops = $listShops . '{' 
                . '"id":"' . $row1['id'] . '",'
                . '"name":"' . $row1['name'] . '",'
                . '"address":"' . $row1['address'] . '",'
                . '"phone_number":"' . $row1['phone_number'] . '",'
                . '"category":"' . $row1['category'] . '",'
                . '"exchange_ratio":"' . $row1['exchange_ratio'] . '",'
                . '"image":"' . $row1['image'] . '",'
                . '"cardImg":"' . $row1['background'] . '"},';
    }
    $listShops = $listShops . "],";
    $listEvents = $listEvents . '{' 
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
echo '{"error":"", "listEvents":['.$listEvents.'], "listShops":['.$listShops.']}';

mysqli_close($localhost);
?>