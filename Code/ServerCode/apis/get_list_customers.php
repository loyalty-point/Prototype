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
    echo '{"error":"token not found", "listCustomers":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listCustomers":[]}';
    die();
}

// Lấy ra tất cả các shop của đến tài khoản này
$listCardID = array();
$query = "select * from admin_card where admin_username='".$username."'";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listCustomers":[';
while($row = mysqli_fetch_array($query_exec)) {
    $cur_cardID = $row['card_id'];
    $query = "select * from card_shop where card_id='".$cur_cardID."'";
    $card_shop_query_exec = mysqli_query($localhost, $query);
    
    while($card_shop_row = mysqli_fetch_array($card_shop_query_exec)) {
        $cur_shopID = $card_shop_row['shop_id'];
        if($cur_shopID == $shopID) {
            $listCardID[count($listCardID)] = $cur_cardID;

            // Duyệt trong bảng customer_card, lẩy ra tất cả customer của card này
            $query = "select * from customer_card where card_id='" . $cur_cardID . "'";
            $customer_card_query = mysqli_query($localhost, $query);

            while($customer_card_row = mysqli_fetch_array($customer_card_query)) {
                $cur_username = $customer_card_row['username'];

                // Lấy thông tin của customer ra, trả về
                $query = "select * from customer_users where username='" . $cur_username . "'";
                $customer_users_exec = mysqli_query($localhost, $query);
                $customer_users_row = mysqli_fetch_array($customer_users_exec);
                $result = $result . '{' 
                    . '"username":"' . $customer_users_row['username'] . '",'
                    . '"fullname":"' . $customer_users_row['name'] . '",'
                    . '"phone":"' . $customer_users_row['phone_number'] . '",'
                    . '"email":"' . $customer_users_row['email'] . '",'
                    . '"address":"' . $customer_users_row['address'] . '",'
                    . '"avatar":"' . $customer_users_row['avatar'] . '",'
                    . '"barcode":"' . $customer_users_row['barcode'] . '"},';
            }
            break;
        }
    }
}

if(count($listCardID) > 0) {
    // Shop này thuộc tài khoản này
    $result = $result . ']}';
    echo $result;
} else {
    // Shop không thuộc tài khoản này
    echo '{"error":"not your shop", "listCustomers":[]}';
}

mysqli_close($localhost);
?>