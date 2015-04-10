<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];

if(strlen($token)!=64){
    echo '{"error":"token not found","listShops":[]}';
    die();
}

/* check token and return username */
$query = "select username from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo '{"error":"wrong token","listShops":[]}';
	die();
}
/**/

// Token hợp lệ
$query = "select * from shop";

$query_exec = mysqli_query($localhost, $query);

while($row = mysqli_fetch_array($query_exec)){

    $result = $result . '{' 
                . '"id":"' . $row['id'] . '",'
                . '"name":"' . $row['name'] . '",'
                . '"address":"' . $row['address'] . '",'
                . '"phone_number":"' . $row['phone_number'] . '",'
                . '"category":"' . $row['category'] . '",'
                . '"exchange_ratio":"' . $row['exchange_ratio'] . '",'
                . '"image":"' . $row1['image'] . '",'
                . '"cardImg":"' . $row1['background'] . '"},';
}
$result = $result . ']}';
echo $result;


mysqli_close($localhost);
?>