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
    echo '{"error":"token not found", "listCards":[]}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "listCards":[]}';
	die();
}

// Lấy danh sách các card từ username
$query = "select * from admin_card where admin_username='".$username."'";
$query_exec = mysqli_query($localhost, $query);
$card_num_rows = mysqli_num_rows($query_exec);

if($card_num_rows == 0) {
    // account chưa có card nào hết
    echo '{"error":"", "listCards":[]}';
    die();   
}

// Lấy ra danh sách các cardID
$listCards = array();
while($row = mysqli_fetch_array($query_exec)){
    $cardID = $row['card_id'];
    $listCards[count($listCards)] = $cardID;
}

// Query để lấy thông tin của card, rồi trả về kết quả
$result = '{"error":"", "listCards":[';    
for($i=0; $i < count($listCards); $i++) {
    $query = "select * from card where id='".$listCards[$i]."'";
    $query_exec = mysqli_query($localhost, $query);
    $card = mysqli_fetch_array($query_exec);

    $result = $result . '{' 
            . '"id":"' . $card['id'] . '",'
            . '"name":"' . $card['name'] . '",'
            . '"image":"' . $card['image'] . '"},';
}

$result = $result . ']}';
echo $result;

mysqli_close($localhost);
?>