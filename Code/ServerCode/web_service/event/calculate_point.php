<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shop_id'];
$cardID = $_POST['card_id'];
$totalMoney = $_POST['total_money'];
$listProducts = $_POST['list_products'];
$listProducts = json_decode($listProducts, true);

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "pointsFromMoney":"", "achievedEvents":[], "totalPoints":""}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token", "pointsFromMoney":"", "achievedEvents":[], "totalPoints":""}';
	die();
}
// kiểm tra xem shopID có thuộc user có user.token == token hay không
/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

$query = "select * from card_shop where card_id='".$cardID."' and shop_id = '" . $shopID . "'";

$query_exec = mysqli_query($localhost, $query);
$shop_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"not your card", "pointsFromMoney":"", "achievedEvents":[], "totalPoints":""}';
}else if($shop_rows == 0){
    echo '{"error":"not your shop", "pointsFromMoney":"", "achievedEvents":[], "totalPoints":""}';
}else{
    // Lấy danh sách events hiện tại của shop
    $query = "select * from event where id in (select event_id from event_card_shop where card_id = '".$cardID."' and shop_id = '".$shopID."')";
    $events = mysqli_query($localhost, $query);

    // Tính điểm cho event loại 0 (combo sản phẩm -> điểm)
    $totalPointsFromEvents = 0;
    $listAchievedEvents = "[";

    // Duyệt từng product trong listProducts
    foreach($listProducts as $product) { 
        $barcode = $product['barcode'];
        $quantity = $product['quantity'];

        // Tính điểm
        foreach ($events as $event) {

            if($event['type'] == 0 && $event['barcode'] == $barcode && $event['number'] <= $quantity) {
                // Cộng số điểm hiện tại vào $totalPointsFromEvents
                $eventQuantity = (int)($quantity / $event['number']);
                $point =  (int)($eventQuantity * $event['point']);
                $totalPointsFromEvents += $point;

                // Thêm event này vào listAchievedEvents
                $listAchievedEvents = $listAchievedEvents . '{"quantity":"' . $eventQuantity 
                                        . '","event":{' 
                                        . '"id":"' . $event['id'] . '",'
                                        . '"name":"' . $event['name'] . '",'
                                        . '"point":"' . $event['point'] . '",'
                                        . '"image":"' . $event['image'] . '"}},';
            }
        }
    }
      

    // Tính điểm cho event loại 1 (tiền -> điểm)
    // Duyệt trong events, tìm event nào có ratio cao nhất
    $minRatio = 999999999999;
    foreach ($events as $event) {
            if($event['type'] == 1 && $event['ratio'] < $minRatio) {
                $minRatio = $event['ratio'];
                $listAchievedEvents = $listAchievedEvents . '{"quantity":"0","event":{' 
                                        . '"id":"' . $event['id'] . '",'
                                        . '"name":"' . $event['name'] . '",'
                                        . '"point":"' . $event['point'] . '",'
                                        . '"image":"' . $event['image'] . '"}},';
            }
    }
    $listAchievedEvents .= ']';  
    $pointsFromMoney = (int)($totalMoney / $minRatio);

    $totalPoints = (int)($pointsFromMoney + $totalPointsFromEvents);

    $result = '{"error":"", "pointsFromMoney":"' . $pointsFromMoney
                . '", "achievedEvents":' . $listAchievedEvents 
                . ', "totalPoints":"' . $totalPoints . '"}';

    echo $result;
}
mysqli_close($localhost);
?>