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
    echo '{"error":"token not found", "listAwards":[]}';
    die();
}

$query = "select username from customer_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listAwards":[]}';
	die();
}


// Lấy tất cả các dòng trong bảng buy_award_history, thỏa điều kiện
// username == $username, sắp xếp theo thời gian giảm dần
$query = "select * from customer_tickets where username='".$username."' order by time desc";
$query_exec = mysqli_query($localhost, $query);

$result = '{"error":"", "listAwards":[';

while($row = mysqli_fetch_array($query_exec)){
        $result = $result . '{' 
                . '"time":"' . $row['time'] . '",'
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