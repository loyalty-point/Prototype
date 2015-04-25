<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shop_id'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "listHistories":[]}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listHistories":[]}';
	die();
}
// kiểm tra xem shopID có thuộc user có user.token == token hay không
/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_shop where admin_username='".$username."' and shop_id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$shopID = $row['shop_id'];

if($shopID == ""){
    echo '{"error":"not your shop", "listHistories":[]}';
	die();
}

// Lấy tất cả các dòng trong bảng update_point_history, thỏa điều kiện
// shopID == $shopID
$query = "select * from history where shop_id='".$shopID."' order by time desc";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listHistories":[';

while($row = mysqli_fetch_array($query_exec)){
        $result = $result . '{' 
                . '"time":"' . $row['time'] . '",'
                . '"billImage":"' . $row['image'] . '",'
                . '"id":"' . $row['id'] . '",'
                . '"type":"' . $row['type'] . '",'
                . '"username":"' . $row['username'] . '",'
                . '"fullname":"' . $row['fullname'] . '",'
                . '"phone":"' . $row['phone'] . '",'
                . '"totalPoint":"' . $row['total_point'] . '"},';
}

$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>