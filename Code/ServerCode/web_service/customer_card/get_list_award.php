<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$cardID = $_POST['cardID'];

if(strlen($token)!=64){
    echo '{"error":"token not found", "listAwards":[]}';
    die();
}

/* check token and return username */
$query = "select username from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listAwards":[]}';
    die();
}
/**/

/* check exist card id in "card_shop" table*/
$query = "select * from card_shop where card_id='".$cardID."'";

$query_exec = mysqli_query($localhost, $query);
$rows = mysqli_num_rows($query_exec);

$result = '{"error":"", "listAwards":[';
while($row1 = mysqli_fetch_array($query_exec)){
    // mọi thông tin cung cấp đều đúng
    // lấy danh sách awards rồi trả về cho người dùng
    $query = "select * from shop where id = '" . $row1['shop_id'] . "'";
    $query_exec1 = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($query_exec1);
    $shopname = $row['name'];

    $query = "select * from award where id in (select award_id from award_card_shop where shop_id = '".$row1['shop_id']."' and card_id = '".$cardID."')";
    
    $query_exec2 = mysqli_query($localhost, $query);   

    while($row = mysqli_fetch_array($query_exec2)){
            $result = $result . '{' 
                    . '"shopName":"' . $shopname . '",'
                    . '"id":"' . $row['id'] . '",'
                    . '"name":"' . $row['name'] . '",'
                    . '"point":"' . $row['point'] . '",'
                    . '"quantity":"' . $row['quantity'] . '",'
                    . '"description":"' . $row['description'] . '",'
                    . '"image":"' . $row['image'] . '",'
                    . '"shopID":"' . $row['shopID'] . '"},';
    }
}
$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>