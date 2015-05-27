<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$cardID = $_POST['cardID'];
$shopID = $_POST['shopID'];

if(strlen($token)!=64){
    echo '{"error":"token not found", "listAwards":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listAwards":[]}';
	die();
}
/**/

/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

$query = "select * from card_shop where card_id='".$cardID."' and shop_id = '" . $shopID . "'";

$query_exec = mysqli_query($localhost, $query);
$shop_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"It' . "'" . 's not your card", "listAwards":[]}';
}else if($shop_rows == 0){
    echo '{"error":"It' . "'" . 's not your shop", "listAwards":[]}';
}else {

    // mọi thông tin cung cấp đều đúng
    // lấy danh sách awards rồi trả về cho người dùng
    $query = "select * from award where id in (select award_id from award_card_shop where shop_id = '".$shopID."' and card_id = '".$cardID."')";
    $query_exec = mysqli_query($localhost, $query);

    $result = '{"error":"", "listAwards":[';

    while($row = mysqli_fetch_array($query_exec)){
            $result = $result . '{' 
                    . '"id":"' . $row['id'] . '",'
                    . '"name":"' . $row['name'] . '",'
                    . '"point":"' . $row['point'] . '",'
                    . '"quantity":"' . $row['quantity'] . '",'
                    . '"description":"' . $row['description'] . '",'
                    . '"image":"' . $row['image'] . '",'
                    . '"shopID":"' . $row['shopID'] . '"},';
    }

    $result = $result . ']}';
    echo $result;
}

mysqli_close($localhost);
?>