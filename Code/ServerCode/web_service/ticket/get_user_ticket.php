<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopId = $_POST['shopId'];
$userId = $_POST['userId'];

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "listTickets":[]}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listTickets":[]}';
    die();
}


// Lấy tất cả các dòng trong bảng buy_award_history, thỏa điều kiện
// username == $username, sắp xếp theo thời gian giảm dần
$query = "select * from buy_award_history where username='".$userId."' and shopID='".$shopId."' order by time desc";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listTickets":[';

while($row = mysqli_fetch_array($query_exec)){
        $result = $result . '{' 
                . '"id":"' . $row['id'] . '",'
                . '"time":"' . $row['time'] . '",'
                . '"username":"' . $row['username'] . '",'
                . '"userFullName":"' . $row['userFullName'] . '",'
                . '"shopID":"' . $row['shopID'] . '",'
                . '"awardImage":"' . $row['awardImage'] . '",'
                . '"awardName":"' . $row['awardName'] . '",'
                . '"quantity":"' . $row['quantity'] . '",'
                . '"shopImage":"' . $row['shopImage'] . '",'
                . '"shopName":"' . $row['shopName'] . '",'
                . '"isTaken":"' . $row['isTaken'] . '"},';
}

$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>