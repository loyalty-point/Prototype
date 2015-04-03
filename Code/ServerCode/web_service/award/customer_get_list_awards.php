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

// mọi thông tin cung cấp đều đúng
// lấy danh sách awards rồi trả về cho người dùng
$query = "select * from award where shopID = '" . $shopID . "'";

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

mysqli_close($localhost);
?>