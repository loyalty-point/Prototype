<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$cardID = $_POST['cardID'];
$token = $_POST['token'];

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
	echo "wrong token";
	die();
}
/**/
/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no card in database
    echo "not your card";
    die();
}

$query_search = "select * from card where id='".$cardID."'";
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) { //card không có
    echo "";
}
else  {
    while($row = mysqli_fetch_array($query_exec)){
        echo '{"id":"'.$row['id'].
        	'","name":"'.$row['name'].
        	'","image":"'.$row['image'].'"}';
    }
}
mysqli_close($localhost);
?>