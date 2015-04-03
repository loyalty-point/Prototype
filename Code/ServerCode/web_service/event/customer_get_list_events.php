<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shopID'];

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

// mọi thông tin cung cấp đều đúng
// lấy danh sách awards rồi trả về cho người dùng
$query = "select * from event where shop_id = '" . $shopID . "'";

$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listEvents":[';

while($row = mysqli_fetch_array($query_exec)){
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