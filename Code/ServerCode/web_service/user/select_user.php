<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);

$token = $_POST['token'];

$query_search = "select * from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//Không có token này trong db
    echo "";
}
else  {
    while($row = mysqli_fetch_array($query_exec)){
        echo '{"username":"'.$row['username'].
        	'","password":"","fullname":"'.$row['name'].
        	'","phone":"'.$row['phone_number'].
        	'","email":"'.$row['email'].
        	'","address":"'.$row['address'].
        	'","avatar":"'.$row['avatar'].
        	'","token":"'.$row['token'].'"}';
    }
}
mysqli_close($localhost);
?>			