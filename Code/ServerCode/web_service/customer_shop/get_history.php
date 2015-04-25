<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$historyID = $_POST['history_id'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "history":{}}';
    die();
}

$query = "select username from customer_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];
$customer_users = $row;

if($username == ""){
    echo '{"error":"wrong token", "history":{}}';
	die();
}

// Lấy tất cả các dòng trong bảng update_point_history, thỏa điều kiện
// shopID == $shopID
$query = "select * from history where id='".$historyID."'";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "history":';

while($row = mysqli_fetch_array($query_exec)){
        $result = $result . '{' 
                . '"time":"' . $row['time'] . '",'
                . '"billImage":"' . $row['image'] . '",'
                . '"id":"' . $row['id'] . '",'
                . '"type":"' . $row['type'] . '",'
                . '"username":"' . $row['username'] . '",'
                . '"fullname":"' . $row['fullname'] . '",'
                . '"phone":"' . $row['phone'] . '",'
                . '"totalPoint":"' . $row['total_point'] . '"}}';
}

echo $result;

mysqli_close($localhost);
?>