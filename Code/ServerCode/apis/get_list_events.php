<?php

$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shop_id'];


if(strlen($token)!=64){
    echo '{"error":"token not found", "listEvents":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listEvents":[]}';
    die();
}

// Kiểm tra shop này có thuộc user hay không
$listShopID = array();
$query = "select * from admin_card where admin_username='".$username."'";
$query_exec = mysqli_query($localhost, $query);

while($row = mysqli_fetch_array($query_exec)) {
    $query = "select * from card_shop where card_id='".$row['card_id']."'";
    $card_shop_query_exec = mysqli_query($localhost, $query);
    
    while($card_shop_row = mysqli_fetch_array($card_shop_query_exec)) {
        $listShopID[count($listShopID)] = $card_shop_row['shop_id'];
    }
}

$isHavingShop = false;
$listShopID = array_unique($listShopID);
for($i = 0; $i < count($listShopID); $i++) {
    if($shopID == $listShopID[$i]) {
        $isHavingShop = true;
        break;
    }
}

if(!$isHavingShop) {
    echo '{"error":"not your shop", "listEvents":[]}';
    die();
}

// Duyệt trong bảng event_card_shop, lấy tất cả các event có shop_id = $shopID
$query = "select * from event_card_shop where shop_id='" . $shopID . "'";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listEvents":[';
while($event_card_shop_row = mysqli_fetch_array($query_exec)) {
    $eventID = $event_card_shop_row['event_id'];

    $query = "select * from event where id='" . $eventID . "'";
    $event_query_exec = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($event_query_exec);

    $result = $result . '{' 
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

$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>