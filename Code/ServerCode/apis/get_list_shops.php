<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "listShops":[]}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listShops":[]}';
	die();
}

// Lấy danh sách các card từ username
$query = "select card_id from admin_card where admin_username='".$username."'";
$query_exec = mysqli_query($localhost, $query);
$card_num_rows = mysqli_num_rows($query_exec);

if($card_num_rows == 0) {
    // account chưa có card nào hết
    echo '{"error":"", "listShops":[]}';
    die();   
}

// Lấy danh sách các shop từ mỗi card (nhớ kiếm tra trùng lặp trong trường hợp một shop có >= 2 cards)
$listShops = array();


while($row = mysqli_fetch_array($query_exec)){
    $cardID = $row['card_id'];

    // Lấy danh sách shop dùng chung card tương ứng
    $query = "select shop_id from card_shop where card_id='".$cardID."'";
    $shop_query_exec = mysqli_query($localhost, $query);
    while($shop_row = mysqli_fetch_array($shop_query_exec)) {
        $shopID = $shop_row['shop_id'];
        $listShops[count($listShops)] = $shopID;
    }
}

// Duyệt $listShops, loại bỏ các phần tử trùng (các shop bị trùng)
$listShops = array_unique($listShops);


$result = '{"error":"", "listShops":[';
// Duyệt $listShops, ghi kết quả
for($i = 0; $i < count($listShops); $i++) {
    // Tìm thông tin shop trong bảng shop
    $query = "select * from shop where id='".$listShops[$i]."'";
    $query_exec = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($query_exec);

    $result = $result . '{' 
                . '"id":"' . $row['id'] . '",'
                . '"name":"' . $row['name'] . '",'
                . '"address":"' . $row['address'] . '",'
                . '"phone_number":"' . $row['phone_number'] . '",'
                . '"category":"' . $row['category'] . '",'
                . '"image":"' . $row['image'] . '",'
                . '"exchange_ratio":"' . $row['exchange_ratio'] . '",'
                . '"cardImg":"' . $row['background'] . '"},';
}
$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>